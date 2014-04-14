package org.delafer.recoder.deprecated;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.helpers.utils.UtilsStream;
import org.delafer.recoder.resources.RepositoryResources;

@Deprecated
public class InternalFile implements IFileAbstract {


	private String name;

	private String internalPath;

	private boolean read = false;

	private byte[] data;

	public InternalFile(String fullPath) {
		this.internalPath = fullPath;
		int idx = internalPath.lastIndexOf("/");
		this.name = idx >= 0 ?  fullPath.substring(idx+1) : fullPath;
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
		return "<internal>";
	}

	public String getInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append("internal - ");
		sb.append(getName());
		sb.append(", ");
		sb.append(Convertors.autoSize(getSize()));
		return sb.toString();

	}


	public boolean isFileExists() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getSize()
	 */
	public long getSize() {
		checkInit();
		return data.length;
	}

	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#openInputStream()
	 */
	public InputStream openInputStream() {
		checkInit();
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		return is;
	}

	private void checkInit() {
		if (!read) {
			InputStream resource = RepositoryResources.getInternalFileStream(internalPath);
			data = UtilsStream.toByteArray(resource);
			read = true;
		}
	}
}
