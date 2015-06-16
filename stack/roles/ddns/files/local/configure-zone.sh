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

# mv -f /tmp/ddns/named.conf.local /etc/bind/named.conf.local
# mv -f /tmp/ddns/bind/db.* /var/lib/bind/

echo "zone \"queenbee.lan\" {
	type master;
	file \"/var/lib/bind/db.queenbee.lan\";
	allow-update { key rndc-key; };
};
zone \"53.168.192.in-addr.arpa\" {
	type master;
	file \"/var/lib/bind/db.queenbee.lan.inv\";
	allow-update { key rndc-key; };
};
" > /etc/bind/named.conf.local

echo "\$TTL	3600
@				IN		SOA		ddns.queenbee.lan. root.queenbee.lan. (
	2007010401	; Serial
	3600		; Refresh [1h]
	600			; Retry   [10m]
	86400		; Expire  [1d]
	600			; Negative Cache TTL [1h]
);

@				IN		NS		ddns.queenbee.lan.
@				IN		MX		2 ddns.queenbee.lan.

ddns			IN		A		192.168.53.2

pop				IN		CNAME	ddns
www				IN		CNAME	ddns
mail			IN		CNAME	ddns
" > /var/lib/bind/db.queenbee.lan

echo "@		IN		SOA		ddns.queenbee.lan. root.queenbee.lan. (
	2007010401	; Serial
	3600		; Refresh [1h]
	600			; Retry   [10m]
	86400		; Expire  [1d]
	600			; Negative Cache TTL [1h]
);

@		IN		NS		ddns.queenbee.lan.

2		IN		PTR		ddns.queenbee.lan.
" > /var/lib/bind/db.queenbee.lan.inv

service bind9 restart

