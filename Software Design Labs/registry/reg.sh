#!/bin/sh

path=./tests/acceptance/student
for i in $@
do
  if [ -f $path/at$i.txt ]
  then
    ./EIFGENs/registry/W_code/registry -b $path/at$i.txt > $path/at$i.actual.txt
    ./registry_oracle -b $path/at$i.txt > $path/at$i.expected.txt
    diff -s $path/at$i.expected.txt $path/at$i.actual.txt
  else
    echo "file does not exist"
  fi
done
