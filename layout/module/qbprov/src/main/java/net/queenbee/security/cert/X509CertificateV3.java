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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.OID;
import net.queenbee.asn1.io.BEREncodingException;
import net.queenbee.asn1.io.BEROutputStream;

public class X509CertificateV3
extends X509Certificate
{
	private static final int VERSION3 = 2;
	private static final int TBS_BUF_SIZE = 48 * 1024;
	private static final int ENCODE_BUF_SIZE = TBS_BUF_SIZE + 32 * 1024;
	
	private static final Map<String, OID> sigAlgs;
	
	static
	{
		OID usaOID = new OID(OID.ISO_MEM_BODY, 840);
		sigAlgs = new HashMap<String, OID>();
		
		OID pkcs1OID = new OID(usaOID, 113549, 1, 1);
		sigAlgs.put("MD2withRSA", new OID(pkcs1OID, 2));
		sigAlgs.put("MD5withRSA", new OID(pkcs1OID, 4));
		sigAlgs.put("SHA1withRSA", new OID(pkcs1OID, 5));
		sigAlgs.put("SHA224withRSA", new OID(pkcs1OID, 14));
		sigAlgs.put("SHA256withRSA", new OID(pkcs1OID, 11));
		sigAlgs.put("SHA384withRSA", new OID(pkcs1OID, 12));
		sigAlgs.put("SHA512withRSA", new OID(pkcs1OID, 13));
		
		sigAlgs.put("SHA1withDSA", new OID(usaOID, 10040, 4, 3));
		
		OID signOID = new OID(usaOID, 10045, 4);
		sigAlgs.put("SHA1withECDSA", new OID(signOID, 1));
		sigAlgs.put("SHA224withECDSA", new OID(signOID, 3, 1));
		sigAlgs.put("SHA256withECDSA", new OID(signOID, 3, 2));
		sigAlgs.put("SHA384withECDSA", new OID(signOID, 3, 3));
		sigAlgs.put("SHA512withECDSA", new OID(signOID, 3, 4));
	}
	
	private PublicKey publicKey;
	private X500Principal issuer;
	private X500Principal subject;
	private BigInteger serialNumber;
	private Date notBefore;
	private Date notAfter;
	private String digestAlgorithm;
	private boolean[] issuerUniqueID;
	private boolean[] subjectUniqueID;
	private Integer basicConstraints;
	private boolean keyUsageLoaded;
	private boolean[] keyUsage;
	private boolean extendedKeyUsageLoaded;
	private List<String> extendedKeyUsage;
	private Set<X509ExtensionEntry> extensions;
	private AlgorithmParameters sigAlgParams;
	private byte[] signatureBytes;
	private byte[] encoded;
	private byte[] tbsCertificate;
	
	public X509CertificateV3(PublicKey publicKey)
	{
		this.publicKey = publicKey;
		issuer = null;
		subject = null;
		serialNumber = BigInteger.ZERO;
		notBefore = new Date();
		notAfter = new Date();
		digestAlgorithm = null;
		issuerUniqueID = null;
		subjectUniqueID = null;
		basicConstraints = null;
		keyUsageLoaded = false;
		keyUsage = null;
		extendedKeyUsageLoaded = false;
		extendedKeyUsage = null;
		extensions = new HashSet<X509ExtensionEntry>();
		sigAlgParams = null;
		signatureBytes = null;
		encoded = null;
		tbsCertificate = null;
	}
	
	@Override
	public int getVersion()
	{
		return VERSION3;
	}
	
	@Override
	public PublicKey getPublicKey()
	{
		return publicKey;
	}

	@Override
	public Principal getIssuerDN()
	{
		return issuer;
	}

	public void setIssuer(X500Principal issuer)
	{
		this.issuer = issuer;
	}
	
	@Override
	public Principal getSubjectDN()
	{
		return subject;
	}
	
	public void setSubject(X500Principal subject)
	{
		this.subject = subject;
	}

	@Override
	public BigInteger getSerialNumber()
	{
		return serialNumber;
	}
	
	public void setSerialNumber(BigInteger serialNumber)
	{
		this.serialNumber = serialNumber;
	}
	
	@Override
	public Date getNotBefore()
	{
		return notBefore;
	}

	public void setNotBefore(Date notBefore)
	{
		this.notBefore = notBefore;
	}

	@Override
	public Date getNotAfter()
	{
		return notAfter;
	}

	public void setNotAfter(Date notAfter)
	{
		this.notAfter = notAfter;
	}
	
	@Override
	public boolean[] getIssuerUniqueID()
	{
		return issuerUniqueID;
	}

	public void setIssuerUniqueID(boolean[] issuerUniqueID)
	{
		this.issuerUniqueID = issuerUniqueID;
	}

	@Override
	public boolean[] getSubjectUniqueID()
	{
		return subjectUniqueID;
	}
	
	public void setSubjectUniqueID(boolean[] subjectUniqueID)
	{
		this.subjectUniqueID = subjectUniqueID;
	}

	@Override
	public int getBasicConstraints()
	{
		if (basicConstraints == null)
			basicConstraints = X509ExtensionEntries.getBasicConstraints(
					extensions);
		return basicConstraints;
	}
	
	@Override
	public boolean[] getKeyUsage()
	{
		if (!keyUsageLoaded)
		{
			keyUsage = X509ExtensionEntries.getKeyUsage(extensions);
			keyUsageLoaded = true;
		}
		return keyUsage;
	}
	
	@Override
	public List<String> getExtendedKeyUsage()
	throws CertificateParsingException
	{
		if (!extendedKeyUsageLoaded)
		{
			extendedKeyUsage = X509ExtensionEntries.getExtendedKeyUsage(
					extensions);
			extendedKeyUsageLoaded = true;
		}
		return extendedKeyUsage;
	}

	@Override
	public Set<String> getCriticalExtensionOIDs()
	{
		return getExtensionOIDs(true);
	}

	@Override
	public Set<String> getNonCriticalExtensionOIDs()
	{
		return getExtensionOIDs(false);
	}

	@Override
	public byte[] getExtensionValue(String oid)
	{
		for (X509ExtensionEntry extension : extensions)
			if (oid.equals(extension.getOID()))
				return extension.getValue();
		return null;
	}
	
	public void setExtensions(Set<X509ExtensionEntry> extensions)
	{
		this.extensions = extensions;
	}
	
	@Override
	public boolean hasUnsupportedCriticalExtension()
	{
		for (X509ExtensionEntry extension : extensions)
			if (extension.isCritical()
					&& !X509ExtensionEntries.supportedExtension(extension))
				return true;
		return false;
	}
	
	@Override
	public String getSigAlgOID()
	{
		OID sigAlgOID = sigAlgs.get(getSigAlgName());
		if (sigAlgOID == null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Could not determine signature algorithm OID for ");
			sb.append(getSigAlgName());
			throw new IllegalStateException(sb.toString());
		}
		return sigAlgOID.toString();
	}
	
	@Override
	public String getSigAlgName()
	{
		if (digestAlgorithm == null || digestAlgorithm.isEmpty())
			throw new IllegalStateException("Empty digest algorithm");
		
		StringBuilder sb = new StringBuilder();
		sb.append(digestAlgorithm.toUpperCase());
		sb.append(" with ");
		sb.append(publicKey.getAlgorithm().toUpperCase());
		return sb.toString();
	}
	
	@Override
	public byte[] getEncoded()
	throws CertificateEncodingException
	{
		if (encoded == null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(
					ENCODE_BUF_SIZE);
			try (BEROutputStream out = new BEROutputStream(baos))
			{
				// Certificate sequence
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_SEQUENCE, true));
				
				// TBS certificate
				out.writeRaw(getTBSCertificate());
				
				// Signature algorithm
				writeSignatureAlgorithm(out);
				
				// Signature value
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_BIT_STRING, false));
				out.write(getSignature());
				out.conclude(true);
				
				out.conclude(true);
			}
			catch (IOException | BEREncodingException exception)
			{
				baos.reset();
				throw new CertificateEncodingException(exception);
			}
			finally
			{
				encoded = baos.toByteArray();
			}
		}
		return encoded;
	}

	@Override
	public byte[] getTBSCertificate()
	throws CertificateEncodingException
	{
		if (tbsCertificate == null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(
					TBS_BUF_SIZE);
			try (BEROutputStream out = new BEROutputStream(baos))
			{
				// Certificate sequence
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_SEQUENCE, true));
				
				// Version 3
				out.writeTag(new ASN1Tag(ASN1Class.CONTEXT_SPECIFIC, 0, true));
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_INTEGER, false));
				out.writeInteger(BigInteger.valueOf(VERSION3));
				out.conclude(true);
				out.conclude(true);
				
				// Serial number
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_INTEGER, false));
				out.writeInteger(serialNumber);
				out.conclude(true);
				
				// Signature algorithm
				writeSignatureAlgorithm(out);
				
				// Issuer name
				out.writeRaw(issuer.getEncoded());
				
				// Validity
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_SEQUENCE, true));
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_UTC_TIME, false));
				out.writeUTCTime(notBefore);
				out.conclude(true);
				out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
						ASN1Tag.TN_UTC_TIME, false));
				out.writeUTCTime(notAfter);
				out.conclude(true);
				out.conclude(true);
				
				// Subject name
				out.writeRaw(subject.getEncoded());
				
				// Subject public key info
				out.writeRaw(publicKey.getEncoded());
				
				// Issuer unique ID
				writeUniqueID(out, getIssuerUniqueID(), 1);
				
				// Subject unique ID
				writeUniqueID(out, getSubjectUniqueID(), 2);
				
				// Extensions
				if (!extensions.isEmpty())
				{
					out.writeTag(new ASN1Tag(ASN1Class.CONTEXT_SPECIFIC, 3,
							true));
					
					// Extensions sequence
					out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
							ASN1Tag.TN_SEQUENCE, true));
					for (X509ExtensionEntry extension : extensions)
					{
						// Extension OID
						out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
								ASN1Tag.TN_OBJECT_INDENTIFIER, false));
						out.writeObjectIdentifier(OID.parseOID(
								extension.getOID()));
						out.conclude(true);
						
						// Extension critical
						out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
								ASN1Tag.TN_BOOLEAN, false));
						out.writeBoolean(extension.isCritical());
						out.conclude(true);
						
						// Extension value
						out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
								ASN1Tag.TN_OCTET_STRING, false));
						out.writeOctetString(extension.getValue());
						out.conclude(true);
					}
					out.conclude(true);
					
					out.conclude(true);
				}
				
				out.conclude(true);
			}
			catch (IOException | BEREncodingException exception)
			{
				baos.reset();
				throw new CertificateEncodingException(exception);
			}
			finally
			{
				tbsCertificate = baos.toByteArray();
			}
		}
		return tbsCertificate;
	}
	
	@Override
	public byte[] getSignature()
	{
		if (signatureBytes == null)
			throw new IllegalStateException("Certificate not signed");
		return signatureBytes;
	}
	
	@Override
	public byte[] getSigAlgParams()
	{
		try
		{
			return sigAlgParams == null ? null : sigAlgParams.getEncoded();
		}
		catch (IOException exception)
		{
			return null;
		}
	}
	
	public void setSigAlgParams(AlgorithmParameters sigAlgParams)
	{
		this.sigAlgParams = sigAlgParams;
	}

	public void setDigestAlgorithm(String digestAlgorithm)
	{
		this.digestAlgorithm = digestAlgorithm;
	}
	
	@Override
	public void checkValidity()
	throws CertificateNotYetValidException, CertificateExpiredException
	{
		checkValidity(new Date());
	}

	@Override
	public void checkValidity(Date date)
	throws CertificateNotYetValidException, CertificateExpiredException
	{
		if (date.before(notBefore))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Certificate is not valid before ");
			sb.append(notBefore);
			sb.append(", at ");
			sb.append(date);
			throw new CertificateNotYetValidException(sb.toString());
		}
		if (date.after(notAfter))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Certificate is not valid after ");
			sb.append(notAfter);
			sb.append(", at ");
			sb.append(date);
			throw new CertificateNotYetValidException(sb.toString());
		}
	}

	@Override
	public void verify(PublicKey key)
	throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException
	{
		verify(key, null);
	}

	@Override
	public void verify(PublicKey key, String sigProvider)
	throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException
	{
		Signature signature = null;
		if (sigProvider == null)
			signature = Signature.getInstance(getSigAlgName());
		else
			signature = Signature.getInstance(getSigAlgName(), sigProvider);
		signature.initVerify(key);
		signature.update(getTBSCertificate());
		if (!signature.verify(signatureBytes))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Invalid public key for ");
			sb.append(this);
			throw new InvalidKeyException(sb.toString());
		}
	}
	
	public void sign(PrivateKey privateKey)
	throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
			CertificateEncodingException
	{
		Signature signature = Signature.getInstance(getSigAlgName());
		signature.initSign(privateKey);
		signature.update(getTBSCertificate());
		signatureBytes = signature.sign();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("X509 Certificate (");
		sb.append(String.format("%X", serialNumber));
		sb.append(") ");
		sb.append(subject);
		sb.append(" issued by ");
		sb.append(issuer);
		return sb.toString();
	}
	
	private Set<String> getExtensionOIDs(boolean critical)
	{
		Set<String> extensionOIDs = new HashSet<String>();
		for (X509ExtensionEntry extension : extensions)
			if (extension.isCritical() == critical)
				extensionOIDs.add(extension.getOID());
		return Collections.unmodifiableSet(extensionOIDs);
	}
	
	private void writeSignatureAlgorithm(BEROutputStream out)
	throws IOException, BEREncodingException
	{
		out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
				ASN1Tag.TN_SEQUENCE, true));
		out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
				ASN1Tag.TN_OBJECT_INDENTIFIER, false));
		out.writeObjectIdentifier(OID.parseOID(getSigAlgOID()));
		out.conclude(true);
		byte[] algParams = getSigAlgParams();
		if (algParams != null)
			out.writeRaw(algParams);
		else
		{
			out.writeTag(new ASN1Tag(ASN1Class.UNIVERSAL,
					ASN1Tag.TN_NULL, false));
			out.conclude(true);
		}
		out.conclude(true);
	}
	
	private static void writeUniqueID(BEROutputStream out, boolean[] uniqueID,
			int tagNumber)
	throws IOException, BEREncodingException
	{
		if (uniqueID != null)
		{
			out.writeTag(new ASN1Tag(ASN1Class.CONTEXT_SPECIFIC, tagNumber,
					false));
			out.writeBitString(uniqueID);
			out.conclude(true);
		}
	}
}