FROM ingensi/play-framework

RUN yum install -y tar openssl git

ADD http://mirror.cc.columbia.edu/pub/software/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz ./

RUN tar xzf apache-maven-3.3.9-bin.tar.gz -C /usr/local &&\
     ln -s /usr/local/apache-maven-3.3.9 /usr/local/maven

ENV M2_HOME=/usr/local/maven
ENV PATH=${M2_HOME}/bin:${PATH}

COPY . /data

WORKDIR /data

RUN mvn clean test || mvn allure:report

RUN buildDate=`date +"%m-%d-%y-%H:%M:%S"` && \
    cp -r target/site/allure-maven-plugin /tmp/$buildDate

RUN git status

#RUN git checkout gh-pages
#RUn git pull && \
#    mv /tmp/$buildDate builds/$buildDate && \
#    git add -A && git commit -m "CI build form $buildDate" && git push


#RUN activator universal:package-xz-tarball


RUN rm -rf *
