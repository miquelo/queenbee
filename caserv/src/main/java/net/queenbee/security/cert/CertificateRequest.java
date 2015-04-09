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

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;

/**
 * Certificate request.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public abstract class CertificateRequest
implements Serializable
{
	private static final long serialVersionUID = 8920223311753236650L;
	
	private String type;
	private PublicKey publicKey;
	
	/**
	 * Protected constructor of the given type and with the given public key.
	 * 
	 * @param type
	 * 			Certificate request type.
	 * @param publicKey
	 * 			Request public key.
	 */
	protected CertificateRequest(String type, PublicKey publicKey)
	{
		this.type = type;
		this.publicKey = publicKey;
	}

	/**
	 * Certificate type.
	 */
	public final String getType()
	{
		return type;
	}
	
	/**
	 * Request public key.
	 */
	public PublicKey getPublicKey()
	{
		return publicKey;
	}

	/**
	 * Gives the encoded certificate request.
	 * 
	 * @return
	 * 			The encoded certificate request.
	 * 
	 * @throws CertificateRequestEncodingException
	 * 			If an encoding error has been occurred.
	 */
	public abstract byte[] getEncoded()
	throws CertificateRequestEncodingException;
	
	/**
	 * Verify the certificate request with the given public key.
	 * 
	 * @param key
	 * 			Public key for verifying this certificate request.
	 * 
	 * @throws CertificateRequestException
	 * 			If an error has been occurred.
	 * @throws NoSuchAlgorithmException
	 * 			If algorithm is not supported.
	 * @throws InvalidKeyException
	 * 			If the given key is invalid.
	 * @throws NoSuchProviderException
	 * 			If no provider is available.
	 * @throws SignatureException
	 * 			If a signature error has been occurred.
	 */
	public abstract void verify(PublicKey key)
	throws CertificateRequestException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException;
	
	/**
	 * Verify the certificate request with the given public key.
	 * 
	 * @param key
	 * 			Public key for verifying this certificate request.
	 * @param sigProvider
	 * 			Signature provider name.
	 * 
	 * @throws CertificateRequestException
	 * 			If an error has been occurred.
	 * @throws NoSuchAlgorithmException
	 * 			If algorithm is not supported.
	 * @throws InvalidKeyException
	 * 			If the given key is invalid.
	 * @throws NoSuchProviderException
	 * 			If no provider is available.
	 * @throws SignatureException
	 * 			If a signature error has been occurred.
	 */
	public abstract void verify(PublicKey key, String sigProvider)
	throws CertificateRequestException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException;
}
