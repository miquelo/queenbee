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

package net.queenbee.ca.core;

import java.security.Principal;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;

import net.queenbee.keystore.KeyStoreInfo;
import net.queenbee.keystore.KeyStoreListener;

@MessageDriven(
	activationConfig={
		@ActivationConfigProperty(
			propertyName="listenerName",
			propertyValue="ca"
		)
	}
)
public class CAKeyStoreListenerBean
implements KeyStoreListener
{
	@Inject
	private MessageDrivenContext context;
	
	public CAKeyStoreListenerBean()
	{
		context = null;
	}
	
	@Override
	public KeyStoreInfo getInfo()
	{
		return null;
	}
	
	private String getKetStoreName()
	{
		Principal principal = context.getCallerPrincipal();
		return principal.getName();
	}
}