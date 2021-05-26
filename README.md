# STEP 1.Run a mysql and redis docker

## MYSQL is requiered

Create  db using docker or if you have installed a mysql onpremise create a new database modusbox:
```
sudo docker run -d -p 3306:3306 --name test_mysql -e MYSQL_ROOT_PASSWORD=261289 mysql
sudo docker exec -it test_mysql mysql -u root -p
--enter 261289 as password

mysql> create database modusbox;
mysql> exit
```

## Redis is used for retrying

sudo docker run -d --name redis -p 6379:6379 redis

# STEP 2.Checkout the project
In your local directory and go to the folder where the  pom and dockerfile is located.

# STEP 3. Install the proyect using maven
sudo docker run -it --rm -v "$PWD":/app -w /app maven:3-openjdk-11 mvn clean install

# STEP 4. Delete the existing container
sudo docker stop modustest-stg && sudo docker rm modustest-stg

# STEP 5. Build the image
sudo docker build -t modustest:0.0.1-SNAPSHOT .

# STEP 6. Run the aplication
sudo docker run -d \
    --name modustest-stg \
    --network host \
    --restart always \
    modustest:0.0.1-SNAPSHOT

# Another option

For running the project instead doing step 2 to 6 is is to pull the image from my dockerhub repository and then run it.

sudo docker pull 22955721/modustest-dev:latest
sudo docker run -p 9091:8080 -t modustest:0.0.1-SNAPSHOT
