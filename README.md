# 图床网

* 文件名处理方式：使用时间戳转换为二进制，加入随机数之后在转换为62进制
* 请求示例
~~~
curl --location --request POST 'abiao.me:6688/upload' \
--form 'file=@"/C:/Users/Tom/Desktop/test.jpg"'
~~~
* 部署：采用 docker-compose 部署，执行：
~~~
git clone https://github.com/qilihui/drawingbed.git
cd drawingbed
docker-compose up -d
~~~
* 修改
~~~
version: '3'
services:
  drawingbed-server:
    container_name: drawingbed-server
    build: .
    working_dir: /work
    restart: always
    environment:
      - TZ=Asia/Shanghai
    ports:
      - 6688:8080
      # 6688 为主机端口号，任意修改
    volumes:
      - ./data/images:/images
      # ./data/images 是保存在本机的文件，任意修改
    entrypoint: ["java","-Dspring.profiles.active=aliyun","-jar","/work/target/drawingbed-0.0.1-SNAPSHOT.jar"]
~~~
### 待开发
* 限制用户请求的频率