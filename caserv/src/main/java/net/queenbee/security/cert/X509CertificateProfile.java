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

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

/**
 * X.509 Certificate profile.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class X509CertificateProfile
implements CertificateProfile
{
	private static final long serialVersionUID = 6545465803339985515L;
	
	private PublicKey publicKey;
	private X500Principal subject;
	private BigInteger serialNumber;
	private Date notBefore;
	private Date notAfter;
	private String digestAlgorithm;
	private boolean[] issuerUniqueID;
	private boolean[] subjectUniqueID;
	private int basicConstraints;
	private boolean[] keyUsage;
	private List<String> extendedKeyUsage;
	private Set<X509ExtensionEntry> extensions;
	private AlgorithmParameters sigAlgParams;

	/**
	 * Certificate profile with its public key.
	 * 
	 * @param publicKey
	 * 			Public key of the certificate profile.
	 */
	public X509CertificateProfile(PublicKey publicKey)
	{
		this.publicKey = publicKey;
		serialNumber = BigInteger.ZERO;
		subject = null;
		notBefore = new Date();
		notAfter = new Date();
		digestAlgorithm = null;
		issuerUniqueID = null;
		subjectUniqueID = null;
		basicConstraints = -1;
		keyUsage = null;
		extendedKeyUsage = null;
		extensions = new HashSet<X509ExtensionEntry>();
		sigAlgParams = null;
	}

	/*
	 * Public key.
	 */
	@Override
	public PublicKey getPublicKey()
	{
		return publicKey;
	}

	/**
	 * Certificate serial number.
	 */
	public BigInteger getSerialNumber()
	{
		return serialNumber;
	}

	/**
	 * Set the certificate serial number.
	 * 
	 * @param serialNumber
	 * 			The new serial number.
	 */
	public void setSerialNumber(BigInteger serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	/**
	 * Certificate subject.
	 */
	public X500Principal getSubject()
	{
		return subject;
	}

	/**
	 * Set the certificate subject.
	 * 
	 * @param subject
	 * 			The new certificate subject.
	 */
	public void setSubject(X500Principal subject)
	{
		this.subject = subject;
	}

	/**
	 * Valid begin date, inclusive.
	 */
	public Date getNotBefore()
	{
		return notBefore;
	}

	/**
	 * Set the valid begin date.
	 * 
	 * @param notBefore
	 * 			The new valid begin date.
	 */
	public void setNotBefore(Date notBefore)
	{
		this.notBefore = notBefore;
	}

	/**
	 * Valid end date, inclusive.
	 */
	public Date getNotAfter()
	{
		return notAfter;
	}

	/**
	 * Set the valid end date.
	 * 
	 * @param notAfter
	 * 			The new valid end date.
	 */
	public void setNotAfter(Date notAfter)
	{
		this.notAfter = notAfter;
	}

	/**
	 * Certificate digest algorithm name.
	 */
	public String getDigestAlgorithm()
	{
		return digestAlgorithm;
	}

	/**
	 * Set the certificate digest algorithm name.
	 * 
	 * @param digestAlgorithm
	 * 			The new digest algorithm name.
	 */
	public void setDigestAlgorithm(String digestAlgorithm)
	{
		this.digestAlgorithm = digestAlgorithm;
	}

	/**
	 * Certificate issuer unique identifier.
	 */
	public boolean[] getIssuerUniqueID()
	{
		return issuerUniqueID;
	}

	/**
	 * Set the certificate issuer unique identifier.
	 *  
	 * @param issuerUniqueID
	 * 			The new issuer unique identifier.
	 */
	public void setIssuerUniqueID(boolean[] issuerUniqueID)
	{
		this.issuerUniqueID = issuerUniqueID;
	}

	/**
	 * Certificate subject unique identifier.
	 */
	public boolean[] getSubjectUniqueID()
	{
		return subjectUniqueID;
	}

	/**
	 * Set the certificate subject unique identifier.
	 * 
	 * @param subjectUniqueID
	 * 			The new subject unique identifier.
	 */
	public void setSubjectUniqueID(boolean[] subjectUniqueID)
	{
		this.subjectUniqueID = subjectUniqueID;
	}

	/**
	 * Certificate basic constraints.
	 * 
	 * <ul>
	 * <li>
	 * If value is -1, this profile is for a non CA certificate.
	 * </li>
	 * <li>
	 * If it is {@link Integer#MAX_VALUE}, this profile is for a CA certificate
	 * without a path length constraint.
	 * </li>
	 * <li>
	 * Otherwise, this profile is for a CA certificate with the given path
	 * constraint.
	 * </li>
	 * </ul>
	 * 
	 * <p>
	 * An extension entry with OID 2.5.29.19 shall take precedence over it.
	 * </p>
	 */
	public int getBasicConstraints()
	{
		return basicConstraints;
	}

	/**
	 * Set the certificate basic constraints.
	 * 
	 * @param basicConstraints
	 * 			The new basic constraints value.
	 */
	public void setBasicConstraints(int basicConstraints)
	{
		this.basicConstraints = basicConstraints;
	}

	/**
	 * Certificate key usage or {@code null} if does not has any value.
	 * 
	 * <p>
	 * An extension entry with OID 2.5.29.15 shall take precedence over it.
	 * </p>
	 */
	public boolean[] getKeyUsage()
	{
		return keyUsage;
	}

	/**
	 * Set the certificate key usage.
	 * 
	 * @param keyUsage
	 * 			The new key usage array. Can be {@code null}.
	 */
	public void setKeyUsage(boolean[] keyUsage)
	{
		this.keyUsage = keyUsage;
	}

	/**
	 * Certificate extended key usage or {@code null} if does not has any value.
	 * 
	 * <p>
	 * An extension entry with OID 2.5.29.37 shall take precedence over it.
	 * </p>
	 */
	public List<String> getExtendedKeyUsage()
	{
		return extendedKeyUsage;
	}

	/**
	 * Set the certificate extended key usage.
	 * 
	 * @param extendedKeyUsage
	 * 			The new extended key usage list. Can be {@code null}.
	 */
	public void setExtendedKeyUsage(List<String> extendedKeyUsage)
	{
		this.extendedKeyUsage = extendedKeyUsage;
	}

	/**
	 * Certificate extensions.
	 */
	public Set<X509ExtensionEntry> getExtensions()
	{
		return extensions;
	}

	/**
	 * Set the certificate extension set.
	 * 
	 * @param extensions
	 * 			The new certificate extension set.
	 */
	public void setExtensions(Set<X509ExtensionEntry> extensions)
	{
		this.extensions = extensions;
	}

	/**
	 * Signature algorithm parameters.
	 */
	public AlgorithmParameters getSigAlgParams()
	{
		return sigAlgParams;
	}

	/**
	 * Set the signature algorithm parameters.
	 * 
	 * @param sigAlgParams
	 * 			The new algorithm parameters.
	 */
	public void setSigAlgParams(AlgorithmParameters sigAlgParams)
	{
		this.sigAlgParams = sigAlgParams;
	}
}