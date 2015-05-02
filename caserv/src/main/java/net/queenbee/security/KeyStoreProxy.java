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
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;

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
	
	/**
	 * Checks if the given alias exists in this keystore.
	 * 
	 * @param alias
	 * 			The alias name.
	 * 
	 * @return
	 * 			{@code true} if the alias exists, {@code false} otherwise.
	 */
	public boolean containsAlias(String alias);
	
	/**
	 * Lists all the alias names of this keystore.
	 * 
	 * @return
	 * 			Enumeration of the alias names.
	 */
	public Enumeration<String> aliases();
	
	/**
	 * Retrieves the number of entries in this keystore.
	 * 
	 * @return
	 * 			The number of entries in this keystore.
	 */
	public int size();
	
	/**
	 * Returns {@code true} if the entry identified by the given alias was
	 * created by a call to {@code setCertificateEntry}, or created by a call to
	 * {@code setEntry} with a {@code TrustedCertificateEntry}.
	 * 
	 * @param alias
	 * 			The alias for the keystore entry to be checked.
	 * 
	 * @return
	 * 			{@code true} if the entry identified by the given alias contains
	 *			a trusted certificate, {@code false} otherwise.
	 */
	public boolean isCertificateEntry(String alias);
	
	/**
	 * Returns {@code true} if the entry identified by the given alias was
	 * created by a call to {@code setKeyEntry}, or created by a call to
	 * {@code setEntry} with a {@code PrivateKeyEntry} or a
	 * {@code SecretKeyEntry}.
	 * 
	 * @param alias
	 * 			The alias for the keystore entry to be checked.
	 * 
	 * @return
	 * 			{@code true} if the entry identified by the given alias is a
	 * 			key-related entry, {@code false} otherwise.
	 */
	public boolean isKeyEntry(String alias);
	
	/**
	 * Returns the (alias) name of the first keystore entry whose certificate
	 * matches the given certificate.
	 * 
	 * <p>
	 * This method attempts to match the given certificate with each keystore
	 * entry. If the entry being considered was created by a call to
	 * {@code setCertificateEntry}, or created by a call to {@code setEntry}
	 * with a {@code TrustedCertificateEntry}, then the given certificate is
	 * compared to that entry's certificate.
	 * </p>
	 * 
	 * <p>
	 * If the entry being considered was created by a call to
	 * {@code setKeyEntry}, or created by a call to {@code setEntry} with a
	 * {@code PrivateKeyEntry}, then the given certificate is compared to the
	 * first element of that entry's certificate chain.
	 * </p>
	 * 
	 * @param cert
	 * 			The certificate to match with.
	 * 
	 * @return
	 * 			The alias name of the first entry with a matching certificate,
	 * 			or {@code null} if no such entry exists in this keystore.
	 */
	public String getCertificateAlias(Certificate cert);
	
	/**
	 * Returns the creation date of the entry identified by the given alias.
	 * 
	 * @param alias
	 * 			The alias name.
	 * 
	 * @return
	 * 			The creation date of this entry, or {@code null} if the given
	 * 			alias does not exist.
	 */
	public Date getCreationDate(String alias);
	
	/**
	 * Returns the certificate associated with the given alias.
	 * 
	 * <p>
	 * If the given alias name identifies an entry created by a call to
	 * {@code setCertificateEntry}, or created by a call to {@code setEntry}
	 * with a {@code TrustedCertificateEntry}, then the trusted certificate
	 * contained in that entry is returned.
	 * </p>
	 * 
	 * <p>
	 * If the given alias name identifies an entry created by a call to
	 * {@code setKeyEntry}, or created by a call to {@code setEntry} with a
	 * {@code PrivateKeyEntry}, then the first element of the certificate chain
	 * in that entry is returned.
	 * </p>
	 * 
	 * @param alias
	 * 			The alias name.
	 * 
	 * @return
	 * 			The certificate, or {@code null} if the given alias does not
	 * 			exist or does not contain a certificate.
	 */
	public Certificate getCertificate(String alias);
	
	/**
	 * Returns the key associated with the given alias, using the given password
	 * to recover it. The key must have been associated with the alias by a call
	 * to {@code setKeyEntry}, or by a call to {@code setEntry} with a
	 * {@code PrivateKeyEntry} or {@code SecretKeyEntry}.
	 * 
	 * @param alias
	 * 			The alias name.
	 * @param password
	 * 			The password for recovering the key.
	 * 
	 * @return
	 * 			The requested key, or {@code null} if the given alias does not
	 * 			exist or does not identify a key-related entry.
	 * 
	 * @throws NoSuchAlgorithmException
	 * 			If the algorithm for recovering the key cannot be found.
	 * @throws UnrecoverableKeyException
	 * 			If the key cannot be recovered (e.g., the given password is
	 * 			wrong).
	 */
	public Key getKey(String alias, char[] password)
	throws NoSuchAlgorithmException, UnrecoverableKeyException;
	
	/**
	 * Returns the certificate chain associated with the given alias. The
	 * certificate chain must have been associated with the alias by a call to
	 * {@code setKeyEntry}, or by a call to {@code setEntry} with a
	 * {@code PrivateKeyEntry}.
	 * 
	 * @param alias
	 * 			The alias name.
	 * 
	 * @return
	 * 			The certificate chain (ordered with the user's certificate first
	 * 			followed by zero or more certificate authorities), or
	 * 			{@code null} if the given alias does not exist or does not
	 * 			contain a certificate chain.
	 */
	public Certificate[] getCertificateChain(String alias);
	
	/**
	 * Assigns the given trusted certificate to the given alias.
	 * 
	 * <p>
	 * If the given alias identifies an existing entry created by a call to
	 * {@code setCertificateEntry}, or created by a call to {@code setEntry}
	 * with a {@code TrustedCertificateEntry}, the trusted certificate in the
	 * existing entry is overridden by the given certificate.
	 * </p>
	 * 
	 * @param alias
	 * 			The alias name.
	 * @param cert
	 * 			The certificate.
	 * 
	 * @throws KeyStoreException
	 * 			If the keystore has not been initialized, or the given alias
	 * 			already exists and does not identify an entry containing a
	 * 			trusted certificate, or this operation fails for some other
	 * 			reason.
	 */
	public void setCertificateEntry(String alias, Certificate cert)
	throws KeyStoreException;
	
	/**
	 * Assigns the given key to the given alias, protecting it with the given
	 * password.
	 * 
	 * <p>
	 * If the given key is of type {@code java.security.PrivateKey}, it must be
	 * accompanied by a certificate chain certifying the corresponding public
	 * key.
	 * </p>
	 * 
	 * <p>
	 * If the given alias already exists, the keystore information associated
	 * with it is overridden by the given key (and possibly certificate chain).
	 * </p>
	 * 
	 * @param alias
	 * 			The alias name.
	 * @param key
	 * 			The key to be associated with the alias.
	 * @param password
	 * 			The password to protect the key.
	 * @param chain
	 * 			The certificate chain for the corresponding public key (only
	 * 			required if the given key is of type
	 * 			{@code java.security.PrivateKey}).
	 * 
	 * @throws KeyStoreException
	 * 			If the keystore has not been initialized (loaded), the given key
	 * 			cannot be protected, or this operation fails for some other
	 * 			reason.
	 */
	public void setKeyEntry(String alias, Key key, char[] password,
			Certificate[] chain)
	throws KeyStoreException;
	
	/**
	 * Assigns the given key (that has already been protected) to the given
	 * alias.
	 * 
	 * <p>
	 * If the protected key is of type {@code java.security.PrivateKey}, it must
	 * be accompanied by a certificate chain certifying the corresponding public
	 * key. If the underlying keystore implementation is of type {@code jks},
	 * key must be encoded as an {@code EncryptedPrivateKeyInfo} as defined in
	 * the PKCS #8 standard.
	 * </p>
	 * 
	 * <p>
	 * If the given alias already exists, the keystore information associated
	 * with it is overridden by the given key (and possibly certificate chain).
	 * </p>
	 * 
	 * @param alias
	 * 			The alias name.
	 * @param key
	 * 			The key (in protected format) to be associated with the alias.
	 * @param chain
	 * 			The certificate chain for the corresponding public key (only
	 * 			useful if the protected key is of type
	 * 			{@code java.security.PrivateKey}).
	 * 
	 * @throws KeyStoreException
	 * 			If the keystore has not been initialized (loaded), or if this
	 * 			operation fails for some other reason.
	 */
	public void setKeyEntry(String alias, byte[] key, Certificate[] chain)
	throws KeyStoreException;
	
	/**
	 * Deletes the entry identified by the given alias from this keystore.
	 * 
	 * @param alias
	 * 			The alias name.
	 * 
	 * @throws KeyStoreException
	 * 			If the entry cannot be removed.
	 */
	public void deleteEntry(String alias)
	throws KeyStoreException;
}
