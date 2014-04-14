package org.korvin.mp3remover.libs;

public class Logger {

	public void error(String string, Exception e) {
		System.out.println(string);
		e.printStackTrace();

	}

}
