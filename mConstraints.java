import java.util.ArrayList;
import java.util.HashMap;

public class mConstraints {
	
	//map that stores what regions a variable borders
	protected HashMap<String, ArrayList<String>> borders;
	
	//array that stores the unary constraints of a map CSP
	protected String[] unary;
	
	//constructor which instantiates the members
	public mConstraints(int size) {
		borders = new HashMap<>();
		unary = new String[size];
		for (int i = 0; i < size; i++) {
			unary[i] = "x";
		}
	}
	
	//sets if a region needs to a be a certain color or needs to not be a certain color
	public void addUnary(int region, String pref) {
		unary[region] = pref;
	}
	
	//sets borders
	public void addBorder(String region, String touching) {
		if (!borders.containsKey(region)) {
			borders.put(region, new ArrayList<String>());
			borders.get(region).add(touching);
		}
		else {
			borders.get(region).add(touching);
		}
	}
}
