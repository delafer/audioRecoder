package org.delafer.recoder.deprecated;

import java.io.InputStream;

public interface IFileAbstract {

	public abstract String getName();

	public abstract String getPath();

	public abstract long getSize();

	public abstract InputStream openInputStream();
	
	public String getInformation();
	
	public boolean isFileExists();
}