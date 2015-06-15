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

import java.security.Provider;
import java.security.Provider.Service;
import java.util.Collections;

public class X509CertificateAuthorityService
extends Service
{
	private static final String TYPE = "Authority";
	private static final String ALGORITHM = "X509";
	private static final String CLASSNAME = serviceClassName();
	
	public X509CertificateAuthorityService(Provider provider)
	{
		super(provider, TYPE, ALGORITHM, CLASSNAME,
				Collections.<String>emptyList(),
				Collections.<String, String>emptyMap());
	}
	
	private static String serviceClassName()
	{
		return X509CertificateAuthority.class.getName();
	}
}