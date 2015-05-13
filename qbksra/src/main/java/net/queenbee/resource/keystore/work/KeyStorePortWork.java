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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;

import net.queenbee.resource.keystore.util.Util;

public class KeyStorePortWork
implements Work
{
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private WorkManager workManager;
	private int port;
	private Integer maxConnections;
	private ServerSocket serverSocket;
	private Map<String, MessageEndpointFactory> endpointFactoryMap;
	
	public KeyStorePortWork(WorkManager workManager, int port,
			Integer maxConnections)
	{
		this.workManager = workManager;
		this.port = port;
		this.maxConnections = maxConnections;
		serverSocket = null;
		endpointFactoryMap = new HashMap<>();
	}
	
	@Override
	public void run()
	{
		try
		{
			serverSocket = newServerSocket();
		}
		catch (IOException exception)
		{
			logger.severe(exception.getMessage());
		}
	}

	@Override
	public void release()
	{
		try
		{
			// TODO Signal to listeners and connected proxies
			serverSocket.close();
		}
		catch (IOException exception)
		{
			logger.severe(exception.getMessage());
		}
	}
	
	public void updateEndpointFactory(String listenerName,
			MessageEndpointFactory endpointFactory)
	{
		// TODO ...
	}
	
	public void releaseEndpointFactory(String listenerName,
			MessageEndpointFactory endpointFactory)
	{
		// TODO ...
	}
	
	private ServerSocket newServerSocket()
	throws IOException
	{
		if (maxConnections == null)
			return new ServerSocket(port);
		return new ServerSocket(port, maxConnections);
	}
}