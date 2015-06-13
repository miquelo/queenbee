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

# dnssec-keygen -a HMAC-MD5 -b 512 -n USER ns-queenbee-lan_rndc-key
# Not possible. Not enough entropy :(
cp /etc/bind/rndc.key /etc/bind/ns-queenbee-lan_rndc.key
cp -f /tmp/ddns/bind/named.* /etc/bind/
cp /tmp/ddns/bind/db.queenbee.* /var/lib/bind/

mkdir -p /var/log/bind
chown bind /var/log/bind

a2enmod php5 rewrite
a2dissite 000-default

cp /tmp/ddns/web/site.conf /etc/apache2/sites-available/ddns.conf
mkdir /var/www/ddns
cp /tmp/ddns/web/*.php /var/www/ddns/
a2ensite ddns

usermod -a -G bind www-data

rm -rf /tmp/ddns/

