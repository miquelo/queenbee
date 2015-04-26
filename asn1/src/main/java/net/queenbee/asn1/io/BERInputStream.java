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

package net.queenbee.asn1.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.OID;

/**
 * BER input stream.
 * 
 * @author Miquel A. Ferran <miquel.ferran.gonzalez@gmail.com>
 */
public class BERInputStream
extends InputStream
{
	private static DateFormat[] utcFmt;
	
	static
	{
		utcFmt = new DateFormat[4];
		utcFmt[0] = new SimpleDateFormat("yyMMddHHmmz");
		utcFmt[1] = new SimpleDateFormat("yyMMddHHmmZ");
		utcFmt[2] = new SimpleDateFormat("yyMMddHHmmssz");
		utcFmt[3] = new SimpleDateFormat("yyMMddHHmmssZ");
	}
	
	private TagInput input;
	
	/**
	 * Creates a BER input stream for the given underlying input stream.
	 * 
	 * @param in
	 * 			Underlying input stream.
	 */
	public BERInputStream(InputStream in)
	{
		this.input = new RootTagInput(in);
	}
	
	/**
	 * Read the next tag.
	 * 
	 * @return
	 * 			The read tag.
	 * 
	 * @throws IOException
	 * 			If an input/output error has been occurred.
	 * @throws BEREncodingException
	 * 			If an encoding error has been occurred.
	 */
	public ASN1Tag readTag()
	throws IOException, BEREncodingException
	{
		ASN1Tag tag = new ASN1Tag();
		input = input.readTag(tag);
		return tag;
	}
	
	/**
	 * Read universal boolean.
	 * 
	 * @return
	 * 			Boolean value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public boolean readBoolean()
	throws IOException, BEREncodingException
	{
		int b = read();
		if (b < 0)
			throw new BEREncodingException("Empty boolean");
		return b > 0;
	}
	
	/**
	 * Read universal integer.
	 * 
	 * @return
	 * 			Integer value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public BigInteger readInteger()
	throws IOException, BEREncodingException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			final int bufSize = 32;
			byte[] buf = new byte[bufSize];
			int len = read(buf, 0, bufSize);
			while (len > 0)
			{
				baos.write(buf, 0, len);
				len = read(buf, 0, bufSize);
			}
			if (baos.size() == 0)
				throw new BEREncodingException("Empty integer");
			return new BigInteger(baos.toByteArray());
		}
	}
	
	/**
	 * Read universal bit string.
	 * 
	 * @return
	 * 			Bit string value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public boolean[] readBitString()
	throws IOException, BEREncodingException
	{
		int padding = read();
		if (padding < 0)
			throw new BEREncodingException("No padding octet available");
		
		List<Boolean> valueList = new ArrayList<>();
		int b = read();
		while (b >= 0)
		{
			for (int i = 0; i < 8; ++i)
			{
				valueList.add((b & 0x80) > 0);
				b = b << 1;
			}
			b = read();
		}
		
		boolean[] values = new boolean[valueList.size()];
		for (int i = 0; i < values.length - padding; ++i)
			values[i] = valueList.get(i);
		return values;
	}
	
	/**
	 * Read universal octet string.
	 * 
	 * @return
	 * 			Octet string value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public byte[] readOctetString()
	throws IOException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			int bufLen = 16;
			byte[] buf = new byte[bufLen];
			int len = read(buf, 0, bufLen);
			while (len > 0)
			{
				baos.write(buf, 0, len);
				len = read(buf, 0, bufLen);
			}
			return baos.toByteArray();
		}
	}
	
	/**
	 * Read universal object identifier.
	 * 
	 * @return
	 * 			Object identifier value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public OID readObjectIdentifier()
	throws IOException, BEREncodingException
	{
		int b = read();
		if (b < 0)
			throw new BEREncodingException("Empty object identifier");
		int id1 = b / 40;
		int id2 = b % 40;
		
		List<Integer> idnList = new ArrayList<>();
		boolean completed = false;
		int idi = 0;
		b = read();
		while (b >= 0)
		{
			idi = idi | (b & 0x7f);
			if ((b & 0x80) > 0)
			{
				idi = idi << 7;
				completed = false;
			}
			else
			{
				idnList.add(idi);
				idi = 0;
				completed = true;
			}
		}
		if (!completed)
			throw new BEREncodingException("Incomplete object identifier");
		
		int[] idn = new int[idnList.size()];
		for (int i = 0; i < idn.length; ++i)
			idn[i] = idnList.get(i);
		return new OID(id1, id2, idn);
	}
	
	/**
	 * Read universal real.
	 * 
	 * @return
	 * 			Real value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public BigDecimal readReal()
	throws IOException
	{
		// TODO Read BER universal real
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Read universal UTF-8 string.
	 * 
	 * @return
	 * 			UTF-8 string value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public String readUTF8String()
	throws IOException, BEREncodingException
	{
		Reader reader = new InputStreamReader(this, StandardCharsets.UTF_8);
		StringBuilder sb = new StringBuilder();
		int bufLen = 32;
		char[] buf = new char[bufLen];
		int len = reader.read(buf, 0, bufLen);
		while (len > 0)
		{
			sb.append(buf);
			len = reader.read(buf, 0, bufLen);
		}
		return sb.toString();
	}
	
	/**
	 * Read universal UTC time.
	 * 
	 * @return
	 * 			UTC time value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public Date readUTCTime()
	throws IOException, BEREncodingException
	{
		Reader reader = new InputStreamReader(this, StandardCharsets.US_ASCII);
		StringBuilder sb = new StringBuilder();
		int minLen = 10;
		int maxLen = 17;
		char[] minBuf = new char[minLen];
		int len = reader.read(minBuf, 0, minLen);
		if (len < minLen)
			throw new BEREncodingException("Incomplete UTC time");
		sb.append(minBuf);
		
		int c = reader.read();
		while (c >= 0 && sb.length() < maxLen)
		{
			sb.append((char) c);
			for (DateFormat df : utcFmt)
				try
				{
					return df.parse(sb.toString());
				}
				catch (ParseException exception)
				{
				}
			c = reader.read();
		}
		throw new BEREncodingException("Invalid UTC time");
	}
	
	/**
	 * Read universal generalized time.
	 * 
	 * @return
	 * 			Generalized time value.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 * @throws BEREncodingException
	 * 			If some encoding error has been occurred.
	 */
	public Date readGeneralizedTime()
	throws IOException, BEREncodingException
	{
		// TODO Read BER universal generalized time
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Skips the current tag.
	 * 
	 * @throws IOException
	 * 			If an input/output error has been occurred.
	 * @throws BEREncodingException
	 * 			If an encoding error has been occurred.
	 */
	public void skip()
	throws IOException, BEREncodingException
	{
		input = input.skip();
	}
	
	/**
	 * Read the next available byte of primitive content.
	 */
	@Override
	public int read()
	throws IOException
	{
		try
		{
			return input.readByte();
		}
		catch (BEREncodingException exception)
		{
			throw new IOException(exception);
		}
	}
	
	/**
	 * Close the underlying input stream.
	 */
	@Override
	public void close()
	throws IOException
	{
		input.close();
	}
	
	private static TagInput constructedReadTag(TagInput input, ASN1Tag tag)
	throws IOException, BEREncodingException
	{
		byte[] b = new byte[1];
		
		// Available length check
		if (input.read(b, 0, 1) < 1)
			return input;
		
		// Tag identifier data
		boolean eocTag = false;
		ASN1Class tc = ASN1Class.UNIVERSAL;
		boolean constr = false;
		int tn = 0;
		
		// Promoted to be an EOC tag
		if (b[0] == 0x00)
			eocTag = true;
		// Not an EOC tag
		else
		{
			// Tag number
			tn = b[0] & 0x1f;
			// Primitive/constructed
			constr = ((b[0] >> 5) & 0x01) == 0x01;
			// Tag class
			tc = ASN1Class.values()[(b[0] >> 6) & 0x03];
			
			// Tag number: Long form
			if (tn == 0x1f)
			{
				tn = 0;
				do
				{
					if (input.read(b, 0, 1) < 1l)
						throw new BEREncodingException("Incomplete tag number");
					tn += b[0] & 0x7f;
				}
				while ((b[0] & 0x80) != 0);
			}
		}
		
		// Tag length
		if (input.read(b, 0, 1) < 1)
			throw new BEREncodingException("No length");
			
		// Is an EOC 
		if (eocTag && b[0] == 0x00)
			return input;
		
		BigInteger len = null;
		int b7f = b[0] & 0x7f;
		
		// Tag length: Short form
		if ((b[0] & 0x80) == 0)
			len = BigInteger.valueOf(b7f);
		// Tag length: Definite long form
		else if (b7f > 0)
		{
			byte[] lenBuf = new byte[b7f];
			if (input.read(lenBuf, 0, b7f) < b7f)
				throw new BEREncodingException("Incomplete length");
			len = new BigInteger(lenBuf);
		}
		
		// Determine inner input
		TagInput innerInput = null;
		if (constr)
			innerInput = len == null ? new UndefiniteConstructedTagInput(input)
					: new DefiniteConstructedTagInput(input, len);
		else if (len == null)
			throw new BEREncodingException("Undefinite primitive tag");
		else
			innerInput = new PrimitiveTagInput(input, len);
		
		// Tag construction
		tag.setTagClass(tc);
		tag.setTagNumber(tn);
		tag.setConstructed(constr);
		
		return innerInput;
	}
	
	private static abstract class TagInput
	extends InputStream
	{
		public abstract int readByte()
		throws IOException, BEREncodingException;
		
		public abstract TagInput readTag(ASN1Tag tag)
		throws IOException, BEREncodingException;
		
		public abstract TagInput skip()
		throws IOException, BEREncodingException;
	}
	
	private static abstract class DefiniteTagInput
	extends TagInput
	{
		protected TagInput parent;
		private BigInteger avail;
		
		protected DefiniteTagInput(TagInput parent, BigInteger length)
		{
			this.parent = parent;
			avail = length;
		}
		
		@Override
		public TagInput skip()
		throws IOException, BEREncodingException
		{
			while (avail.compareTo(BigInteger.ZERO) > 0)
			{
				long l = parent.skip(avail.longValue());
				if (l < 1l)
					throw new IOException("No bytes available");
				avail = avail.subtract(BigInteger.valueOf(l));
			}
			return parent;
		}

		@Override
		public int read()
		throws IOException
		{
			if (avail.compareTo(BigInteger.ZERO) > 0)
			{
				int r = parent.read();
				if (r > 0)
					avail = avail.subtract(BigInteger.ONE);
				return r;
			}
			return -1;
		}
	}
	
	private static class RootTagInput
	extends TagInput
	{
		private InputStream in;
		
		public RootTagInput(InputStream in)
		{
			this.in = in;
		}
		
		@Override
		public int readByte()
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Reading byte from root input");
		}

		@Override
		public TagInput readTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			return constructedReadTag(this, tag);
		}
		
		@Override
		public TagInput skip()
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Skipping root input");
		}
		
		@Override
		public int read()
		throws IOException
		{
			return in.read();
		}
	}
	
	private static class PrimitiveTagInput
	extends DefiniteTagInput
	{
		public PrimitiveTagInput(TagInput parent, BigInteger length)
		{
			super(parent, length);
		}

		@Override
		public int readByte()
		throws IOException, BEREncodingException
		{
			return read();
		}

		@Override
		public TagInput readTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Reading tag from primitive");
		}
	}
	
	private static class DefiniteConstructedTagInput
	extends DefiniteTagInput
	{
		public DefiniteConstructedTagInput(TagInput parent, BigInteger length)
		{
			super(parent, length);
		}

		@Override
		public int readByte()
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Reading byte from constructed");
		}

		@Override
		public TagInput readTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			return constructedReadTag(this, tag);
		}
	}
	
	private static class UndefiniteConstructedTagInput
	extends TagInput
	{
		private TagInput parent;
		
		public UndefiniteConstructedTagInput(TagInput parent)
		{
			this.parent = parent;
		}
		
		@Override
		public int readByte()
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Reading byte from constructed");
		}

		@Override
		public TagInput readTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			return constructedReadTag(this, tag);
		}

		@Override
		public TagInput skip()
		throws IOException, BEREncodingException
		{
			ASN1Tag tag = new ASN1Tag();
			do
				readTag(tag).skip();
			while (!tag.isEOC());
			return parent;
		}

		@Override
		public int read()
		throws IOException
		{
			return parent.read();
		}
	}
}