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
import java.net.Socket;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;

import net.queenbee.security.qbks.EntrySet;

public class QBKSKeyStoreProxy
implements KeyStoreProxy
{
	private EntrySet entrySet;
	private Socket socket;
	
	public QBKSKeyStoreProxy()
	{
		entrySet = new EntrySet();
		socket = null;
	}
	
	@Override
	public void load(JKSPLoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}

	@Override
	public void store(JKSPLoadStoreParameter param)
	throws IOException, NoSuchAlgorithmException, CertificateException
	{
		// TODO ...
	}

	@Override
	public boolean containsAlias(String alias)
	{
		// TODO ...
		return false;
	}

	@Override
	public Enumeration<String> aliases()
	{
		// TODO ...
		return null;
	}

	@Override
	public int size()
	{
		// TODO ...
		return 0;
	}

	@Override
	public boolean isCertificateEntry(String alias)
	{
		// TODO ...
		return false;
	}

	@Override
	public boolean isKeyEntry(String alias)
	{
		// TODO ...
		return false;
	}

	@Override
	public String getCertificateAlias(Certificate cert)
	{
		// TODO ...
		return null;
	}

	@Override
	public Date getCreationDate(String alias)
	{
		// TODO ...
		return null;
	}

	@Override
	public Certificate getCertificate(String alias)
	{
		// TODO ...
		return null;
	}

	@Override
	public Key getKey(String alias, char[] password)
	throws NoSuchAlgorithmException, UnrecoverableKeyException
	{
		// TODO ...
		return null;
	}

	@Override
	public Certificate[] getCertificateChain(String alias)
	{
		// TODO ...
		return null;
	}

	@Override
	public void setCertificateEntry(String alias, Certificate cert)
	throws KeyStoreException
	{
		// TODO ...
	}

	@Override
	public void setKeyEntry(String alias, Key key, char[] password,
			Certificate[] chain)
	throws KeyStoreException
	{
		// TODO ...
	}

	@Override
	public void setKeyEntry(String alias, byte[] key, Certificate[] chain)
	throws KeyStoreException
	{
		// TODO ...
	}

	@Override
	public void deleteEntry(String alias)
	throws KeyStoreException
	{
		// TODO ...
	}
}