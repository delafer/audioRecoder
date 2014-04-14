package org.delafer.recoder.helpers;

public class Logger {

	public void error(String string, Exception e) {
		System.out.println(string);
		e.printStackTrace();

	}

}
