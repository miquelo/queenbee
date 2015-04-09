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

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * X.509 Certificate extension entry.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class X509ExtensionEntry
implements Serializable
{
	private static final long serialVersionUID = -6199420338732331202L;
	
	private String oid;
	private byte[] value;
	private boolean critical;
	
	/**
	 * Empty constructor.
	 */
	public X509ExtensionEntry()
	{
		this(null, null, false);
	}
	
	/**
	 * Complete constructor.
	 * 
	 * @param oid
	 * @param value
	 * @param critical
	 */
	@ConstructorProperties({
		"oid",
		"value",
		"critical"
		
	})
	public X509ExtensionEntry(String oid, byte[] value, boolean critical)
	{
		this.oid = oid;
		this.value = value;
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
	 * Extension value.
	 */
	public byte[] getValue()
	{
		return value;
	}

	/**
	 * Set the extension value.
	 * 
	 * @param value
	 * 			The new extension value.
	 */
	public void setValue(byte[] value)
	{
		this.value = value;
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
	
	/*
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
	
	/*
	 * Hash by OID.
	 */
	@Override
	public int hashCode()
	{
		return oid == null ? 0 : oid.hashCode();
	}
	
	/*
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