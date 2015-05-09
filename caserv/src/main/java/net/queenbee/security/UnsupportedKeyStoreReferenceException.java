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

import java.net.URI;

/**
 * Exception thrown when a KeyStore proxy reference is not supported.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class UnsupportedKeyStoreReferenceException
extends Exception
{
	private static final long serialVersionUID = -667026065342075405L;
	
	private URI unsupportedRef;
	
	/**
	 * Exception with the given unsupported reference.
	 * 
	 * @param unsupportedRef
	 * 			The unsupported reference.
	 */
	public UnsupportedKeyStoreReferenceException(URI unsupportedRef)
	{
		super(createMessage(unsupportedRef));
		this.unsupportedRef = unsupportedRef;
	}
	
	/**
	 * Unsupported KeyStore proxy reference.
	 */
	public URI getUnsupportedRef()
	{
		return unsupportedRef;
	}

	/*
	 * Exception message.
	 */
	private static String createMessage(URI unsupportedRef)
	{
		StringBuilder msg = new StringBuilder();
		msg.append("KeyStore proxy reference ").append(unsupportedRef);
		msg.append(" is not supported");
		return msg.toString();
	}
}
