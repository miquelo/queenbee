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
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.ProtectionParameter;

/**
 * Java KeyStore Proxy load/store parameter.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class JKSPLoadStoreParameter
implements LoadStoreParameter
{
	private URI reference;
	private ProtectionParameter protParam;
	
	/**
	 * Complete constructor.
	 * 
	 * @param reference
	 * 			Key store reference.
	 * @param protParam
	 * 			Protection parameter for accessing key store.
	 */
	public JKSPLoadStoreParameter(URI reference,
			ProtectionParameter protParam)
	{
		this.reference = reference;
		this.protParam = protParam;
	}
	
	/**
	 * Key store reference.
	 */
	public URI getReference()
	{
		return reference;
	}

	/**
	 * Protection parameter for accessing key store. 
	 */
	@Override
	public ProtectionParameter getProtectionParameter()
	{
		return protParam;
	}
}
