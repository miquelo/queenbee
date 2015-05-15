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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.WorkContextProvider;

import net.queenbee.resource.keystore.util.Util;

public class EndpointWork
implements Work, WorkContextProvider
{
	private static final long serialVersionUID = -6648066241045476777L;
	
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private MessageEndpoint endpoint;
	private Socket socket;
	private KeyStoreSecurityContext securityContext;
	
	public EndpointWork(MessageEndpoint endpoint, String keyStoreName,
			char[] password, Socket socket)
	{
		this.endpoint = endpoint;
		this.socket = socket;
		securityContext = new KeyStoreSecurityContext(socket, keyStoreName,
				password);
	}
	
	@Override
	public void run()
	{
		try
		{
			logger.info("Running endpoint work");
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			// TODO Request keystore info, causing security check
		}
		catch (IOException exception)
		{
		}
	}

	@Override
	public void release()
	{
	}
	
	@Override
	public List<WorkContext> getWorkContexts()
	{
		return Arrays.<WorkContext>asList(securityContext);
	}
}