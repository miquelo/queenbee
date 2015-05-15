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
import java.util.ArrayList;
import java.util.List;

import javax.resource.spi.work.SecurityContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;

public class KeyStoreSecurityContext
extends SecurityContext
{
	private static final long serialVersionUID = -4557446375146528380L;

	private Closeable closeable;
	private String keyStoreName;
	private char[] password;
	
	public KeyStoreSecurityContext(Closeable closeable, String keyStoreName,
			char[] password)
	{
		this.closeable = closeable;
		this.keyStoreName = keyStoreName;
		this.password = password;
	}
	
	@Override
	public void setupSecurityContext(CallbackHandler handler,
			Subject executionSubject, Subject serviceSubject)
	{
		try
		{
			executionSubject.getPrincipals().add(new KeyStorePrincipal(
					keyStoreName));
			List<Callback> callbackList = new ArrayList<>();
			
			CallerPrincipalCallback cpc = new CallerPrincipalCallback(
					executionSubject, new KeyStorePrincipal(keyStoreName));
			callbackList.add(cpc);
			
			PasswordValidationCallback pvc = new PasswordValidationCallback(
					executionSubject, keyStoreName, password);
			callbackList.add(pvc);
			
			Callback[] callbacks = new Callback[callbackList.size()];
			handler.handle(callbackList.toArray(callbacks));
			
			if (!pvc.getResult())
				closeable.close();
		}
		catch (UnsupportedCallbackException exception)
		{
		}
		catch (IOException exception)
		{
		}
	}
}