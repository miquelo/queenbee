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

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;

/**
 * Certificate request factory.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public final class CertificateRequestFactory
{
	private static final String SERVICE_NAME = "CertificateRequestFactory";
	
	private CertificateRequestFactorySpi requestFactorySpi;
	private Provider provider;
	private String type;
	
	/**
	 * Protected constructor.
	 * 
	 * @param requestFactorySpi
	 * 			Factory Service Provider Interface implementation.
	 * @param provider
	 * 			Security Provider.
	 * @param type
	 * 			Type of certificate request factory.
	 */
	protected CertificateRequestFactory(
			CertificateRequestFactorySpi requestFactorySpi, Provider provider,
			String type)
	{
		this.requestFactorySpi = requestFactorySpi;
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
	 * Type of certificate request factory.
	 */
	public final String getType()
	{
		return type;
	}
	
	/**
	 * Generates a certificate request by reading it from the given input
	 * stream.
	 * 
	 * @param input
	 * 			Source input stream.
	 * 
	 * @return
	 * 			A certificate request instance.
	 * 
	 * @throws CertificateRequestException
	 * 			If the certificate request could not be read.
	 */
	public final CertificateRequest generateRequest(InputStream input)
	throws CertificateRequestException
	{
		return requestFactorySpi.engineGenerateRequest(input);
	}
	
	/**
	 * Retrieves an instance of a certificate request factory of the given type.
	 * 
	 * @param type
	 * 			Type of the certificate request factory.
	 * 
	 * @return
	 * 			Certificate request factory instance.
	 * 
	 * @throws CertificateRequestException
	 * 			If some error has been occurred.
	 */
	public static CertificateRequestFactory getInstance(String type)
	throws CertificateRequestException
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
			throw new CertificateRequestException(sb.toString());
		}
		return getInstance(type, providers[0]);
	}
	
	/**
	 * Retrieves an instance of a certificate request factory of the given type
	 * and security provider.
	 * 
	 * @param type
	 * 			Type of the certificate request factory.
	 * @param provider
	 * 			Security provider.
	 * 
	 * @return
	 * 			Certificate request factory instance.
	 * 
	 * @throws CertificateRequestException
	 * 			If some error has been occurred.
	 */
	public static CertificateRequestFactory getInstance(String type,
			Provider provider)
	throws CertificateRequestException
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
			throw new CertificateRequestException(sb.toString());
		}
		return newInstance(service);
	}
	
	/**
	 * Retrieves an instance of a certificate request factory of the given type
	 * and security provider by name.
	 * 
	 * @param type
	 * 			Type of the certificate request factory.
	 * @param provider
	 * 			Security provider name.
	 * 
	 * @return
	 * 			Certificate request factory instance.
	 * 
	 * @throws CertificateRequestException
	 * 			If some error has been occurred.
	 * @throws NoSuchProviderException
	 * 			Provider with the given name does not exist.
	 * @throws IllegalArgumentException
	 * 			If provider name is empty.
	 */
	public static CertificateRequestFactory getInstance(String type,
			String provider)
	throws CertificateRequestException, NoSuchProviderException,
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
	 * Creates a certificate request factory from the given Security Service.
	 */
	private static CertificateRequestFactory newInstance(Service service)
	throws CertificateRequestException
	{
		try
		{
			CertificateRequestFactorySpi factorySpi = 
					(CertificateRequestFactorySpi) service.newInstance(null);
			Provider provider = service.getProvider();
			String type = service.getAlgorithm();
			return new CertificateRequestFactory(factorySpi, provider, type);
		}
		catch (NoSuchAlgorithmException exception)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Service ");
			sb.append(service.getType());
			sb.append(": Algorithm ");
			sb.append(service.getAlgorithm());
			sb.append(" not available");
			throw new CertificateRequestException(sb.toString(), exception);
		}
		catch (ClassCastException exception)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Service ");
			sb.append(service.getType());
			sb.append(" Algorithm: ");
			sb.append(service.getAlgorithm());
			sb.append(" implemented by another class");
			throw new CertificateRequestException(sb.toString(), exception);
		}
	}
}