# Kafka Integration Testing

## Set Up

Run `git clone https://github.com/linkedin/Burrow.git` in this directory.

## Running docker-compose

    $ docker-compose build
    $ docker-compose up --exit-code-from test
    $ docker-compose down

The exit code from the `docker-compose up --exit-code-from test` command
indicates the result of the test; i.e. `0` for success and any non-zero code
for failure.
