/*
10. GRAPH + DIJKSTRA - Minimum Toll Path (Google Maps)
Problem:
Given a road network with tolls and traffic weights,
find the cheapest path from source to destination.
Requirements:
- Edge cost formula:
totalCost = toll + (trafficWeight × 0.5)
- Must output:
o cheapest path
o total cost
o list of intersections
o second-best alternative path
- Graph may contain cycles and one-way edges.
- Use Dijkstra + parent path reconstruction. 
*/

import java.util.*;
public class Q10Graph {

    static class Road {
        String destination;
        double toll;
        double traffic;

        public Road(String destination, double toll, double traffic) {
            this.destination = destination;
            this.toll = toll;
            this.traffic = traffic;
        }

        public double cost() {
            return toll + 0.5 * traffic;
        }
    }

    static class CityMap {
        Map<String, List<Road>> roads = new HashMap<>();

        public void addRoad(String from, String to, double toll, double traffic) {
            roads.putIfAbsent(from, new ArrayList<>());
            roads.get(from).add(new Road(to, toll, traffic));
        }

        public List<Road> getRoads(String city) {
            return roads.getOrDefault(city, new ArrayList<>());
        }
    }

    static class Route {
        double totalCost;
        List<String> path;

        public Route(double totalCost, List<String> path) {
            this.totalCost = totalCost;
            this.path = path;
        }
    }

    public static class TollCalculator {

        public static Route[] findCheapestPaths(CityMap map, String start, String end) {
            PriorityQueue<Route> pq = new PriorityQueue<>(Comparator.comparingDouble(r -> r.totalCost));
            pq.add(new Route(0, new ArrayList<>(List.of(start))));

            Map<String, Double> visitedCost = new HashMap<>();
            List<Route> completedRoutes = new ArrayList<>();

            while (!pq.isEmpty()) {
                Route current = pq.poll();
                String currentCity = current.path.get(current.path.size() - 1);

                if (visitedCost.containsKey(currentCity) && visitedCost.get(currentCity) <= current.totalCost) {
                    continue;
                }
                visitedCost.put(currentCity, current.totalCost);

                if (currentCity.equals(end)) {
                    completedRoutes.add(current);
                    continue;
                }

                for (Road road : map.getRoads(currentCity)) {
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(road.destination);
                    double newCost = current.totalCost + road.cost();
                    pq.add(new Route(newCost, newPath));
                }
            }

            completedRoutes.sort(Comparator.comparingDouble(r -> r.totalCost));

            Route best = completedRoutes.size() > 0 ? completedRoutes.get(0) : null;
            Route secondBest = completedRoutes.size() > 1 ? completedRoutes.get(1) : null;

            return new Route[]{best, secondBest};
        }

        public static void main(String[] args) {
            CityMap map = new CityMap();
            map.addRoad("A", "B", 5, 4);
            map.addRoad("B", "C", 3, 2);
            map.addRoad("A", "C", 10, 1);
            map.addRoad("C", "D", 1, 3);
            map.addRoad("B", "D", 4, 4);

            Route[] result = findCheapestPaths(map, "A", "D");

            if (result[0] != null) {
                System.out.println("Cheapest Path: " + result[0].path);
                System.out.println("Total Cost: " + result[0].totalCost);
            }

            if (result[1] != null) {
                System.out.println("Second-Best Path: " + result[1].path);
                System.out.println("Total Cost: " + result[1].totalCost);
            }
        }
    }
}
