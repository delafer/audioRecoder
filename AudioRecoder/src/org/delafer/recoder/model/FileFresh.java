package org.delafer.recoder.model;

import java.io.File;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.NotNull;
import org.delafer.recoder.helpers.utils.UtilsFile;


public class FileFresh implements IFile {

	private String baseName;
	private String ext;
	private String path;
	private String fullNameWithPath;
	private boolean temp;

	private FileFresh(String ext) {
		this.baseName = UtilsFile.getTempFileName();
		this.ext = NotNull.string(ext);
		this.path = Config.instance().tempDirectory.getString();
		this.fullNameWithPath = path + baseName + '.'+ext;
		this.temp = true;
	}

	private FileFresh(String path, String newName) {

		this.path = UtilsFile.correctPath(path, false);
		this.fullNameWithPath = this.path + newName;

		this.baseName = UtilsFile.removeExtension(newName);
		this.ext =  UtilsFile.getExtension(newName);

		this.temp = false;
	}


	public static FileFresh getTempFile(String ext) {
		return new FileFresh(ext);
	}

	public static FileFresh getDestinationFile(String path, String newName) {
		return new FileFresh(path, newName);
	}


	public void delete() {
		File fl = getFile();
		if (fl.exists()) {
			if (fl.canWrite())
				fl.delete();
			else
				fl.deleteOnExit();
		}
	}

	public void close() {
		if (isTempFile()) delete();
	}


	public File getFile() {
		return new File(getFullNameWithPath());
	}


	public synchronized String getPath() {
		return path;
	}


	public  String getFullNameWithPath() {
		return this.fullNameWithPath;
	}


	public String getBaseNameWithPath() {
		return this.path + this.baseName;
	}


	@Override
	public String getFullName() {
		StringBuilder sb = new StringBuilder(baseName.length()+4);
		sb.append(baseName).append('.').append(ext);
		return sb.toString();
	}


	@Override
	public String getBaseName() {
		return baseName;
	}


	@Override
	public String getExtension() {
		return ext;
	}


	@Override
	public boolean isTempFile() {
		return true;
	}


	@Override
	public boolean exists() {
		return getFile().exists();
	}


	@Override
	public long size() {
		return getFile().length();
	}


	@Override
	public String toString() {
		return "FileTemp [" + fullNameWithPath + "]";
	}


}
