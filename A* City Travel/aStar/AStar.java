import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AStar extends Pathfinder {
	
	private static  String file_location = "No file chosen.";

	private int verbocity = 0;
	private City[] cities;
	private LinkedList<State> frontier = new LinkedList<State>();
	
	/**
	 * Constructor to build the A* object.
	 * Calls read_text_file which builds the system's nodes.
	 */
	public AStar() {
		//Added after project.
		//Get file location for cities
		UserInterface ui = new UserInterface();
		file_location = ui.setFileLocation();
		
		read_text_file();
		
		System.out.println(getPath(ui.getCity("Choose start city.", cities), ui.getCity("Choose end city.", cities)));
		
		getPath("La Crosse", "Milwaukee");
	}
	
	@Override
	public void setVerbose(int level) {
		this.verbocity = level;
		if(this.verbocity > 0) {
			System.out.println("Vebose set to on, level : " + this.verbocity + ".");
		}
	}

	/**
	 * FoundPath algorithm based on the book Artificial Intelligence A Modern Approach page 96
	 */
	@Override
	public FoundPath getPathInfo(String startCity, String endCity) {
		int frontier_total_size = 0;
		frontier = new LinkedList<State>(); //Reset frontier for multiple runs
		//Change city strings into objects.
		City start = null, end = null;
		for(City c : cities) {
			if(c.get_name().equals(startCity)) {
				start = c;
			}
			if(c.get_name().equals(endCity)) {
				end = c;
			}
		}
		if(start == null || end == null) {
			System.err.println("ERROR: Start and end cities do not exist in the current system.");
			System.exit(-1);
		}
		
		//Create initial state
		State first_state = new State(start, end);
		//add_to_frontier(first_state);
		add_to_frontier(first_state);
		frontier_total_size++;
		
		/* While Loop:
		 * Steps:
		 * 	1)Remove the highest priority (lowest f() value) from the frontier
		 * 	2)Use this removed state and check if it's the final and optimal state.
		 * 	3)If the state was not the final state, add all child states into the frontier
		 * 	4)Return a found path
		 */
		while(frontier.size() > 0) {			
			//Get the highest priority state.
			State cur_state = frontier.remove(0);
			
			if(verbocity > 0) {
				System.out.println("Expanding state: " + cur_state);
			}
			
			//Check if final state
			if(cur_state.get_city() == end) {
				if(verbocity > 0) {
					System.out.println(cur_state);
					System.out.println("Current Frontier Size: " + frontier.size());
					System.out.println("Total Nodes Placed In Frontier: " + frontier_total_size);
				}
				//Create a path found object.
				MyFoundPath mfp = new MyFoundPath(cur_state, frontier_total_size, frontier.size());
				return mfp;
			}
			
			//Expand all immediate children of this state
			for(Connection conn : cur_state.get_city().get_connections()) {
				State child_state = new State(conn.get_city()
									, getDirectDistance(cur_state.get_city().get_name()
									, conn.get_city_name())
									, cur_state);
				//Add all connections to frontier as state.
				if(child_state.is_valid()) {
					add_to_frontier(child_state);
					frontier_total_size++;
				}
				
			}
		}
		//No solution found
		MyFoundPath mfp = new MyFoundPath(null, frontier_total_size, frontier.size());
		return mfp;
	}

	@Override
	public Optional<Double> getDirectDistance(String startCity, String endCity) {
		if(startCity == null || endCity == null) {
			Optional<Double> empty_opt = Optional.empty();
			return empty_opt;
		}
		
		//find first city
		City tmpC = new City(startCity, 0, 0); //Searchable startCity object, not the actual object 
		int tmpCIndex = Arrays.binarySearch(cities, tmpC);
		if(tmpCIndex > 0) {
			//first city found, find second city in first's connections
			tmpC = cities[tmpCIndex]; //tempC is now the actual startCity object
			Connection tmpCon = new Connection(new City(endCity, 0, 0), 0);
			int connectionIndex = Arrays.binarySearch(tmpC.get_connections(), tmpCon);
			
			if(connectionIndex < 0) {
				//These two cities are not connected
				Optional<Double> empty_opt = Optional.empty();
				return empty_opt;
			}
			Double distance = tmpC.get_connections()[connectionIndex].get_distance();
			Optional<Double> opt = Optional.of(distance);
			return opt;
		}
		//These two cities are not connected
		Optional<Double> empty_opt = Optional.empty();
		return empty_opt;
	}

	@Override
	public List<String> getCities() {
		List<String> l = new LinkedList<String>();
		for(City c : cities) {
			l.add(c.get_name());
		}
		return l;
	}
	
	/**
	 * Adds new state to frontier in the proper order as a priority queue.
	 * 
	 * @param new_state is the newly created state to add to frontier
	 * 
	 */
	private void add_to_frontier(State new_state) {
		int index = 0;
		
		for(State s : frontier) {
			if( new_state.get_f() < s.get_f()) {
				//new_state has lower f() value. f() = heuristic + current cost
				frontier.add(index, new_state);
				return;
			}
			index++;
		}
		//Add to last position, this is our worst estimate.
		frontier.add(index, new_state);
	}
	
	/**
	 * Pulls the specified text file from storage and builds the cities in memory.
	 */
	private void read_text_file() {
		//Create a temp list to hold cities, this will be changed to array after.
		LinkedList<City> citiesList = new LinkedList<City>();
		
		//Open and read file
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(file_location));
					String line = reader.readLine(); //get first line "# name latitude longitude"
					line = reader.readLine(); //Skip the first line because it is a comment.
					
					//Extract Cities with Lat and Lon 
					while(line !=null && !line.contains("#")) {
						String[] ar = line.split(" ");
						double lat, lon;
						String city_name = "";
						
						//"City Name Latitude Longitude"
						lon = Double.parseDouble(ar[ar.length - 1]);
						lat = Double.parseDouble(ar[ar.length - 2]);
						//Index 0 to index ar.length - 3 contain the name of the city.
						for(int i = 0; i < ar.length - 2; i++) {
							city_name += ar[i] + " ";
						}
						//Remove last piece of white space from the city name.
						city_name = city_name.substring(0, city_name.length() - 1);
						
						//Store city object in main memory.
						City c = new City(city_name, lat, lon);
						citiesList.add(c);
						
						line = reader.readLine();
					}
					
					//Extract distances between cities
					line = reader.readLine(); //"# distances" Skip this comment in text file
					while(line !=null) {
						String city1, city2;
						Double distance = Double.MAX_VALUE; //Set to max value for sanity check later.
						//Get city names
						city1 = line.substring(0, line.indexOf(','));
						city2 = line.substring(line.indexOf(',') + 2, line.indexOf(':'));
						distance = Double.parseDouble(line.substring(line.indexOf(':') + 1, line.length()).trim());
						//Store these values in main memory.
						City c1 = null;
						City c2 = null;
						for(City c : citiesList) {
							if(c.get_name().equals(city1)) {
								c1 = c;
							}
							if(c.get_name().equals(city2)) {
								c2 = c;
							}
							if(c1 != null && c2 != null) {
								//Avoid a little extra work.
								break;
							}
						}
						c1.add_connection(c2, distance);
						c2.add_connection(c1, distance);
						
						line = reader.readLine();
					}
				} catch (Exception e) {
					//Catch any exception to handle improper files
					System.err.println("\nCould not read city file from system.  ABORT APPLICATION.\n");
					e.printStackTrace();
					System.exit(-1);
				}
				//change list of cities to sorted array
				cities = citiesList.toArray(new City[citiesList.size()]);
				Arrays.sort(cities);
				
	}
}
