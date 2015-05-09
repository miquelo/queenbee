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
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class ASN1Tag
implements Serializable
{
	private static final long serialVersionUID = -630147763738254334L;
	
	/**
	 * Universal tag number for EOC.
	 */
	public static final int TN_EOC = 0;
	
	/**
	 * Universal tag number for BOOLEAN type.
	 */
	public static final int TN_BOOLEAN = 1;
	
	/**
	 * Universal tag number for INTEGER type.
	 */
	public static final int TN_INTEGER = 2;
	
	/**
	 * Universal tag number for BIT STRING type.
	 */
	public static final int TN_BIT_STRING = 3;
	
	/**
	 * Universal tag number for OCTET STRING type.
	 */
	public static final int TN_OCTET_STRING = 4;
	
	/**
	 * Universal tag number for NULL type.
	 */
	public static final int TN_NULL = 5;
	
	/**
	 * Universal tag number for OBJECT IDENTIFIER type.
	 */
	public static final int TN_OBJECT_INDENTIFIER = 6;
	
	/**
	 * Universal tag number for Object Descriptor type.
	 */
	public static final int TN_OBJECT_DESCRIPTOR = 7;
	
	/**
	 * Universal tag number for EXTERNAL type.
	 */
	public static final int TN_EXTERNAL = 8;
	
	/**
	 * Universal tag number for REAL type.
	 */
	public static final int TN_REAL = 9;
	
	/**
	 * Universal tag number for ENUMERATED type.
	 */
	public static final int TN_ENUMERATED = 10;
	
	/**
	 * Universal tag number for EMBEDDED PDV type.
	 */
	public static final int TN_EMBEDDED_PDV = 11;
	
	/**
	 * Universal tag number for UTF8String type.
	 */
	public static final int TN_UTF8_STRING = 12;
	
	/**
	 * Universal tag number for RELATIVE-OID type.
	 */
	public static final int TN_RELATIVE_OID = 13;
	
	/**
	 * Universal tag number for SEQUENCE and SEQUENCE OF types.
	 */
	public static final int TN_SEQUENCE = 16;
	
	/**
	 * Universal tag number for SET and SET OF types.
	 */
	public static final int TN_SET = 17;
	
	/**
	 * Universal tag number for NumericString type.
	 */
	public static final int TN_NUMERIC_STRING = 18;
	
	/**
	 * Universal tag number for PrintableString type.
	 */
	public static final int TN_PRINTABLE_STRING = 19;
	
	/**
	 * Universal tag number for T61String type.
	 */
	public static final int TN_T61_STRING = 20;
	
	/**
	 * Universal tag number for VideotexString type.
	 */
	public static final int TN_VIDEOTEX_STRING = 21;
	
	/**
	 * Universal tag number for IA5String type.
	 */
	public static final int TN_IA5_STRING = 22;
	
	/**
	 * Universal tag number for UTCTime type.
	 */
	public static final int TN_UTC_TIME = 23;
	
	/**
	 * Universal tag number for GeneralizedTime type.
	 */
	public static final int TN_GENERALIZED_TIME = 24;
	
	/**
	 * Universal tag number for GraphicString type.
	 */
	public static final int TN_GRAPHIC_STRING = 25;
	
	/**
	 * Universal tag number for VisibleString type.
	 */
	public static final int TN_VISIBLE_STRING = 26;
	
	/**
	 * Universal tag number for GeneralString type.
	 */
	public static final int TN_GENERAL_STRING = 27;
	
	/**
	 * Universal tag number for UniversalString type.
	 */
	public static final int TN_UNIVERSAL_STRING = 28;
	
	/**
	 * Universal tag number for CHARACTER STRING type.
	 */
	public static final int TN_CHARACTER_STRING = 29;
	
	/**
	 * Universal tag number for BMPString type.
	 */
	public static final int TN_BMP_STRING = 30;
	
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