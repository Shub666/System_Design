package Cache.LFU;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LFUDemo {

    int capacity;
    Map<String, String> cache;
    Map<String, Integer> cacheFreq;

    LFUDemo(int capacity){
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.cacheFreq = new LinkedHashMap<>();
    }

    public String get(String key){

        if(!cache.containsKey(key)){
            return null;
        }
        String value = cache.get(key);
        cacheFreq.put(key, cacheFreq.get(key)+1);
        return value;
    }

    public void put(String key, String value){

        if(capacity <= 0) return;

        if(cache.containsKey(key)){
            cache.put(key, value);
            cacheFreq.put(key, cacheFreq.get(key)+1);
            return;
        }

        if(cache.size() >= capacity){

            String k = cacheFreq.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
            String v = cache.get(k);
            cache.remove(k);
            cacheFreq.remove(k);
        }

        cache.put(key, value);
        cacheFreq.put(key, 1);
    }

    public static void main(String[] args) {

         // Test 1: Basic put and get
    LFUDemo cache = new LFUDemo(2);
    
    cache.put("key1", "value1");
    cache.put("key2", "value2");
    
    System.out.println("Get key1: " + cache.get("key1")); // Expected: value1
    System.out.println("Get key2: " + cache.get("key2"));  // Expected: value2
    System.out.println("Get key1: " + cache.get("key1"));
    
    // Test 2: Access frequency - key1 accessed twice, key2 once
    System.out.println("key1 frequency: " + cache.cacheFreq.get("key1")); // Expected: 2
    System.out.println("key2 frequency: " + cache.cacheFreq.get("key2")); // Expected: 1
    
    // Test 3: Eviction - add key3, should evict key2 (lowest frequency)
    cache.put("key3", "value3");
    System.out.println("cache after update : "+ cache.cache);
    System.out.println("fre cache after update : "+ cache.cacheFreq);
    System.out.println("Get key2 after eviction: " + cache.get("key2")); // Expected: null
    System.out.println("Get key3: " + cache.get("key3")); // Expected: value3
    System.out.println("fre cache after update : "+ cache.cacheFreq);
    
    // Test 4: Update existing key
    cache.put("key1", "updated_value1");
    System.out.println("Get key1 after update: " + cache.get("key1")); // Expected: updated_value1
    System.out.println("cache after update : "+ cache.cache);
    System.out.println("fre cache after update : "+ cache.cacheFreq);

    }
    
}
