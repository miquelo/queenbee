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

package net.queenbee.security.qbks;

import java.security.cert.Certificate;
import java.util.Date;

public abstract class Entry
{
	private String alias;
	private Date creationDate;
	
	public Entry()
	{
		this(null, null);
	}
	
	public Entry(String alias, Date creationDate)
	{
		this.alias = alias;
		this.creationDate = creationDate;
	}
	
	public String getAlias()
	{
		return alias;
	}
	
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	
	/*
	 * Used to determine if cached entry must be invalidated.
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}
	
	public boolean match(String alias)
	{
		return alias.equalsIgnoreCase(this.alias);
	}
	
	public abstract boolean match(Certificate cert);
	
	public abstract boolean isCertificateEntry();
	
	public abstract boolean isKeyEntry();
	
	public abstract Certificate getCertificate();
	
	public abstract byte[] getProtectedKey();
	
	public abstract Certificate[] getCertificateChain();
	
	@Override
	public int hashCode()
	{
		String lowerAlias = alias.toLowerCase();
		return lowerAlias == null ? 0 : lowerAlias.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Entry)
		{
			Entry entry = (Entry) obj;
			return alias != null && alias.equalsIgnoreCase(entry.alias);
		}
		return false;
	}
}