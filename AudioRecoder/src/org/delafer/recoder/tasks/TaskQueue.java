package org.delafer.recoder.tasks;

import java.util.Queue;

public class TaskQueue {

	public static final transient Tasks queue = new Tasks();

	public static BatchTask getNextTask() {
		return queue.getNextTask();
	}

	public static final void setTasks(Queue<BatchTask> tasks) {
		queue.setTasks(tasks);
	}

	public final static class Tasks {

		private Queue<BatchTask> tasks;

		private Tasks() {}

		public final synchronized BatchTask getNextTask() {
			return tasks != null ? tasks.poll() : null;
		}

		public final void setTasks(Queue<BatchTask> tasks) {
			this.tasks = tasks;
		}


	}

	public static int size() {
		return queue.tasks.size();
	}


}
