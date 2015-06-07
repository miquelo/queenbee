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

# Ansible prequesites
apt-get -y install python ruby ruby-json facter ohai

# Install backports for wheezy
if [ $(facter lsbdistcodename) = 'wheezy' ] ; then
    echo "deb http://http.debian.net/debian wheezy-backports main" > /etc/apt/sources.list.d/backports.list
    apt-get update
fi

# Install Ansible
apt-get -y install ansible
touch /var/log/ansible.log
chown vagrant. /var/log/ansible.log

