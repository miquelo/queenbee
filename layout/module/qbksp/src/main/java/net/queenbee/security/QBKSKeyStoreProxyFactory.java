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

import net.queenbee.security.KeyStoreProxy;
import net.queenbee.security.KeyStoreProxyFactory;

public class QBKSKeyStoreProxyFactory
extends KeyStoreProxyFactory
{
	private static final String REFERENCE_SCHEME = "qbks";
	
	@Override
	public boolean supported(URI keyStoreRef)
	{
		return REFERENCE_SCHEME.equals(keyStoreRef.getScheme());
	}

	@Override
	public KeyStoreProxy createProxy()
	{
		return new QBKSKeyStoreProxy();
	}
}