FROM ingensi/play-framework

COPY . /data

WORKDIR /data

RUN yum install -y tar openssl

ADD http://mirror.cc.columbia.edu/pub/software/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz

RUN tar xzf apache-maven-3.0.5-bin.tar.gz -C /usr/local &&\
     ln -s /usr/local/apache-maven-3.0.5 /usr/local/maven

ENV M2_HOME /usr/local/maven
ENV PATH ${M2_HOME}/bin:${PATH}

RUN mvn clean test site

RUN buildDate=`date +"%m-%d-%y-%H:%M:%S"` && \
    cp -r target/site/allure-maven-plugin /tmp/$buildDate &&\
    git checkout gh-pages && git pull && \
    mv /tmp/$buildDate builds/$buildDate && \
    git add -A && git commit -m "CI build form $buildDate" && git push


RUN activator universal:package-xz-tarball


RUN rm -rf *
