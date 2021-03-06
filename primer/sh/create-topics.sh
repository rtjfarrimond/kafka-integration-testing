#!/bin/bash

set -x
set -e
set -o pipefail
set -u

echo 'Creating topic...'

bin/kafka-topics.sh \
  --create \
  --bootstrap-server kafka:9092 \
  --topic foo

echo 'Publishing a message to the topic...'
echo bar | \
    bin/kafka-console-producer.sh \
        --topic foo \
        --bootstrap-server kafka:9092

echo 'Consuming the message from the topic...'
bin/kafka-consumer-perf-test.sh \
  --broker-list kafka:9092 \
  --topic foo \
  --messages 1 \
  --group test-consumer

echo 'Opening health check port...'

nc -k -l -p 1234

trap : TERM INT; sleep infinity & wait
