#!/bin/bash

curl -sSL https://get.docker.com/ | sh

# Configure remote access
sed -i "s/^#DOCKER_OPTS=\".*\"$/DOCKER_OPTS=\"-H tcp:\/\/0.0.0.0:4243\"/" /etc/default/docker

# Fix docker service to use DOCKER_OPTS
sed -i "s/^ExecStart=\/usr\/bin\/docker -d -H fd:\/\/$/ExecStart=\/usr\/bin\/docker -d -H fd:\/\/ \$DOCKER_OPTS\nEnvironmentFile=-\/etc\/default\/docker\//" /lib/systemd/system/docker.service
systemctl daemon-reload

# Enable docker service
systemctl enable docker

# Non-root access
# gpasswd -a ${USER} docker

