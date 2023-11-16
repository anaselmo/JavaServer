# Java Web Server
Basic web server made with Java using the `slf4k-api` and `sqlite-jdbc` libraries. HTML preproccesing cannot be done with PHP due to low-level programming we are doing here, so I will be using my [Java Hypertext Preproccesor](https://github.com/anaselmo/JHTPP).

# Table of Contents

1. [Install Java in our machine](#install-java-in-our-machine)
2. [Install Docker in our machine](#install-docker-in-our-machine)
3. [Create and run our Docker container](#create-and-run-our-docker-container)
    1. [Things we can do with our Docker container](#things-we-can-do-with-our-docker-container)
4. [Run our server](#run-our-server)
5. [Stop our server](#stop-our-server)

## How to get started

### Install Java in our machine

If you don't have the `OpenJDK` package installed in your machine, you can run the commands:
```bash
sudo apt-get update
sudo apt install default-jdk
```
After the installation is complete, you can verify that **Java** is installed by checking the version:

```bash
java -version
```

### Install Docker in our machine

If you don't have the `Docker` package installed in your machine, you can run the commands:
```bash
sudo apt-get update
sudo apt install docker.io
```

### Create our Docker image

Firstly, we will need to create our docker image. That's the reason for the **`Dockerfile`**:

```Dockerfile
FROM ubuntu:latest

WORKDIR /app

RUN apt-get update && apt-get install -y \
    openjdk-19-jdk-headless \
    sqlite3 \
    iproute2 \
    && rm -rf /var/lib/apt/lists/*

ENV PORT=8080

EXPOSE 8080
```

We will use the latest version of ubuntu (`ubuntu:latest`). Our working directory inside the docker will be /app. We run an `apt-get update` and install a series of things for our server to work.  
Then we do `ENV PORT=8080` to set the environment variable named `PORT` with a default value of `8080`. This variable can be used within the Dockerfile, and it can also be accessed by processes running inside the container.  
Finally, we do `EXPOSE 8080` to inform Docker that the container will listen on port `8080` at runtime.  
It's important to note that the `EXPOSE` instruction alone does not publish the specified ports to the host machine. To publish ports, you would typically use the `-p` option with the `docker run` command when starting a container.  
  
To end with the docker image, we will run the command `docker build -t java-server:1.0 .` to create it.
- - - 

### Create and run our Docker container

Once our image is created, we can create and run the image. To do this, we will just run the script **`dockerBuild.sh`**:  
```bash 
./dockerBuild.sh
```

**`dockerBuild.sh`**:
```bash 
docker build -t java-server:1.0 . 
docker run -p 8080:8080 -v "$(pwd)/app:/app" -it --name java-server -d java-server:1.0
```
**Once we first execute this script, we do no longer have to execute it more.**

In the `dockerBuild.sh` script, we already build the image with `docker build -t java-server:1.0 .` and we run the container `java-server:1.0` using the command `docker run -p 8080:8080 -v "$(pwd)/app:/app" -it --name java-server -d java-server:1.0`, where:
- `-p 8080`: This option publishes the container's port 8080 to the host machine's port 8080. This means that you can access the application running inside the container on http://localhost:8080 on the host machine.  

- `-v "$(pwd)/app:/app"`: This option mounts a volume from the host machine to the container. It binds the local directory `$(pwd)/app` to the `/app` directory inside the container. This is often used for persisting data or sharing data between the host and the container.  

- `-it`: This option makes the container run in interactive mode, attaching the terminal. This is often used for running containers that require user interaction.  

- `--name java-server`: This option sets a custom name for the container. In this case, the container will be named "java-server."  

- `-d`: This option runs the container in detached mode, meaning it runs in the background and doesn't block the terminal. The container will continue running even if the terminal is closed.  

- `java-server:1.0`: This is the name and tag of the Docker image to use when creating the container. It specifies the image named "java-server" with version/tag "1.0."  



- - - 
### Things we can do with our Docker container

To run an interactive shell (`/bin/bash`) inside our running Docker container `java-server`, we can:
```bash
docker exec java-server -it /bin/bash
```
If we run the command `docker ps -a` we can see a list of all the docker containers we have created, if our `java-server` would happen to be Exited or Stopped, we can use the following commands:
- `docker start java-server` to start the `java-server` docker container.
- `docker restart java-server` to restart it.
- `docker stop java-server` to stop it.
- `docker pause java-server` and `docker unpause java-server` to pause or unpause the execution of processes in the running container.

If we want to remove the container, we can run the `docker rm java-server` command, and to remove the Docker image we can run `docker rmi java-server:1.0`.

- - - 

### Run our server
From our computer or from the container, we can initiate the server with the **`initServer.sh`** script:
```bash 
./scripts/initServer.sh
```

**`initServer.sh`**:
```bash
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
```

*Note: If there is an error with Java, the **port** may be being used. Run the command `job` and kill the Java processes.*

- - - 
### Stop our server

To stop our server we have to run the **`stopServer.sh`** script:
```bash 
./scripts/stopServer.sh
```

**`stopServer.sh`**:
```bash
#!/bin/bash
kill $(cat ../serverPID)
```

Let's break down the components of this command:
- `cat ../serverPID`: This command reads the contents of the serverPID file and prints them to the standard output. In this case, it outputs the process ID (PID) of the Java server.  

- `kill $(...)`: The $(...) is command substitution in Bash. It takes the output of the enclosed command (in this case, cat serverPID) and uses it as an argument for the kill command.  
- `kill`: This is the command used to send signals to processes. By default, kill sends the TERM signal, which asks the process to terminate gracefully.

Putting it all together, `kill $(cat ../serverPID)` reads the process ID from the serverPID file and sends a termination signal to the process with that PID. This is a common way to gracefully stop a background process by signaling it to shut down.

- - - 
