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

import java.security.Principal;

public class KeyStorePrincipal
implements Principal
{
	private String keyStoreName;
	
	public KeyStorePrincipal(String keyStoreName)
	{
		this.keyStoreName = keyStoreName;
	}
	
	@Override
	public String getName()
	{
		return keyStoreName;
	}
	
	@Override
	public int hashCode()
	{
		return keyStoreName == null ? 0 : keyStoreName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof KeyStorePrincipal)
		{
			KeyStorePrincipal principal = (KeyStorePrincipal) obj;
			return keyStoreName != null && keyStoreName.equals(
					principal.keyStoreName);
		}
		return false;
	}
}