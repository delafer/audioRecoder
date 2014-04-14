package org.delafer.recoder.deprecated;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.helpers.utils.UtilsStream;
import org.delafer.recoder.resources.RepositoryResources;

public class ExternalFile implements IFileAbstract {
	
	
	private String name;
	private String path;
	
	private String fullPath;
	
	private boolean read = false;
	
	private byte[] data;
	
	private long size;
	
	public ExternalFile(String fullPath) {
		setPaths(fullPath);
	}
	
	 public final static boolean compare(final String s1, final String s2) {
		      return s1 != null ? s1.equals(s2) : s1 == s2;
	}


	private void setPaths(String pathToSet) {
		if (compare(this.fullPath, pathToSet)) return ;
		this.fullPath = pathToSet;	
		int idx = pathToSet.lastIndexOf(Config.PATH_SEPARATOR);
		this.name = idx >= 0 ?  pathToSet.substring(idx+1) : pathToSet;
		this.path = idx >= 0 ? pathToSet.substring(0, idx) : "";
		this.read = false;
	}
	
	
	public boolean isFileExists() {
		if (fullPath==null || fullPath.length()==0) return false;
		
		try {
			File file = new File(fullPath.trim());
			boolean exists = file != null && file.exists() && file.length() > 0;
			return exists;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setFullPath(String fullPath) {
		setPaths(fullPath);
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getName()
	 */
	public String getName() {
		return name != null ? name : "";
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getPath()
	 */
	public String getPath() {
		return  path != null ? path : "";
	}
	
	public String getInformation() {
		if (isFileExists()) 
			return this.getName() + ", "+Convertors.autoSize(this.getSize()) ;
		else
			return "<Select custom file>";
//		StringBuilder sb = new StringBuilder();
//		sb.append("internal - ");
//		sb.append(getName());
//		sb.append(", ");
//		sb.append(Convertors.autoSize(getSize()));
//		return sb.toString();
//		return "Custom / user file";
		
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#getSize()
	 */
	public long getSize() {
		boolean exists = isFileExists();
		return exists ? new File(fullPath.trim()).length() : 0l;
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.resources.IFileAbstract#openInputStream()
	 */
	public InputStream openInputStream() {
		checkRead();
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		return is;
	}

	private void checkRead() {
		if (!read) {
			InputStream resource = RepositoryResources.getExternalFileStream(fullPath);
			data = UtilsStream.toByteArray(resource);
			read = true;
		}
	}
}
