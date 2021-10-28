package MsgApp;

import java.util.*;
import redis.clients.jedis.Jedis;

public class App 
{
    private Jedis jedis;

    public App() {
        this.jedis = new Jedis("localhost");
    }

    public void addUser(String name){
        jedis.sadd("Users", name);
    }

    public void delUser(String name){
        jedis.srem("Users", name);
        jedis.del("User:"+name+":Followers");
        jedis.del("User:"+name+":MsgQueue");
    }

    public void storeMsg(String msg, String name){
        if(jedis.scard("User:"+name+":Followers")>0){
            Set<String> followers = jedis.smembers("User:"+name+":Followers");
            String finalMsg = name+": "+msg;
            for(String f : followers){
                jedis.lpush("User:"+f+":MsgQueue", finalMsg);
            }
        }
    }

    public void readMsgs(String name){
        long len = jedis.llen("User:"+name+":MsgQueue");
        for(int i=0; i<len; i++){
            System.out.println(jedis.rpop("User:"+name+":MsgQueue"));
        }
    }

    public void listUsers(){
        jedis.smembers("Users").stream().forEach(System.out::println);
    }

    public void follow(String follower, String toFollow){
        if(jedis.sismember("Users",toFollow)){
            jedis.sadd("User:"+toFollow+":Followers", follower);
        }
    }
    

    public static void main( String[] args ){

        App app = new App();


        Scanner input = new Scanner(System.in);
        System.out.println("Insert your name:");
        String username = input.nextLine();
        app.addUser(username);


        int op;
        while(true){
            System.out.println("\nMessage App");
            System.out.println("1.) See all users ");
            System.out.println("2.) Follow a user.");
            System.out.println("3.) Send a message.");
            System.out.println("4.) Read my messages.");
            System.out.println("5.) Delete my Account and Exit");
            System.out.println("6.) Exit");
            System.out.println("Enter Your Menu Choice: ");

            op = input.nextInt();
            input.nextLine();
            System.out.println();

            switch(op){

                case 1:
                    app.listUsers();
                    break;

                case 2:    
                    System.out.println("Insert the name of the user:");
                    String toFollow = input.nextLine();
                    app.follow(username, toFollow);
                    break;

                case 3:
                    System.out.println("Write your message:");
                    String msg = input.nextLine();
                    app.storeMsg(msg, username);
                    break;

                case 4: 
                    app.readMsgs(username);
                    break;

                case 5:
                    app.delUser(username);

                case 6: 
                    System.out.println("Exiting Message App...");
                    input.close();
                    System.exit(0);
                    break;
                default:
                        System.out.println("This is not a valid Menu Option!");
                        break;

            }

        }
    }
}
