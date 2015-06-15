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

package net.queenbee.security.qbks;

import java.security.cert.Certificate;
import java.util.Date;

public class KeyEntry
extends Entry
{
	private byte[] protectedKey;
	private Certificate[] certificateChain;
	
	public KeyEntry(String alias, byte[] key, Certificate[] chain)
	{
		this(alias, new Date(), key, chain);
	}
	
	public KeyEntry(String alias, Date creationDate, byte[] key,
			Certificate[] chain)
	{
		super(alias, creationDate);
		protectedKey = key;
		certificateChain = chain;
	}

	@Override
	public boolean match(Certificate cert)
	{
		return cert.equals(getCertificate());
	}

	@Override
	public boolean isCertificateEntry()
	{
		return false;
	}

	@Override
	public boolean isKeyEntry()
	{
		return true;
	}

	@Override
	public Certificate getCertificate()
	{
		return certificateChain == null || certificateChain.length < 1
				? null : certificateChain[0];
	}

	@Override
	public byte[] getProtectedKey()
	{
		return protectedKey;
	}

	@Override
	public Certificate[] getCertificateChain()
	{
		return certificateChain;
	}
}