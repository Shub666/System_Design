package Cache.LRU;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUDemo{

    int capacity;
    LinkedList<String> keyList;
    Map<String, String> cache;

    LRUDemo(int capacity){
        this.capacity = capacity;
        keyList = new LinkedList<>();
        cache = new HashMap<>();
    }

    public void put(String key, String value){

        if(cache.containsKey(key)){
            keyList.remove(key);
            keyList.addLast(key);
        }

        if (cache.size() >= capacity){
            String k = keyList.remove();
            cache.remove(k);
        }

        cache.put(key, value);
        keyList.add(key);
    }


    public String get(String key){

        if(!cache.containsKey(key)){
            return null;
        }
        keyList.remove(key);
        keyList.add(key);
        return cache.get(key);
    }

    public static void main(String[] args) {

        LRUDemo cachDemo = new LRUDemo(4);
        cachDemo.put("abc1", "test1");
        cachDemo.put("abc2", "test2");
        cachDemo.put("abc3", "test3");

        System.out.println("Key List "+ cachDemo.keyList);
        System.out.println("Cache "+ cachDemo.cache);
        System.out.println("***********get data from cache**************");
        System.out.println(cachDemo.get("abc1"));

        System.out.println("***********now print keylist***********");
        System.err.println("updated key list "+ cachDemo.keyList);

        System.out.println("******* add new data to cache ***********");
        cachDemo.put("abc4", "test4");
        System.out.println("added new data in cache list "+ cachDemo.keyList);
        System.out.println(cachDemo.get("abc3"));

        System.out.println("***********now print keylist***********");
        System.err.println("updated key list "+ cachDemo.keyList);

        System.out.println("******* add new extera data to cache ***********");
        cachDemo.put("abc5", "test5");
        System.out.println("added new data in cache list "+ cachDemo.keyList);
        System.out.println("updatded Cache after adding new element "+ cachDemo.cache);

        
    }
}