package org.delafer.recoder.model;

import java.io.File;

public interface IFile {



		/**
		 * @return
		 */
		public File getFile();

		/**
		 * @retun [path] only!
		 */
		public String getPath();

		/**
		 * @return [path] + [name] + [extension]
		 */
		public String getFullNameWithPath();

		/**
		 * @return [path] + [name] - {!extension}
		 */
		public String getBaseNameWithPath();

		/**
		 * @return [name] + [extension]
		 */
		public String getFullName();

		/**
		 * @return [name] - {!extension}
		 */
		public String getBaseName();

		/**
		 * @return [extension]
		 */
		public String getExtension();

		/**
		 * @return if temp file
		 */
		public boolean isTempFile();

		/**
		 * @return is file exists
		 */
		public boolean exists();

		/**
		 * @return file size
		 */
		public long size();

		public void close();

}
