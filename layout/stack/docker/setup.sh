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

# Docker repository init script 
echo -e "#!/bin/bash
### BEGIN INIT INFO
# Provides:				docker-repo
# Required-Start:		\$local_fs \$network
# Required-Stop:		\$local_fs
# Default-Start:		2 3 4 5
# Default-Stop:			0 1 6
# Short-Description:	Docker Repository
# Description:			Repository for Docker images
### END INIT INFO

case \"\$1\" in
	start)
	docker run -d -p 5000:5000 --restart=always --name registry registry
	;;
	stop)
	docker stop --name registry
	;;
esac
" > /etc/init.d/docker-repo
chmod 755 /etc/init.d/docker-repo
update-rc.d docker-repo defaults

# Running Docker repository at provision time
/etc/init.d/docker-repo start

