#!/bin/bash
port=$1

echo will use $1 as port

file=urls.txt

echo will use $file to hold the URLs

echo Running curl command, this should return something from the server:
curl -k --http1.0 "https://127.0.0.1:$port/mclovin"

rm $file
for i in {1..1000}
do
   echo "url=https://127.0.0.1:$port/mclovin" >> $file
done

for i in {1..900000}
do 
   echo $i
   curl -k -K $file --http1.0  > /dev/null 2>/dev/null
done


