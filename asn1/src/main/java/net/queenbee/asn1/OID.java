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

package net.queenbee.asn1;

import java.io.Serializable;

/**
 * ASN.1 object identifier.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class OID
implements Serializable
{
	private static final long serialVersionUID = 8250303483922327832L;
	
	private int[] subId;
	
	/**
	 * Constructs an OID given its sub-identifiers.
	 * 
	 * @param id1
	 * 			First sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 */
	public OID(int id1, int... idn)
	{
		subId = new int[1 + idn.length];
		subId[0] = id1;
		for (int i = 1; i < idn.length; ++i)
			subId[i + 1] = idn[i];
	}
	
	/**
	 * Constructs an OID from its string representation.
	 * 
	 * @param str
	 * 			String representation.
	 * 
	 * @throws IllegalArgumentException
	 * 			If the string representation is not valid.
	 */
	public OID(String str)
	{
		// TODO ...
	}
	
	/**
	 * Constructs an OID given its sub-identifiers and base OID.
	 * 
	 * @param base
	 * 			Base OID.
	 * @param id1
	 * 			First sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 */
	public OID(OID base, int id1, int... idn)
	{
		// TODO ...
	}
	
	/**
	 * Constructs an OID from its string representation and the given base OID.
	 * 
	 * @param base
	 * 			Base OID.
	 * @param str
	 * 			String representation.
	 * 
	 * @throws IllegalArgumentException
	 * 			If the string representation is not valid.
	 */
	public OID(OID base, String str)
	{
		// TODO ...
	}
	
	/**
	 * Count of sub-identifiers of this OID.
	 * 
	 * @return
	 * 			Sub-identifier count.
	 */
	public int length()
	{
		return subId.length;
	}
	
	/**
	 * Get the sub-identifier located at the given position.
	 * 
	 * @param index
	 * 			Sub-identifier position.
	 * 			
	 * @return
	 * 			Value of the sub-identifier.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 			If {@code index < 0} or {@code index > size() - 1}.
	 */
	public int get(int index)
	{
		return subId[index];
	}
	
	/**
	 * Based on the first sub-identifier.
	 */
	@Override
	public int hashCode()
	{
		return subId[0];
	}
	
	/**
	 * Two OIDs are equals if, and only if they have same sub-identifiers.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof OID)
		{
			OID oid = (OID) obj;
			if (length() != oid.length())
				return false;
		}
		return false;
	}
	
	/**
	 * String representation of this OID.
	 */
	@Override
	public String toString()
	{
		// TODO ...
		return null;
	}
}