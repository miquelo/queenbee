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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(
	namespace="http://xmlns.queenbee.net/xml/ns/jrks",
	name="JRKSKeyStoreDefinitionType"
)
@XmlRootElement(
	namespace="http://xmlns.queenbee.net/xml/ns/jrks",
	name="keystore"
)
public class JRKSKeyStoreDefinition
{
	private URI reference;
	
	public JRKSKeyStoreDefinition()
	{
		reference = null;
	}

	@XmlElement(
		namespace="http://xmlns.queenbee.net/xml/ns/jrks",
		name="reference"
	)
	public URI getReference()
	{
		return reference;
	}

	public void setReference(URI reference)
	{
		this.reference = reference;
	}
}