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

package net.queenbee.asn1;

/**
 * ASN.1 class.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public enum ASN1Class
{
	/**
	 * The type is native to ASN.1.
	 */
	UNIVERSAL,
	
	/**
	 * The type is only valid for one specific application.
	 */
	APPLICATION,
	
	/**
	 * Meaning of this type depends on the context (such as within a
	 * sequence, set or choice).
	 */
	CONTEXT_SPECIFIC,
	
	/**
	 * Defined in private specifications.
	 */
	PRIVATE
}