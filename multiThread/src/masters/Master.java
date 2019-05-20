package masters;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import tasks.ITask;
import tasks.TaskResult;
import workers.Worker;

public class Master {

	private ConcurrentLinkedQueue<ITask> tasksQueue = new ConcurrentLinkedQueue();
	private CopyOnWriteArrayList<TaskResult> results = new CopyOnWriteArrayList<>();
	private LinkedBlockingQueue<Thread> workers;

	public Master() {}
	public Master(Worker worker, int workerNum) {

		worker.setTasks(tasksQueue);
		worker.setResults(results);
		workers = new LinkedBlockingQueue<>(workerNum);
		for (int i = 0; i < workerNum; i++) {
			this.workers.add(new Thread(worker));
		}
	}

	public void submitTask(ITask t) {
		this.tasksQueue.add(t);
	}

	public void execute() {
		this.workers.forEach(t -> {
			t.start();
		});

	}
	public boolean isCompleted() {
		for (Thread work : workers) {
			if (work.getState() != Thread.State.TERMINATED)
				return false;
		}
		return true;
	}

	public CopyOnWriteArrayList<TaskResult> getResult() {
		return this.results;
	}

}
