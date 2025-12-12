/*
1. ARRAYS - Fuel-Efficient Route Detection (Google Maps)
Problem:
You are given an array representing fuel cost per kilo-meter for a long trip.
Find the most fuel-efficient contiguous route segment of length ≥ k.
Requirements:
- Must run in O(n) using sliding window or prefix sums.
- Return: start index, end index, and minimum total cost.
- If two segments have same cost → choose shorter one.
- If still tie → choose the one that starts earlier. 
 */

class Q1Arrays{

    public static BestRoute findCheapestRoute(int[] fuelCosts, int k) {
        int totalStops = fuelCosts.length;
        
        int lowestCost = Integer.MAX_VALUE;
        int shortestTrip = Integer.MAX_VALUE;
        int startPoint = -1;
        int endPoint = -1;
        
        for (int start = 0; start < totalStops; start++) {
            
            for (int end = start; end < totalStops; end++) {
                
                int distance = end - start + 1;
                
                if (distance < k) {
                    continue;
                }
                
                int routeCost = 0;
                for (int i = start; i <= end; i++) {
                    routeCost += fuelCosts[i];
                }
                
                boolean isBetter = false;
                
                if (routeCost < lowestCost) {
                    isBetter = true;
                } else if (routeCost == lowestCost && distance < shortestTrip) {
                    isBetter = true;
                } else if (routeCost == lowestCost && distance == shortestTrip && start < startPoint) {
                    isBetter = true;
                }
                
                if (isBetter) {
                    lowestCost = routeCost;
                    shortestTrip = distance;
                    startPoint = start;
                    endPoint = end;
                }
            }
        }
        
        return new BestRoute(startPoint, endPoint, lowestCost);
    }
    
    static class BestRoute {
        int start;
        int end;
        int cost;
        
        public BestRoute(int start, int end, int cost) {
            this.start = start;
            this.end = end;
            this.cost = cost;
        }
        
        @Override
        public String toString() {
            int distance = end - start + 1;
            return "Start Index: " + start + " | End Index: " + end + " | Cost: " + cost + " | Distance: " + distance + " km";
        }
    }
    
    public static void main(String[] args) {
        int[] route1 = {4, 2, 1, 7, 8, 1, 2, 8, 1, 0};
        int minTrip = 4;
        
        System.out.println("Route 1 Fuel Costs: 4, 2, 1, 7, 8, 1, 2, 8, 1, 0");
        System.out.println("Minimum Distance / k value: " + minTrip + " km");
        
        BestRoute answer1 = findCheapestRoute(route1, minTrip);
        System.out.println("Cheapest Route: " + answer1);
        System.out.println();
        

        // int[] route2 = {5, 3, 3, 1, 2, 4, 6, 2, 1};
        // int minTrip2 = 3;
        
        // System.out.println("Route 2 Fuel Costs: 5, 3, 3, 1, 2, 4, 6, 2, 1");
        // System.out.println("Minimum Distance Required: " + minTrip2 + " km");
        
        // BestRoute answer2 = findCheapestRoute(route2, minTrip2);
        // System.out.println("Cheapest Route: " + answer2);
        // System.out.println();
        

        // int[] route3 = {3, 3, 3, 3, 3};
        // int minTrip3 = 2;
        
        // System.out.println("Route 3 Fuel Costs: 3, 3, 3, 3, 3");
        // System.out.println("Minimum Distance Required: " + minTrip3 + " km");
        
        // BestRoute answer3 = findCheapestRoute(route3, minTrip3);
        // System.out.println("Cheapest Route: " + answer3);
    }
}