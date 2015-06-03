#!/bin/bash

curl -sSL https://get.docker.com/ | sh

# Non-root access
usermod -aG docker ${USER}
