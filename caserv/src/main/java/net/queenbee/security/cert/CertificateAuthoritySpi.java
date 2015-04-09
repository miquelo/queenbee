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
import java.security.cert.Certificate;

/**
 * Certificate Authority SPI.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public abstract class CertificateAuthoritySpi
{
	/**
	 * Protected constructor to be overridden if necessary.
	 */
	protected CertificateAuthoritySpi()
	{
	}
	
	/**
	 * Initialize the Certificate Authority with the given private key.
	 * 
	 * @param privateKey
	 * 			Private key of the Certificate Authority.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If an error has been occurred.
	 */
	public abstract void engineInit(PrivateKey privateKey)
	throws CertificateAuthorityException;
	
	/**
	 * Emits a signed certificate for the given profile.
	 * 
	 * @param profile
	 * 			Certificate profile.
	 * 
	 * @return
	 * 			Signed certificate.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If an error has been occurred.
	 */
	public abstract Certificate engineSignedCert(CertificateProfile profile)
	throws CertificateAuthorityException;
	
	/**
	 * Emits a signed certificate for the given profile and the given issued
	 * certificate.
	 * 
	 * @param profile
	 * 			Certificate profile.
	 * @param issuerCert
	 * 			Issued certificate.
	 * 
	 * @return
	 * 			Signed certificate.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If an error has been occurred.
	 */
	public abstract Certificate engineSignedCert(CertificateProfile profile,
			Certificate issuerCert)
	throws CertificateAuthorityException;
}
