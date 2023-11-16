#!/bin/bash
#Intended to store the process ID (PID) of the running Java Server
touch ../serverPID

# The script checks if a process with the PID stored in app/serverPID is running.
# If it finds a process, it attempts to kill it and outputs a message indicating that the previous process has been removed.
if kill $(cat ../app/serverPID) 2>/dev/null; then
        echo "Removed previous process"
# If it doesn't find a process, it uses pkill -f java to forcefully kill any Java processes.
else
        pkill -f java
        echo "Previous process not found, cleaning all java processes"
fi

sleep 1

# Compile the server
javac ../app/src/*.java -d ../app/class

# Launching the server
java -classpath "../app/class/:sqlite-jdbc-3.43.2.1.jar:slf4j-api-1.7.36.jar" Server &

# Save the server PID
if [ $? -eq 0 ]; then
        echo $! | cat > ../app/serverPID
        echo "Server started succesfully!"
else
        echo "Could not start server"
fi