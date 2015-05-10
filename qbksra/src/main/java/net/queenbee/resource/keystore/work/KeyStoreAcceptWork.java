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
import java.net.Socket;
import java.util.logging.Logger;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BERInputStream;
import net.queenbee.resource.keystore.util.Util;

public class KeyStoreAcceptWork
implements Work
{
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private WorkManager workManager;
	private Socket socket;
	private MessageEndpoint endpoint;
	
	public KeyStoreAcceptWork(WorkManager workManager, Socket socket,
			MessageEndpoint endpoint)
	{
		this.workManager = workManager;
		this.socket = socket;
		this.endpoint = endpoint;
	}
	
	@Override
	public void run()
	{
		try (BERInputStream in = new BERInputStream(socket.getInputStream()))
		{
			ASN1Tag tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_SEQUENCE, true)))
				throw new BEREncodingException("Bad authentication sequence");
			
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_IA5_STRING, false)))
				throw new BEREncodingException("Bad keystore name");
			
			String keyStoreName = in.readIA5String();
			in.skip();
			if (keyStoreName.isEmpty())
				throw new BEREncodingException("Empty keystore name");
			
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_IA5_STRING, false)))
				throw new BEREncodingException("Bad password");
			
			String password = in.readIA5String();
			in.skip();
			if (password.isEmpty())
				throw new BEREncodingException("Empty password");
			
			in.skip();
			
			workManager.scheduleWork(new KeyStoreListenerWork(socket, endpoint,
					keyStoreName, password.toCharArray()));
		}
		catch (Exception exception)
		{
			logger.severe(exception.getMessage());
			release();
		}
	}

	@Override
	public void release()
	{
		try
		{
			socket.close();
		}
		catch (IOException exception)
		{
			logger.severe(exception.getMessage());
		}
	}
}