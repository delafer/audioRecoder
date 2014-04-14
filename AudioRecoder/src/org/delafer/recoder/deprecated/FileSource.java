package org.delafer.recoder.deprecated;

import java.io.Serializable;

public class FileSource<E> implements Serializable {

	private static final long serialVersionUID = -7930645876057434479L;

	private String fileName;

	private E data;

	public FileSource() {
	}

	public FileSource(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "File: "+fileName;
	}

	@Override
	public int hashCode() {
		return fileName == null ? 0 : fileName.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof FileSource)) return false;
		final String o2 = ((FileSource<?>)obj).fileName;
		return this.fileName==o2 ? true : this.fileName != null ? this.fileName.equals(o2) : false;

	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

}
