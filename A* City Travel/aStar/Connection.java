
public class Connection implements Comparable<Connection>{
	private City connected_to;
	private double distance;
	
	public Connection(City connected_to, double distance) {
		this.connected_to = connected_to;
		this.distance = distance;
	}
	
	public City get_city() {
		return connected_to;
	}
	
	public double get_distance() {
		return distance;
	}
	
	public String get_city_name() {
		return connected_to.get_name();
	}
	
	@Override
	public int compareTo(Connection otherConnection) {
		return this.get_city_name().compareTo(otherConnection.get_city_name());
	}
}
