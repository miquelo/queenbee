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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class JKSPKeyStore
extends KeyStoreSpi
{
	private static final String REFERENCE_PROP_NAME = "reference";
	
	private KeyStoreProxy proxy;
	private URI reference;
	private Map<String, Object> extraParams;
	
	public JKSPKeyStore()
	{
		proxy = null;
		reference = null;
		extraParams = new HashMap<>();
	}
	
	@Override
	public void engineLoad(InputStream stream, char[] password)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		try
		{
			Properties properties = new Properties();
			properties.load(stream);
			if (!properties.contains(REFERENCE_PROP_NAME))
				throw new IOException("Proxy reference is not defined");
			URI ref = new URI(properties.getProperty(REFERENCE_PROP_NAME));
			Map<String, Object> params = new HashMap<>();
			for (Entry<Object, Object> entry : properties.entrySet())
			{
				Object propertyName = entry.getKey();
				if (!REFERENCE_PROP_NAME.equals(propertyName))
					params.put(propertyName.toString(), entry.getValue());
			}
			JKSPLoadStoreParameter param = new JKSPLoadStoreParameter(ref,
					new PasswordProtection(password), params);
			load(param);
		}
		catch (URISyntaxException exception)
		{
			throw new IOException("Bad KeyStore proxy reference", exception);
		}
	}
	
	@Override
	public void engineLoad(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		load(param);
	}
	
	@Override
	public void engineStore(OutputStream stream, char[] password)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		JKSPLoadStoreParameter param = new JKSPLoadStoreParameter(
				reference, new PasswordProtection(password), extraParams);
		store(param);
		Properties properties = new Properties();
		for (Entry<String, Object> entry : extraParams.entrySet())
			properties.put(entry.getKey(), entry.getValue());
		properties.store(stream, null);
	}
	
	@Override
	public void engineStore(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		store(param);
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
	
	private void load(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		try
		{
			if (param instanceof JKSPLoadStoreParameter)
			{
				JKSPLoadStoreParameter jkspParam =
						(JKSPLoadStoreParameter) param;
				reference = jkspParam.getReference();
				KeyStoreProxyFactory fact =
						KeyStoreProxyFactory.newSupported(reference);
				extraParams.clear();
				extraParams.putAll(jkspParam.getExtraParameters());
				proxy = fact.createProxy();
				proxy.load(jkspParam);
			}
			throwUnsupportedLoadStoreParameter(param);
		}
		catch (UnsupportedKeyStoreReferenceException exception)
		{
			throw new IOException("KeyStore load error", exception);
		}
	}
	
	private void store(LoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		if (param instanceof JKSPLoadStoreParameter)
		{
			JKSPLoadStoreParameter jkspParam =
					(JKSPLoadStoreParameter) param;
			proxy.store(jkspParam);
		}
		throwUnsupportedLoadStoreParameter(param);
	}
	
	private static void throwUnsupportedLoadStoreParameter(
			LoadStoreParameter param)
	throws IOException
	{
		StringBuilder msg = new StringBuilder();
		msg.append("Unsupported LoadStoreParameter type ");
		msg.append(param.getClass());
		throw new IOException(msg.toString());
	}
}