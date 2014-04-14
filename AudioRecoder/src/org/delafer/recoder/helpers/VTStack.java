package org.delafer.recoder.helpers;

import java.util.concurrent.atomic.AtomicInteger;


@SuppressWarnings("unchecked")
public final class VTStack<E> {

	public final static int LOG_LINES = 32;

	private transient volatile E[] array;
	private transient AtomicInteger pointer;
	private transient int size;


	public VTStack() {
		this(LOG_LINES);
	}

	public VTStack(int size) {
		this.size = size;
		this.array =(E[])new Object[size];
		pointer = new AtomicInteger(size-1);
	}

	public final void add(final E element) {
		if (pointer.incrementAndGet() >= size) pointer.set(0);
		array[pointer.get()] = element;
	}

	public final E get(final int at) {
		return array[(pointer.get() + at + 1) % size];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size; i++) {
			if (sb.length()>0) sb.append(',');
			sb.append(get(i));
		}
		sb.insert(0,"VST[");
		sb.append(']');
		return sb.toString();
	}


}
