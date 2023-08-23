package bikesharing;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
    	
    	boolean act = true;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                
		while (act) { // Infinite loop to keep showing the menu until an exit condition is met
		    System.out.println("---------------------------------------");
		    System.out.println("1. Available bikes");
		    System.out.println("2. Rented bikes");
		    System.out.println("3. Rent bike");
		    System.out.println("4. return bike");
		    System.out.println("5. Bike coordinates");
		    System.out.println("6. Change gear");
		    System.out.println("7. Front brake");
		    System.out.println("8. Rear brake");
		    System.out.println("---------------------------------------");
		    System.out.println("Choose action to perform: ");
		    
		    String userInput = null;
	        try {
	            userInput = reader.readLine();
	        	String payload = userInput; // TODO: read the bike id from the user
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        String action = userInput;

		    switch (action) {
		        case "1": {
		            availableBikes();
		            break;
		        }
		        case "2": {
		            rentedBikes();
		            break;
		        }
		        case "3": {
		            rentBike();
		            act = false;
		            break;
		        }
		        case "4": {
		            returnBike();
		            act = false;
		            break;
		        }
		        case "5": {
		        	bikeCoordinates();
		            act = false;
		            break;
		        }
		        case "6": {
		        	bikeGear();
		            act = false;
		            break;
		        }
		        case "7": {
		        	bikeFrontBrake();
		            act = false;
		            break;
		        }
		        case "8": {
		        	bikeRearBrake();
		            act = false;
		            break;
		        }
		        default:
		        	System.out.println("Choose number between 1-4");
		    }
		}
        
    }
    
    public static void availableBikes() {
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/available");
        CoapResponse response = client.get();
        print_response(response);
    }
    
    public static void rentedBikes() {
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/rented");
        CoapResponse response = client.get();
        print_response(response);
    }
    
    public static void rentBike() {
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/rent"); 
    	Scanner obj = new Scanner(System.in);
    	System.out.println("Enter bike ID");
    	long bike_id = obj.nextLong();   // TODO: read the bike id from the user
    	obj.nextLine();
    	System.out.println("Enter fiscal code");
    	String fiscal_code = obj.nextLine();   // TODO: read the fiscal code from the user
    	String requestBody = "fiscal_code="+fiscal_code+"&bike_id="+bike_id;
    	obj.close();
	    CoapResponse response = client.post(requestBody, MediaTypeRegistry.TEXT_PLAIN);
    	print_response(response);
    }
    
    public static void returnBike() {
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/return");
    	Scanner obj = new Scanner(System.in);
    	System.out.println("Enter bike ID \n");
    	long bike_id = obj.nextLong();   // TODO: read the bike id from the user
    	obj.nextLine();
    	System.out.println("Enter fiscal code");
    	String fiscal_code = obj.nextLine();   // TODO: read the fiscal code from the user
    	String requestBody = "fiscal_code="+fiscal_code+"&bike_id="+bike_id;
    	obj.close();
	    CoapResponse response = client.post(requestBody, MediaTypeRegistry.TEXT_PLAIN);
    	print_response(response);
    }
    
    public static void bikeCoordinates() throws InterruptedException {
		// TODO: OBSERVE 2
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String bike_id = null;
    	try {
            System.out.println("Enter bike ID:");
            bike_id = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/bike-coordinates?bike_id="+bike_id);
    	System.out.println("OBSERVE (press enter to exit)");
		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override public void onLoad(CoapResponse response) {
				String content = response.getResponseText();
				System.out.println(content);
			}
			
			@Override public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}
		});
		
		// wait for user
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try { br.readLine(); } catch (IOException e) { }
		
		System.out.println("CANCELLATION");
		
		relation.proactiveCancel();
		// TODO: END OF OBSERVE 2
    	
    }
    
    public static void bikeGear() throws InterruptedException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String bike_id = null;
	    String gear = null;
    	try {
            System.out.println("Enter bike ID:");
            bike_id = reader.readLine();
            System.out.println("Enter gear speed(0-8):");
            gear = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/bike-gear?bike_id="+bike_id+"&gear="+gear);
    	System.out.println("OBSERVE (press enter to exit)");
		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override public void onLoad(CoapResponse response) {
				String content = response.getResponseText();
				System.out.println(content);
			}
			
			@Override public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}
		});

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try { br.readLine(); } catch (IOException e) { }
		System.out.println("CANCELLATION");
		
		relation.proactiveCancel();
    	
    }
    
    public static void bikeFrontBrake() throws InterruptedException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String bike_id = null;
	    String brake = null;
    	try {
            System.out.println("Enter bike ID:");
            bike_id = reader.readLine();
            System.out.println("Front brake: 1 pressed, 0 released");
            brake = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/bike-front-brake?bike_id="+bike_id+"&brake="+brake);
    	System.out.println("OBSERVE (press enter to exit)");
		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override public void onLoad(CoapResponse response) {
				String content = response.getResponseText();
				System.out.println(content);
			}
			
			@Override public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}
		});

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try { br.readLine(); } catch (IOException e) { }
		System.out.println("CANCELLATION");
		
		relation.proactiveCancel();
    	
    }
    
    public static void bikeRearBrake() throws InterruptedException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    String bike_id = null;
	    String brake = null;
    	try {
            System.out.println("Enter bike ID:");
            bike_id = reader.readLine();
            System.out.println("Rear brake: 1 pressed, 0 released");
            brake = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	CoapClient client = new CoapClient("coap://localhost:5689/bikes/bike-rear-brake?bike_id="+bike_id+"&brake="+brake);
    	System.out.println("OBSERVE (press enter to exit)");
		CoapObserveRelation relation = client.observe(new CoapHandler() {
			@Override public void onLoad(CoapResponse response) {
				String content = response.getResponseText();
				System.out.println(content);
			}
			
			@Override public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}
		});

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try { br.readLine(); } catch (IOException e) { }
		System.out.println("CANCELLATION");
		
		relation.proactiveCancel();
    	
    }
    
    public static void print_response(CoapResponse response) {
    	if (response!=null) {
        	System.out.println( response.getCode() );
        	System.out.println( response.getOptions() );
        	System.out.println( response.getResponseText() );
        } else {
        	System.out.println("Request failed");
        }
    }

}
