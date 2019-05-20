package workers;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import tasks.ITask;
import tasks.OrderTask;
import tasks.TaskResult;
import utils.KeyGenerator;

public class OrderWorker extends Worker {
	public OrderWorker() {
		super();
	}

	public OrderWorker(int consumeTime) {
		super(consumeTime);
	}

	@Override
    public  TaskResult taskHandler(ITask task) {
		OrderTask order=(OrderTask)task;
		try {
			 order.setId (KeyGenerator.genUniqueKey());
	         order.setPrice (new Random ().nextInt (1000)+1000);
			 TimeUnit.SECONDS.sleep(this.getConsumeTime());
			//TODO
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return new TaskResult(order);
    }
}
