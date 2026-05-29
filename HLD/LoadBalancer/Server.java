package LoadBalancer;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final String name;
    private final int weight;
    
    // Tracks current ongoing requests
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    // Tracks total requests sent to this server for our Main class validation
    private final AtomicInteger totalRequestsHandled = new AtomicInteger(0);

    public Server(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() { return name; }
    public int getWeight() { return weight; }
    public int getActiveConnections() { return activeConnections.get(); }
    public int getTotalRequests() { return totalRequestsHandled.get(); }

    public void startRequest() {
        activeConnections.incrementAndGet();
        totalRequestsHandled.incrementAndGet();
    }

    public void finishRequest() {
        activeConnections.decrementAndGet();
    }

    // Helper to reset state between tests
    public void reset() {
        activeConnections.set(0);
        totalRequestsHandled.set(0);
    }
}