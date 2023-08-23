package bikesharing;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import static org.eclipse.californium.core.coap.CoAP.Type;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import org.eclipse.californium.core.server.resources.ResourceObserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import java.lang.Thread;

class BikeDatabase {
	private Map<Long, Bike> bikes;
    private Map<String, Cyclist> cyclists;
    private Map<Long, RentedBike> rentedBikes;
	
	public BikeDatabase() {
		bikes = new HashMap<>();
		cyclists = new HashMap<>();
		rentedBikes = new HashMap<>();
	}
	
	public void addBike(long id, String status, int bF, int bR, int g, double lat, double lon) {
        Bike newBike = new Bike(id, status, bF, bR, g, lat, lon);
        bikes.put(id, newBike);
    }
	
	public void addCyclist(String fiscal_code, String names) {
		Cyclist newCyclist = new Cyclist(fiscal_code, names);
        cyclists.put(fiscal_code, newCyclist);
    }
	
	public void addRentedBike(long id, String fiscal_code) {
		RentedBike rentedBike = new RentedBike(id, fiscal_code);
		rentedBikes.put(id, rentedBike);
    }
    
    public Bike getBike(long id) {
        return bikes.get(id);
    }
    
    public boolean setBikeStatus(long id, String status) {
    	if (bikes.containsKey(id)) {
            Bike bike = bikes.get(id);
            bike.setStatus(status);
            bikes.put(id, bike);
            return true;
        } else {
        	return false;
        }
    }
    
	public void setBikeGPS(long id, double lat, double lon) {
		if (bikes.containsKey(id)) {
	        Bike bike = bikes.get(id);
	        bike.setGPS(lat, lon);
	        bikes.put(id, bike);
	    } else {
	        System.out.println("Bike with ID " + id + " not found.");
	    }
	}

	public void setBikeGear(long id, int g) {
		if (bikes.containsKey(id)) {
		    Bike bike = bikes.get(id);
		    bike.setGear(g);
		    bikes.put(id, bike);
		} else {
		    System.out.println("Bike with ID " + id + " not found.");
		}
	}

	public void setBikeFrontBrake(long id, int bF) {
		if (bikes.containsKey(id)) {
		    Bike bike = bikes.get(id);
		    bike.setBf(bF);
		    bikes.put(id, bike);
		} else {
		    System.out.println("Bike with ID " + id + " not found.");
		}
	}

	public void setBikeRearBrake(long id, int bR) {
		if (bikes.containsKey(id)) {
		    Bike bike = bikes.get(id);
		    bike.setBf(bR);
		    bikes.put(id, bike);
		} else {
		    System.out.println("Bike with ID " + id + " not found.");
		}
	}

    public Cyclist getCyclist(String id) {
        return cyclists.get(id);
    }
    
    public void removeBike(long id) {
        bikes.remove(id);
    }
    
    public void removeCyclist(String id) {
    	cyclists.remove(id);
    }

    public void removeRentedBike(long id) {
    	rentedBikes.remove(id);
    }
    
    public List<Bike> listOfBikes() {
        List<Bike> bikeList = new ArrayList<>();
        for (Bike bike : bikes.values()) {
            bikeList.add(bike);
        }
        return bikeList;
    }
        
    public List<Bike> availableBikes() {
        List<Bike> bikeList = new ArrayList<>();
        for (Bike bike : bikes.values()) {
            if (bike.status == "available") {
            	bikeList.add(bike);
			}
        }
        return bikeList;
    }
    
    public List<RentedBike> rentedBikes() {
        List<RentedBike> bikeList = new ArrayList<>();
        for (RentedBike bike : rentedBikes.values()) {
        	bikeList.add(bike);
        }
        return bikeList;
    }
    
    public List<Cyclist> listOfCyclists() {
        List<Cyclist> cyclistList = new ArrayList<>();
        for (Cyclist cyclist : cyclists.values()) {
        	cyclistList.add(cyclist);
        }
        return cyclistList;
    }

    public static class Bike {
    	public long id; // TODO: 10 unique characters
    	public String status; // TODO: available or rented
    	// TODO: Brake status: 1 pressed, 0 released
    	protected int bF; // TODO break front
    	protected int bR; // TODO: brake rear
    	protected int g; // TODO: Speed-gear: 0-8
    	private double lat; // TODO latitude
    	private double lon; // TODO longitude

        public Bike(long id, String status, int bF, int bR, int g, double lat, double lon) {
            this.id = id;
            this.status = status;
            this.bF = bF;
            this.bR = bR;
            this.g = g;
            this.lat = lat;
            this.lon = lon;
            generateRandomCoordinates();
        }

        @Override
        public String toString() {
            return "ID:" + id + " Status:"+status;
        }
        
        public String getGPS() {
        	return "Lat: "+this.lat + " Lon: "+this.lon;
        }
        
        public void setGPS(double lat, double lon) {
        	this.lat = lat;
        	this.lon = lon;
        }
        
        public void setStatus(String status) {
        	this.status = status;
        }
        
        public String getBf() {
        	String brake_status;
        	if (this.bF == 1) {
				brake_status = "Pressed";
			} else {
				brake_status = "Released";
			}
        	return brake_status;
        }
        
        public void setBf(int bF) {
        	this.bF = bF;
        }
        
        public String getBr() {
        	String brake_status;
        	if (this.bR == 1) {
				brake_status = "Pressed";
			} else {
				brake_status = "Released";
			}
        	return brake_status;
        }
        
        public void setBr(int bR) {
        	this.bR = bR;
        }
        
        public int getGear() {
        	return this.g;
        }
        
        public void setGear(int g) {
        	this.g = g;
        }
        
        // Method to generate random GPS coordinates for the bike
        private void generateRandomCoordinates() {
            Random rand = new Random();
            // Replace with appropriate latitude and longitude bounds for your smart city
            this.lat = 40.7128 + (rand.nextDouble() * 0.1 - 0.05); // Example: New York City
            this.lon = -74.0060 + (rand.nextDouble() * 0.1 - 0.05);
        }
        
    }
        
    public static class Cyclist {
		public String fiscal_code; // TODO: 16 unique characters
		public String names;
		
    	public Cyclist(String fiscal_code, String names) {
    		this.fiscal_code = fiscal_code;
    		this.names = names;
    	}
    	
    	@Override
    	public String toString() {
    		return "Fiscal code:"+fiscal_code+" names:"+names;
    	}
	}
    
    public static class RentedBike {
    	public long bike_id; 
    	public String cyclist_id;
    	
    	public RentedBike(long bike_id, String cyclist_id) {
    		this.bike_id = bike_id;
    		this.cyclist_id = cyclist_id;
    	}
    	
    	@Override
    	public String toString() {
    		return "BID:"+bike_id+" CID:"+cyclist_id;
    	}
    }
    
}


public class Server {

	public static void main(String[] args) {
		CoapServer server = new CoapServer(5689);
						
		// TODO: Create bikes database
		BikeDatabase bikeDB = new BikeDatabase();
		bikeDB.addBike(1234567891L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567892L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567893L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567894L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567895L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567896L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567897L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567898L, "available", 0, 0, 0, 0.0, 0.0);
		bikeDB.addBike(1234567899L, "available", 0, 0, 0, 0.0, 0.0); 
		
		// TODO: Create cyclists database
		bikeDB.addCyclist("100000000000001", "Brave");
		bikeDB.addCyclist("100000000000002", "Joselyne");
		bikeDB.addCyclist("100000000000003", "Meca");
		bikeDB.addCyclist("100000000000004", "Jules");

		// TODO add Coap resources to the server

		DockingStation dockingStation = new DockingStation(bikeDB);
		server.add(dockingStation);
		server.start();
	}
	
	public static class DockingStation extends CoapResource{
		private BikeDatabase bikeDB;
		
		public DockingStation(BikeDatabase bikeDB) {
			super("bikes");
            this.bikeDB = bikeDB;
			getAttributes().setTitle("List of bikes");
						
			add(new AvailableBikes(bikeDB)); // Return available bikes
	        add(new RentedBikes(bikeDB)); // return rented bikes
	        add(new CyclistList(bikeDB));
	        add(new RentBike(bikeDB));
	        add(new ReturnBike(bikeDB));
	        add(new BikeLocationObserver(bikeDB));
	        add(new SetBikeLocation(bikeDB));
	        add(new SetBikeGear(bikeDB));
	        add(new BikeGearObserver(bikeDB));
	        add(new BikeFrontBrakeObserver(bikeDB));
	        add(new SetBikeFrontBrake(bikeDB));
	        add(new BikeRearBrakeObserver(bikeDB));
	        add(new SetBikeRearBrake(bikeDB));
		}
				
		@Override
	    public void handleGET(CoapExchange exchange) {
			List<BikeDatabase.Bike> bikes = bikeDB.listOfBikes();
            
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            String bikesJson = objectMapper.writeValueAsString(bikes);
	            exchange.respond(CONTENT, bikesJson);
	        } catch (Exception e) {
	            exchange.respond(INTERNAL_SERVER_ERROR, "Error processing response");
	        }
	    }
		
		private class CyclistList extends CoapResource {
			private BikeDatabase bikeDB;
	        public CyclistList(BikeDatabase bikeDB) {
	            super("cyclists");
	            this.bikeDB = bikeDB;
	        }

	        @Override
	        public void handleGET(CoapExchange exchange) {
	        	List<BikeDatabase.Cyclist> cyclists = bikeDB.listOfCyclists(); 
	            
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String bikesJson = objectMapper.writeValueAsString(cyclists);
	                exchange.respond(CONTENT, bikesJson);
	            } catch (Exception e) {
	                exchange.respond(INTERNAL_SERVER_ERROR, "Error processing cyclists");
	            }
	        }
	    }
		
		private class AvailableBikes extends CoapResource {
			private BikeDatabase bikeDB;
	        public AvailableBikes(BikeDatabase bikeDB) {
	            super("available");
	            this.bikeDB = bikeDB;
	        }

	        @Override
	        public void handleGET(CoapExchange exchange) {
	        	List<BikeDatabase.Bike> bikes = bikeDB.availableBikes();
	        	
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String bikesJson = objectMapper.writeValueAsString(bikes);
	                exchange.respond(CONTENT, bikesJson);
	            } catch (Exception e) {
	                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
	            }
	        }
	    }
		
		private class RentedBikes extends CoapResource {
			private BikeDatabase bikeDB;
	        public RentedBikes(BikeDatabase bikeDB) {
	            super("rented");
	            this.bikeDB = bikeDB;
	        }

	        @Override
	        public void handleGET(CoapExchange exchange) {
	        	List<BikeDatabase.RentedBike> bikes = bikeDB.rentedBikes();
	        	
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String bikesJson = objectMapper.writeValueAsString(bikes);
	                exchange.respond(CONTENT, bikesJson);
	            } catch (Exception e) {
	                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
	            }
	        }
	    }

		public class RentBike extends CoapResource {
			private BikeDatabase bikeDB;
			public RentBike(BikeDatabase bikeDB) {
				super("rent");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
//				long fiscal_code = Long.parseLong(exchange.getRequestText());
				String requestBody = exchange.getRequestText();
				String fiscal_code = ""; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		            String[] params = requestBody.split("&");
		            for (String param : params) {
		                String[] keyValue = param.split("=");
		                if (keyValue.length == 2) {
		                    if ("fiscal_code".equals(keyValue[0])) {
		                        fiscal_code = keyValue[1];
		                    } else if ("bike_id".equals(keyValue[0])) {
		                        bike_id = Long.parseLong(keyValue[1]);
		                    }
		                }
		            }
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
				
				BikeDatabase.Cyclist cyclist = bikeDB.getCyclist(fiscal_code);
				if (cyclist != null) {
					BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
					if (bike != null) {
						if (bike.status == "available") {
							boolean updated = bikeDB.setBikeStatus(bike_id, "rented");
							if (updated) {
								bikeDB.addRentedBike(bike_id, fiscal_code);
								exchange.respond("Bike rented successfully!");
							} else {
								exchange.respond("Bike renting failed, please try again.");
							}
						} else {
							exchange.respond("Bike not available");
						}
					}
					else {
						exchange.respond("Bike not found");
					}
				}
				else {
					exchange.respond("Cyclist not found");
				}
			}
			
		}


		public class ReturnBike extends CoapResource {
			private BikeDatabase bikeDB;
			public ReturnBike(BikeDatabase bikeDB) {
				super("return");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
//				long fiscal_code = Long.parseLong(exchange.getRequestText());
				String requestBody = exchange.getRequestText();
				String fiscal_code = ""; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		            String[] params = requestBody.split("&");
		            for (String param : params) {
		                String[] keyValue = param.split("=");
		                if (keyValue.length == 2) {
		                    if ("fiscal_code".equals(keyValue[0])) {
		                        fiscal_code = keyValue[1];
		                    } else if ("bike_id".equals(keyValue[0])) {
		                        bike_id = Long.parseLong(keyValue[1]);
		                    }
		                }
		            }
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
				
				BikeDatabase.Cyclist cyclist = bikeDB.getCyclist(fiscal_code);
				if (cyclist != null) {
					BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
					if (bike != null) {
						if (bike.status == "rented") {
							boolean updated = bikeDB.setBikeStatus(bike_id, "available");
							if (updated) {
								bikeDB.removeRentedBike(bike_id);
								exchange.respond("Bike returned successfully!");
							} else {
								exchange.respond("Bike returning failed, please try again.");
							}
						} else {
							exchange.respond("Bike available, it is not rented");
						}
					}
					else {
						exchange.respond("Bike not found");
					}
				}
				else {
					exchange.respond("Cyclist not found");
				}
			}
			
		}
		
		public class SetBikeLocation extends CoapResource {
			private BikeDatabase bikeDB;
		    private Random random = new Random();
		    
			public SetBikeLocation(BikeDatabase bikeDB) {
				super("set-bike-location");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
				String requestBody = exchange.getRequestText();

				long bike_id = Long.parseLong(requestBody);
				System.out.println(bike_id);
				BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
				if (bike != null) {
					double lat = 40.7128 + (random.nextDouble() * 0.1 - 0.05); // Example: New York City
		            double lon = -74.0060 + (random.nextDouble() * 0.1 - 0.05);
		            bikeDB.setBikeGPS(bike_id, lat, lon);
		            exchange.respond("Bike location update");
				}
				else {
					exchange.respond("Bike not found");
				}
			}
			
		}
		
		public class BikeLocationObserver extends CoapResource {

			private BikeDatabase bikeDB;

		    public BikeLocationObserver(BikeDatabase bikeDB) {
		        super("bike-coordinates");
		        this.bikeDB = bikeDB;
		        setObservable(true);
		        setObserveType(Type.CON); // configure the notification type to CONs
		        getAttributes().setObservable(); // mark observable in the Link-Format
		        getAttributes().setTitle("Bike Location Observer");
		    }

		    public void startUpdateTask(long bike_id) {
		        UpdateTask updateTask = new UpdateTask(bike_id);
		        new Timer().schedule(updateTask, 0, 1000); // Update every 5 seconds (5000 milliseconds)
		    }

		    private class UpdateTask extends TimerTask {
		    	private long bike_id;

		    	public UpdateTask(long bike_id) {
		            this.bike_id = bike_id;
		        }

	    	   @Override
	    	   public void run() {
	    		   	synchronized (UpdateTask.class) { // Use a class-level lock
	    	            CoapClient client = new CoapClient("coap://localhost:5689/bikes/set-bike-location"); 
	    	            CoapResponse response = client.post(Long.toString(bike_id), MediaTypeRegistry.TEXT_PLAIN);
	    	            changed(); // notify all observers when the location is updated
	    	            try {
							Thread.sleep(5000); // generate new GPS random data every 5 seconds
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }		            
	    	   }
	    	}
		    
		    @Override
	        public void handleGET(CoapExchange exchange) {
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		        	String bike_id1 = exchange.getRequestOptions().getURIQueries().get(0); // Get the first parameter
		        	 String[] keyValue = bike_id1.split("=");
		        	 bike_id = Long.parseLong(keyValue[1]);
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format."+e);
		            return;
		        }

		        BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
		        if (bike != null) {
		        	// Start the UpdateTask for the specified bike ID
	                startUpdateTask(bike_id);
	                
		            try {
		                exchange.respond("GPS: "+bike.getGPS());
		            } catch (Exception e) {
		                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
		            }
		        } else {
		            exchange.respond("Bike not found");
		        }
	        }
		}

		
		public class SetBikeGear extends CoapResource {
			private BikeDatabase bikeDB;
		    
			public SetBikeGear(BikeDatabase bikeDB) {
				super("change-bike-gear");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
				String requestBody = exchange.getRequestText();
				int g = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		            String[] params = requestBody.split("&");
		            for (String param : params) {
		                String[] keyValue = param.split("=");
		                if (keyValue.length == 2) {
		                    if ("gear".equals(keyValue[0])) {
		                        g = Integer.parseInt(keyValue[1]);
		                    } else if ("bike_id".equals(keyValue[0])) {
		                        bike_id = Long.parseLong(keyValue[1]);
		                    }
		                }
		            }
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
				BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
				if (bike != null) {
		            bikeDB.setBikeGear(bike_id, g);
		            exchange.respond("Bike gear update");
				}
				else {
					exchange.respond("Bike not found");
				}
			}
			
		}
		
		public class BikeGearObserver extends CoapResource {

			private BikeDatabase bikeDB;

		    public BikeGearObserver(BikeDatabase bikeDB) {
		        super("bike-gear");
		        this.bikeDB = bikeDB;
		        setObservable(true);
		        setObserveType(Type.CON); // configure the notification type to CONs
		        getAttributes().setObservable(); // mark observable in the Link-Format
		        getAttributes().setTitle("Bike Location Observer");
		    }

		    public void startUpdateTask(long bike_id, int gear) {
		        UpdateTask updateTask = new UpdateTask(bike_id, gear);
		        new Timer().schedule(updateTask, 0, 1000); // Update every 5 seconds (5000 milliseconds)
		    }

		    private class UpdateTask extends TimerTask {
		    	private long bike_id;
		    	private int gear;

		    	public UpdateTask(long bike_id, int gear) {
		            this.gear = gear;
		            this.bike_id = bike_id;
		        }

	    	   @Override
	    	   public void run() {
	    		   	synchronized (UpdateTask.class) { // Use a class-level lock
	    	            CoapClient client = new CoapClient("coap://localhost:5689/bikes/change-bike-gear"); 
	    	            String requestBody = "gear="+gear+"&bike_id="+bike_id;
	    	            CoapResponse response = client.post(requestBody, MediaTypeRegistry.TEXT_PLAIN);
	    	            changed(); // notify all observers when the location is updated
	    	            try {
							Thread.sleep(5000); // generate new GPS random data every 5 seconds
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }		            
	    	   }
	    	}
		    
		    @Override
	        public void handleGET(CoapExchange exchange) {
		    	String requestBody = exchange.getRequestText();
		    	int gear = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		        	String bike_id1 = exchange.getRequestOptions().getURIQueries().get(0); // Get the first parameter
		        	String[] keyValue = bike_id1.split("=");
		        	bike_id = Long.parseLong(keyValue[1]);
		        	
		        	String gear1 = exchange.getRequestOptions().getURIQueries().get(1); // Get the first parameter
		        	String[] keyValue1 = gear1.split("=");
		        	gear = Integer.parseInt(keyValue1[1]);

		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
		        BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
		        if (bike != null) {
		        	// Start the UpdateTask for the specified bike ID
	                startUpdateTask(bike_id, gear);
	                
		            try {
		                exchange.respond("Current Gear Speed: "+bike.getGear());
		            } catch (Exception e) {
		                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
		            }
		        } else {
		            exchange.respond("Bike not found");
		        }
	        }
		}

		public class SetBikeFrontBrake extends CoapResource {
			private BikeDatabase bikeDB;
		    
			public SetBikeFrontBrake(BikeDatabase bikeDB) {
				super("change-bike-front-brake");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
				String requestBody = exchange.getRequestText();
				int bF = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		            String[] params = requestBody.split("&");
		            for (String param : params) {
		                String[] keyValue = param.split("=");
		                if (keyValue.length == 2) {
		                    if ("brake".equals(keyValue[0])) {
		                        bF = Integer.parseInt(keyValue[1]);
		                    } else if ("bike_id".equals(keyValue[0])) {
		                        bike_id = Long.parseLong(keyValue[1]);
		                    }
		                }
		            }
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
				BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
				if (bike != null) {
		            bikeDB.setBikeFrontBrake(bike_id, bF);
		            exchange.respond("Bike front brake update");
				}
				else {
					exchange.respond("Bike not found");
				}
			}
			
		}
		
		public class BikeFrontBrakeObserver extends CoapResource {

			private BikeDatabase bikeDB;

		    public BikeFrontBrakeObserver(BikeDatabase bikeDB) {
		        super("bike-front-brake");
		        this.bikeDB = bikeDB;
		        setObservable(true);
		        setObserveType(Type.CON); // configure the notification type to CONs
		        getAttributes().setObservable(); // mark observable in the Link-Format
		        getAttributes().setTitle("Bike Front Brake Observer");
		    }

		    public void startUpdateTask(long bike_id, int bF) {
		        UpdateTask updateTask = new UpdateTask(bike_id, bF);
		        new Timer().schedule(updateTask, 0, 1000); // Update every 5 seconds (5000 milliseconds)
		    }

		    private class UpdateTask extends TimerTask {
		    	private long bike_id;
		    	private int bF;

		    	public UpdateTask(long bike_id, int bF) {
		            this.bF = bF;
		            this.bike_id = bike_id;
		        }

	    	   @Override
	    	   public void run() {
	    		   	synchronized (UpdateTask.class) { // Use a class-level lock
	    	            CoapClient client = new CoapClient("coap://localhost:5689/bikes/change-bike-front-brake"); 
	    	            String requestBody = "brake="+bF+"&bike_id="+bike_id;
	    	            CoapResponse response = client.post(requestBody, MediaTypeRegistry.TEXT_PLAIN);
	    	            changed(); // notify all observers when the location is updated
	    	            try {
							Thread.sleep(5000); // generate new GPS random data every 5 seconds
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }		            
	    	   }
	    	}
		    
		    @Override
	        public void handleGET(CoapExchange exchange) {
		    	String requestBody = exchange.getRequestText();
		    	int bF = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		        	String bike_id1 = exchange.getRequestOptions().getURIQueries().get(0); // Get the first parameter
		        	String[] keyValue = bike_id1.split("=");
		        	bike_id = Long.parseLong(keyValue[1]);
		        	
		        	String bF1 = exchange.getRequestOptions().getURIQueries().get(1); // Get the first parameter
		        	String[] keyValue1 = bF1.split("=");
		        	bF = Integer.parseInt(keyValue1[1]);

		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
		        BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
		        if (bike != null) {
		        	// Start the UpdateTask for the specified bike ID
	                startUpdateTask(bike_id, bF);
	                
		            try {
		                exchange.respond("Current Front Brake: "+bike.getBf());
		            } catch (Exception e) {
		                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
		            }
		        } else {
		            exchange.respond("Bike not found");
		        }
	        }
		}

		public class SetBikeRearBrake extends CoapResource {
			private BikeDatabase bikeDB;
		    
			public SetBikeRearBrake(BikeDatabase bikeDB) {
				super("change-bike-rear-brake");
				this.bikeDB = bikeDB;
			}
			
			@Override
			public void handlePOST(CoapExchange exchange) {
				String requestBody = exchange.getRequestText();
				int bR = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		            String[] params = requestBody.split("&");
		            for (String param : params) {
		                String[] keyValue = param.split("=");
		                if (keyValue.length == 2) {
		                    if ("brake".equals(keyValue[0])) {
		                    	bR = Integer.parseInt(keyValue[1]);
		                    } else if ("bike_id".equals(keyValue[0])) {
		                        bike_id = Long.parseLong(keyValue[1]);
		                    }
		                }
		            }
		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
				BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
				if (bike != null) {
		            bikeDB.setBikeFrontBrake(bike_id, bR);
		            exchange.respond("Bike rear brake update");
				}
				else {
					exchange.respond("Bike not found");
				}
			}
			
		}
		
		public class BikeRearBrakeObserver extends CoapResource {

			private BikeDatabase bikeDB;

		    public BikeRearBrakeObserver(BikeDatabase bikeDB) {
		        super("bike-rear-brake");
		        this.bikeDB = bikeDB;
		        setObservable(true);
		        setObserveType(Type.CON); // configure the notification type to CONs
		        getAttributes().setObservable(); // mark observable in the Link-Format
		        getAttributes().setTitle("Bike Rear Brake Observer");
		    }

		    public void startUpdateTask(long bike_id, int bR) {
		        UpdateTask updateTask = new UpdateTask(bike_id, bR);
		        new Timer().schedule(updateTask, 0, 1000); // Update every 5 seconds (5000 milliseconds)
		    }

		    private class UpdateTask extends TimerTask {
		    	private long bike_id;
		    	private int bR;

		    	public UpdateTask(long bike_id, int bR) {
		            this.bR = bR;
		            this.bike_id = bike_id;
		        }

	    	   @Override
	    	   public void run() {
	    		   	synchronized (UpdateTask.class) { // Use a class-level lock
	    	            CoapClient client = new CoapClient("coap://localhost:5689/bikes/change-bike-front-brake"); 
	    	            String requestBody = "brake="+bR+"&bike_id="+bike_id;
	    	            CoapResponse response = client.post(requestBody, MediaTypeRegistry.TEXT_PLAIN);
	    	            changed(); // notify all observers when the location is updated
	    	            try {
							Thread.sleep(5000); // generate new GPS random data every 5 seconds
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    	        }		            
	    	   }
	    	}
		    
		    @Override
	        public void handleGET(CoapExchange exchange) {
		    	String requestBody = exchange.getRequestText();
		    	int bR = 0; // Initialize with an invalid value
		        long bike_id = -1;    // Initialize with an invalid value
		        
		        try {
		        	String bike_id1 = exchange.getRequestOptions().getURIQueries().get(0); // Get the first parameter
		        	String[] keyValue = bike_id1.split("=");
		        	bike_id = Long.parseLong(keyValue[1]);
		        	
		        	String bF1 = exchange.getRequestOptions().getURIQueries().get(1); // Get the first parameter
		        	String[] keyValue1 = bF1.split("=");
		        	bR = Integer.parseInt(keyValue1[1]);

		        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
		            exchange.respond("Invalid request format.");
		            return;
		        }
		        
		        BikeDatabase.Bike bike = bikeDB.getBike(bike_id);
		        if (bike != null) {
		        	// Start the UpdateTask for the specified bike ID
	                startUpdateTask(bike_id, bR);
	                
		            try {
		                exchange.respond("Current rear Brake: "+bike.getBr());
		            } catch (Exception e) {
		                exchange.respond(INTERNAL_SERVER_ERROR, "Error: "+e);
		            }
		        } else {
		            exchange.respond("Bike not found");
		        }
	        }
		}
	
	}

}
