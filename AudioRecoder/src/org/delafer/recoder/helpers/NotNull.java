package org.delafer.recoder.helpers;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The Class NotNull
 * Null object Pattern -  Avoid null references by providing a default object.
 * http://en.wikipedia.org/wiki/Null_Object_pattern
 * @author Alexander Tavrovschi
 */
@SuppressWarnings({"unchecked","rawtypes"})
public final class NotNull {


   private static final String EMPTY = "".intern();


	/** The Constant EMPTY_ARRAY. */
	private static final Object[] EMPTY_ARRAY = new Object[0];

	/** The Constant current. */
	private static final transient Date current = new Date();

	/** The Constant zLong. */
	private static final transient Long 	zLong 		= Long.valueOf(0l);

	/** The Constant zInt. */
	private static final transient Integer zInt 		   = Integer.valueOf(0);

	/** The Constant zDouble. */
	private static final transient Double 	zDouble		= Double.valueOf(0d);

	/** The Constant zFloat. */
	private static final transient Float 	zFloat 		= Float.valueOf(0f);

	/** The Constant zBoolean. */
	private static final transient Boolean zBoolean 	= Boolean.valueOf(false);

	/** The Constant zByte. */
	private static final transient Byte 	zByte 		= Byte.valueOf((byte)0);

	/** The Constant zShort. */
	private static final transient Short 	zShort 		= Short.valueOf((short)0);

	/**
	 * Sets the.
	 *
	 * @param <E> the element type
	 * @param s the s
	 * @return the sets the
	 */
	public static final <E> Set<E> set(Set<E> s) {
		return null!=s ? s : Collections.EMPTY_SET;
	}

	/**
	 * List.
	 *
	 * @param <E> the element type
	 * @param s the s
	 * @return the list
	 */
	public static final <E> List<E> list(List<E> s) {
		return null!=s ? s : Collections.EMPTY_LIST;
	}

	/**
	 * Map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param s the s
	 * @return the map
	 */
	public static final <K, V> Map<K, V> map(Map<K, V> s) {
		return null!=s ? s : Collections.EMPTY_MAP;
	}

	/**
	 * Iterator.
	 *
	 * @param <E> the element type
	 * @param s the s
	 * @return the iterator
	 */
	public static final <E>Iterator<E> iterator(Iterator<E> s) {
		return null!=s ? s : new EmptyIterator<E>();
	}

	/**
	 * Collection.
	 *
	 * @param <E> the element type
	 * @param s the s
	 * @return the collection
	 */
	public static final  <E> Collection<E> collection(Collection<E> s) {
		return null!=s ? s : Collections.EMPTY_LIST;
	}

	/**
	 * Iterable.
	 *
	 * @param <E> the element type
	 * @param s the s
	 * @return the iterable
	 */
	public static final  <E>Iterable<E> iterable(Iterable<E> s) {
		return null!=s ? s : Collections.EMPTY_LIST;
	}

	/**
	 * String.
	 *
	 * @param text the text
	 * @return the string
	 */
	public static final String string(String text) {
		return text != null ? text : EMPTY;
	}

	/**
	 * Date.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static final Date date(Date date) {
		return date != null ? date : current;
	}

	/**
	 * Array.
	 *
	 * @param <E> the element type
	 * @param array the array
	 * @return the e[]
	 */
	public static final <E>E[] array(E[] array) {
		return array != null ? array : (E[])EMPTY_ARRAY;
	}

	/**
	 * Number.
	 *
	 * @param n the n
	 * @return the long
	 */
	public static final Long number(Long n) {
		return n != null ? n : zLong;
	}

	/**
	 * Number.
	 *
	 * @param n the n
	 * @return the integer
	 */
	public static final Integer number(Integer n) {
		return n != null ? n : zInt;
	}

	/**
	 * Number.
	 *
	 * @param n the n
	 * @return the double
	 */
	public static final Double number(Double n) {
		return n != null ? n : zDouble;
	}

	/**
	 * Number.
	 *
	 * @param n the n
	 * @return the float
	 */
	public static final Float number(Float n) {
		return n != null ? n : zFloat;
	}

	/**
	 * Number.
	 *
	 * @param n the n
	 * @return the boolean
	 */
	public static final Boolean number(Boolean n) {
		return n != null ? n : zBoolean;
	}

	/**
	 * Number byte.
	 *
	 * @param n the n
	 * @return the byte
	 */
	public static final Byte numberByte(Byte n) {
      return n != null ? n : zByte;
   }

	/**
	 * Number short.
	 *
	 * @param n the n
	 * @return the short
	 */
	public static final Short numberShort(Short n) {
      return n != null ? n : zShort;
   }

	/**
	 * Object.
	 *
	 * @param obj the obj
	 * @param clz the clz
	 * @return the object
	 */
	public static final Object object(Object obj, Class clz) {
		if (obj != null) return obj;
		try {
			return clz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The Class EmptyIterator.
	 *
	 * @param <E> the element type
	 */
	protected static class EmptyIterator<E>  implements Iterator<E> {

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {return false;}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public E next() {return null;}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {}
	}


   /**
    * Checks if is empty collection.
    *
    * @param lst the lst
    * @return true, if is empty collection
    */
   public final static boolean isEmptyCollection(Collection lst) {
		return null == lst || 0 == lst.size();
	}

	/**
	 * Checks if is empty map.
	 *
	 * @param map the map
	 * @return true, if is empty map
	 */
	public final static boolean isEmptyMap(Map map) {
		return null == map || 0 == map.size();
	}

	/**
	 * Checks if is empty set.
	 *
	 * @param set the set
	 * @return true, if is empty set
	 */
	public final static boolean isEmptySet(Set set) {
		return isEmptyCollection(set);
	}

	/**
	 * Checks if is empty list.
	 *
	 * @param lst the lst
	 * @return true, if is empty list
	 */
	public final static boolean isEmptyList(List lst) {
		return isEmptyCollection(lst);
	}
}
