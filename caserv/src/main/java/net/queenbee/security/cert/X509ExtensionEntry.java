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

package net.queenbee.security.cert;

import java.io.Serializable;

/**
 * X.509 Certificate extension entry.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public abstract class X509ExtensionEntry
implements Serializable
{
	private static final long serialVersionUID = -6199420338732331202L;
	
	private String oid;
	private boolean critical;
	
	/**
	 * Protected constructor.
	 * 
	 * @param oid
	 * 			Object identifier of this extension entry.
	 * @param critical
	 * 			Whether this extension is critical or not.
	 */
	protected X509ExtensionEntry(String oid, boolean critical)
	{
		this.oid = oid;
		this.critical = critical;
	}

	/**
	 * Extension identifier.
	 */
	public String getOID()
	{
		return oid;
	}

	/**
	 * Set the extension identifier.
	 * 
	 * @param oid
	 * 			The new extension identifier.
	 */
	public void setOID(String oid)
	{
		this.oid = oid;
	}

	/**
	 * Whether extension is critical or not.
	 */
	public boolean isCritical()
	{
		return critical;
	}

	/**
	 * Set whether extension is critical or not.
	 * 
	 * @param critical
	 * 			The new critical value.
	 */
	public void setCritical(boolean critical)
	{
		this.critical = critical;
	}
	
	/**
	 * DER encoded extension value.
	 */
	public abstract byte[] getValue();
	
	/**
	 * String representation.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(oid);
		if (critical)
			sb.append(" [!] ");
		return sb.toString();
	}
	
	/**
	 * Hash by OID.
	 */
	@Override
	public int hashCode()
	{
		return oid == null ? 0 : oid.hashCode();
	}
	
	/**
	 * Equality by OID.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof X509ExtensionEntry)
		{
			X509ExtensionEntry entry = (X509ExtensionEntry) obj;
			return oid != null && oid.equals(entry.oid);
		}
		return false;
	}
}