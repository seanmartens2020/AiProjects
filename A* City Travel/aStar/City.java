import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 * @author Sean Martens
 * @created 9/12/19
 *
 */
public class City implements Comparable<City>{
	private String name;
	private double latitude;
	private double longitude;
	private Connection[] connections = new Connection[0];
	
	
	/**
	 * 
	 * @param City
	 * @param latitude
	 * @param longitude
	 */
	public City(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String get_name() {
		return name;
	}
	
	public double get_latitude() {
		return latitude;
	}
	
	public double get_longitude() {
		return longitude;
	}
	
	public Connection[] get_connections() {
		return connections;
	}
	
	public void add_connection(City c, double distance) {
		if(c == null || distance < 0) {
			return;
		}
		
		boolean cities_already_connected = false;
		
		Connection searchConnection = new Connection(c, 0);
		if(Arrays.binarySearch(connections, searchConnection) > 0) {
			//The city already exists in the connections.
			cities_already_connected = true;
		}
		
		if(!cities_already_connected) {
			//add the connection between cities
			//Create new, larger array
			Connection[] tmp = new Connection[connections.length + 1];
			
			//Copy array
			for(int i = 0; i < connections.length; i++) {
				tmp[i] = connections[i];
			}
			
			Connection conn = new Connection(c, distance);
			tmp[tmp.length - 1] = conn;
			connections = tmp;
			Arrays.sort(connections);
		}
	}
	
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(City otherCity) {
		return this.name.compareTo(otherCity.get_name());
	}
}
