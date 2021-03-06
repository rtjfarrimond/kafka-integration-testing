FROM ubuntu:latest as builder

RUN apt-get update && apt-get -y dist-upgrade
RUN apt-get -y --no-install-recommends install \
    curl \
    ca-certificates

WORKDIR /tmp

COPY SHA512SUMS .

RUN curl -fsSL -o kafka_2.13-2.5.1.tgz https://archive.apache.org/dist/kafka/2.5.1/kafka_2.13-2.5.1.tgz

RUN sha512sum --check SHA512SUMS

RUN tar -C /opt -zxf kafka_2.13-2.5.1.tgz


FROM ubuntu:latest

RUN apt-get update && apt-get -y dist-upgrade
RUN apt-get -y --no-install-recommends install \
    openjdk-11-jre-headless \
    curl \
    wait-for-it \
    jq \
    ncat && \
    apt-get clean all

COPY --from=builder /opt/kafka_2.13-2.5.1 /opt/kafka

WORKDIR /opt/kafka

CMD trap : TERM INT; sleep infinity & wait
