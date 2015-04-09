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
 * ASN.1 tag.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class ASN1Tag
implements Serializable
{
	private static final long serialVersionUID = -630147763738254334L;
	
	/**
	 * Universal tag number for End-Of-Content.
	 */
	public static final int TN_EOC = 0;
	
	private ASN1Class tagClass;
	private int tagNumber;
	private boolean constructed;
	
	/**
	 * EOC tag.
	 */
	public ASN1Tag()
	{
		this(ASN1Class.UNIVERSAL, TN_EOC, true);
	}
	
	/**
	 * Complete constructor.
	 * 
	 * @param tagClass
	 * 			Tag class.
	 * @param tagNumber
	 * 			Tag number.
	 * @param constructed
	 * 			Whether this tag is primitive or constructed.
	 */
	public ASN1Tag(ASN1Class tagClass, int tagNumber, boolean constructed)
	{
		this.tagClass = tagClass;
		this.tagNumber = tagNumber;
		this.constructed = constructed;
	}

	/**
	 * Tag class.
	 */
	public ASN1Class getTagClass()
	{
		return tagClass;
	}

	/**
	 * Set the tag class.
	 * 
	 * @param tagClass
	 * 			The new tag class.
	 */
	public void setTagClass(ASN1Class tagClass)
	{
		this.tagClass = tagClass;
	}

	/**
	 * Tag number.
	 */
	public int getTagNumber()
	{
		return tagNumber;
	}

	/**
	 * Set the tag number.
	 * 
	 * @param tagNumber
	 * 			The new tag number.
	 */
	public void setTagNumber(int tagNumber)
	{
		this.tagNumber = tagNumber;
	}

	/**
	 * Whether the tag is primitive or it is constructed.
	 */
	public boolean isConstructed()
	{
		return constructed;
	}

	/**
	 * Set whether the tag is primitive or it is constructed.
	 * 
	 * @param constructed
	 * 			The new primitive/constructed value.
	 */
	public void setConstructed(boolean constructed)
	{
		this.constructed = constructed;
	}
	
	/**
	 * Check if this tag is an End-Of-Content tag
	 * 
	 * @return
	 * 			Whether this is an EOC tag.
	 */
	public boolean isEOC()
	{
		return tagClass.equals(ASN1Class.UNIVERSAL) && tagNumber == TN_EOC
				&& constructed == false;
	}
	
	/**
	 * Hash code by tag number.
	 */
	@Override
	public int hashCode()
	{
		return tagNumber;
	}
	
	/**
	 * Equals only when all fields have same value.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof ASN1Tag)
		{
			ASN1Tag tag = (ASN1Tag) obj;
			return tagClass.equals(tag.tagClass) && tagNumber == tag.tagNumber
					&& constructed == tag.constructed;
		}
		return false;
	}
	
	/**
	 * String representation.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(tagClass).append(" ").append(tagNumber).append(" (");
		sb.append(constructed ? "CONSTRUCTED" : "PRIMITIVE");
		sb.append(")");
		return sb.toString();
	}
}