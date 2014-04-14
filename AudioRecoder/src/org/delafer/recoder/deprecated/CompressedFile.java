package org.delafer.recoder.deprecated;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.delafer.recoder.helpers.Convertors;

public class CompressedFile implements IFileAbstract {
	
	
	private String name;
	
	private byte[] data;
	private int size;
	
	public CompressedFile(byte[] data, IFileAbstract source) {
		this.data = data;
		this.size = data.length;
		this.name = source.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getPath()
	 */
	public String getPath() {
		return "<in memory>";
	}
	
	public String getInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append("in memory - ");
		sb.append(getName());
		sb.append("(compressed), ");
		sb.append(Convertors.autoSize(getSize()));
		return sb.toString();
		
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getSize()
	 */
	public long getSize() {
		return this.size;
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#openInputStream()
	 */
	public InputStream openInputStream() {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		return is;
	}

	public boolean isFileExists() {
		return true;
	}

}
