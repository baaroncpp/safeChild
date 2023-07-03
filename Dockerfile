FROM postgres:15.1-alpine

LABEL author="bkaaron"
LABEL description="test"
LABEL version="1.0"

COPY *.sql /docker-entrypoint-initdb.d/