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

import java.security.PublicKey;

import javax.security.auth.x500.X500Principal;

/**
 * PKCS#10 certificate request.
 * 
 * @author Miquel A. Ferran Gonzalez &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public abstract class PKCS10CertificateRequest
extends CertificateRequest
{
	private static final long serialVersionUID = 2400802355002336334L;
	
	private static final String TYPE = "PKCS10";

	/**
	 * Protected constructor.
	 * 
	 * @param publicKey
	 * 			Request public key.
	 */
	protected PKCS10CertificateRequest(PublicKey publicKey)
	{
		super(TYPE, publicKey);
	}
	
	/**
	 * Encoded certificate request info.
	 * 
	 * @return
	 * 			Encoded request info.
	 * 
	 * @throws CertificateRequestEncodingException
	 * 			If an encoding error has been occurred.
	 */
	public abstract byte[] getCertificationRequestInfo()
	throws CertificateRequestEncodingException;
	
	/**
	 * Request version.
	 */
	public abstract int getVersion();

	/**
	 * Request subject.
	 */
	public abstract X500Principal getSubject();
	
	/**
	 * Signature algorithm OID.
	 */
	public abstract String getSigAlgOID();
	
	/**
	 * Signature algorithm name.
	 */
	public abstract String getSigAlgName();
	
	/**
	 * Signature algorithm parameters.
	 */
	public abstract byte[] getSigAlgParams();
	
	/**
	 * Request attributes.
	 */
	public abstract byte[] getAttributes();
	
	/**
	 * Request signature.
	 */
	public abstract byte[] getSignature();
}