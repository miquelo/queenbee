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
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JRKSKeyStore
extends KeyStoreSpi
{
	public JRKSKeyStore()
	{
	}
	
	@Override
	public void engineLoad(InputStream stream, char[] password)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}
	
	@Override
	public void engineLoad(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}
	
	@Override
	public void engineStore(OutputStream stream, char[] password)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}
	
	@Override
	public void engineStore(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}
	
	@Override
	public boolean engineContainsAlias(String alias)
	{
		// TODO ...
		return false;
	}
	
	@Override
	public Enumeration<String> engineAliases()
	{
		// TODO ...
		return null;
	}
	
	@Override
	public int engineSize()
	{
		// TODO ...
		return 0;
	}
	
	@Override
	public boolean engineIsCertificateEntry(String alias)
	{
		// TODO ...
		return false;
	}
	
	@Override
	public boolean engineIsKeyEntry(String alias)
	{
		// TODO ...
		return false;
	}
	
	@Override
	public String engineGetCertificateAlias(Certificate cert)
	{
		// TODO ...
		return null;
	}
	
	@Override
	public Date engineGetCreationDate(String alias)
	{
		// TODO ...
		return null;
	}
	
	@Override
	public Certificate engineGetCertificate(String alias)
	{
		// TODO ...
		return null;
	}
	
	@Override
	public Key engineGetKey(String alias, char[] password)
	throws NoSuchAlgorithmException, UnrecoverableKeyException
	{
		// TODO ...
		return null;
	}
	
	@Override
	public Certificate[] engineGetCertificateChain(String alias)
	{
		// TODO ...
		return null;
	}
	
	@Override
	public void engineSetCertificateEntry(String alias, Certificate cert)
	throws KeyStoreException
	{
		// TODO ...
	}

	@Override
	public void engineSetKeyEntry(String alias, Key key, char[] password,
			Certificate[] chain)
	throws KeyStoreException
	{
		// TODO ...
	}

	@Override
	public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain)
	throws KeyStoreException
	{
		// TODO ...
	}
	
	@Override
	public void engineDeleteEntry(String alias)
	throws KeyStoreException
	{
		// TODO ...
	}
	
	private static JRKSKeyStoreDefinition definitionLoad(InputStream stream)
	throws JAXBException
	{
		JAXBContext ctx = JAXBContext.newInstance(JRKSKeyStoreDefinition.class);
		Unmarshaller u = ctx.createUnmarshaller();
		return (JRKSKeyStoreDefinition) u.unmarshal(stream);
	}
}