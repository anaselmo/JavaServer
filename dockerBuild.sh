#Build our image
docker build -t java-server:1.0 . 

docker run -p 8080:8080 -v "$(pwd)/app:/app" -it --name java-server -d java-server:1.0