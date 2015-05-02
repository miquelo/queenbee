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

package net.queenbee.security;

import java.net.URI;
import java.util.ServiceLoader;

/**
 * KeyStore proxy factory.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public abstract class KeyStoreProxyFactory
{
	/**
	 * Protected constructor.
	 */
	protected KeyStoreProxyFactory()
	{
	}
	
	/**
	 * Determines if KeyStore reference is supported by this factory.
	 * 
	 * @param keyStoreRef
	 * 			Requested KeyStore reference.
	 * 
	 * @return
	 * 			Whether the given KeyStore reference is supported.
	 */
	public abstract boolean supported(URI keyStoreRef);
	
	/**
	 * Creates a KeyStore proxy.
	 * 
	 * @return
	 * 			The created proxy.
	 */
	public abstract KeyStoreProxy createProxy();
	
	/**
	 * Does a KeyStore factory lookup for a supported one and returns a new
	 * instance of it.
	 * 
	 * @param keyStoreRef
	 * 			The reference to be supported.
	 * 
	 * @return
	 * 			The supported factory instance.
	 * 
	 * @throws UnsupportedKeyStoreReferenceException
	 * 			When KeyStore reference is not supported by any available
	 * 			factory.
	 */
	public static KeyStoreProxyFactory newSupported(URI keyStoreRef)
	throws UnsupportedKeyStoreReferenceException
	{
		ServiceLoader<KeyStoreProxyFactory> loader =
				ServiceLoader.load(KeyStoreProxyFactory.class);
		for (KeyStoreProxyFactory fact : loader)
			if (fact.supported(keyStoreRef))
				return fact;
		throw new UnsupportedKeyStoreReferenceException(keyStoreRef);
	}
}
