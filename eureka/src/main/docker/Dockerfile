#基础镜像：我自己构建的jdk基础镜像 仓库是 johnrepo，标签用 centos_jdk8
FROM registry.cn-hangzhou.aliyuncs.com/johnwall/johnrepo:centos_jdk8
#当前镜像的维护者和联系方式
MAINTAINER "" 1047994907@qq.com
#由于 Spring Boot 使用内置的Tomcat容器，Tomcat 默认使用/tmp作为工作目录，
#这个命令的效果是：在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录。
VOLUME /tmp
#将打包好的spring程序拷贝到容器中的指定位置
ADD eureka-1.0-SNAPSHOT.jar /opt
#容器对外暴露8080端口
EXPOSE 8762
#容器启动后需要执行的命令,为了缩短 Tomcat 的启动时间，添加java.security.egd的系统属性指向/dev/urandom作为 ENTRYPOINT
CMD java -Djava.security.egd=file:/dev/./urandom -jar /opt/eureka-1.0-SNAPSHOT.jar
