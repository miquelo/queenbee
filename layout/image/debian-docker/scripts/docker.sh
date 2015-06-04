#!/bin/bash

curl -sSL https://get.docker.com/ | sh

# Configure remote access
sed -i "s/DOCKER_OPTS=$/DOCKER_OPTS=\'-H tcp:\/\/0.0.0.0:4243 -H unix:\/\/\/var\/run\/docker.sock\'" /etc/init/docker.conf

# Non-root access
gpasswd -a ${USER} docker
