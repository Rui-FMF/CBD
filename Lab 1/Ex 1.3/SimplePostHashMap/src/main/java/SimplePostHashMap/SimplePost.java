package SimplePostHashMap;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class SimplePost {
    private Jedis jedis;

    public static String COUNTRIES = "countries"; // Key set for users' name

    public SimplePost() {
        this.jedis = new Jedis("localhost");
    }

    public void saveCountry(String country, String capital) {
        jedis.hset(COUNTRIES, country, capital);
    }

    public Map<String, String> getCountry() {
        return jedis.hgetAll(COUNTRIES);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

public static void main(String[] args) {
        SimplePost board = new SimplePost();
        // set some users
        String[] countries = { "Portugal", "Spain", "France", "England" };
        String[] capitals = { "Lisbon", "Madrid", "Paris", "London" };

        for (int i=0; i<4 ;i++){
            board.saveCountry(countries[i],capitals[i]);
        }
            
        board.getAllKeys().stream().forEach(System.out::println);
        board.getCountry().keySet().stream().forEach(System.out::println);
    }

}

