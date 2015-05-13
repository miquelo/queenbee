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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

public class X509CertificateAuthority
extends CertificateAuthoritySpi
{
	private PrivateKey privateKey;
	
	public X509CertificateAuthority()
	{
		privateKey = null;
	}
	
	@Override
	public void engineInit(PrivateKey privateKey)
	throws CertificateAuthorityException
	{
		this.privateKey = privateKey;
	}

	@Override
	public Certificate engineSignedCert(CertificateProfile profile)
	throws CertificateAuthorityException
	{
		X509CertificateProfile prof = profileCast(profile);
		return signedCert(prof, prof.getSubject(), prof.getSubjectUniqueID());
	}

	@Override
	public Certificate engineSignedCert(CertificateProfile profile,
			Certificate issuerCert)
	throws CertificateAuthorityException
	{
		X509CertificateProfile prof = profileCast(profile);
		X509Certificate cert = certificateCast(issuerCert);
		return signedCert(prof, cert.getSubjectX500Principal(),
				cert.getSubjectUniqueID());
	}
	
	private X509Certificate signedCert(X509CertificateProfile profile,
			X500Principal issuerPrincipal, boolean[] issuerUniqueID)
	throws CertificateAuthorityException
	{
		if (privateKey == null)
			throw new CertificateAuthorityException("Not initialized");
		
		try
		{
			PublicKey publicKey = profile.getPublicKey();
			X509CertificateV3 cert = new X509CertificateV3(publicKey);
			cert.setIssuer(issuerPrincipal);
			cert.setSubject(profile.getSubject());
			cert.setSerialNumber(profile.getSerialNumber());
			cert.setNotBefore(profile.getNotBefore());
			cert.setNotAfter(profile.getNotAfter());
			cert.setDigestAlgorithm(profile.getDigestAlgorithm());
			cert.setIssuerUniqueID(issuerUniqueID);
			cert.setSubjectUniqueID(profile.getSubjectUniqueID());
			
			Set<X509ExtensionEntry> extensions = new HashSet<>();
			
			// Basic constraints
			int basicConstraints = profile.getBasicConstraints();
			if (basicConstraints < 0)
				extensions.add(X509ExtensionEntries.createBasicConstraints(
						basicConstraints, true));
			
			// Key usage
			boolean[] keyUsage = profile.getKeyUsage();
			if (keyUsage != null)
				extensions.add(X509ExtensionEntries.createKeyUsage(keyUsage,
						true));
			
			// Extended key usage
			List<String> extendedKeyUsage = profile.getExtendedKeyUsage();
			if (extendedKeyUsage != null)
				extensions.add(X509ExtensionEntries.createExtendedKeyUsage(
						extendedKeyUsage, true));
			
			extensions.addAll(profile.getExtensions());
			cert.setExtensions(extensions);
			
			cert.setSigAlgParams(profile.getSigAlgParams());
			cert.sign(privateKey);
			return cert;
		}
		catch (Exception exception)
		{
			throw new CertificateAuthorityException(exception);
		}
	}
	
	private static X509CertificateProfile profileCast(
			CertificateProfile profile)
	throws CertificateAuthorityException
	{
		if (profile instanceof X509CertificateProfile)
			return (X509CertificateProfile) profile;
		
		StringBuffer sb = new StringBuffer();
		sb.append("Certificate profile of type ");
		sb.append(profile.getClass());
		sb.append(" is not supported by this certificate authority");
		throw new CertificateAuthorityException(sb.toString());
	}
	
	private static X509Certificate certificateCast(Certificate cert)
	throws CertificateAuthorityException
	{
		if (cert instanceof X509Certificate)
			return (X509Certificate) cert;
		
		StringBuffer sb = new StringBuffer();
		sb.append("Certificate of type ");
		sb.append(cert.getClass());
		sb.append(" is not supported by this certificate authority");
		throw new CertificateAuthorityException(sb.toString());
	}
}