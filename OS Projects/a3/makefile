#Joshua Phillip, 207961907, EECS 3221, Assignment 3

#I have included a script send.sh that can be used instead of sender.
#If the pid it is reporting does not match the pid reported by receiver it will not work.
#Sender pid should then be used as per the program specification.

all: shared sender receiver

shared:
	dd if=/dev/zero of=sharedfile count=10 bs=10

sender: sender.c
	gcc sender.c -o sender	
	
receiver: receiver.c
	gcc receiver.c -o receiver
	
clean:
	rm -f sharedfile sender receiver
	
tar: sender.c receiver.c makefile send.sh
	tar -cvf asg3.tar sender.c receiver.c makefile send.sh