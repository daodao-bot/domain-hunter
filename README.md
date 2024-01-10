# domain-hunter

---

## 说明

**domain-hunter** 【域名猎手】 是一个域名搜索服务。

---

## 部署

微服务依赖

- mysql
- redis
- rabbitmq

---

### mysql

```shell
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=root \
  -v /data/docker/mysql:/var/lib/mysql \
  mysql:latest
```

初始化数据库

参考 src/main/resources/static/doc/mysql.sql

### redis

```shell
docker run -d \
  --name redis \
  -p 6379:6379 \
  -e TZ=Asia/Shanghai \
  -v /data/docker/redis/data:/data \
  redis:latest
```

### rabbitmq

```shell
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e TZ=Asia/Shanghai \
  -v /data/docker/rabbitmq:/var/lib/rabbitmq \
  rabbitmq:management
```

创建 virtual-host = domain-hunter

---

### application

使用环境变量配置参数，参考 src/main/resources/application.yml

```shell
docker run -d \
  --name domain-hunter \
  -p 80:80 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_HOST=localhost \
  -e MYSQL_PORT=3306 \
  -e MYSQL_USERNAME=root \
  -e MYSQL_PASSWORD=root \
  -e REDIS_HOST=localhost \
  -e REDIS_PORT=localhost \
  -e REDIS_USERNAME= \
  -e REDIS_PASSWORD= \
  -e REDIS_DATABASE=1 \
  -e RABBITMQ_HOST=localhost \
  -e RABBITMQ_PORT=5672 \
  -e RABBITMQ_VIRTUAL_HOST=domain-hunter \
  -e RABBITMQ_USERNAME=guest \
  -e RABBITMQ_PASSWORD=guest \
  daodaobot/domain-hunter:latest
```

---

## 使用

OpenAPI 接口文档地址： /doc.html

数据查询参考 sql

```mysql
SELECT *
FROM `domain_hunter`.`domain`
WHERE `tld` = 'com'
  AND LENGTH(`sld`) = 3
  AND `avail` = 1
  AND `price` IS NOT NULL
  AND `valid` = 1
ORDER BY `price` ASC
LIMIT 10;
```
