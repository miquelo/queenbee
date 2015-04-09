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

/**
 * Exception occurred when retrieving an encoded certificate request.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class CertificateRequestEncodingException
extends Exception
{
	private static final long serialVersionUID = -8964773662429372662L;

	/**
	 * Empty exception.
	 */
	public CertificateRequestEncodingException()
	{
	}
	
	/**
	 * Exception with the given message.
	 * 
	 * @param message
	 * 			Exception message.
	 */
	public CertificateRequestEncodingException(String message)
	{
		super(message);
	}
	
	/**
	 * Exception with the given cause.
	 * 
	 * @param cause
	 * 			Exception cause.
	 */
	public CertificateRequestEncodingException(Throwable cause)
	{
		super(cause);
	}
	
	/**
	 * Exception with the given message and cause.
	 * 
	 * @param message
	 * 			Exception message.
	 * @param cause
	 * 			Exception cause.
	 */
	public CertificateRequestEncodingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
