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

echo "#!/bin/bash
### BEGIN INIT INFO
# Provides:				ddns-update
# Required-Start:		\$local_fs \$network
# Required-Stop:		\$local_fs
# Default-Start:		2 3 4 5
# Default-Stop:			0 1 6
# Short-Description:	DDNS Update
# Description:			Updates domain for this host IP on DDNS
### END INIT INFO

INTERFACE=\"eth1\"
ZONE=\"queenbee.lan\"
DOMAIN_NAME=\$(hostname)
PRIMARY_IP=\$(ifconfig \$INTERFACE | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*')

case \"\$1\" in
	start)
	curl --data \"zone=\$ZONE&name=\$DOMAIN_NAME&ip=\$PRIMARY_IP\" http://ddns.\$ZONE/update 2> /dev/null
	;;
	stop)
	curl --data \"zone=\$ZONE&name=\$DOMAIN_NAME\" http://ddns.\$ZONE/delete 2> /dev/null
	;;
esac
" > /etc/init.d/ddns-update
chmod 755 /etc/init.d/ddns-update
update-rc.d ddns-update defaults

# Updating DDNS at provision time
/etc/init.d/ddns-update start

