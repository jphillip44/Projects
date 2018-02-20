/*Joshua Phillip, 207961907, EECS 3221, Assignment 2*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#define N_DEF 100
#define M_DEF 1000
#define L_DEF 10

typedef struct{
	pthread_cond_t start_line, init_line;
	pthread_mutex_t countmut, filemut;
	int N, M, L, colour_lock, turn_lock, redcount, greencount, start_lock;
	FILE *fp;
} Var;

void red(Var *v), green(Var *v);

int main(int argc, char *argv[]){
	/**
	* Here the variables N, M and L are initialized. The specification is vague but I have
	* allowed for N to be initialized without M and L, and similarly, N and M to be
	* initialized without L, or none at all. Then, the fp for pthread_stats is opened to 
	* allow the threads to write to it.
	*/
	
	Var v;
	int i;
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
	if(pthread_mutex_init(&v.countmut, NULL)){
		perror("mutex init");
		exit(EXIT_FAILURE);
	}
	v.greencount = 0;
	v.redcount = 0;
	v.colour_lock = 0;
	v.turn_lock = 0;
	v.start_lock = 0;
	v.N = argc > 1 ? atoi(argv[1]) : N_DEF;
	v.M = argc > 2 ? atoi(argv[2]) : M_DEF;
	v.L = argc > 3 ? atoi(argv[3]) : L_DEF;	
	pthread_t red_tid[v.N], green_tid[v.N];
	
	if(!(v.fp = fopen("pthread_stats", "w"))){
		perror("file");
		exit(EXIT_FAILURE);
	}
	
	/**
	* The next part creates all the red and green threads. After that, the program grabs 
	* countmut and wait on the conditional variable init_line. It then waits until it is
	* signalled, which means all the threads have been created. The variable turn_lock, which 
	* is intially 0, is used as a control to prevent threads from waking up before all 
	* other threads are ready. It is incremented and then all the threads are then 
	* signalled via the broadcast call, as to not require chain signalling. The threads 
	* are then joined back to the main thread, fp is closed, start_line and the mutex 
	* are destroyed and the program terminates.
	*/
	for(i=0; i < v.N; i++){
		if(pthread_create(&red_tid[i],NULL, (void*) red, &v)){
			perror("thread");
			exit(EXIT_FAILURE);
		}
		if(pthread_create(&green_tid[i],NULL, (void*) green, &v)){
			perror("thread");
			exit(EXIT_FAILURE);
		}
	}
	if(pthread_mutex_lock(&v.countmut)){
		perror("lock");
		exit(EXIT_FAILURE);
	}
	while(!v.start_lock){
		if(pthread_cond_wait(&v.init_line, &v.countmut)){
			perror("wait");
			exit(EXIT_FAILURE);
		}
	}
	if(pthread_mutex_unlock(&v.countmut)){
			perror("unlock");
			exit(EXIT_FAILURE);
	}
		
	v.turn_lock++;
	if(pthread_cond_broadcast(&v.start_line)){
		perror("broadcast");
		exit(EXIT_FAILURE);
	}
	for(i=0; i < v.N; i++){
		if(pthread_join(red_tid[i], NULL)){
			perror("join");
			exit(EXIT_FAILURE);
		}
		if(pthread_join(green_tid[i], NULL)){
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
	if(pthread_mutex_destroy(&v.countmut)){
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

void red(Var *v){
	/**
	* Here is where the red threads execute their code. Initially, the threads grab the mutex countmut, 
	* then checks if v->start_lock is in use, if not, checks to see if all red and green threads
	* have been made. If they have, it unlocks start_lock and signals the parent thread to proceed.
	* Then they go into a loop where they wait on start_line via turn_lock being equal to
	* k. Before the threads are signalled, turn_lock is incremented, allowing the loop to be
	* broken when the threads are signalled. The mutex is then grabbed, and the pid is written
	* to file. When a thread is done all M of it's writes, it grabs the other mutex, increments 
	* redcount and if it is the last thread to do so, sets colour_lock to 1 allowing all green 
	* threads to now proceed and broadcasts them. The red threads which are now blocking on 
	* both turn_lock and colour_lock will be stuck in the loop and wait again until they are signalled.
	* After going L times, the threads exit.
	*/
	int j, k;
	for(k = 0;k < v->L; k++){
		if(pthread_mutex_lock(&v->countmut)){
			perror("lock");
			exit(EXIT_FAILURE);
		}
		if(!v->start_lock && ++v->redcount == v->N && v->greencount == v->N){
			v->greencount = 0;
			v->redcount = 0;
			v->start_lock++;
			if(pthread_cond_signal(&v->init_line)){
				perror("signal");
				exit(EXIT_FAILURE);
			}
		}
		while(v->colour_lock || v->turn_lock == k){
			if(pthread_cond_wait(&v->start_line, &v->countmut)){
				perror("wait");
				exit(EXIT_FAILURE);
			}
		}
		if(pthread_mutex_unlock(&v->countmut)){
			perror("unlock");
			exit(EXIT_FAILURE);
		}
		
		for(j = 0; j < v->M; j++){
		
			if(pthread_mutex_lock(&v->filemut)){
				perror("lock");
				exit(EXIT_FAILURE);
			}
			fprintf(v->fp, "%lu red\n", pthread_self());
			if(pthread_mutex_unlock(&v->filemut)){
				perror("unlock");
				exit(EXIT_FAILURE);
			}
		}
		
		if(pthread_mutex_lock(&v->countmut)){
			perror("lock");
			exit(EXIT_FAILURE);
		}
		if(++v->redcount == v->N){
			v->colour_lock = 1;
			v->redcount = 0;
			if(pthread_cond_broadcast(&v->start_line)){
				perror("broadcast");
				exit(EXIT_FAILURE);
			}
		}
		if(pthread_mutex_unlock(&v->countmut)){
			perror("unlock");
			exit(EXIT_FAILURE);
		}
	}
	pthread_exit(NULL);
}
void green(Var *v){
	/**
	* Here is where the green threads exexute their code. Initially, the threads grab the mutex countmut, 
	* then checks if v->start_lock is in use, if not, checks to see if all red and green threads
	* have been made. If they have, it unlocks start_lock and signals the parent thread to proceed.
	* Then they go into a loop where they wait on start_line via both, turn_lock and colour_lock.
	* When the threads are signalled in the parent thread, they are still blocked on colour_lock but 
	* not turn_lock. After the red threads are done, they unlock colour_lock for green and signal which 
	* allows the green threads to proceed. The mutex is then grabbed, and the pid is written to
	* file. When a thread is done all M of it's writes, it grabs the other mutex, increments 
	* greencount and if it is the last thread to do so, sets colour_lock to 0 amd increments turn_lock, 
	* allowing all red threads to now proceed and broadcasts them. The green threads are now
	* blocking only on colour_lock and can proceed when red releases it and signals them. After going 
	* L times, the threads exit.
	*/
	int j, k;
	for(k = 0;k < v->L; k++){
		if(pthread_mutex_lock(&v->countmut)){
			perror("lock");
			exit(EXIT_FAILURE);
		}
		if(!v->start_lock && ++v->greencount == v->N && v->redcount == v->N){
			v->greencount = 0;
			v->redcount = 0;
			v->start_lock++;
			if(pthread_cond_signal(&v->init_line)){
				perror("signal");
				exit(EXIT_FAILURE);
			}
		}
		while(!v->colour_lock || v->turn_lock == k){
			if(pthread_cond_wait(&v->start_line, &v->countmut)){
				perror("wait");
				exit(EXIT_FAILURE);
			}
		}
		if(pthread_mutex_unlock(&v->countmut)){
			perror("unlock");
			exit(EXIT_FAILURE);
		}
		
		for(j = 0; j < v->M; j++){	
			if(pthread_mutex_lock(&v->filemut)){
				perror("lock");
				exit(EXIT_FAILURE);
			}		
			fprintf(v->fp, "%lu green\n", pthread_self());
			if(pthread_mutex_unlock(&v->filemut)){
				perror("unlock");
				exit(EXIT_FAILURE);
			}
		}
		
		if(pthread_mutex_lock(&v->countmut)){
			perror("lock");
			exit(EXIT_FAILURE);
		}
		if(++v->greencount == v->N){
			v->colour_lock = 0;
			v->greencount = 0;
			v->turn_lock++;
			if(pthread_cond_broadcast(&v->start_line)){
				perror("broadcast");
				exit(EXIT_FAILURE);
			}
		}
		if(pthread_mutex_unlock(&v->countmut)){
			perror("unlock");
			exit(EXIT_FAILURE);
		}

	}
	pthread_exit(NULL);
}