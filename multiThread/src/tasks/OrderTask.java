package tasks;

public class OrderTask implements ITask {
	private String id;
	private double price;
 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderTask() {
		super();
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Order ID: "+this.id +" Order Price: "+this.price;
	}

}
