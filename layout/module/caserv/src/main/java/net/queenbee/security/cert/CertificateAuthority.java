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

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * Certificate Authority.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public final class CertificateAuthority
{
	private static final String SERVICE_NAME = "Authority";
	
	private CertificateAuthoritySpi authoritySpi;
	private Provider provider;
	private String type;
	
	/**
	 * Protected constructor.
	 * 
	 * @param authoritySpi
	 * 			Service Provider Interface implementation.
	 * @param provider
	 * 			Security Provider.
	 * @param type
	 * 			Type of Certificate Authority.
	 */
	protected CertificateAuthority(CertificateAuthoritySpi authoritySpi,
			Provider provider, String type)
	{
		this.authoritySpi = authoritySpi;
		this.provider = provider;
		this.type = type;
	}
	
	/**
	 * Security Provider.
	 */
	public final Provider getProvider()
	{
		return provider;
	}
	
	/**
	 * Type of Certificate Authority.
	 */
	public final String getType()
	{
		return type;
	}
	
	/**
	 * Initialize the Certificate Authority with the given private key.
	 * 
	 * @param issuerPrivateKey
	 * 			Issuer private key.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 */
	public final void init(PrivateKey issuerPrivateKey)
	throws CertificateAuthorityException
	{
		authoritySpi.engineInit(issuerPrivateKey);
	}
	
	/**
	 * Emits a signed certificate for the given profile.
	 * 
	 * @param profile
	 * 			Certificate profile.
	 * 
	 * @return
	 * 			Signed certificate.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 */
	public final Certificate signedCert(CertificateProfile profile)
	throws CertificateAuthorityException
	{
		return authoritySpi.engineSignedCert(profile);
	}
	
	/**
	 * Emits a signed certificate for the given profile and issuer certificate.
	 * 
	 * @param profile
	 * 			Certificate profile.
	 * @param issuerCert
	 * 			Issuer certificate.
	 * 
	 * @return
	 * 			Signed certificate.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 */
	public final Certificate signedCert(CertificateProfile profile,
			Certificate issuerCert)
	throws CertificateAuthorityException
	{
		return authoritySpi.engineSignedCert(profile, issuerCert);
	}
	
	/**
	 * Retrieves an instance of a Certificate Authority of the given type.
	 * 
	 * @param type
	 * 			Type of the Certificate Authority.
	 * 
	 * @return
	 * 			Certificate Authority instance.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 */
	public static CertificateAuthority getInstance(String type)
	throws CertificateAuthorityException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(SERVICE_NAME);
		sb.append('.');
		sb.append(type);
		Provider[] providers = Security.getProviders(sb.toString());
		
		if (providers == null || providers.length == 0)
		{
			sb = new StringBuilder();
			sb.append("No such provider supporting ");
			sb.append(SERVICE_NAME);
			sb.append(" service of type ");
			sb.append(type);
			throw new CertificateAuthorityException(sb.toString());
		}
		return getInstance(type, providers[0]);
	}
	
	/**
	 * Retrieves an instance of a Certificate Authority of the given type and
	 * security provider.
	 * 
	 * @param type
	 * 			Type of the Certificate Authority.
	 * @param provider
	 * 			Security provider.
	 * 
	 * @return
	 * 			Certificate Authority instance.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 */
	public static CertificateAuthority getInstance(String type,
			Provider provider)
	throws CertificateAuthorityException
	{
		Service service = provider.getService(SERVICE_NAME, type);
		if (service == null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(provider.getName());
			sb.append(' ');
			sb.append(provider.getVersion());
			sb.append(": ");
			sb.append(SERVICE_NAME);
			sb.append('.');
			sb.append(type);
			sb.append(" not available");
			throw new CertificateAuthorityException(sb.toString());
		}
		return newInstance(service);
	}
	
	/**
	 * Retrieves an instance of a Certificate Authority of the given type and
	 * security provider by name.
	 * 
	 * @param type
	 * 			Type of the Certificate Authority.
	 * @param provider
	 * 			Security provider name.
	 * 
	 * @return
	 * 			Certificate Authority instance.
	 * 
	 * @throws CertificateAuthorityException
	 * 			If some error has been occurred.
	 * @throws NoSuchProviderException
	 * 			Provider with the given name does not exist.
	 * @throws IllegalArgumentException
	 * 			If provider name is empty.
	 */
	public static CertificateAuthority getInstance(String type,
			String provider)
	throws CertificateAuthorityException, NoSuchProviderException,
			IllegalArgumentException
	{
		if (provider == null)
			throw new IllegalArgumentException("Null provider name");
		if (provider.isEmpty())
			throw new IllegalArgumentException("Empty provider name");
		
		Provider prov = Security.getProvider(provider);
		if (prov == null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("The provider '");
			sb.append(provider);
			sb.append("' is not registered in the security provider list");
			throw new NoSuchProviderException(sb.toString());
		}
		return getInstance(type, prov);
	}
	
	/*
	 * Creates a Certificate Authority from the given Security Service.
	 */
	private static CertificateAuthority newInstance(Service service)
	throws CertificateAuthorityException
	{
		try
		{
			CertificateAuthoritySpi authoritySpi = (CertificateAuthoritySpi)
					service.newInstance(null);
			Provider provider = service.getProvider();
			String type = service.getAlgorithm();
			return new CertificateAuthority(authoritySpi, provider, type); 
		}
		catch (NoSuchAlgorithmException exception)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Service ");
			sb.append(service.getType());
			sb.append(": Algorithm ");
			sb.append(service.getAlgorithm());
			sb.append(" not available");
			throw new CertificateAuthorityException(sb.toString(), exception);
		}
		catch (ClassCastException exception)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Service ");
			sb.append(service.getType());
			sb.append(" Algorithm: ");
			sb.append(service.getAlgorithm());
			sb.append(" implemented by another class");
			throw new CertificateAuthorityException(sb.toString(), exception);
		}
	}
}
