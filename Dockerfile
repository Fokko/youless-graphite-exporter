FROM openjdk:8

ENV SBT_VERSION 1.2.8

RUN \
  apt-get update && \
  apt-get -y install curl

RUN \
  curl -s -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt

ADD . /app/

WORKDIR /app/

RUN sbt compile

EXPOSE 8000

HEALTHCHECK --interval=22s --timeout=60s CMD curl -s -f http://127.0.0.1:8000/

CMD sbt run
