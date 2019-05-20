package workers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import tasks.ITask;
import tasks.OrderTask;
import tasks.TaskResult;

public abstract class Worker implements Runnable {

	private ConcurrentLinkedQueue<ITask> tasks;
	private CopyOnWriteArrayList<TaskResult> results;
    private int consumeTime;
    
    public Worker(int consumeTime) {
		super();
		this.consumeTime = consumeTime;
	}
    public int getConsumeTime() {
		return consumeTime;
	}
	public Worker() {
		// TODO Auto-generated constructor stub
	}
	public void setResults(CopyOnWriteArrayList<TaskResult> results) {
		this.results = results;
	}	


	public void setTasks(ConcurrentLinkedQueue<ITask> tasks) {
		this.tasks = tasks;
	}
	@Override
	public void run() {
		while (true) {
			ITask task = this.tasks.poll();
			if (task == null) break;
			TaskResult result = taskHandler(task);
			this.results.add(result);
			
		}
	}
	public abstract TaskResult taskHandler(ITask task);


}
