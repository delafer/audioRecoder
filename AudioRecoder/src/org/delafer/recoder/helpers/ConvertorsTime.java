package org.delafer.recoder.helpers;

import java.util.concurrent.TimeUnit;

public class ConvertorsTime {

	public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);

        addTimeUnit(sb, days, "Day");
        addTimeUnit(sb, hours, "Hour");
        addTimeUnit(sb, minutes, "Minute");
        addTimeUnit(sb, seconds, "Second");

        if (sb.length()==0 && seconds==0L) sb.append("0 Seconds");

        return(sb.toString());
    }

	private static void addTimeUnit(StringBuilder sb, long value, String name) {
		if (value > 0) {
        	sb.append(value);
        	sb.append(' ');
        	sb.append(name);
        	if (value>1) sb.append('s');
        	sb.append(' ');
        }
	}

//	public static void main(String[] args) {
//
//		long l1 = System.currentTimeMillis()-180000;
//		for (int i = 0; i < 10; i++) {
//			System.out.println(getDurationBreakdown(System.currentTimeMillis() - l1));
//			try {
//				Thread.currentThread().sleep(13000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}

}
