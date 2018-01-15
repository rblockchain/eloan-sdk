/**
 * Copyright 2016 Digital Asset Holdings, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rongzer.blockchain.common;

import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.rongzer.utils.Hash;

/**
 * generic byte array utilities
 */
public class ByteUtils {
    private static final char[] b58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final int[] r58 = new int[256];

    static {
        for (int i = 0; i < 256; ++i) {
            r58[i] = -1;
        }
        for (int i = 0; i < b58.length; ++i) {
            r58[b58[i]] = i;
        }
    }

    /**
     * convert a byte array to a human readable base58 string. Base58 is a Bitcoin specific encoding similar to widely used base64 but avoids using characters
     * of similar shape, such as 1 and l or O an 0
     *
     * @param b
     * @return
     */
    public static String toBase58(byte[] b) {
        if (b.length == 0) {
            return "";
        }

        int lz = 0;
        while (lz < b.length && b[lz] == 0) {
            ++lz;
        }

        StringBuilder s = new StringBuilder();
        BigInteger n = new BigInteger(1, b);
        while (n.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] r = n.divideAndRemainder(BigInteger.valueOf(58));
            n = r[0];
            char digit = b58[r[1].intValue()];
            s.append(digit);
        }
        while (lz > 0) {
            --lz;
            s.append("1");
        }
        return s.reverse().toString();
    }

    /**
     * Encode in base58 with an added checksum of four bytes.
     *
     * @param b
     * @return
     */
    public static String toBase58WithChecksum(byte[] b) {
        byte[] cs = Hash.hash(b);
        byte[] extended = new byte[b.length + 4];
        System.arraycopy(b, 0, extended, 0, b.length);
        System.arraycopy(cs, 0, extended, b.length, 4);
        return toBase58(extended);
    }

    /**
     * reverse a byte array in place
     * WARNING the parameter array is altered and returned.
     *
     * @param data
     * @return
     */
    public static byte[] reverse(byte[] data) {
        for (int i = 0, j = data.length - 1; i < data.length / 2; i++, j--) {
            data[i] ^= data[j];
            data[j] ^= data[i];
            data[i] ^= data[j];
        }
        return data;
    }

    /**
     * convert a byte array to hexadecimal
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {
        return new String(Hex.encodeHex(data));
    }

    /**
     * recreate a byte array from hexadecimal
     *
     * @param hex
     * @return
     */
    public static byte[] fromHex(String hex) {
    	if (hex == null || hex.length()<1)
    	{
    		return new byte[0];
    	}
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            return null;
        }
    }
    

	/**
	 * 截取byte[]
	 * @param key
	 * @return
	 */
	public static byte[] subBuf(byte[] inBuf ,int nStart,int nSize)
	{
		if (inBuf == null)
		{
			return inBuf;
		}
		
		if (nStart+nSize > inBuf.length)
		{
			return null;
		}
		
		byte[] outBuf = new byte[nSize];
		for (int i=0;i<nSize;i++)
		{
			outBuf[i] = inBuf[nStart+i];
		}
				
		return outBuf;
	}
	
	/**
	 * byte[]增加结束符
	 * @param key
	 * @return
	 */
	public static byte[] addEnd(byte[] inBuf)
	{
		
		if (inBuf == null)
		{
			inBuf = new byte[0];
		}
		
		if (inBuf.length >3 && inBuf[inBuf.length-4] == '#' && inBuf[inBuf.length-3] == 'E' && inBuf[inBuf.length-2] == 'N'&& inBuf[inBuf.length-1] == 'D')
		{
			return inBuf;
		}
		
		byte[] outBuf = new byte[inBuf.length+4];
				
		for (int i=0;i<inBuf.length;i++)
		{
			outBuf[i] = inBuf[i];
		}
		
		outBuf[inBuf.length] = '#';
		outBuf[inBuf.length+1] = 'E';
		outBuf[inBuf.length+2] = 'N';
		outBuf[inBuf.length+3] = 'D';
		
		return outBuf;
		
	}
	
	/**
	 * 截取byte[]至结束符
	 * @param key
	 * @return
	 */
	public static byte[] trimBuf(byte[] inBuf)
	{
		if (inBuf == null)
		{
			return inBuf;
		}
		int nLen = inBuf.length;
		
		for (int i=0;i<inBuf.length-3;i++)
		{
			if (inBuf[i]=='#'&&inBuf[i+1]=='E'&&inBuf[i+2]=='N'&&inBuf[i+3]=='D')
			{
				nLen = i;
				break;
			}
		}
			
		
		byte[] outBuf = new byte[nLen];
		for (int i=0;i<nLen;i++)
		{
			outBuf[i] = inBuf[i];
		}
				
		return outBuf;
	}
	
}