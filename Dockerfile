FROM ingensi/play-framework

COPY . /data

WORKDIR /data

RUN yum install -y tar openssl mvn

RUN mvn clean test site

RUN activator universal:package-xz-tarball

RUN rm -rf *
