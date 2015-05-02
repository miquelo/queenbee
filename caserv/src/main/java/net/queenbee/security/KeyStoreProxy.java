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

package net.queenbee.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Generic contracts for KeyStore proxy.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public interface KeyStoreProxy
{
	/**
	 * KeyStore loading.
	 * 
	 * @param param
	 * 			Load/store parameter.
	 * 
	 * @throws IOException
	 * 			If there is an I/O or format problem with the keystore data, if
	 * 			a password is required but not given, or if the given password
	 * 			was incorrect. If the error is due to a wrong password, the
	 * 			{@link Throwable#getCause() cause} of the {@code IOException}
	 * 			should be an {@code UnrecoverableKeyException}.
	 * @throws NoSuchAlgorithmException
	 * 			If the algorithm used to check the integrity of the keystore
	 * 			cannot be found.
	 * @throws CertificateException
	 * 			If any of the certificates in the keystore could not be loaded.
	 */
	public void load(JKSPLoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException;
	
	/**
	 * KeyStore storing.
	 * 
	 * @param param
	 * 			Load/store parameter.
	 * 
	 * @throws IllegalArgumentException
	 * 			If the given LoadStoreParameter input is not recognized.
	 * @throws IOException
	 * 			If there was an I/O problem with data.
	 * @throws NoSuchAlgorithmException
	 * 			If the appropriate data integrity algorithm could not be found.
	 * @throws CertificateException
	 * 			If any of the certificates included in the keystore data could
	 * 			not be stored.
	 */
	public void store(JKSPLoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException;
}
