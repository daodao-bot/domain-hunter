# domain-hunter

---

[TOC]

---

## 说明

domain-hunter 域名猎手 是一个域名搜索服务。

---

## 部署

### mysql

```shell
docker run -d --name mysql -p 3306:3306 -e TZ=Asia/Shanghai -e MYSQL_ROOT_PASSWORD=root -v /data/docker/mysql:/var/lib/mysql mysql:latest
```

初始化数据库

参考 src/main/resources/static/doc/mysql.sql

### redis

```shell
docker run -d --name redis -p 6379:6379 -e TZ=Asia/Shanghai -v /data/docker/redis/data:/data redis:latest
```

### rabbitmq

```shell
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -e TZ=Asia/Shanghai -v /data/docker/rabbitmq:/var/lib/rabbitmq rabbitmq:management
```

创建 virtual-host = domain-hunter

---

### application

使用环境变量配置参数，参考 src/main/resources/application.yml

```shell
docker run -d --name domain-hunter -p 80:80 -e TZ=Asia/Shanghai -e MYSQL_HOST=localhost daodaobot/domain-hunter:latest
```

---

## 使用

访问 http://localhost/doc.html
