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
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.WorkContextProvider;

import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.keystore.KeyStoreInfo;
import net.queenbee.resource.keystore.util.Util;

public class EndpointWork
implements Work, WorkContextProvider
{
	private static final long serialVersionUID = -6648066241045476777L;
	
	private static final String METHOD_GET_INFO = "getInfo";
	
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
			
			OutputStream out = socket.getOutputStream();
			
			writeResponse(out, endpointCall(KeyStoreInfo.class,
					METHOD_GET_INFO));
		}
		catch (Exception exception)
		{
			Util.tryClose(socket);
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
	
	private <T> T endpointCall(Class<T> returnType, String methodName,
			Object... params)
	throws ResourceException
	{
		Method method = null;
		
		try
		{
			Class<?> type = endpoint.getClass();
			Class<?>[] parameterTypes = new Class<?>[params.length];
			for (int i = 0; i < params.length; ++i)
				parameterTypes[i] = params[i].getClass();
			method = type.getDeclaredMethod(methodName, parameterTypes);
		}
		catch (Exception exception)
		{
			throw new ResourceException(exception);
		}
		
		try
		{
			endpoint.beforeDelivery(method);
			return returnType.cast(method.invoke(endpoint, params));
		}
		catch (Exception exception)
		{
			throw new ResourceException(exception);
		}
		finally
		{
			endpoint.afterDelivery();
		}
	}
	
	private void writeResponse(OutputStream out, KeyStoreInfo keyStoreInfo)
	throws IOException, BEREncodingException
	{
		// TODO ...
	}
}