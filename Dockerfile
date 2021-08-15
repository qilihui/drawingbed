FROM openjdk:8

ADD target/*.jar /work/app.jar
#ENV MY_HOME=/work
#RUN mkdir -p $MY_HOME
#WORKDIR $MY_HOME
#ADD pom.xml $MY_HOME

# get all the downloads out of the way
#RUN ["/usr/local/bin/mvn-entrypoint.sh","mvn","verify","clean","--fail-never","-Dmaven.test.skip=true"]

# add source
#ADD . $MY_HOME

# run maven verify
#RUN ["/usr/local/bin/mvn-entrypoint.sh","mvn","package","-Dmaven.test.skip=true"]
#ENTRYPOINT ["java","-Dspring.profiles.active=aliyun","-jar","/work/target/drawingbed-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["java","-Dspring.profiles.active=aliyun","-jar","/work/app.jar"]