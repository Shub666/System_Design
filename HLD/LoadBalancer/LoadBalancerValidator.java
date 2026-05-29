package LoadBalancer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class LoadBalancerValidator {

    private static final int TOTAL_REQUESTS = 6000;
    private static final int CONCURRENT_THREADS = 50;

    public static void main(String[] args) throws InterruptedException {
        // Create 3 servers with weights 1, 2, and 3
        Server s1 = new Server("Server-A", 1);
        Server s2 = new Server("Server-B", 2);
        Server s3 = new Server("Server-C", 3);
        List<Server> cluster = Arrays.asList(s1, s2, s3);

        System.out.println("Starting Thread-Safe Load Balancer Validation...\n");

        // 1. Validate Round Robin
        RoundRobinLoadBalancer rr = new RoundRobinLoadBalancer(cluster);
        runConcurrentTest("Round Robin", cluster, () -> {
            Server target = rr.getServer();
            simulateWork(target); // Simulates target.startRequest() and target.finishRequest()
        });
        // Expect: 6000 / 3 = 2000 per server

        // 2. Validate Weighted Round Robin
        WeightedRoundRobinLoadBalancer wrr = new WeightedRoundRobinLoadBalancer(cluster);
        runConcurrentTest("Weighted Round Robin", cluster, () -> {
            Server target = wrr.getServer();
            simulateWork(target);
        });
        // Expect: A gets 1000, B gets 2000, C gets 3000 (Total Weight = 6)

        // 3. Validate Random
        RandomLoadBalance randomLB = new RandomLoadBalance(cluster);
        runConcurrentTest("Random", cluster, () -> {
            Server target = randomLB.getServer();
            simulateWork(target);
        });
        // Expect: Roughly 2000 each (varies slightly due to randomness)

        // 4. Validate Least Connections
        LeastConnectionsLoadBalancer lcLB = new LeastConnectionsLoadBalancer(cluster);
        runConcurrentTest("Least Connections", cluster, () -> {
            // Note: startRequest() is handled inside getServer() for this specific algorithm 
            // to ensure atomic locking, so we only call finishRequest() afterwards.
            Server target = lcLB.getServer(); 
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(5)); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                target.finishRequest();
            }
        });
        // Expect: Roughly 2000 each. 

        // 5. Validate IP Hash
        IpHashLoadBalancer ipLB = new IpHashLoadBalancer(cluster);
        runConcurrentTest("IP Hash", cluster, () -> {
            // Simulate 3 distinct users
            String[] ips = {"192.168.1.1", "10.0.0.5", "172.16.0.8"};
            String randomIp = ips[ThreadLocalRandom.current().nextInt(ips.length)];
            
            Server target = ipLB.getServer(randomIp);
            simulateWork(target);
        });
        // Expect: Consistent routing for the same IP. 
    }

    /**
     * Executes the requests using a ThreadPool to simulate massive concurrency.
     */
    private static void runConcurrentTest(String name, List<Server> cluster, Runnable lbTask) throws InterruptedException {
        cluster.forEach(Server::reset); // Clear previous test data

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executor.submit(() -> {
                try {
                    lbTask.run();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait for all 6000 requests to finish
        executor.shutdown();

        System.out.println("=== " + name + " Results ===");
        int total = 0;
        for (Server s : cluster) {
            System.out.printf("%s (Weight %d): Handled %d requests%n", 
                    s.getName(), s.getWeight(), s.getTotalRequests());
            total += s.getTotalRequests();
        }
        System.out.println("Total Requests Processed: " + total + "\n");
    }

    /**
     * Simulates a server processing a request.
     */
    private static void simulateWork(Server server) {
        server.startRequest();
        try {
            // Simulate variable network/processing delay (0-5ms)
            Thread.sleep(ThreadLocalRandom.current().nextInt(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            server.finishRequest();
        }
    }
}
