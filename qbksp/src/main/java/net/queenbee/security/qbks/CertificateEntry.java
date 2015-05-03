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

public class CertificateEntry
extends Entry
{
	private Certificate certificate;
	
	public CertificateEntry(String alias, Certificate cert)
	{
		this(alias, new Date(), cert);
	}
	
	public CertificateEntry(String alias, Date creationDate,
			Certificate cert)
	{
		super(alias, creationDate);
		certificate = cert;
	}

	@Override
	public boolean match(Certificate cert)
	{
		return cert.equals(certificate);
	}

	@Override
	public boolean isCertificateEntry()
	{
		return true;
	}

	@Override
	public boolean isKeyEntry()
	{
		return false;
	}

	@Override
	public Certificate getCertificate()
	{
		return certificate;
	}

	@Override
	public byte[] getProtectedKey()
	{
		throw notKeyEntry();
	}

	@Override
	public Certificate[] getCertificateChain()
	{
		throw notKeyEntry();
	}
	
	private static IllegalStateException notKeyEntry()
	{
		return new IllegalStateException("Not a key entry");
	}
}