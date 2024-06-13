# CoAP-Based Bike Sharing System using Californium Framework

## Introduction

This project implements a bike-sharing system using the CoAP (Constrained Application Protocol) and the Californium framework. The system allows users to perform various operations on bikes such as renting, returning, changing gears, and applying brakes through a command-line interface (CLI).

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven
- Californium CoAP Framework

## Setup

**Clone the Repository**: Clone the repository to your local machine.
   ```bash
   git clone https://github.com/mecatheclau/coap-based-bike-sharing.git
   cd coap-based-bike-sharing
   ```

## CLI Menu

Once the client application is running, you will see the following menu:

```
CLI Menu
------------------
1. Available bikes
2. Rented bikes
3. Rent bike
4. Return bike
5. Bike coordinates
6. Change gear
7. Front brake
8. Rear brake
---------------------------------------
Choose action to perform: 
```

### Menu Options

1. **Available Bikes**: List all the bikes currently available for rent.
2. **Rented Bikes**: List all the bikes that are currently rented out.
3. **Rent Bike**: Rent a bike by providing the bike ID.
4. **Return Bike**: Return a rented bike by providing the bike ID.
5. **Bike Coordinates**: Get the current coordinates of a bike by providing the bike ID.
6. **Change Gear**: Change the gear of a bike by providing the bike ID and the desired gear number.
7. **Front Brake**: Apply the front brake of a bike by providing the bike ID.
8. **Rear Brake**: Apply the rear brake of a bike by providing the bike ID.

## Example Usage

Here is an example of how to use the CLI to rent a bike:

1. Choose the "Available bikes" option to see which bikes are available:
   ```plaintext
   Choose action to perform: 1
   ```

2. Note the ID of the bike you want to rent and choose the "Rent bike" option:
   ```plaintext
   Choose action to perform: 3
   Enter bike ID to rent: 101
   ```

3. To return the bike, choose the "Return bike" option:
   ```plaintext
   Choose action to perform: 4
   Enter bike ID to return: 101
   ```
![Screenshot from 2024-06-13 18-49-49](https://github.com/mecatheclau/coap-based-bike-sharing/assets/43791985/c6d5c003-4007-40fb-b7a1-28ffccfa21ac)
