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

import net.queenbee.security.JKSPLoadStoreParameter;
import net.queenbee.security.KeyStoreProxy;

public class QBKSKeyStoreProxy
implements KeyStoreProxy
{
	public QBKSKeyStoreProxy()
	{
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
}