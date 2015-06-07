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

apt-get install -y bind9 dnsutils apache2 php5-common libapache2-mod-php5

# http://andrwe.org/linux/own-ddns
# https://wiki.debian.org/Bind9#Configuration

# http://code.tutsplus.com/tutorials/a-beginners-guide-to-http-and-rest--net-16340

a2dissite 000-default
cp /tmp/ddns/site.conf /etc/apache2/sites-available/ddns.conf
cp /tmp/ddns/*.php /var/www/ddns/
a2ensite ddns

rm -rf /tmp/ddns/
