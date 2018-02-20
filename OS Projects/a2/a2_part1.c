/*Joshua Phillip, 207961907, EECS 3221, Assignment 2*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#define N_DEF 100
#define M_DEF 1000

typedef struct{
	pthread_cond_t start_line, init_line; 
	pthread_mutex_t filemut;
	int N, M, lock, count;
	FILE *fp;
} Var;

void thread(Var *v);

int main(int argc, char *argv[]){
	/**
	* Here the variables N amd M are initialized. The specification is vague but I have
	* allowed for N to be initialized without M, or both. Then, the fp for pthread_stats is 
	* opened to allow the threads to write to it.
	*/
	Var v;
	int i;
	v.lock = 0;
	v.count = 0;
	if(pthread_cond_init(&v.start_line, NULL)){
		perror("condition init");
		exit(EXIT_FAILURE);
	}
	if(pthread_cond_init(&v.init_line, NULL)){
		perror("condition init");
		exit(EXIT_FAILURE);
	}
	if(pthread_mutex_init(&v.filemut, NULL)){
		perror("mutex init");
		exit(EXIT_FAILURE);
	}
	v.N = argc > 1 ? atoi(argv[1]) : N_DEF;
	v.M = argc > 2 ? atoi(argv[2]) : M_DEF;
	pthread_t pid[v.N];

	if(!(v.fp = fopen("pthread_stats", "w"))){
		perror("file");
		exit(EXIT_FAILURE);
	}
	
	/**
	* The next part creates all the threads. After that, the program grabs countmut and
	* waits on the conditional variable init_line. It then waits until it is signalled, 
	* which means all the threads have been created. The variable lock, which is
	* intially 0, is used as a control to prevent threads from waking up before all other
	* threads are ready. All the threads are then signalled via the broadcast call, as
	* to not require chain signalling. The threads are then joined back to the main thread
	* fp is closed, start_line and the mutex are destroyed and the program terminates.
	*/
	for(i=0; i < v.N; i++){
		if((pthread_create(&pid[i],NULL, (void*) thread, &v))){
			perror("thread");
			exit(EXIT_FAILURE);
		}
	}
	if(pthread_mutex_lock(&v.filemut)){
		perror("lock");
		exit(EXIT_FAILURE);
	}
	while(v.count < v.N ){
		if(pthread_cond_wait(&v.init_line, &v.filemut)){
			perror("wait");
			exit(EXIT_FAILURE);
		}
	}
	if(pthread_mutex_unlock(&v.filemut)){
			perror("unlock");
			exit(EXIT_FAILURE);
	}
	
	v.lock = 1;
	if(pthread_cond_broadcast(&v.start_line)){
		perror("broadcast");
		exit(EXIT_FAILURE);
	}
	for(i=0; i < v.N; i++){
		if(pthread_join(pid[i],NULL)){
			perror("join");
			exit(EXIT_FAILURE);
		}
	}

	if(fclose(v.fp)){
		perror("close");
		exit(EXIT_FAILURE);
	}
	if(pthread_mutex_destroy(&v.filemut)){
		perror("destroy mutex");
		exit(EXIT_FAILURE);
	}
	if(pthread_cond_destroy(&v.start_line)){
		perror("destroy condition variable");
		exit(EXIT_FAILURE);
	}
	if(pthread_cond_destroy(&v.init_line)){
		perror("destroy condition variable");
		exit(EXIT_FAILURE);
	}
	
	if(system("python calc.py")){
		perror("calc");
		exit(EXIT_FAILURE);
	}
	exit(EXIT_SUCCESS);
}
void thread(Var *v){
	/**
	* Here is where all the threads execute their code. Initially, it grabs the mutex countmut, 
	* then checks if v->count == v->N and if it is, all threads are ready so it signals 
	* the parent to proceed. Then they go into a loop where they wait on start_line and release 
	* the mutex, by the time each thread is signalled, the loop condition is false so it can 
	* regrab the mutex, writes it's pid to the file, release the mutex and exits. 
	*/
	int j;
	if(pthread_mutex_lock(&v->filemut)){
			perror("lock");
			exit(EXIT_FAILURE);
	}
	if(++v->count == v->N){
		if(pthread_cond_signal(&v->init_line)){
			perror("signal");
			exit(EXIT_FAILURE);
		}
	}
	while(!v->lock){
		if(pthread_cond_wait(&v->start_line, &v->filemut)){
			perror("wait");
			exit(EXIT_FAILURE);
		}
	}
	if(pthread_mutex_unlock(&v->filemut)){
			perror("unlock");
			exit(EXIT_FAILURE);
	}

	for(j = 0; j < v->M; j++){
		if(pthread_mutex_lock(&v->filemut)){
			perror("lock");
			exit(EXIT_FAILURE);
		}
		fprintf(v->fp, "%lu\n", pthread_self());
		if(pthread_mutex_unlock(&v->filemut)){
			perror("unlock");
			exit(EXIT_FAILURE);
		}
	}
	pthread_exit(NULL);

}
