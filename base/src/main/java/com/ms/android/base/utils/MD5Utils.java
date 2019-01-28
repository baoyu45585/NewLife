package com.ms.android.base.utils;

import java.security.MessageDigest;
import java.util.Random;

public class MD5Utils {
	private final static char digits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getRandom() {
		byte[] random = { 0, 0, 0, 0, 0, 0, 0, 0 };
		new Random().nextBytes(random);

		return Byte2Hex(random);
	}

	public static String Byte2Hex(byte[] arr) {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; ++i) {
				sb.append(String.format("%02x", arr[i]));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String MD5Hash(String data) {
		try {
			byte[] btInput = data.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			mdInst.update(btInput);
			byte[] md = mdInst.digest();

			int k = 0;
			int j = md.length;
			char str[] = new char[j * 2];

			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = digits[byte0 >>> 4 & 0xF];

				str[k++] = digits[byte0 & 0xF];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
}
