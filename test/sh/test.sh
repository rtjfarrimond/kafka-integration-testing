#!/bin/bash

set -x
set -e
set -o pipefail
set -u

curl 'burrow:8000/v3/kafka/local/consumer/test-consumer' | \
  jq --exit-status '[.topics.foo[].offsets[] | .offset] | add == 1'
