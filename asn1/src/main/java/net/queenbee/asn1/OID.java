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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ASN.1 object identifier.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class OID
implements Serializable
{
	private static final long serialVersionUID = 8250303483922327832L;
	
	/**
	 * CCITT Recommendation OID.
	 */
	public static final OID CCITT_REC;
	
	/**
	 * CCITT Question OID.
	 */
	public static final OID CCITT_QUEST;
	
	/**
	 * CCITT Administration OID.
	 */
	public static final OID CCITT_ADMIN;
	
	/**
	 * CCITT Network Operator OID.
	 */
	public static final OID CCITT_NET_OP;
	
	/**
	 * ISO Standard OID.
	 */
	public static final OID ISO_STD;
	
	/**
	 * ISO Registration Authority OID.
	 */
	public static final OID ISO_REG_AUTH;
	
	/**
	 * ISO Member Body OID.
	 */
	public static final OID ISO_MEM_BODY;
	
	/**
	 * ISO Identified Organization OID.
	 */
	public static final OID ISO_ID_ORG;
	
	/**
	 * Join-ISO-CCITT DS OID.
	 */
	public static final OID JOIN_DS;
	
	static
	{
		CCITT_REC = newCCITT(0);
		CCITT_QUEST = newCCITT(1);
		CCITT_ADMIN = newCCITT(2);
		CCITT_NET_OP = newCCITT(3);
		
		ISO_STD = newISO(0);
		ISO_REG_AUTH = newISO(1);
		ISO_MEM_BODY = newISO(2);
		ISO_ID_ORG = newISO(3);
		
		JOIN_DS = newJOIN(5);
	}
	
	private int[] subId;
	
	/**
	 * Constructs an OID given its sub-identifiers.
	 * 
	 * @param id1
	 * 			First sub-identifier.
	 * @param id2
	 * 			Second sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 * 
	 * @throws IllegalArgumentException
	 * 			If first and/or second sub-identifiers are not valid.
	 */
	public OID(int id1, int id2, int... idn)
	{
		validate12(id1, id2);
		subId = new int[2 + idn.length];
		subId[0] = id1;
		subId[1] = id2;
		for (int i = 0; i < idn.length; ++i)
			subId[i + 2] = idn[i];
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
		subId = validated(parse(str));
	}
	
	/**
	 * Constructs an OID given its sub-identifiers and base OID.
	 * 
	 * @param base
	 * 			Base OID.
	 * @param idn
	 * 			Next sub-identifiers.
	 */
	public OID(OID base, int... idn)
	{
		subId = compound(base, idn);
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
		subId = compound(base, parse(str));
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
			return Arrays.equals(subId, oid.subId);
		}
		return false;
	}
	
	/**
	 * Equality comparison until the given depth.
	 * 
	 * @param oid
	 * 			Object identifier to be compared.
	 * @param depth
	 * 			Comparison depth from the first sub-identifier.
	 * 
	 * @return
	 * 			{@code true} if, and only if two object identifiers have same
	 * 			sub-identifiers until the given depth.
	 */
	public boolean equals(OID oid, int depth)
	{
		try
		{
			for (int i = 0; i < depth; ++i)
				if (subId[i] != oid.subId[i])
					return false;
			return true;
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	/**
	 * String representation of this OID.
	 */
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append(subId[0]);
		for (int i = 1; i < subId.length; ++i)
			str.append('.').append(subId[i]);
		return str.toString();
	}
	
	/**
	 * Creates an OID from an string representation.
	 * 
	 * @param str
	 * 			OID string representation.
	 * 
	 * @throws IllegalArgumentException
	 * 			If the string representation is not valid.
	 */
	public static OID parseOID(String str)
	{
		return new OID(str);
	}
	
	/*
	 * Custom serialization read.
	 */
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		subId = validated(parse(in.readUTF()));
	}
	
	/*
	 * Custom serialization write.
	 */
	private void writeObject(ObjectOutputStream out)
	throws IOException
	{
		out.writeChars(toString());
	}
	
	/*
	 * Parse from string and check number validity.
	 */
	private static int[] parse(String str)
	{
		try
		{
			List<Integer> valueList = new ArrayList<>();
			StringTokenizer tokenizer = new StringTokenizer(str, ".");
			while (tokenizer.hasMoreTokens())
				valueList.add(Integer.parseInt(tokenizer.nextToken()));
			
			int[] values = new int[valueList.size()];
			for (int i = 0; i < values.length; ++i)
				values[i] = valueList.get(i);
			return values;
		}
		catch (NumberFormatException exception)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid sub-identifier for OID representation '");
			msg.append(str).append("'");
			throw new IllegalArgumentException(msg.toString(), exception);
		}
	}
	
	/*
	 * Create values array from base OID and sub-values.
	 */
	private static int[] compound(OID base, int[] values)
	{
		int[] comp = new int[base.length() + values.length];
		for (int i = 0; i < base.length(); ++i)
			comp[i] = base.subId[i];
		for (int i = 0; i < values.length; ++i)
			comp[base.length() + i] = values[i];
		return comp;
	}
	
	/*
	 * Check complete OID values.
	 */
	private static int[] validated(int[] values)
	{
		if (values == null)
			throw new IllegalArgumentException("Null OID values");
		if (values.length < 2)
			throw new IllegalArgumentException("Too short OID");
		
		validate12(values[0], values[1]);
		return values;
	}
	
	/*
	 * Check first and second sub-identifiers.
	 */
	private static void validate12(int id1, int id2)
	{
		if (id1 < 0 || id1 > 2)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid first OID sub-identifier: ").append(id1);
			throw new IllegalArgumentException(msg.toString());
		}
		if (id2 < 0 || id2 > 39)
		{
			StringBuilder msg = new StringBuilder();
			msg.append("Invalid second OID sub-identifier: ").append(id2);
			throw new IllegalArgumentException(msg.toString());
		}
	}
	
	/**
	 * Creates a CCITT object identifier.
	 * 
	 * @param id2
	 * 			Second sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 * 
	 * @return
	 * 			The CCITT object identifier.
	 * 
	 * @throws IllegalArgumentException
	 * 			If second sub-identifier is not valid.
	 * 			
	 */
	private static OID newCCITT(int id2, int... idn)
	{
		return new OID(0, id2, idn);
	}
	
	/**
	 * Creates an ISO object identifier.
	 * 
	 * @param id2
	 * 			Second sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 * 
	 * @return
	 * 			The ISO object identifier.
	 * 
	 * @throws IllegalArgumentException
	 * 			If second sub-identifier is not valid.
	 * 			
	 */
	private static OID newISO(int id2, int... idn)
	{
		return new OID(1, id2, idn);
	}
	
	/**
	 * Creates a Join-ISO-CCITT object identifier.
	 * 
	 * @param id2
	 * 			Second sub-identifier.
	 * @param idn
	 * 			Next sub-identifiers.
	 * 
	 * @return
	 * 			The Join-ISO-CCITT object identifier.
	 * 
	 * @throws IllegalArgumentException
	 * 			If second sub-identifier is not valid.
	 * 			
	 */
	private static OID newJOIN(int id2, int... idn)
	{
		return new OID(2, id2, idn);
	}
}