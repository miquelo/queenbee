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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.OID;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BERInputStream;

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
}