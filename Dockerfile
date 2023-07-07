FROM ubuntu:20.04

RUN apt-get update && apt-get install -y curl unzip less wget

RUN wget https://download.oracle.com/java/19/archive/jdk-19.0.2_linux-x64_bin.deb

RUN apt-get -qqy install ./jdk-19.0.2_linux-x64_bin.deb

RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-19/bin/java 1919

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

RUN unzip awscliv2.zip && ./aws/install

WORKDIR /

ADD SQSTest.jar SQSTest.jar

ADD .aws/credentials root/.aws/credentials

ADD .aws/config root/.aws/config

CMD ["java", "-jar", "SQSTest.jar"]


