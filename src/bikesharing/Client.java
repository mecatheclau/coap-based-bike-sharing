package bikesharing;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
    	boolean act = true;
	    Scanner obj = new Scanner(System.in);
		while (act) { // Infinite loop to keep showing the menu until an exit condition is met
		    System.out.println("---------------------------------------");
		    System.out.println("1. Available bikes");
		    System.out.println("2. Rented bikes");
		    System.out.println("3. Rent bike");
		    System.out.println("4. return bike");
		    System.out.println("---------------------------------------");
		    System.out.println("Choose action to perform: ");

		    int action = obj.nextInt();

		    switch (action) {
		        case 1: {
		            availableBikes();
		            break;
		        }
		        case 2: {
		            rentedBikes();
		            break;
		        }
		        case 3: {
		            rentBike();
		            act = false;
		            break;
		        }
		        case 4: {
		            returnBike();
		            act = false;
		            break;
		        }
		        default:
		        	System.out.println("Choose number between 1-4");
		    }
		}
	    obj.close();
        
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
