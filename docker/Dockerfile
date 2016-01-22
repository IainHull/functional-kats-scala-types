FROM isuper/java-oracle:jdk_8
MAINTAINER Iain Hull "iain.hull@workday.com"

RUN apt-get update
RUN apt-get install git -y

RUN curl --silent --location --retry 3 https://dl.bintray.com/sbt/native-packages/sbt/0.13.9/sbt-0.13.9.tgz | tar xz -C /tmp && mv /tmp/sbt /usr/local/lib/sbt && ln -s /usr/local/lib/sbt/bin/sbt /usr/local/bin

RUN mkdir -p /usr/local/src

RUN cd /usr/local/src && git clone https://github.com/IainHull/functional-kats-scala-types.git

WORKDIR /usr/local/src/functional-kats-scala-types

RUN for step in $(git branch -a | grep step | cut -d / -f 3); do git checkout -b $step origin/$step ; done

RUN git checkout step-0

RUN sbt test

ENTRYPOINT bash
