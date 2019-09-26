# conscrypt-jetty
Presents an issue when using conscrypt and jetty together

# Run it
## First build it with:

```
mvn install dependency:copy-dependencies
```

## then run the web server:

```
java -XX:NativeMemoryTracking=summary -Xmx512m -Xms512m -Dcom.sun.management.jmxremote -Djava.net.preferIPv4Stack=true -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxMetaspaceSize=256m -XX:CompressedClassSpaceSize=256m -cp target/dependency/*:target/jetty-conscrypt-fifo-0.0.1-SNAPSHOT.jar com.luke.ConscryptJetty
```


The test will start a jetty server and tell you the port it is listening on, the server will stay open listening to requests.

# Make requests to the server

Make a stack of http 1.0 requests.

This can be done with:

```
src/test/resources/example.sh <port>
```

When you run the test it will tell you which port, and give you the command to run.

# Memory usage
Find the pid of jetty (java) and discover the memory usage with:
```
./src/test/resources/jmemuse.sh <PID>
```

Example output:
```
./src/test/resources/jmemuse.sh 2314
Thu 26 Sep 16:54:58 AEST 2019
 2314 1023164
```

the number on the left is the PID, the number on the right is RSS reported by ps.

It will print the memory usage every 10s.

very very slowly you will see the memory grow forever. It might take days for this
to finish.

# Sample output
Look under the sample folder to see some sample output and a graph I created by:
running:
```
cat sample/date_and_mem.txt | grep -v "AEST" | cut -d " " -f2 | nl > /tmp/memory_over_time.png
gnuplot
plot '/tmp/memory_over_time.png'
```

# See also
https://github.com/google/conscrypt/issues/711
