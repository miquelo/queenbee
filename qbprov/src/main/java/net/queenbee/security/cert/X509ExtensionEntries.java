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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.OID;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BERInputStream;
import net.queenbee.asn1.io.BEROutputStream;

public class X509ExtensionEntries
{
	private static final OID BASIC_CONSTRAINTS_OID;
	private static final OID KEY_USAGE_OID;
	private static final OID EXTENDED_KEY_USAGE_OID;
	
	private static final OID[] supportedExtensionOIDs;
	
	static
	{
		OID certExtOID = new OID(OID.JOINT_DS, 29);
		BASIC_CONSTRAINTS_OID = new OID(certExtOID, 19);
		KEY_USAGE_OID = new OID(certExtOID, 15);
		EXTENDED_KEY_USAGE_OID = new OID(certExtOID, 37);
		
		supportedExtensionOIDs = new OID[3];
		supportedExtensionOIDs[0] = BASIC_CONSTRAINTS_OID;
		supportedExtensionOIDs[1] = KEY_USAGE_OID;
		supportedExtensionOIDs[2] = EXTENDED_KEY_USAGE_OID;
	}
	
	private X509ExtensionEntries()
	{
	}
	
	public static boolean supportedExtension(X509ExtensionEntry extension)
	{
		for (OID supportedExtensionOID : supportedExtensionOIDs)
			if (supportedExtensionOID.equals(OID.parseOID(extension.getOID())))
				return true;
		return false;
	}
	
	public static int getBasicConstraints(Set<X509ExtensionEntry> extensions)
	{
		X509ExtensionEntry extension = findExtension(extensions,
				BASIC_CONSTRAINTS_OID);
		if (extension == null)
			return -1;
		
		try (BERInputStream in = openExtensionValue(extension))
		{
			// Must be a sequence
			ASN1Tag tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_SEQUENCE, true)))
				return -1;
			
			// Check for CA
			tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_BOOLEAN,
					false)) || !in.readBoolean())
				return -1;
			in.skip();
			
			// Get path length constraint
			tag = in.readTag();
			if (tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_INTEGER,
					false)))
				return in.readInteger().intValue();
			
			return Integer.MAX_VALUE;
		}
		catch (IOException | BEREncodingException exception)
		{
			return -1;
		}
	}
	
	public static boolean[] getKeyUsage(Set<X509ExtensionEntry> extenstions)
	{
		X509ExtensionEntry extension = findExtension(extenstions,
				KEY_USAGE_OID);
		if (extension == null)
			return null;
		
		try (BERInputStream in = openExtensionValue(extension))
		{
			ASN1Tag tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_BIT_STRING, false)))
				return null;
			
			return in.readBitString();
		}
		catch (IOException | BEREncodingException exception)
		{
			return null;
		}
	}
	
	public static List<String> getExtendedKeyUsage(
			Set<X509ExtensionEntry> extensions)
	{
		X509ExtensionEntry extension = findExtension(extensions,
				EXTENDED_KEY_USAGE_OID);
		if (extension == null)
			return null;
			
		try (BERInputStream in = openExtensionValue(extension))
		{
			// Must be a sequence
			ASN1Tag tag = in.readTag();
			if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_SEQUENCE, true)))
				return null;
			
			List<String> extKeyUsageList = new ArrayList<>();
			tag = in.readTag();
			while (!tag.isEOC())
			{
				if (!tag.equals(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_OBJECT_INDENTIFIER, false)))
					return null;
				
				OID oid = in.readObjectIdentifier();
				in.skip();
				extKeyUsageList.add(oid.toString());
				
				tag = in.readTag();
			}
			return extKeyUsageList;
		}
		catch (IOException | BEREncodingException exception)
		{
			return null;
		}
	}
	
	public static X509ExtensionEntry createBasicConstraints(int bc,
			boolean critical)
	{
		return new BasicConstraints(bc, critical);
	}
	
	public static X509ExtensionEntry createKeyUsage(boolean[] ku,
			boolean critical)
	{
		return new KeyUsage(ku, critical);
	}
	
	public static X509ExtensionEntry createExtendedKeyUsage(List<String> eku,
			boolean critical)
	{
		return new ExtendedKeyUsage(eku, critical);
	}
	
	private static X509ExtensionEntry findExtension(
			Set<X509ExtensionEntry> extensions, OID oid)
	{
		for (X509ExtensionEntry extension : extensions)
			if (oid.equals(OID.parseOID(extension.getOID())))
				return extension;
		return null;
	}
	
	private static BERInputStream openExtensionValue(
			X509ExtensionEntry extension)
	{
		return new BERInputStream(new ByteArrayInputStream(
				extension.getValue()));
	}
	
	private static abstract class X509ExtensionEntryBase
	extends X509ExtensionEntry
	{
		private static final long serialVersionUID = -4895179071991830029L;
		
		private byte[] value;
		
		protected X509ExtensionEntryBase(String oid, boolean critical)
		{
			super(oid, critical);
			value = null;
		}
		
		@Override
		public byte[] getValue()
		{
			if (value == null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try (BEROutputStream out = new BEROutputStream(baos))
				{
					encodeValue(out);
				}
				catch (IOException | BEREncodingException exception)
				{
					baos.reset();
				}
				finally
				{
					value = baos.toByteArray();
				}
			}
			return value;
		}
		
		protected abstract void encodeValue(BEROutputStream out)
		throws IOException, BEREncodingException;
	}
	
	private static class BasicConstraints
	extends X509ExtensionEntryBase
	{
		private static final long serialVersionUID = -3377855886236162549L;
		
		private int bc;
		
		public BasicConstraints(int bc, boolean critical)
		{
			super(BASIC_CONSTRAINTS_OID.toString(), critical);
			this.bc = bc;
		}

		@Override
		protected void encodeValue(BEROutputStream out)
		throws IOException, BEREncodingException
		{
			// Sequence
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_SEQUENCE,
					true));
			
			// CA
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_BOOLEAN,
					false));
			out.writeBoolean(bc >= 0);
			out.conclude(true);
			
			// Path length constraint
			if (bc < Integer.MAX_VALUE)
			{
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_INTEGER, false));
				out.writeInteger(BigInteger.valueOf(bc));
				out.conclude(true);
			}
			
			out.conclude(true);
		}
	}
	
	private static class KeyUsage
	extends X509ExtensionEntryBase
	{
		private static final long serialVersionUID = 4568156270883286101L;
		
		private boolean[] ku;
		
		public KeyUsage(boolean[] ku, boolean critical)
		{
			super(KEY_USAGE_OID.toString(), critical);
			this.ku = ku;
		}

		@Override
		protected void encodeValue(BEROutputStream out)
		throws IOException, BEREncodingException
		{
			// Bit string
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_BIT_STRING,
					false));
			out.writeBitString(ku);
			out.conclude(true);
		}
	}
	
	private static class ExtendedKeyUsage
	extends X509ExtensionEntryBase
	{
		private static final long serialVersionUID = 6197629796703703092L;
		
		private List<String> eku;
		
		public ExtendedKeyUsage(List<String> eku, boolean critical)
		{
			super(EXTENDED_KEY_USAGE_OID.toString(), critical);
			this.eku = eku;
		}

		@Override
		protected void encodeValue(BEROutputStream out)
		throws IOException, BEREncodingException
		{
			// Sequence
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL, ASN1Tag.TN_SEQUENCE,
					true));
			
			for (String oid : eku)
			{
				// Object identifier
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_OBJECT_INDENTIFIER, false));
				out.writeObjectIdentifier(OID.parseOID(oid));
				out.conclude(true);
			}
			
			out.conclude(true);
		}
	}
}