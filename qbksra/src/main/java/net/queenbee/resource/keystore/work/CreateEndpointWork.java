/*
 * This file is part of QueenBee Project.
 *
 * QueenBee Project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QueenBee Project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QueenBee Project.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.queenbee.resource.keystore.work;

import java.net.Socket;
import java.util.logging.Logger;

import javax.resource.spi.work.Work;

import net.queenbee.resource.keystore.util.Util;

public class CreateEndpointWork
implements Work
{
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private PortWork portWork;
	private Socket socket;
	
	public CreateEndpointWork(PortWork portWork, Socket socket)
	{
		this.portWork = portWork;
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		logger.info("Creating endpoint work");
		
		// TODO Retrieve listenerName, keyStoreName and password from socket
		String listenerName = null;
		String keyStoreName = null;
		char[] password = null;
		portWork.createEndpointWork(listenerName, keyStoreName, password,
				socket);
	}

	@Override
	public void release()
	{
	}
}