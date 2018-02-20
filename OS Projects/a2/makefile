#Joshua Phillip, 207961907, EECS 3221, Assignment 2

all: a2p1 a2p2

a2p1: a2_part1.c
	gcc -pthread -o a2p1 a2_part1.c
    
a2p2: a2_part2.c
	gcc -pthread -o a2p2 a2_part2.c
	
clean:
	rm -f a2p1 a2p2 pthread_stats
	
tar: a2_part1.c a2_part2.c a2_ques.txt calc.py makefile
	tar -cvf asg2.tar a2_part1.c a2_part2.c a2_ques.txt calc.py makefile