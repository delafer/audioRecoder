package org.delafer.recoder.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.helpers.ConvertorsTime;
import org.delafer.recoder.helpers.VTStack;

public final class ProgressModel {


	public transient static final ThreadLocal<Context> conextStore = new ThreadLocal<>();

	public final static ThreadLocal<Context> getContextStore() {
		return conextStore;
	}

	public enum State{ New, InProgress, Success, Fail };
	public enum TaskType {Decoding, Processing, Encoding, Checking};

	private static int tasksCount = TaskType.values().length;

	public static transient AtomicInteger processedCnt = new AtomicInteger();
	public static transient AtomicInteger remainingCnt = new AtomicInteger();


	public static transient AtomicLong sizeOriginal = new AtomicLong();
	public static transient AtomicLong sizeRepacked = new AtomicLong();

	public static VTStack<String> console = new VTStack<String>();

	public static State[][] states = new State[Config.instance().threads][tasksCount];
	public static String[] names = new String[Config.instance().threads];

	private static long initialTime;

	public static transient AtomicLong  sizeTotal;

	public static void setTotalTasks(int value) {
		remainingCnt.set(value);
		initialTime = System.currentTimeMillis();
	}

	public static void incrementProcessedTasks() {
		processedCnt.incrementAndGet();

		if (remainingCnt.get()==0) return ;
		remainingCnt.decrementAndGet();
	}

	public static void setTaskState(int thread, TaskType task, State state) {
		if (thread<1 || thread > states.length) return ;
		states[thread-1][task.ordinal()] = state;
	}

	public static void setTaskName(int thread, String name) {
		if (thread<1 || thread > names.length) return ;
		names[thread-1] = name;
	}

	public static void addSizeOriginal(long size) {
		sizeOriginal.addAndGet(size);
	}

	public static void addSizeRepacked(long size) {
		sizeRepacked.addAndGet(size);
	}

	public final static void toConsole(String value) {
		console.add(value);
	}


//	public static String getTotalBytes() {
//		return Convertors.autoSize(pkdData.get() );
//	}

	public static void reset() {

	}


	public static class Context implements Serializable {
		private static final long serialVersionUID = -2196926069224810219L;

		int threadNum;

		public Context() {
		}

		public Context(int threadNr) {
			this.threadNum = threadNr;
		}


		public int getThreadNr() {
			return threadNum;
		}

		public void setThreadNr(int threadNum) {
			this.threadNum = threadNum;
		}

	}

	public static int getProgress() {
		int done = processedCnt.get();
		int total = done + remainingCnt.get();
		int rate = total != 0 ? Math.round( 100f * (float)done / (float)total) : 100;
		return rate;
	}

	public static String getProcessedCnt() {
		return String.valueOf(processedCnt.get());
	}

	public static String getRemainingCnt() {
		return String.valueOf(remainingCnt.get());
	}

	public static String getSizeOriginal() {
		return Convertors.autoSize(sizeOriginal.get() );
	}

	public static String getSizeRepacked() {
		return Convertors.autoSize(sizeRepacked.get() );
	}

	public static String getElapssedTime() {
		return ConvertorsTime.getDurationBreakdown(elapsedTime());
	}

	private static long elapsedTime() {
		return System.currentTimeMillis()-initialTime;
	}


	private static double getLeft2DoneRatio() {
		long done1 = sizeOriginal.get();
		if (done1==0L) done1 = 1L;
		long left1 = sizeTotal.get() - done1;
		if (left1<0) left1 = 0L;
		double ratio1 = (double)left1 / (double)done1;

		long done2 = processedCnt.get();
		if (done2==0L) done2 = 1L;
		long left2 = remainingCnt.get();
		double ratio2 = (double)left2 / (double)done2;

		return (ratio2+ratio1)*0.5d;
	}

	public static String getRemainingTime() {
		return ConvertorsTime.getDurationBreakdown(Math.round(elapsedTime() * getLeft2DoneRatio()));
	}

	public static void setSizeTotal(long sizeTotal) {
		ProgressModel.sizeTotal = new AtomicLong(sizeTotal);
	}



}
