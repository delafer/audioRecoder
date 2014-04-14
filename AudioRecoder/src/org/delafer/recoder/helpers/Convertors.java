package org.delafer.recoder.helpers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class Convertors {

	private static final long POWER = 10l;

	public static final long SIZE_BYTE = 1l;
	public static final long SIZE_KILOBYTE = SIZE_BYTE << POWER;
	public static final long SIZE_MEGABYTE = SIZE_KILOBYTE << POWER;
	public static final long SIZE_GIGABYTE = SIZE_MEGABYTE << POWER;
	public static final long SIZE_TERABYTE = SIZE_GIGABYTE << POWER;
	public static final long SIZE_PETABYTE = SIZE_TERABYTE << POWER;
	public static final long SIZE_EXABYTE = SIZE_PETABYTE << POWER;

	private static final transient DecimalFormat floatFormat		= new DecimalFormat("0.##");

	public static enum ByteUnit{Byte(0), KB(1), MB(2), GB(3), TB(4), PB(5), EB(6);

	private final long size;
	private final long multiply;
	private final String unit;

	public long getBytes() {
		return size;
	}

	public long getMultiply() {
		return multiply;
	}

	public String getUnit() {
		return this.unit;
	}

	private ByteUnit(long multiply){
			this.multiply = multiply;
			this.size = SIZE_BYTE << (POWER * multiply);
			this.unit = this.toString();
	}
	};

	public static float convert(float value, ByteUnit from, ByteUnit to) {
		if (from.equals(to)) return value;
		long dif = from.getMultiply() - to.getMultiply();
		return dif > 0 ? value * (float)  (SIZE_BYTE << (POWER * dif)) : value / (float)  (SIZE_BYTE << (- POWER * dif));

	}

	public static String autoSize(long size) {
		StringBuilder sb = new StringBuilder();
		ByteUnit unit = null;
		if (size < SIZE_MEGABYTE) {
			if (size < SIZE_KILOBYTE) {
				unit = ByteUnit.Byte;
			} else {
				unit = ByteUnit.KB;
			}
		} else {
			if (size < SIZE_GIGABYTE) {
				unit = ByteUnit.MB;
			} else
			if (size < SIZE_TERABYTE)
			{
				unit = ByteUnit.GB;
			} else
			if (size < SIZE_PETABYTE)
			{
				unit = ByteUnit.TB;
			} else
			if (size < SIZE_EXABYTE)
			{
				unit = ByteUnit.PB;
			} else
				unit = ByteUnit.EB;
		}

		sb.append(unit == ByteUnit.Byte ? size : floatFormat.format(convert(size, ByteUnit.Byte, unit)));
		sb.append(' ');
		sb.append(unit.getUnit());

		return sb.toString();
	}

	public static String formatValue(double val) {
		return floatFormat.format(val);
	}

	public static void test(ByteUnit aaa) {
	}

}
