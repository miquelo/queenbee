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

package net.queenbee.resource.keystore;

import java.io.Serializable;

import javax.resource.ResourceException;
import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;

import net.queenbee.keystore.KeyStoreListener;

@Activation(
	messageListeners=KeyStoreListener.class
)
public class QBKeyStoreActivationSpec
implements ActivationSpec, Serializable
{
	private static final long serialVersionUID = 5340132307487762015L;
	
	private static final String DEFAULT_PORT = "6578";
	private static final String DEFAULT_MAXIMUM_CONNECTIONS = "20";
	
	private ResourceAdapter ra;
	private String port;
	private String maximumConnections;
	
	public QBKeyStoreActivationSpec()
	{
		ra = null;
		port = null;
		maximumConnections = null;
	}

	@Override
	public ResourceAdapter getResourceAdapter()
	{
		return ra;
	}

	@Override
	public void setResourceAdapter(ResourceAdapter ra)
	throws ResourceException
	{
		this.ra = ra;
	}

	@Override
	public void validate()
	throws InvalidPropertyException
	{
		if (!validInt(port))
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid port ").append(port);
			throw new InvalidPropertyException(msg.toString());
		}
	}

	@ConfigProperty(
		type=Integer.class,
		defaultValue=DEFAULT_PORT
	)
	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}
	
	@ConfigProperty(
		type=Integer.class,
		defaultValue=DEFAULT_MAXIMUM_CONNECTIONS
	)
	public String getMaximumConnections()
	{
		return maximumConnections;
	}

	public void setMaximumConnections(String maximumConnections)
	{
		this.maximumConnections = maximumConnections;
	}

	private static boolean validInt(String str)
	{
		try
		{
			Integer.parseInt(str);
			return true;
		}
		catch (Exception exception)
		{
			return false;
		}
	}
}