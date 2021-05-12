# 图床网

* 前端地址
~~~
https://github.com/kilicmu/Tools-WebSite
~~~
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

* 返回值
    - status: 状态码
        - 200: 正常
        - 410: 图片为空
        - 420: 图片类型错误
        - 500: 服务器内部错误
    - msg: 消息提示
    - data: 图片URL or null

### 待开发

* 限制用户请求的频率