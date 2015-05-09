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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import net.queenbee.asn1.ASN1Class;
import net.queenbee.asn1.ASN1Tag;
import net.queenbee.asn1.OID;

/**
 * BER output stream.
 * 
 * @author Miquel A. Ferran &lt;miquel.ferran.gonzalez@gmail.com&gt;
 */
public class BEROutputStream
extends OutputStream
{
	private static DateFormat utcFmt;
	
	/**
	 * Default buffer size used to allocate temporary contents.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 4 * 1024;
	
	static
	{
		utcFmt = new SimpleDateFormat("yyMMddHHmmssZ");
	}
	
	private TagOutput output;
	private OutputStream out;
	
	/**
	 * Creates a BER output stream to the given underlying output stream.
	 * 
	 * @param out
	 * 			Underlying output stream.
	 */
	public BEROutputStream(OutputStream out)
	{
		output = new RootTagOutput(DEFAULT_BUFFER_SIZE);
		this.out = out;
	}
	
	/**
	 * Creates a BER output stream to the given underlying output stream and
	 * defined buffer size.
	 * 
	 * @param out
	 * 			Underlying output stream.
	 * @param bufferSize
	 * 			Buffer size.
	 */
	public BEROutputStream(OutputStream out, int bufferSize)
	{
		this.output = new RootTagOutput(bufferSize);
		this.out = out;
	}
	
	/**
	 * Write the next tag.
	 * 
	 * @param tag
	 * 			Tag to be written.
	 * 
	 * @throws IOException
	 * 			If an input/output error has been occurred.
	 * @throws BEREncodingException
	 * 			If an encoding error has been occurred.
	 */
	public void writeTag(ASN1Tag tag)
	throws IOException, BEREncodingException
	{
		output = output.writeTag(tag);
	}
	
	/**
	 * Write tag identifier and contents as raw bytes.
	 * 
	 * @param bytes
	 * 			Bytes containing tag identifier and contents.
	 * 
	 * @throws IOException
	 * 			If an input/output error has been occurred.
	 * @throws BEREncodingException
	 * 			If an encoding error has been occurred.
	 */
	public void writeRaw(byte[] bytes)
	throws IOException, BEREncodingException
	{
		output = output.writeRaw(bytes);
	}
	
	/**
	 * Write universal boolean.
	 * 
	 * @param b
	 * 			Boolean to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeBoolean(boolean b)
	throws IOException
	{
		write(b ? 1 : 0);
	}
	
	/**
	 * Write universal integer.
	 * 
	 * @param i
	 * 			Integer to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeInteger(BigInteger i)
	throws IOException
	{
		write(i.toByteArray());
	}
	
	/**
	 * Write universal bit string.
	 * 
	 * @param bs
	 * 			Bit string to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeBitString(boolean[] bs)
	throws IOException
	{
		write((8 - bs.length % 8) % 8);
		int off = 0;
		int b = 0;
		for (boolean v : bs)
		{
			b = b | (v ? 1 : 0);
			if (off < 7)
			{
				++off;
				b = b << 1;
			}
			else
			{
				write(b);
				off = 0;
				b = 0;
			}
		}
		if (off < 7)
			write(b << (7 - off));
	}
	
	/**
	 * Write universal octet string.
	 * 
	 * @param os
	 * 			Octet string to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeOctetString(byte[] os)
	throws IOException
	{
		write(os);
	}
	
	/**
	 * Write universal object identifier.
	 * 
	 * @param oid
	 * 			Object identifier to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeObjectIdentifier(OID oid)
	throws IOException
	{
		write(oid.get(0) * 40 + oid.get(1));
		for (int i = 2; i < oid.length(); ++i)
		{
			Stack<Integer> stack = new Stack<Integer>();
			int idi = oid.get(i);
			stack.push(idi & 0x7f);
			idi = idi >>> 7;
			while (idi > 0)
			{
				stack.push(idi & 0x7f);
				idi = idi >>> 7;
			}
			while (!stack.isEmpty())
				write(stack.pop() | (stack.isEmpty() ? 0x00 : 0x80));
		}
	}
	
	/**
	 * Write universal real.
	 * 
	 * @param real
	 * 			Real to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeReal(BigDecimal real)
	throws IOException
	{
		// TODO Write BER universal real
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Write universal UTF-8 string.
	 * 
	 * @param str
	 * 			UTF-8 string to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeUTF8String(String str)
	throws IOException
	{
		Writer writer = new OutputStreamWriter(this, StandardCharsets.UTF_8);
		writer.write(str);
		writer.flush();
	}
	
	/**
	 * Write universal UTC time.
	 * 
	 * @param time
	 * 			UTC time to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeUTCTime(Date time)
	throws IOException
	{
		Writer writer = new OutputStreamWriter(this, StandardCharsets.US_ASCII);
		writer.write(utcFmt.format(time));
		writer.flush();
	}
	
	/**
	 * Write universal generalized time.
	 * 
	 * @param time
	 * 			UTC time to be written.
	 * 
	 * @throws IOException
	 * 			If some input/output stream error has been occurred.
	 */
	public void writeGeneralizedTime(Date time)
	throws IOException
	{
		// TODO Write BER universal generalized time
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Concludes the current tag.
	 * 
	 * @param eoc
	 * 			Includes EOC even though constructed length is determined.
	 * 
	 * @throws IOException
	 * 			If an input/output error has been occurred.
	 * @throws BEREncodingException
	 * 			If an encoding error has been occurred.
	 */
	public void conclude(boolean eoc)
	throws IOException, BEREncodingException
	{
		output = output.conclude(eoc);
	}
	
	/**
	 * Write the next byte of the primitive content.
	 */
	@Override
	public void write(int b)
	throws IOException
	{
		try
		{
			output.writeByte(b);
		}
		catch (BEREncodingException exception)
		{
			throw new IOException(exception);
		}
	}
	
	/**
	 * Flush buffered content to the underlying output stream.
	 */
	@Override
	public void flush()
	throws IOException
	{
		try
		{
			output.flush(out);
			out.flush();
		}
		catch (BEREncodingException exception)
		{
			throw new IOException(exception);
		}
	}
	
	/**
	 * Flush buffered content to the underlying output stream and close it.
	 */
	public void close()
	throws IOException
	{
		flush();
		out.close();
		output.close();
	}
	
	private static BigInteger getTagIdentifierSize(ASN1Tag tag,
			BigInteger length)
	{
		int tn = tag.getTagNumber();
		
		// Tag identifier and length
		BigInteger size = BigInteger.valueOf(2l);
		
		// Extra tag number
		if (tn > 30)
		{
			size = size.add(BigInteger.valueOf(tn / 127));
			size = size.add(tn % 127 > 0 ? BigInteger.ONE : BigInteger.ZERO);
		}
		
		// Extra length
		if (length != null && length.compareTo(BigInteger.valueOf(127l)) > 0)
		{
			BigInteger clen = length;
			while (clen.compareTo(BigInteger.ZERO) > 0)
			{
				size = size.add(BigInteger.ZERO);
				clen = clen.divide(BigInteger.valueOf(256l));
			}
		}
		
		// Computed size
		return size;
	}
	
	private static void tagIdentifierFlush(OutputStream out, ASN1Tag tag,
			BigInteger length)
	throws IOException
	{
		ASN1Class tc = tag.getTagClass();
		int tn = tag.getTagNumber();
		
		int bi = 0;
		byte[] b = new byte[1];
		
		// Tag class
		bi = tc.ordinal() & 0x03;
		
		// Primitive/constructed
		bi = bi << 1;
		bi = bi | (tag.isConstructed() ? 0x01 : 0x00);
		
		// Tag number
		bi = bi << 5;
		if (tn <= 30)
		{
			// Tag number: Short form
			bi = bi | tn;
			b[0] = (byte) (bi & 0xff);
			out.write(b, 0, 1);
		}
		else
		{
			// Tag number: Long form
			bi = bi | 31;
			b[0] = (byte) (bi & 0xff);
			out.write(b, 0, 1);
		
			int num = tn;
			while (num > 127)
			{
				b[0] = (byte) 0xff;
				out.write(b, 0, 1);
				num -= 127;
			}
			b[0] = (byte) (num % 128);
			out.write(b, 0, 1);
		}
		
		// Tag length: Indefinite
		if (length == null)
		{
			b[0] = (byte) 0x80;
			out.write(b, 0, 1);
		}
		// Tag length: Short form
		else if (length.compareTo(BigInteger.valueOf(128l)) < 0)
		{
			b[0] = length.byteValue();
			out.write(b, 0, 1);
		}
		// Tag length: Long form
		else
		{
			byte[] len = length.toByteArray();
			b[0] = (byte) (0x80 | len.length);
			out.write(b, 0, 1);
			out.write(len);
		}
	}
	
	private static abstract class TagOutput
	extends OutputStream
	{
		public abstract void writeByte(int b)
		throws IOException, BEREncodingException;
		
		public abstract TagOutput writeTag(ASN1Tag tag)
		throws IOException, BEREncodingException;
		
		public abstract TagOutput writeRaw(byte[] bytes)
		throws IOException, BEREncodingException;
		
		public abstract TagOutput conclude(boolean eoc)
		throws IOException, BEREncodingException;
		
		public abstract BigInteger getSize();
		
		public abstract boolean flush(OutputStream out)
		throws IOException, BEREncodingException;
		
		protected abstract BigInteger getBufferIndex();
		
		protected abstract void dumpBuffer(OutputStream out, BigInteger length)
		throws IOException;
	}
	
	private static abstract class CompositeTagOutput
	extends TagOutput
	{
		private List<TagOutput> outputList;
		
		protected CompositeTagOutput()
		{
			outputList = new ArrayList<>();
		}
		
		@Override
		public TagOutput writeTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			TagOutput output = tag.isConstructed()
					? new ConstructedTagOutput(tag, this)
					: new PrimitiveTagOutput(tag, this);
			outputList.add(output);
			return output;
		}
		
		@Override
		public TagOutput writeRaw(byte[] bytes)
		throws IOException, BEREncodingException
		{
			outputList.add(new RawTagOutput(bytes));
			return this;
		}
		
		protected BigInteger getChildrenLength()
		{
			BigInteger size = BigInteger.ZERO;
			for (TagOutput child : outputList)
			{
				BigInteger childSize = child.getSize();
				if (childSize == null)
					return null;
				size = size.add(childSize);
			}
			return size;
		}
		
		protected boolean flushChildren(OutputStream out)
		throws IOException, BEREncodingException
		{
			while (!outputList.isEmpty())
			{
				TagOutput child = outputList.get(0);
				if (!child.flush(out))
					return false;
				outputList.remove(0);
			}
			return true;
		}
	}
	
	private static class RootTagOutput
	extends CompositeTagOutput
	{
		private ByteBuffer buffer;
		private BigInteger bufferIndex;
		private BigInteger dumpIndex;
		
		public RootTagOutput(int bufferSize)
		{
			buffer = ByteBuffer.allocate(bufferSize);
			bufferIndex = BigInteger.ZERO;
			dumpIndex = null;
		}
		
		@Override
		public void writeByte(int b)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Writing byte to root output");
		}

		@Override
		public TagOutput conclude(boolean eoc)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Concluding root output");
		}
		
		@Override
		public BigInteger getSize()
		{
			return getChildrenLength();
		}
		
		@Override
		public boolean flush(OutputStream out)
		throws IOException, BEREncodingException
		{
			dumpIndex = BigInteger.ZERO;
			boolean completed = flushChildren(out);
			buffer.reset();
			bufferIndex = BigInteger.ZERO;
			dumpIndex = null;
			return completed;
		}
		
		@Override
		public void write(int b)
		throws IOException
		{
			buffer.put((byte) (b & 0xff));
			bufferIndex = bufferIndex.add(BigInteger.ONE);
		}
		
		@Override
		protected BigInteger getBufferIndex()
		{
			return bufferIndex;
		}

		@Override
		protected void dumpBuffer(OutputStream out, BigInteger length)
		throws IOException
		{
			byte[] buff = new byte[length.intValue()];
			buffer.get(buff, dumpIndex.intValue(), length.intValue());
			out.write(buff, 0, length.intValue());
			dumpIndex = dumpIndex.add(length);
		}
	}
	
	private static class PrimitiveTagOutput
	extends TagOutput
	{
		private ASN1Tag tag;
		private TagOutput parent;
		private BigInteger start;
		private BigInteger length;
		
		public PrimitiveTagOutput(ASN1Tag tag, TagOutput parent)
		{
			this.tag = tag;
			this.parent = parent;
			start = getBufferIndex();
			length = null;
		}

		@Override
		public void writeByte(int b)
		throws IOException, BEREncodingException
		{
			write(b);
		}

		@Override
		public TagOutput writeTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Writing tag to primitive output");
		}
		
		@Override
		public TagOutput writeRaw(byte[] bytes)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException("Writing tag to primitive output");
		}

		@Override
		public TagOutput conclude(boolean eoc)
		throws IOException, BEREncodingException
		{
			length = getBufferIndex().subtract(start);
			return parent;
		}
		
		@Override
		public BigInteger getSize()
		{
			return getTagIdentifierSize(tag, length).add(length);
		}
		
		@Override
		public boolean flush(OutputStream out)
		throws IOException, BEREncodingException
		{
			if (length == null)
				throw new BEREncodingException(
						"Flushing not concluded primitive output");
			tagIdentifierFlush(out, tag, length);
			dumpBuffer(out, length);
			return true;
		}
		
		@Override
		public void write(int b)
		throws IOException
		{
			parent.write(b);
		}
		
		@Override
		protected BigInteger getBufferIndex()
		{
			return parent.getBufferIndex();
		}

		@Override
		protected void dumpBuffer(OutputStream out, BigInteger length)
		throws IOException
		{
			parent.dumpBuffer(out, length);
		}
	}
	
	private static class ConstructedTagOutput
	extends CompositeTagOutput
	{
		private ASN1Tag tag;
		private TagOutput parent;
		private BigInteger length;
		private boolean flushed;
		
		public ConstructedTagOutput(ASN1Tag tag, TagOutput parent)
		{
			this.tag = tag;
			this.parent = parent;
			length = null;
			flushed = false;
		}

		@Override
		public void writeByte(int b)
		throws IOException, BEREncodingException
		{
			throw new BEREncodingException(
					"Writting byte to constructed ouput");
		}

		@Override
		public TagOutput conclude(boolean eoc)
		throws IOException, BEREncodingException
		{
			if (eoc || flushed)
				super.writeTag(new ASN1Tag());
			if (!flushed)
				length = getChildrenLength();
			return parent;
		}
		
		@Override
		public BigInteger getSize()
		{
			return length == null ? null
					: getTagIdentifierSize(tag, length).add(length);
		}
		
		@Override
		public boolean flush(OutputStream out)
		throws IOException, BEREncodingException
		{
			if (!flushed)
			{
				tagIdentifierFlush(out, tag, length);
				flushed = true;
			}
			return flushChildren(out);
		}
		
		@Override
		public void write(int b)
		throws IOException
		{
			parent.write(b);
		}
		
		@Override
		protected BigInteger getBufferIndex()
		{
			return parent.getBufferIndex();
		}
		
		@Override
		protected void dumpBuffer(OutputStream out, BigInteger length)
		throws IOException
		{
			parent.dumpBuffer(out, length);
		}
	}
	
	private static class RawTagOutput
	extends TagOutput
	{
		private byte[] bytes;
		
		public RawTagOutput(byte[] bytes)
		{
			this.bytes = bytes;
		}
		
		@Override
		public void writeByte(int b)
		throws IOException, BEREncodingException
		{
		}

		@Override
		public TagOutput writeTag(ASN1Tag tag)
		throws IOException, BEREncodingException
		{
			return null;
		}

		@Override
		public TagOutput writeRaw(byte[] bytes)
		throws IOException, BEREncodingException
		{
			return null;
		}

		@Override
		public TagOutput conclude(boolean eoc)
		throws IOException, BEREncodingException
		{
			return null;
		}

		@Override
		public BigInteger getSize()
		{
			return BigInteger.valueOf(bytes.length);
		}

		@Override
		public boolean flush(OutputStream out)
		throws IOException, BEREncodingException
		{
			out.write(bytes);
			return true;
		}

		@Override
		protected BigInteger getBufferIndex()
		{
			return null;
		}

		@Override
		protected void dumpBuffer(OutputStream out, BigInteger length)
		throws IOException
		{
		}

		@Override
		public void write(int b)
		throws IOException
		{
		}
	}
}