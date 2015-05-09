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
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import net.queenbee.resource.keystore.util.Util;

@Connector(
	displayName="QBKeyStoreResourceAdapter",
	description="QueenBee KeyStore Adapter",
	vendorName="QueenBee",
	eisType="QBKSRA",
	version="0.1",
	transactionSupport=TransactionSupportLevel.NoTransaction
)
public class QBKeyStoreResourceAdapter
implements ResourceAdapter, Serializable
{
	private static final long serialVersionUID = 2235931909271428353L;
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private WorkManager workMgr;
	
	public QBKeyStoreResourceAdapter()
	{
		workMgr = null;
	}

	@Override
	public void start(BootstrapContext ctx)
	throws ResourceAdapterInternalException
	{
		workMgr = ctx.getWorkManager();
		
		logger.info("KeyStore resource adapter started");
	}

	@Override
	public void stop()
	{
		workMgr = null;
		
		logger.info("KeyStore resource adapter stopped");
	}

	@Override
	public void endpointActivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	throws ResourceException
	{
	}

	@Override
	public void endpointDeactivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	{
	}

	@Override
	public XAResource[] getXAResources(ActivationSpec[] specs)
	throws ResourceException
	{
		return null;
	}
}