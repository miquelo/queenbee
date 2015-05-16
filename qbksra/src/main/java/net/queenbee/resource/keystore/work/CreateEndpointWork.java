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

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.resource.spi.work.Work;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BERInputStream;
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
		try
		{
			logger.info("Creating endpoint work");
			createEndpointWork();
		}
		catch (IOException exception)
		{
			tryClose(socket);
			logger.severe(exception.getMessage());
		}
	}

	@Override
	public void release()
	{
	}
	
	private void createEndpointWork()
	throws IOException
	{
		try (BERInputStream in = new BERInputStream(socket.getInputStream()))
		{
			String listenerName = null;
			String keyStoreName = null;
			char[] password = null;
			
			ASN1Tag tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_SEQUENCE, true)))
				throw new IOException("Bad login sequence");
			
			tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_IA5_STRING, false)))
				throw new IOException("Bad listener name type");
			listenerName = in.readIA5String();
			if (listenerName.isEmpty())
				throw new IOException("Listener name is empty");
			in.skip();
			
			tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_IA5_STRING, false)))
				throw new IOException("Bad keystore name type");
			keyStoreName = in.readIA5String();
			if (keyStoreName.isEmpty())
				throw new IOException("Keystore name is empty");
			in.skip();
			
			tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_IA5_STRING, false)))
				throw new IOException("Bad password type");
			password = in.readIA5String().toCharArray();
			in.skip();
			
			in.skip();
			portWork.createEndpointWork(listenerName, keyStoreName, password,
					socket);
		}
		catch (BEREncodingException exception)
		{
			throw new IOException(exception);
		}
	}
	
	private static void tryClose(Closeable closeable)
	{
		try
		{
			closeable.close();
		}
		catch (IOException exception)
		{
			logger.severe(exception.getMessage());
		}
	}
}