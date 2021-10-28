package AutoCompleteb;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class App {
    private Jedis jedis;

    public static String NAMES = "names";

    public App() {
        this.jedis = new Jedis("localhost");
    }

    public void addName(String name, double score) {
        jedis.zadd(NAMES, score, name);
    }
    
    public Set<Tuple> getAllNames() {
        return jedis.zrevrangeWithScores(NAMES, 0, -1);
    }

    public HashMap<String, Double> getCompletions(String s) {
        Set<Tuple> names = getAllNames();
        HashMap<String, Double> completions = new HashMap<String, Double>();
        for(Tuple tuple : names){
            if(tuple.getElement().startsWith(s)){
                completions.put(tuple.getElement(),tuple.getScore());
            }
        }
        return completions;
    }




public static void main(String[] args) {
        App autocomplete = new App();

        //Load names
        try{ 
            FileInputStream file = new FileInputStream("nomes-registados-2020.csv");       
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] parts = line.split(";");
                autocomplete.addName(parts[0],Double.parseDouble(parts[2]));  
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
            Map<String, Double> result = autocomplete.getCompletions(search).entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            for(Map.Entry<String,Double> entry : result.entrySet()){
                System.out.println(entry.getKey());
            }
        }
            
    }

}