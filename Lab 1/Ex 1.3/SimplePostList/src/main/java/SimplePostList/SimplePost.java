package SimplePostList;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class SimplePost {
    private Jedis jedis;

    public static String USERS = "users"; // Key set for users' name

    public SimplePost() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.lpush(USERS, username);
    }

    public List<String> getUser() {
        return jedis.lrange(USERS,0,-1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

public static void main(String[] args) {
        SimplePost board = new SimplePost();
        // set some users
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };

        for (String user: users)
            board.saveUser(user);
            
        board.getAllKeys().stream().forEach(System.out::println);
        board.getUser().stream().forEach(System.out::println);
    }

}
