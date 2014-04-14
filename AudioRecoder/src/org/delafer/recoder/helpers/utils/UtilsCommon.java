package org.delafer.recoder.helpers.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class UtilsCommon {


	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

//	public static final transient Random randGen = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
	public static final transient Random randGen = new Random(500001l);

	public static final transient Random randomizer = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());

	public static int random(int from, int to) {
		if (from > to) {
			int tmp = from;
			from = to;
			to = tmp;
		}
		return randGen.nextInt(to + 1 - from)+from;
	}


	public static final Random getRandomGenerator() {
		return randomizer;
	}

	/**
	 * @param DD.MM.YYYY, e.G. 15.03.2001 as Text
	 * @return converted to Date
	 * @throws ParseException
	 */
	public static Date asDate(String date) {
		try {
			return DATE_FORMAT.parse(date);
		} catch (Exception e) {
			return null;
		}
	}


	public static int asInt(String str) {
		return numeric(str).intValue();
	}

	public static Number numeric(String str) {
		if (str != null && !str.isEmpty())
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {}

		return Double.valueOf(0D);
	}


	public static void main(String[] args) throws ParseException {
		System.out.println(DATE_FORMAT.format(DATE_FORMAT.parse("25.05.1998")));
	}

	public static final long hashString(CharSequence str) {
	 	long hash = 0;

	 	if (str==null) return hash;

	 	//Prehash
	 	 for (int i = 0, l = str.length(); i < l; i++) {
    		hash += str.charAt(i);
    		hash += (hash << 10);
    		hash ^= (hash >> 6);
    	}

    	//Posthash
    	//step 1
    	hash += (hash << 3);
    	hash ^= (hash >> 11);
    	hash += (hash << 15);
    	//step 2
    	hash = rehash(hash);

    	return hash;

	}

	/**
	 * @param hash
	 * @return
	 */
	public static long rehash(long hash) {
    	hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash = hash ^ (hash >>> 7) ^ (hash >>> 4);
        hash += ~(hash << 15);
        hash ^= (hash >>> 10);
        hash += (hash << 3);
        hash ^= (hash >>> 6);
        hash += ~(hash << 11);
        hash ^= (hash >>> 16);
		return hash;
	}

	public static int minMax(int val, int min, int max) {
		if (val < min) val = min;
		if (val > max) val = max;
		return val;
	}

	public static int textAsInt(String txt, int defVal) {
		if (txt!=null && txt.length()>0)
		try {
			return Integer.parseInt(txt);
		} catch (Exception e) {
			try {
				return (int)Double.parseDouble(txt.trim());
			} catch (Exception e2) {
			}
		}
		return defVal;
	}

	public static void freeMemory() {

	      try {
	    	System.gc();
	    	System.gc();
			Thread.currentThread().sleep(250);
			System.runFinalization();
			System.runFinalization();
		    Thread.currentThread().sleep(250);
	    	System.gc();
	    	System.gc();
		} catch (Exception e) {}


	}

    public static double round( double value, int precision )
    {
            double result;
            BigDecimal bigDecimal = BigDecimal.valueOf( value );
            bigDecimal = bigDecimal.setScale( precision, BigDecimal.ROUND_HALF_UP );
            result = bigDecimal.doubleValue();

            return result;
    }


}
