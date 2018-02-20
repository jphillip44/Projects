#!/usr/bin/env python
#Joshua Phillip, 207961907, EECS 3221, Assignment 2

import os.path
runs = []
prev = 0
total = 0

if os.path.isfile("pthread_stats"):	
	with open("pthread_stats") as file:
		for line in file:
			if prev == 0:
				i = 1
			elif line == prev:
				i = i + 1
			else:
				runs.append(i)
				i = 1
			prev = line
		runs.append(i)
	
	mean = float(sum(runs))/len(runs)
	for x in runs:
		total = total + (x - mean)**2
	var = total/len(runs)
	SD = var**(0.5)

	print "mean: %f, SD: %f" % (mean, SD)
else:
	print "pthread_stats does not exist"