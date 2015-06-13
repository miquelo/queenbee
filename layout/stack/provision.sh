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

# DDNS is the primary DNS server
echo -e "nameserver 192.168.33.2\n$(cat /etc/resolv.conf)" > /etc/resolv.conf

# Fix "stdin: is not a tty" (For the second and subsequent provision times)
sed -i "s/^mesg n$/tty -s \&\& mesg n/" ~/.profile

