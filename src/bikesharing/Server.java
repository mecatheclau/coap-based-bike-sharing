package bikesharing;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

class BikeDatabase {
	private Map<Integer, Bike> bikes;
	
	public BikeDatabase() {
		bikes = new HashMap<>();
	}
	
	public void addBike(int id, String status, int bF, int bR, int g) {
        Bike newBike = new Bike(id, status, bF, bR, g);
        bikes.put(id, newBike);
    }

    public Bike getBike(int id) {
        return bikes.get(id);
    }
    
    public void removeBike(int id) {
        bikes.remove(id);
    }

//    public void listOfBikes() {
//        System.out.println("List of Bikes:");
//        for (Bike bike : bikes.values()) {
//            System.out.println(bike);
//        }
//    }
    
    public List<Bike> listOfBikes() {
        List<Bike> bikeList = new ArrayList<>();
        for (Bike bike : bikes.values()) {
            bikeList.add(bike);
        }
        return bikeList;
    }

    public static class Bike {
    	public int id; // TODO: 10 unique characters
    	public String status; // TODO: available or rented
    	// TODO: Brake status: 1 pressed, 0 released
    	protected int bF; // TODO break front
    	protected int bR; // TODO: brake rear
    	protected int g; // TODO: Speed-gear: 0-8

        public Bike(int id, String status, int bF, int bR, int g) {
            this.id = id;
            this.status = status;
            this.bF = bF;
            this.bR = bR;
            this.g = g;
        }

        @Override
        public String toString() {
            return "ID:" + id + " Status:"+status;
        }
    }
}

class Cyclist {
	private String names;
	private int fiscal_code; // TODO: 16 unique characters
}

public class Server {

	public static void main(String[] args) {
		CoapServer server = new CoapServer(5689);
						
		// TODO: Create bikes database
		BikeDatabase bikeDB = new BikeDatabase();
		bikeDB.addBike(1234567891, "available", 0, 0, 0);
		bikeDB.addBike(1234567892, "available", 0, 0, 0);
		bikeDB.addBike(1234567893, "available", 0, 0, 0);
		bikeDB.addBike(1234567894, "available", 0, 0, 0);
		bikeDB.addBike(1234567895, "available", 0, 0, 0);
		bikeDB.addBike(1234567896, "available", 0, 0, 0);
		bikeDB.addBike(1234567897, "available", 0, 0, 0);
		bikeDB.addBike(1234567898, "available", 0, 0, 0);
		bikeDB.addBike(1234567899, "available", 0, 0, 0);

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
		}
				
		@Override
	    public void handleGET(CoapExchange exchange) {
			//exchange.respond("TEST");
			List<BikeDatabase.Bike> bikes = bikeDB.listOfBikes();
            
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            String bikesJson = objectMapper.writeValueAsString(bikes);
	            exchange.respond(CONTENT, bikesJson);
	        } catch (Exception e) {
	            exchange.respond(INTERNAL_SERVER_ERROR, "Error processing response");
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
	        	List<BikeDatabase.Bike> bikes = bikeDB.listOfBikes();
	            
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String bikesJson = objectMapper.writeValueAsString(bikes);
	                exchange.respond(CONTENT, bikesJson);
	            } catch (Exception e) {
	                exchange.respond(INTERNAL_SERVER_ERROR, "Error processing response");
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
	        	List<BikeDatabase.Bike> bikes = bikeDB.listOfBikes();
	        	
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	                String bikesJson = objectMapper.writeValueAsString(bikes);
	                exchange.respond(CONTENT, bikesJson);
	            } catch (Exception e) {
	                exchange.respond(INTERNAL_SERVER_ERROR, "Error processing response");
	            }
	        }
	    }

		
	}

}
