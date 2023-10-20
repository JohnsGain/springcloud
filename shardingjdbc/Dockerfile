FROM openjdk:17.0.2-jdk

# 将本地文件夹挂在到当前容器
VOLUME /tmp

# 复制文件到容器
ADD target/shardingjdbc-0.0.1-SNAPSHOT.jar shardingjdbc-0.0.1-SNAPSHOT.jar

# 声明需要暴露的端口
EXPOSE 9088

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/shardingjdbc-0.0.1-SNAPSHOT.jar"]
