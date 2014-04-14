package org.delafer.recoder.model.common;

import java.util.stream.IntStream;

public final class MString implements CharSequence, Comparable<MString>, Cloneable, java.io.Serializable{

	private static final long serialVersionUID = 38133462228313040L;

	private String string;
	private static final String E = new String();

	public MString() {
	}

	public MString(final String value) {
		this.string = value;
	}

	public String getString() {
		return string != null ? string : E;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getValue() {
		return string;
	}

	public void setValue(String string) {
		this.string = string;
	}


	@Override
	public int hashCode() {
		return string == null ? 0 : string.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof MString)) return false;
		final String o2 = obj.toString();
		return this.string==o2 ? true : this.string != null ? this.string.equals(o2) : false;

	}

	public int compareTo(MString o) {
		return getString().compareTo(o.getString());
	}

	public int length() {
		return this.string != null ? this.string.length() : 0;
	}

	public char charAt(int index) {
		return getString().charAt(index);
	}

	public CharSequence subSequence(int start, int end) {
		return this.string != null ? this.string.subSequence(start, end) : null;
	}

	public IntStream chars() {
		return this.string != null ? this.string.chars() : null;
	}

	public IntStream codePoints() {
		return this.string != null ? this.string.codePoints() : null;
	}

	public String toString() {
		return this.string;
	}

    public boolean isEmpty() {
        return this.string == null ? true : this.string.isEmpty();
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new MString(this.string != null ? new String(this.string) : null);
	}



}
