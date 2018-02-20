#Joshua Phillip, 207961907, EECS 3421, Assignment 2

all: comp

comp: CustDB.java
	javac CustDB.java
	
drop: yrb-drop
	db2 connect to c3421a && db2 -tf yrb-drop && db2 terminate

create: yrb-create
	db2 connect to c3421a && db2 -tf yrb-create && db2 terminate
	
clean:
	rm -f CustDB.class	

zip: CustDB.java makefile yrb-create yrb-drop 3421_Report.pdf
	zip asg2.zip CustDB.java makefile yrb-create yrb-drop 3421_Report.pdf