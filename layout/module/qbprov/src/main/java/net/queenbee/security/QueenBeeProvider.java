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

import java.security.Provider;

import net.queenbee.security.cert.X509CertificateAuthorityService;

public final class QueenBeeProvider
extends Provider
{
	private static final long serialVersionUID = 7705967162008711422L;
	
	private static final String NAME = "QB";
	private static final double VERSION = 1.;
	
	public QueenBeeProvider()
	{
		super(NAME, VERSION, providerInfo());
		putService(new X509CertificateAuthorityService(this));
		putService(new JKSPKeyStoreService(this));
		// TODO putService(new PKCS10CertificateRequestFactoryService(this));
	}
	
	private static final String providerInfo()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Queen Bee provider v");
		sb.append(VERSION);
		sb.append(", implementing X.509 Certificate Authority services and ");
		sb.append("XML and XRKS KeyStore");
		return sb.toString();
	}
}