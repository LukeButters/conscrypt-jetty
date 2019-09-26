#!/bin/bash
while true
do
        date; ps -eo pid,rss | grep $1
        sleep 10
done
