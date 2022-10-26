import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Driver {

	//checks if an assignment is complete (uses the consistent method
	public static boolean completeCheck(CSP csp, HashMap<String, String> assignment) {
		if (!(assignment.size() == csp.getSize())) {
			return false;
		}
		return consistent(csp, assignment);
	}
	
	//checks if the assignment is consistent if a certain value is added
	public static boolean valConsistency(String var, String val, CSP csp, HashMap<String, String> assignment) {
		assignment.put(var, val);
		boolean consistency = consistent(csp, assignment);
		assignment.remove(var, val);
		return consistency;
	}
	
	//checks if a current assignment is consistent
	public static boolean consistent(CSP csp, HashMap<String, String> assignment) {
		if (csp.getType() == 'j') {
			for (int i = 0; i < csp.getSize(); i++) {
				String currVar = csp.variables.get(i);
				int currAssig;
				if (assignment.containsKey(currVar)) {
					currAssig = Integer.parseInt(assignment.get(currVar));
				}
				else {
					continue;
				}
				
				int currLen = csp.jConst.taskLength[i];
				
				if (csp.jConst.precedence.containsKey(currVar)) {
					for (String p : csp.jConst.precedence.get(currVar)) {
						if (!assignment.containsKey(p)) {
							continue;
						}
						if (currAssig + currLen > Integer.parseInt(assignment.get(p))) {
							return false;
						}
					}
				}
				
				if (csp.jConst.disjoint.containsKey(currVar)) {
					for (String d : csp.jConst.disjoint.get(currVar)) {
						if (!assignment.containsKey(d)) {
							continue;
						}
						if (!(currAssig + currLen <= Integer.parseInt(assignment.get(d)) || Integer.parseInt(assignment.get(d)) + csp.jConst.taskLength[csp.getVarNumber(d)] <= currAssig)) {
							return false;
						}
					}
				}
			}
		}
		else {
			for (int i = 0; i < csp.getSize(); i++) {
				String currVar = csp.variables.get(i);
				String currAssig;
				if (assignment.containsKey(currVar)) {
					currAssig = assignment.get(currVar);
				}
				else {
					continue;
				}
				if (csp.mConst.unary[i].charAt(0) == 'y') {
					if (currAssig.charAt(0) != csp.mConst.unary[i].charAt(1)) {
						return false;
					}
				}
				if (csp.mConst.unary[i].charAt(0) == 'n') {
					if (currAssig.charAt(0) == csp.mConst.unary[i].charAt(1)) {
						return false;
					}
				}
				if (csp.mConst.borders.containsKey(currVar)) {
					for (String b : csp.mConst.borders.get(currVar)) {
						if (!assignment.containsKey(b)) {
							continue;
						}
						if (currAssig.charAt(0) == assignment.get(b).charAt(0)) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	//returns a variable that hasn't been given an assignment
	public static String selectUnassig(CSP csp, HashMap<String, String> assignment) {
		for (int i = 0; i < csp.getSize(); i++) {
			if (!assignment.containsKey(csp.variables.get(i))) {
				return csp.variables.get(i);
			}
		}
		return "";
	}
	
	//starts the backtracking process
	public static HashMap<String, String> backtrackingSearch(CSP csp) {
		HashMap<String, String> assignment = new HashMap<>();
		return backtrack(csp, assignment);
	}
	
	//backtrack method following the textbook pseudo-code
	public static HashMap<String, String> backtrack(CSP csp, HashMap<String, String> assignment) {
		if (completeCheck(csp, assignment)) {
			return assignment;
		}
		char type = csp.getType();
		String var = selectUnassig(csp, assignment);
		if (type == 'j' ) {
			for (int d : csp.jMap.get(var)) {
				if (valConsistency(var, String.valueOf(d), csp, assignment)) {
					assignment.put(var, String.valueOf(d));
					HashMap<String, String> result = backtrack(csp, assignment);
					if (result.size() == csp.getSize()) {
						return result;
					}
					else {
						assignment.remove(var, String.valueOf(d));
					}
				}
			}
		}
		else {
			for (char d : csp.mDomain) {
				if (valConsistency(var, String.valueOf(d), csp, assignment)) {
					assignment.put(var, String.valueOf(d));
					HashMap<String, String> result = new HashMap<>();
					result.putAll(backtrack(csp,assignment));
					if (result.size() == csp.getSize()) {
						return result;
					}
					else {
						assignment.remove(var, String.valueOf(d));
					}
				}
			}
		}
		
		return assignment;
	}
	
	//reads in a CSP from a file, runs backtrackingSearch, and prints solution if any
	public static void main(String[] args) throws IOException {
		char type = 'x';
		int counter = 2;
		int varSize = -1;
		
		File file = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String currLine = br.readLine();
		if (currLine.charAt(0) == 'm') {
			type = 'm';
		}
		if (currLine.charAt(0) == 'j') {
			type = 'j';
		}
		CSP csp = new CSP(type);
		
		while ((currLine = br.readLine()) != null) {
			if (counter == 2) {
				counter++;
			}
			else if (counter == 3) {
				String[] vars = currLine.split(" ");
				varSize = vars.length;
				for (int i = 0; i < varSize; i++) {
					csp.addVariable(vars[i]);
				}
				if (type == 'j') {
					csp.setjConst(csp.getSize());
				}
				else {
					csp.setmConst(csp.getSize());
				}
				counter++;
			}
			else if (counter <= 3 + varSize) {
				if (type == 'j') {
					String[] currTask = currLine.split(" ");
					csp.jConst.addTaskLength(csp.getVarNumber(currTask[0]), Integer.parseInt(currTask[1]));
				}
				else {
					String[] currReg = currLine.split(" ");
					for (int i = 1; i < currReg.length; i++) {
						csp.mConst.addBorder(currReg[0], currReg[i]);
					}
				}
				counter++;
			}
			else if (counter == 4 + varSize) {
				if (type == 'j') {
					int maxTime = Integer.parseInt(currLine);
					csp.jConst.setMaxTime(maxTime);
					csp.setJDomain(maxTime);
				}
				else {
					String[] mDom = currLine.split(" ");
					for (int i = 0; i < mDom.length; i++) {
						csp.addMDomain(mDom[i].charAt(0));
					}
				}
				counter++;
			}
			else {
				String[] currConst = currLine.split(" ");
				if (type == 'j') {
					if (currConst[1].charAt(0) == 'b') {
						csp.jConst.addPrecedence(currConst[0], currConst[2]);
					}
					if (currConst[1].charAt(0) == 'd') {
						csp.jConst.addDisjoint(currConst[0], currConst[2]);
					}
				}
				else {
					if (currConst[1].length() == 1) {
						csp.mConst.addUnary(csp.getVarNumber(currConst[0]), "y" + currConst[2]);
					}
					if (currConst[1].length() == 2) {
						csp.mConst.addUnary(csp.getVarNumber(currConst[0]), "n" + currConst[2]);
					}
				}
			}
		}
		br.close();
		
		if (csp.getType() == 'j') {
			csp.jMaxTrim();
			csp.jPrecTrim();
		}
		
		HashMap<String, String> solution = new HashMap<>();
		solution.putAll(backtrackingSearch(csp));
		
		
		if (solution.size() != csp.getSize()) {
			System.out.println("no solution");
		}
		else {
			for (String k : csp.variables) {
				System.out.println(k + " " + solution.get(k));
			}
		}
	}

}
