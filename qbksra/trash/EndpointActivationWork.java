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

import java.util.logging.Logger;

import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import net.queenbee.resource.keystore.QBKeyStoreActivationSpec;
import net.queenbee.resource.keystore.QBKeyStoreResourceAdapter;
import net.queenbee.resource.keystore.util.Util;

public class EndpointActivationWork
implements Work
{
	private static final Logger logger;
	
	static
	{
		logger = Util.getPackageLogger();
	}
	
	private QBKeyStoreResourceAdapter ra;
	private QBKeyStoreActivationSpec spec;
	private WorkManager workManager;
	private MessageEndpointFactory endpointFactory;
	
	public EndpointActivationWork(QBKeyStoreResourceAdapter ra,
			QBKeyStoreActivationSpec spec, WorkManager workManager,
			MessageEndpointFactory endpointFactory)
	{
		this.ra = ra;
		this.spec = spec;
		this.workManager = workManager;
		this.endpointFactory = endpointFactory;
	}
	
	@Override
	public void run()
	{
		try
		{
			KeyStoreServiceWork serviceWork = new KeyStoreServiceWork(spec,
					workManager, endpointFactory.createEndpoint(null));
			workManager.scheduleWork(serviceWork);
			ra.setServiceWork(serviceWork);
			logger.info("QB keystore endpoint created");
		}
		catch (UnavailableException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Message endpoint is not available: ");
			msg.append(exception.getMessage());
			logger.info(msg.toString());
		}
		catch (WorkException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Could not start listener: ");
			msg.append(exception.getMessage());
			logger.info(msg.toString());
		}
	}

	@Override
	public void release()
	{
	}
}