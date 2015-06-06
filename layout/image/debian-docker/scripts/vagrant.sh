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

### WARNING: DO NOT FORGET TO REMOVE IT IF ACCESSIBLE FROM OUTSIDE !!!

function add_vagrant_key {
    homedir=$(su - $1 -c 'echo $HOME')
    mkdir -p $homedir/.ssh
    curl -L 'https://raw.github.com/mitchellh/vagrant/master/keys/vagrant.pub' -o $homedir/.ssh/authorized_keys2
    chown -Rf $1. $homedir/.ssh
    chmod 700 $homedir/.ssh
    chmod 600 $homedir/.ssh/authorized_keys2
}

if [ $(grep -c vagrant /etc/passwd) == 0 ] ; then
    useradd vagrant -m
fi

# Add public key to vagrant user
add_vagrant_key vagrant

