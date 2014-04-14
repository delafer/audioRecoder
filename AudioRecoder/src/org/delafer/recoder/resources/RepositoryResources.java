package org.delafer.recoder.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RepositoryResources {
	
	public static InputStream getInternalFileStream(String name) {
		return RepositoryResources.class.getResourceAsStream(name);
	}
	
	
	public static InputStream getExternalFileStream(String name) {
		
		try {
			FileInputStream fs = new FileInputStream(name);
			return fs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
