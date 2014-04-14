package org.delafer.recoder.model;

import java.io.File;
import java.io.IOException;

import org.delafer.recoder.helpers.utils.UtilsFile;

/**
 * The Class FileInfo.
 */
public class FilePersistent implements IFile {


	private File file; /** The file. */
	private String fullNameWithPath; /** The full name. */

	/**
	 * CONSTRUCTOR
	 * Instantiates a new file info.
	 * @param file - the file
	 * @throws IOException
	 */
	public FilePersistent(File file) throws IOException {
		this.file = file;
		fullNameWithPath = getFullPath(file);
	}


	public FilePersistent(String fullNameWithPath) throws IOException {
		this.fullNameWithPath = fullNameWithPath;
	}

	public static FilePersistent valueOf(File file) {
		try {
			return new FilePersistent(file);
		} catch (IOException e) {
			return null;
		}
	}

	public static FilePersistent valueOf(String fileName) {
		try {
			return new FilePersistent(fileName);
		} catch (IOException e) {
			return null;
		}
	}


	/** Gets the extension of a filename.
	 * @return extension of a filename.
	 */
	public String getExtension() {
		return UtilsFile.getExtension(fullNameWithPath);
	}

	/** Gets the file.
	 * @return Returns the file.
	 */
	public synchronized File getFile() {
		if (null == file) {
			file = new File(getFullNameWithPath());
		}
		return file;
	}

	private final String getFullPath(File file) {
		if (file == null) return "";
		try {
			return file.getCanonicalPath();
		} catch (Exception e) {
			return file.getAbsolutePath();
		}

	}


//	/** Gets the relative path (without base Path).
//	 * @return the path relative
//	 */
//	public String getPathRelative(File rootPath) {
//		String fullPath = getPath();
//
//		String relativePath =
//				fullPath.startsWith(rootPath.getPath()) ?
//				fullPath.substring (rootPath.getPath().length() + 1) :
//				fullPath;
//
//		return relativePath;
//	}

	/**
	 * Gets the name with extension (if exists) <br>
	 * plus the path from a full filename.
	 * @return Returns the fullName with path
	 */
	public String getFullNameWithPath() {
		return fullNameWithPath;
	}

	@Override
	public String getBaseNameWithPath() {
		return UtilsFile.removeExtension(fullNameWithPath);
	}

	@Override
	public String getFullName() {
		return UtilsFile.getFileName(fullNameWithPath);
	}

	@Override
	public String getBaseName() {
		return UtilsFile.getBaseName(fullNameWithPath);
	}

	/** Does the work of getting the path.
	 * @return the path
	 */
	public String getPath() {

		String ret = null;
		try {
			File cFile = getFile();
			File pFile = cFile != null && cFile.exists() ? cFile.getParentFile() : null;
			if (pFile != null) ret= pFile.getPath();
		} catch (Throwable e) {}

		return UtilsFile.getFilePath(fullNameWithPath, true);
	}

	@Override
	public boolean isTempFile() {
		return false;
	}

	public boolean exists() {
		return getFile().exists();
	}

	public long size() {
		return getFile().length();
	}

	public void close() {}


	@Override
	public String toString() {
		return String.format("File [%s]", fullNameWithPath);
	}

}
