package AutoComplete;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Scanner;
import java.util.Collections;
import java.io.FileInputStream;
import java.io.IOException;

import redis.clients.jedis.Jedis;

public class App {
    private Jedis jedis;

    public static String NAMES = "names";

    public App() {
        this.jedis = new Jedis("localhost");
    }

    public void addName(String name) {
        jedis.lpush(NAMES, name);
    }
    
    public ArrayList<String> getAllNames() {
        return new ArrayList<String>(jedis.lrange(NAMES,0,-1));
    }

    public ArrayList<String> getCompletions(String s) {
        ArrayList<String> names = getAllNames();
        ArrayList<String> completions = new ArrayList<String>();
        for(String name : names){
            if(name.startsWith(s)){
                completions.add(name);
            }
        }
        Collections.sort(completions);
        return completions;
    }

public static void main(String[] args) {
        App autocomplete = new App();

        //Load names
        try{ 
            FileInputStream file = new FileInputStream("female-names.txt");       
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){  
                autocomplete.addName(sc.nextLine());  
            }
            sc.close(); 
        
        }catch(IOException e){  
            e.printStackTrace();  
        }

        Scanner sc = new Scanner(System.in);
        String search;

        while(true){
            System.out.println("Search for (Enter to quit):");
            search = sc.nextLine();
            if(search.length()==0){
                break;
            }
            System.out.println();
            autocomplete.getCompletions(search).stream().forEach(System.out::println);
        }
            
    }

}

