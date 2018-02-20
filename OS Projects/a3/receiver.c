/*Joshua Phillip, 207961907, EECS 3221, Assignment 3*/

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <sys/mman.h>
#include <signal.h>

/**
* The two variables here are declared global since they need to be for the signal 
* handler to use them. They are declared of type volatile sig_atomic_t so that they
* behave as intended.
*/
volatile sig_atomic_t flag, lock;

int main(){
	/**
	* Here all the necessary variables are initialized, a fp to open the sharedfile
	* memory, an array for the map and i is to keep track of the input. finfo is to 
    * get the size of the sharedfile and filesize stores it. act is for the sigaction
	* and old_act stores the default. mask is to block signals and old_mask the default
	* set of blocked/unblocked signals.
	*/
	int fp, i;
	char *map;
	struct stat finfo;
	off_t filesize;
	struct sigaction act, old_act;
	sigset_t mask, old_mask;
	
	/**
	* Here we setup the signal handler, setup the basic conditons for the mask.
	* The mask is initialized to empty and then SIGTERM and SIGUSR1 are added.
	* sigaction is called on the signals so they can be handled. sigprocmask
	* is then called with SIG_BLOCK to block the signals.
	*/
	
	void handle_signal(int signal);
	
	act.sa_handler = &handle_signal;
	act.sa_mask = mask;
	act.sa_flags = 0;
	
	if (sigemptyset (&mask)){
		perror("empty set");
		exit(EXIT_FAILURE);
	}
	if (sigaddset(&mask, SIGTERM)){
		perror("add SIGTERM");
		exit(EXIT_FAILURE);
	}
	if (sigaddset(&mask, SIGUSR1)){
		perror("add SIGUSR1");
		exit(EXIT_FAILURE);
	}
	
	if (sigaction(SIGUSR1, &act, &old_act)) {
		perror ("sigaction");
		exit(EXIT_FAILURE);
	}
	if (sigaction(SIGTERM, &act, NULL)) {
		perror ("sigaction");
		exit(EXIT_FAILURE);
	}
	
	if (sigprocmask(SIG_BLOCK, &mask, &old_mask)){
		perror("SIG BLOCK");
		exit(EXIT_FAILURE);
	}
	
	/**
	* Here we open the file, check it's size and memory map it to the map
	* array. Then the pid is printed so the user can send it to sender.
	*/
	
	fp = open("sharedfile", O_RDWR);
	if (fp < 0){
		perror("open");
		exit(EXIT_FAILURE);
	}
	
	if(fstat(fp, &finfo)){
		perror("fstat");
		exit(EXIT_FAILURE);
	}
	filesize = finfo.st_size;
	
	map = mmap(NULL, filesize , PROT_READ | PROT_WRITE, MAP_SHARED, fp, 0);
	if (map == MAP_FAILED){
		perror("mmap");
		exit(EXIT_FAILURE);
	}

	printf("My pid is: %d\n", getpid());
	
	/**
	* The loop here suspends and waits until it receives a signal, if the signal
	* was SIGUSR1, it means there is an input from sender so it prints it out and
	* sets i to the length of the output. If it is SIGTERM, it'll exit the loop.
	*/
	i = 0;
	do{
		lock = 0;		
		while(!lock && !flag)
			sigsuspend(&old_mask);
		if(!flag){
			while(i < filesize && map[i]!='\0')
				printf("%c", map[i++]);
			printf("\n");
		}	
	}while(!flag);
	
	/**
	* Here the sharedfile is cleared so the program can be run again without
	* worrying about data being left inside.  Here the memory is unmapped, file 
	* is closed and a SIGTERM is sent to receiver, the program then exits.
	*/
	
	memset(map, 0, filesize);
	
	if (munmap(map, filesize)) {
		perror("munmap");
		exit(EXIT_FAILURE);
	}
	
	if (close(fp)){
		perror("close");
		exit(EXIT_FAILURE);
	}
	exit(0);
}

void handle_signal(int signal){
	/**
	* The signal handler checks which signal it receives and switches a flag
	* to let the main thread know which signal was received.
	*/
	switch(signal){
		case SIGUSR1:
			lock = 1;
			break;
		case SIGTERM:
			flag = 1;
			break;
	}	
}