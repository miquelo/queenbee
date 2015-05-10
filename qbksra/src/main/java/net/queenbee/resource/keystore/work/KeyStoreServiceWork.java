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
import java.util.logging.Logger;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import net.queenbee.resource.keystore.QBKeyStoreActivationSpec;
import net.queenbee.resource.keystore.util.Util;

public class KeyStoreServiceWork
implements Work
{
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private QBKeyStoreActivationSpec spec;
	private WorkManager workManager;
	private MessageEndpoint endpoint;
	private ServerSocket serverSocket;
	
	public KeyStoreServiceWork(QBKeyStoreActivationSpec spec,
			WorkManager workManager, MessageEndpoint endpoint)
	{
		this.spec = spec;
		this.workManager = workManager;
		this.endpoint = endpoint;
		serverSocket = null;
	}
	
	@Override
	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(Integer.parseInt(spec.getPort()),
					Integer.parseInt(spec.getMaximumConnections()));
			while (acceptListenerWork());
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
			serverSocket.close();
		}
		catch (IOException exception)
		{
			logger.severe(exception.getMessage());
		}
	}
	
	private boolean acceptListenerWork()
	{
		try
		{
			workManager.scheduleWork(new KeyStoreAcceptWork(workManager,
					serverSocket.accept(), endpoint));
			return true;
		}
		catch (WorkException exception)
		{
			logger.severe(exception.getMessage());
			return true;
		}
		catch (IOException exception)
		{
			return false;
		}
	}
}