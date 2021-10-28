package com.example;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;

import org.bson.Document;
import java.util.Arrays;
import java.util.Collection;

import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class App 
{
    public static void insert(MongoCollection collection, Document doc) {
        collection.insertOne(doc);
    }

    public static void update(MongoCollection collection, String param, String old, String novo) {
        collection.updateOne(eq(param, old),set(param, novo));
    }

    public static void query(MongoCollection collection, Document query) {
        FindIterable docs = collection.find(query);
        for (Object doc : docs) { 
            System.out.println(doc);
        }
    }

    public static void main( String[] args )
    {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("rest");

        Document doc = new Document("address", new Document("building", "9322").append("coord", Arrays.asList(-77.84938439999999, 42.688197 ))
                                                                                .append("rua", "University Street")
                                                                                .append("zipcode", "3810"))
                            .append("localidade", "Aveiro")
                            .append("gastronomia", "Portuguesa")
                            .append("grades", Arrays.asList(new Document("date", "2013-12-27T00:00:00Z").append("grade", "A").append("score", 10) ))
                            .append("nome", "AFUAV")
                            .append("restaurant_id", "42358514");

        //insert(collection, doc);
        //update(collection, "nome", "AFUAV","AFFUAV");
        query(collection, new Document("localidade","Aveiro"));
    }
}
