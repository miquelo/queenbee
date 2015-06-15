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
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import net.queenbee.resource.keystore.util.Util;
import net.queenbee.resource.keystore.work.PortWork;

@Connector(
	displayName="QueenBee KeyStore Resource Adapter",
	description="Resource adapter for QueenBee keystore endpoints",
	vendorName="QueenBee",
	eisType="QBKSRA",
	version="0.1",
	transactionSupport=TransactionSupportLevel.NoTransaction
)
public class KeyStoreResourceAdapter
implements ResourceAdapter, Serializable
{
	private static final long serialVersionUID = 2235931909271428353L;
	
	private static final String DEFAULT_PORT = "6578";
	private static final String DEFAULT_MAX_CONNECTIONS = "20";
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private String port;
	private String maxConnections;
	private PortWork portWork;
	
	public KeyStoreResourceAdapter()
	{
		port = null;
		maxConnections = null;
		portWork = null;
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
		defaultValue=DEFAULT_MAX_CONNECTIONS
	)
	public String getMaxConnections()
	{
		return maxConnections;
	}

	public void setMaxConnections(String maxConnections)
	{
		this.maxConnections = maxConnections;
	}

	@Override
	public void start(BootstrapContext ctx)
	throws ResourceAdapterInternalException
	{
		try
		{
			logger.info("Starting KeyStore resource adapter");
			
			WorkManager workManager = ctx.getWorkManager();
			portWork = new PortWork(workManager, getPortValue(),
					getMaxConnectionsValue());
			workManager.scheduleWork(portWork);
		}
		catch (Exception exception)
		{
			throw new ResourceAdapterInternalException(exception);
		}
	}

	@Override
	public void stop()
	{
		logger.info("Stopping KeyStore resource adapter");
		
		// PRE portWork != null
		portWork.release();
		portWork = null;
	}

	@Override
	public void endpointActivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	throws ResourceException
	{
		if (spec instanceof KeyStoreActivationSpec)
		{
			KeyStoreActivationSpec ksSpec = (KeyStoreActivationSpec) spec;
			String listenerName = ksSpec.getListenerName();
			
			StringBuilder msg = new StringBuilder();
			msg.append("Activating endpoint with listener name ");
			msg.append(listenerName);
			logger.info(msg.toString());
			
			// PRE portWork != null
			portWork.endpointActivation(listenerName, endpointFactory);
		}
		throw new ResourceException(unsupportedActivationSpec(spec));
	}
	
	@Override
	public void endpointDeactivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	{
		if (spec instanceof KeyStoreActivationSpec)
		{
			KeyStoreActivationSpec ksSpec = (KeyStoreActivationSpec) spec;
			String listenerName = ksSpec.getListenerName();
			
			StringBuilder msg = new StringBuilder();
			msg.append("Deactivating endpoint with listener name ");
			msg.append(listenerName);
			logger.info(msg.toString());
			
			// PRE portWork != null
			portWork.endpointDeactivation(listenerName, endpointFactory);
		}
		throw new IllegalStateException(unsupportedActivationSpec(spec));
	}

	@Override
	public XAResource[] getXAResources(ActivationSpec[] specs)
	throws ResourceException
	{
		return null;
	}
	
	private int getPortValue()
	throws ResourceAdapterInternalException
	{
		try
		{
			return Integer.parseInt(port);
		}
		catch (Exception exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid port value: ").append(port);
			throw new ResourceAdapterInternalException(msg.toString());
		}
	}
	
	private int getMaxConnectionsValue()
	throws ResourceAdapterInternalException
	{
		try
		{
			return maxConnections == null ? null
					: Integer.parseInt(maxConnections);
		}
		catch (Exception exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid max connections value: ").append(port);
			throw new ResourceAdapterInternalException(msg.toString());
		}
	}
	
	private static String unsupportedActivationSpec(ActivationSpec spec)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported activation spec of type ");
		msg.append(spec.getClass());
		return msg.toString();
	}
}