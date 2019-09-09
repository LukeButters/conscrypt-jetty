# conscrypt-jetty
Presents an issue when using conscrypt and jetty together

Run the test (or just run mvn clean install).

The test will start a jetty server and tell you the port it is listening on, the server will stay open listening to requests.

Make a stack of http 1.0 requests.

This can be done with:

```
src/test/resources/example.sh <port>
```

When you run the test it will tell you which port, and give you the command to run.

Find the pid of jetty (java) and run lsof on it, you will see lots of entries like:
```
java    31296 luke *798r     FIFO               0,12       0t0 6524803 pipe
java    31296 luke *799w     FIFO               0,12       0t0 6524803 pipe
java    31296 luke *800r     FIFO               0,12       0t0 6524805 pipe
java    31296 luke *801w     FIFO               0,12       0t0 6524805 pipe
java    31296 luke *876r     FIFO               0,12       0t0 6504834 pipe
java    31296 luke *877w     FIFO               0,12       0t0 6504834 pipe
java    31296 luke *878r     FIFO               0,12       0t0 6503671 pipe
java    31296 luke *879w     FIFO               0,12       0t0 6503671 pipe
```

If you disable the use of conscrypt you will not see them.
