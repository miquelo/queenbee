#!/bin/bash
#
# This file is part of QueenBee Project.
#
# QueenBee Project is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# QueenBee Project is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with QueenBee Project.  If not, see <http://www.gnu.org/licenses/>.
#

curl -sSL https://get.docker.com/ | sh

# Configure remote access
sed -i "s/^#DOCKER_OPTS=\".*\"$/DOCKER_OPTS=\"-H tcp:\/\/0.0.0.0:4243\"/" /etc/default/docker

# Fix docker service to use DOCKER_OPTS
sed -i "s/^ExecStart=\/usr\/bin\/docker -d -H fd:\/\/$/ExecStart=\/usr\/bin\/docker -d -H fd:\/\/ \$DOCKER_OPTS\nEnvironmentFile=-\/etc\/default\/docker\//" /lib/systemd/system/docker.service
systemctl daemon-reload

# Enable docker service
systemctl enable docker

# Non-root access to vagrant
gpasswd -a vagrant docker

