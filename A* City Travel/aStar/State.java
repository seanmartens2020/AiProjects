import java.util.LinkedList;
import java.util.Optional;

public class State {
	
	private City current_city;
	private City prev_city;
	private City end_city;
	private double heuristic = 0; //Optimistic cost to get from n to the goal
	private double g = 0; //Known & calculated cost to get from start state to this state
	private double f = 0; // frontier value. f(n) = g(n) + h(n) where n = this state; h = heuristic
	private State previous_state = null;
	
	/**
	 * This smaller constructor is for creating the initial state.
	 * 
	 * @param first_city
	 * @param end_city
	 */
	public State(City first_city, City end_city) {
		this.current_city = first_city;
		this.end_city = end_city;
		heuristic = heuristic(first_city, end_city);
		f = heuristic;
	}
	
	/**
	 * This constructor will be used by every node other than the first.
	 * 
	 * @param current_city
	 * @param end_city
	 * @param distance
	 * @param previous_state
	 */
	public State(City current_city, Optional<Double> distance, State previous_state) {
		this.current_city = current_city;
		this.prev_city = previous_state.get_city();
		this.end_city = previous_state.get_end_city();
		this.heuristic = heuristic(current_city, end_city);
		this.previous_state = previous_state;
		
		if(distance.isEmpty()) {
			//Unlinked cities are attempting to be traversed
			this.g = Double.MAX_VALUE - heuristic; //Set this high to make f() = Double.MAX
			System.err.println("Cities are not connected by roads.");
			System.exit(-2);
		} else {
			//g = total cost to get to this city, so far.
			this.g = previous_state.get_g() + distance.get(); //Add actual distance to previous total_cost
		}
		//new f() is the previously accumulated cost, plus heuristic.
		this.f = this.g + heuristic;
	}
	
	/**
	 * Returns true if previous states do not contain current state's city
	 * @return
	 */
	public boolean is_valid() {
		LinkedList<City> cities = new LinkedList<City>();
		State tmp = this.previous_state;
		
		while(tmp != null) {
			cities.add(tmp.get_city());
			tmp = tmp.previous_state;
		}
		
		tmp = this.previous_state;
		while(tmp != null) {
			if(cities.contains(this.get_city())) {
				return false;
			}
			tmp = tmp.previous_state;
		}
		return true;
	}
	
	public City get_city() {
		return current_city;
	}
	
	public double get_f() {
		return f;
	}
	
	public double get_g() {
		return g;
	}
	
	public State get_previous_state() {
		return previous_state;
	}
	
	public City get_end_city() {
		return end_city;
	}
	
	public String toString() {
		if(previous_state == null) {
			return this.current_city.get_name();
		}
		return previous_state.toString() + "-> " + get_city().get_name();
	}
	
	/**
	 * Thank you to https://www.geeksforgeeks.org/program-distance-two-points-earth/ for posting this algorithm.
	 * 
	 * @param city1
	 * @param city2
	 * @return
	 */
	private double heuristic(City city1, City city2) {
		// The math module contains a function 
        // named toRadians which converts from 
        // degrees to radians. 
        double lon1 = Math.toRadians(city1.get_longitude()); 
        double lon2 = Math.toRadians(city2.get_longitude()); 
        double lat1 = Math.toRadians(city1.get_latitude()); 
        double lat2 = Math.toRadians(city2.get_latitude()); 
  
        // Haversine formula  
        double dlon = lon2 - lon1;  
        double dlat = lat2 - lat1; 
        double a = Math.pow(Math.sin(dlat / 2), 2) 
                 + Math.cos(lat1) * Math.cos(lat2) 
                 * Math.pow(Math.sin(dlon / 2),2); 
              
        double c = 2 * Math.asin(Math.sqrt(a)); 
        //Radius of Earth
        double r = 3956; 
  
        // calculate the result 
        return(c * r); 
	}
}
