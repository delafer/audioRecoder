package org.delafer.recoder.helpers.utils;

import java.io.IOException;
import java.io.InputStream;

import org.delafer.recoder.helpers.ByteArrayOS;

public class UtilsStream {

	private final static int BUFF_SIZE = 16384;

	public static byte[] toByteArray(InputStream is) {

		try {

			int read = 0;

			int avail = is.available();

			byte[] buf = new  byte[avail > 0 ? avail : BUFF_SIZE];

			if (avail <= 0) avail = BUFF_SIZE;

			ByteArrayOS os = new ByteArrayOS(avail);

			while ((read = is.read(buf))>0) {
				os.write(buf, 0, read);
			}
			os.flush();
			is.close();

			byte[] ret =  os.toByteArray();

			os.close();

			return ret;


		} catch (IOException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

}
