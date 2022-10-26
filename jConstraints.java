import java.util.ArrayList;
import java.util.HashMap;

public class jConstraints {
	
	//array that stores the task length with the index of every variable
	protected int[] taskLength;
	
	//HashMaps that store what the variables have to precede or have to be disjointed from
	protected HashMap<String, ArrayList<String>> precedence;
	protected HashMap<String, ArrayList<String>> disjoint;
	
	//int member to store the max amount of time the job can take
	private int maxTime;
	
	//constructor which initializes the members
	public jConstraints(int size) {
		taskLength = new int[size];
		precedence = new HashMap<>();
		disjoint = new HashMap<>();
	}
	
	//setter methods for the constraints
	public void addTaskLength(int task, int length) {
		taskLength[task] = length;
	}
	
	public void addPrecedence(String task, String after) {
		if (!precedence.containsKey(task)) {
			precedence.put(task, new ArrayList<String>());
			precedence.get(task).add(after);
		}
		else {
			precedence.get(task).add(after);
		}
	}
	
	public void addDisjoint(String task, String dis) {
		if (!disjoint.containsKey(task)) {
			disjoint.put(task, new ArrayList<String>());
			disjoint.get(task).add(dis);
		}
		else {
			disjoint.get(task).add(dis);
		}
	}
	
	public void setMaxTime(int max) {
		maxTime = max;
	}
	
	//getter method for the max time
	public int getMaxTime() {
		return maxTime;
	}
}
