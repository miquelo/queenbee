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

package net.queenbee.keystore;

import java.io.Serializable;
import java.util.Date;

/**
 * Keystore basic information.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class KeyStoreInfo
implements Serializable
{
	private static final long serialVersionUID = 4796724782203669975L;
	
	private Date creationDate;
	
	/**
	 * Empty constructor.
	 */
	public KeyStoreInfo()
	{
		creationDate = null;
	}

	/**
	 * Date when this keystore was created.
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}

	/**
	 * Set the date when this keystore was created.
	 * 
	 * @param creationDate
	 * 			The new creation date.
	 */
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
}