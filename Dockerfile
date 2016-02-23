FROM ingensi/play-framework

COPY . /data

WORKDIR /data

RUN yum install -y tar openssl

RUN activator universal:package-xz-tarball

#RUN ./upload target/universal/play-java-mongodb-docker-testng-allure-ci-1.1.txz

RUN rm -rf *
