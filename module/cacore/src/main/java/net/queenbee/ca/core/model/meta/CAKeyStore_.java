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

package net.queenbee.ca.core.model.meta;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

import net.queenbee.ca.core.model.CAKeyStore;

public class CAKeyStore_
{
	public static volatile SingularAttribute<CAKeyStore, String> name;
	
	public static volatile SingularAttribute<CAKeyStore, Date> creationDate;
}
