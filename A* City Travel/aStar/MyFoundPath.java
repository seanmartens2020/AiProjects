import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class MyFoundPath implements FoundPath {

	private State finalState;
	private Integer total_cost;
	private int totalNodes;
	private int openNodes;
	
	public MyFoundPath(State finalState, int totalNodes, int openNodes) {
		this.totalNodes = totalNodes;
		this.finalState = finalState;
		this.openNodes = openNodes;
	}
	
	@Override
	public List<String> getPath() {
		//The order of the state cities is from goal to start
		//Use a stack to a queue(List) to invert the order.
		Stack<String> stck = new Stack<String>();
		
		State tmp = finalState;
		while(tmp!=null) {
			stck.add(tmp.get_city().get_name());
			tmp = tmp.get_previous_state();
		}
		
		List<String> lst = new LinkedList<String>();
		tmp = finalState;
		while(!stck.isEmpty()) {
			lst.add(stck.pop());
		}
		return lst;
	}

	@Override
	public Optional<Integer> getTotalCost() {
		int cost = getPath().size();
		if(cost == 0) {
			Optional<Integer> empty_opt = Optional.empty();
			return empty_opt;
		}
		Integer i = Integer.valueOf(cost);
		Optional<Integer> opt = Optional.of(i);
		return opt;
	}

	@Override
	public int totalNodes() {
		return totalNodes;
	}

	@Override
	public int openNodes() {
		return openNodes;
	}
	
	public String toString() {
		return("Path: " + finalState + "\nTotal Nodes Created: " + totalNodes + "\nOpen Nodes: " + openNodes);
	}

}
