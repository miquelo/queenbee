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
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BEROutputStream;
import net.queenbee.resource.keystore.util.Util;

public class PortWork
implements Work
{
	private static final Logger logger;
	
	private static final int CODE_UNAVAILABLE_LISTENER = 1;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private WorkManager workManager;
	private int port;
	private Integer maxConnections;
	private ServerSocket serverSocket;
	private Map<String, MessageEndpointFactory> endpointFactoryMap;
	
	public PortWork(WorkManager workManager, int port, Integer maxConnections)
	{
		this.workManager = workManager;
		this.port = port;
		this.maxConnections = maxConnections;
		serverSocket = null;
		endpointFactoryMap = new ConcurrentHashMap<>();
	}
	
	@Override
	public void run()
	{
		try
		{
			serverSocket = newServerSocket();
			while (acceptConnection());
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
	
	public void endpointActivation(String listenerName,
			MessageEndpointFactory endpointFactory)
	{
		endpointFactoryMap.put(listenerName, endpointFactory);
	}
	
	public void endpointDeactivation(String listenerName,
			MessageEndpointFactory endpointFactory)
	{
		endpointFactoryMap.remove(listenerName);
	}
	
	public void createEndpointWork(String listenerName, String keyStoreName,
			char[] password, Socket socket)
	{
		try
		{
			MessageEndpointFactory fact = endpointFactoryMap.get(listenerName);
			if (fact != null)
				workManager.scheduleWork(new EndpointWork(
						fact.createEndpoint(null), keyStoreName, password,
						socket));
			else
				sendUnavailableEndpointMessage(listenerName, socket);
		}
		catch (Exception exception)
		{
			logger.severe(exception.getMessage());
		}
	}
	
	private ServerSocket newServerSocket()
	throws IOException
	{
		if (maxConnections == null)
			return new ServerSocket(port);
		return new ServerSocket(port, maxConnections);
	}
	
	private boolean acceptConnection()
	{
		try
		{
			workManager.scheduleWork(new CreateEndpointWork(this,
					serverSocket.accept()));
			return true;
		}
		catch (SocketTimeoutException | WorkException exception)
		{
			return true;
		}
		catch (IOException exception)
		{
			return false;
		}
	}
	
	private static void sendUnavailableEndpointMessage(String listenerName,
			Socket socket)
	throws IOException
	{
		try (BEROutputStream out = new BEROutputStream(
				socket.getOutputStream()))
		{
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_SEQUENCE,
					true));
			
			out.writeInteger(BigInteger.valueOf(CODE_UNAVAILABLE_LISTENER));
			out.conclude(true);
			
			StringBuilder msg = new StringBuilder();
			msg.append("Unavailable listener ").append(listenerName);
			out.writeIA5String(msg.toString());
			out.conclude(true);
			
			out.conclude(true);
		}
		catch (IOException | BEREncodingException exception)
		{
			logger.severe(exception.getMessage());
		}
	}
}