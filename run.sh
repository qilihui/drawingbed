#!/bin/sh
echo "开始打包"
mvn clean package -Dmaven.test.skip=true
app="./target/drawingbed-0.0.1-SNAPSHOT.jar"
if [ ! -f $app ]; then
    echo "error: 打包失败"
    exit
fi
docker-compose up -d
echo "打包完成"

