# mysql

sudo docker run -p 3306:3306 --name mysql \
-v /usr/local/docker/mysql/mysql-files:/var/lib/mysql-files \
-v /usr/local/docker/mysql/conf:/etc/mysql/conf.d \
-v /usr/local/docker/mysql/logs:/var/log/mysql \
-v /usr/local/docker/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
-d mysql:8.0.32

# redis

docker run -p 6379:6379 \
--name redis \
-v /usr/local/docker/redis/data:/data \
-v /usr/local/docker/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /usr/local/etc/redis/redis.conf

# rabbitmq

docker run -d \
--name rabbitmq3.7.7 \
-e RABBITMQ_DEFAULT_USER=guest \
-e RABBITMQ_DEFAULT_PASS=guest  \
-v /usr/local/docker/rabbitmq/data:/var/lib/rabbitmq   \
-p 15672:15672 -p 5672:5672 rabbitmq:3.7.7-management

# mongodb

docker run -d --name mongo -v /usr/local/docker/mongodb/data:/data/db -p 27017:27017 mongo:4.4



# nacos-mysql

[nacos-docker/example/standalone-mysql-8.yaml at master · nacos-group/nacos-docker](https://github.com/nacos-group/nacos-docker/blob/master/example/standalone-mysql-8.yaml)

