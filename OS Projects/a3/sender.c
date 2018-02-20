/*Joshua Phillip, 207961907, EECS 3221, Assignment 3*/

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <sys/mman.h>
#include <signal.h>

int main(int argc, char **argv){
	/**
	* Here all the necessary variables are initialized, a fp to open the sharedfile
	* memory, an array for the map, i is to keep track of the input and c is to accept
	* user input. finfo is to get the size of the sharedfile, filesize stores it and
	* pid is the pid of receiver.
	*/
	int fp, i;
	char *map, c;
	struct stat finfo;
	off_t filesize;
	pid_t pid;
	
	/**
	* Here we convert the command line argument to a pid type for use with
	* the sginals, then we open the file, check it's size and memory map 
	* it to the map array.
	*/
	
	if (argc!=2){
		printf("argument for pid needed\n");
		exit(EXIT_FAILURE);
	}
	pid = atoi(argv[1]);
	
	fp = open("sharedfile", O_RDWR, 0660);
	if (fp < 0){
		perror("open");
		exit(EXIT_FAILURE);
	}
			
	if (fstat(fp, &finfo)){
		perror("fstat");
		exit(EXIT_FAILURE);
	}
	filesize = finfo.st_size;
	
	map = mmap(NULL, filesize, PROT_WRITE, MAP_SHARED, fp, 0);
	if (map == MAP_FAILED){
		perror("mmap");
		exit(EXIT_FAILURE);
	}
	
	/**
	* The loop here contains a getchar loop that terminates when the user
	* hits enter and then signals the receiver using SIGUSR1. It repeats
	* this process until it has exceeded the size of sharedfile.
	*/
	i = 0;
	do{
		while((c = getchar())!=EOF && c!='\n'){
			map[i++] = c;
		}
				
		if (kill(pid, SIGUSR1)){
			perror("kill SIGUSR1");
			exit(EXIT_FAILURE);
		}
	}while(i < filesize);
	
	/**
	* Here the memory is unmapped, file is closed and a SIGTERM is sent to
	* receiver, the program then exits.
	*/
	
	if (munmap(map, filesize)) {
		perror("munmap");
		exit(EXIT_FAILURE);
	}
	
	if (close(fp)){
		perror("close");
		exit(EXIT_FAILURE);
	}

	if (kill(pid, SIGTERM)){
		perror("kill SIGTERM");
		exit(EXIT_FAILURE);
	}
	exit(EXIT_SUCCESS);
}