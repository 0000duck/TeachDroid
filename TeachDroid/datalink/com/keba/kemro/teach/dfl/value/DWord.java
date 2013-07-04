package com.keba.kemro.teach.dfl.value;

public final class DWord extends Number {
	private int value = 0;
	public static long MIN_VALUE = 0;
	public static long MAX_VALUE = 4294967295l;
	private static String ZERO = "00000000";

	public DWord(int data) {
		value = data;
	}

	public DWord(Integer data) {
		this(((Integer) data).intValue());
	}

	public double doubleValue() {
		// TODO Auto-generated method stub
		return (double) value;
	}

	public float floatValue() {
		// TODO Auto-generated method stub
		return (float) value;
	}

	public int intValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public long longValue() {
		// TODO Auto-generated method stub
		if (value < 0) {
			return -value;
		}
		return value;
	}

	public String toString() {
		return toString(value);
	}

	public static DWord valueOf(String s) throws NumberFormatException {
		if (s.startsWith("16#")) {
			return new DWord((int) Long.parseLong(s.substring(3), 16));
		}
		return new DWord((int) Long.parseLong(s));
	}

	public static String toString(int val) {
		/*if (false){// binary
			String v = Integer.toBinaryString(val).toUpperCase();
			int cnt = 8 - v.length();
			if (cnt < 0) {
				cnt = cnt + 8;
			}
			return "2#" + ZERO.substring(0, cnt) + v;
		}*/
		String v = Integer.toHexString(val).toUpperCase();
		int cnt = 4 - v.length();
		if (cnt < 0) {
			cnt = cnt + 4;
		}
		return "16#" + ZERO.substring(0, cnt) + v;
	}
}
