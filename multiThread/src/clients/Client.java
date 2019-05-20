package clients;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;
import masters.Master;
import tasks.ITask;
import tasks.OrderTask;
import tasks.TaskResult;
import workers.OrderWorker;

public class Client extends Task {

	private Master master;
	private long start;
	private String id;
	private int time;
	
	public void setId(String id) {
		this.id = id;
	}
	public Client(int workNum, int taskNum,int time,String id) {
		super();
		this.time = time;
		this.id=id;
		this.master =  new Master (new OrderWorker(time), workNum);
		for (int i = 0; i <taskNum; i++) {
            ITask t = new OrderTask();
            master.submitTask(t);
        }
	}
	
	public Client() {
		super();
	}

	@Override
	protected Object call() throws Exception {
		request();
		for (int i = 0; i < time; i++) {
			TimeUnit.SECONDS.sleep(1);
			this.updateProgress(i+1, time);
		}
		response();
		return null;
	}
	public void request() {
		master.execute();
	}
	public  TaskResult response() {
		ITask obj=null;
	    while(true){
			if(master.isCompleted()){
				CopyOnWriteArrayList<TaskResult> response = master.getResult();
				for (TaskResult workerResult : response) {
					return workerResult;
				}
				break;
			}
	    }
		return null;
	}
	@Override
	public String toString() {
		OrderTask task =(OrderTask) response().getResult();
		return "=====>>>Order Client "+id+"   "+task;
	}

}
