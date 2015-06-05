#!/bin/bash

curl -sSL https://get.docker.com/ | sh

# Configure remote access
sed -i "s/^#DOCKER_OPTS=\".*\"$/DOCKER_OPTS=\"-H tcp:\/\/0.0.0.0:4243\"" /etc/default/docker

# Fix docker service to use DOCKER_OPTS
# http://stackoverflow.com/questions/30127580/docker-opts-in-etc-default-docker-ignored-on-debian8
systemctl daemon-reload

# Non-root access
gpasswd -a ${USER} docker
