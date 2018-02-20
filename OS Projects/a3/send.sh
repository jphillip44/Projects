#!/bin/sh

pid=`pgrep -nu $(whoami) receiver`
echo Receiver pid is: $pid
sender $pid

