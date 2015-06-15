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

package net.queenbee.security.qbks;

import java.security.cert.Certificate;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntrySet
implements Iterable<Entry>
{
	private Set<Entry> entrySet;
	
	public EntrySet()
	{
		entrySet = new HashSet<Entry>();
	}
	
	@Override
	public Iterator<Entry> iterator()
	{
		return entrySet.iterator();
	}
	
	public int size()
	{
		return entrySet.size();
	}
	
	public Entry find(String alias)
	{
		for (Entry record : entrySet)
			if (record.match(alias))
				return record;
		return null;
	}
	
	public void put(String alias, Certificate cert)
	{
		entrySet.add(new CertificateEntry(alias, cert));
	}
	
	public void put(String alias, Date creationDate, Certificate cert)
	{
		entrySet.add(new CertificateEntry(alias, creationDate, cert));
	}
	
	public void put(String alias, byte[] key, Certificate[] chain)
	{
		entrySet.add(new KeyEntry(alias, key, chain));
	}
	
	public void put(String alias, Date creationDate, byte[] key,
			Certificate[] chain)
	{
		entrySet.add(new KeyEntry(alias, creationDate, key, chain));
	}
	
	public boolean remove(String alias)
	{
		Entry found = find(alias);
		if (found != null)
			return entrySet.remove(found);
		return false;
	}
}