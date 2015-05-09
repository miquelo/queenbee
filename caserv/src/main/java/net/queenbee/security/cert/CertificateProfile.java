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

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Parameters used by Certificate Authority on the certificate creation.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public interface CertificateProfile
extends Serializable
{
	/**
	 * The public key associated to the generated certificate.
	 * 
	 * @return
	 * 			The associated public key.
	 */
	public PublicKey getPublicKey();
}
