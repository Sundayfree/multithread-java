package tasks;


public class TaskResult<T>{

    private  T result;

	public TaskResult(T result) {
		super();
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
