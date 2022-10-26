import java.util.ArrayList;
import java.util.HashMap;

//This class lays out the framework for both map and jobshop CSPs

public class CSP {
	
	//stores whether the CSP is map or jobshop
	private char type = ' ';
	
	//Stores all the variables for the given CSP
	protected ArrayList<String> variables = new ArrayList<>();
	
	//ArrayList member to store the domain
	protected ArrayList<Character> mDomain = new ArrayList<>();
	protected ArrayList<Integer> jDomain = new ArrayList<>();
	
	//Lays out the constraints of the CSP with their own class
	protected jConstraints jConst;
	protected mConstraints mConst;
	
	//for jobshop problems maps each variable to its possible domain
	protected HashMap<String, ArrayList<Integer>> jMap = new HashMap<>();
	
	//constructor which sets the type of the CSP
	public CSP(char type) {
		this.type = type;
	}
	
	//adds variables to the ArrayList
	public void addVariable(String v) {
		variables.add(v);
	}
	
	//adds all possible values of jDomain to the ArrayList and the HashMap
	public void setJDomain(int d) {
		for (int i = 0; i <= d; i++) {
			jDomain.add(i);
		}
		for (String v : variables) {
			ArrayList<Integer> tempList = new ArrayList<>();
			tempList.addAll(jDomain);
			jMap.put(v, tempList);
		}
	}
	
	//adds possible values of the map domain one by one
	public void addMDomain(char d) {
		mDomain.add(d);
	}
	
	//instantiates jConst
	public void setjConst(int size) {
		jConst = new jConstraints(size);
	}
	
	//instantiates mConst
	public void setmConst(int size) {
		mConst = new mConstraints(size);
	}
	
	//getter methods for certain aspects of the CSP
	public int getVarNumber(String var) {
		return variables.indexOf(var);
	}
	
	public int getSize() {
		return variables.size();
	}
	
	public char getType() {
		return type;
	}
	
	//methods for for trimming the possible domain of a jobshop problem
	public void jMaxTrim() {
		for (String v : jMap.keySet()) {
			ArrayList<Integer> toRemove = new ArrayList<>();
			for (int d : jMap.get(v)) {
				if (d + jConst.taskLength[getVarNumber(v)] > jConst.getMaxTime()) {
					toRemove.add(d);
				}
			}
			jMap.get(v).removeAll(toRemove);
		}
	}
	
	public void jPrecTrim() {
		for (String v : jConst.precedence.keySet()) {
			ArrayList<Integer> toRemove = new ArrayList<>();
			for (int i = 0; i <= jConst.taskLength[getVarNumber(v)] - 1; i++) {
				toRemove.add(i);
			}
			int maxLength = 0;
			for (String p : jConst.precedence.get(v)) {
				if (jConst.taskLength[getVarNumber(p)] > maxLength) {
					maxLength = jConst.taskLength[getVarNumber(p)];
				}
				jMap.get(p).removeAll(toRemove);
			}
			for (int i = 0; i < maxLength; i++) {
				jMap.get(v).remove(jMap.get(v).size() - 1);
			}
		}
	}
}
