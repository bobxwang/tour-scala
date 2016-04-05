#
# Dockerfile for scalatour
#

FROM williamyeh/scala

MAINTAINER bob

# Install netcat
RUN yum update && yum -y install netcat && yum -y install curl

# private only
EXPOSE 80

# 授权访问从容器内到主机上的目录
VOLUME /tmp

# 指定RUN,CMD与ENTRYPOINT命令的工作目录
WORKDIR /scalatour

ADD target/scala-2.11/com.bob.scalatour.jar com.bob.scalatour.jar
