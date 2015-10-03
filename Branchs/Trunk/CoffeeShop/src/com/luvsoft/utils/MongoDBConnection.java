package com.luvsoft.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBConnection {
	private static MongoDBConnection instance;
    private static MongoClient mongoClient;
    private static DB database;
    
    /*
     * @name connectMongoDB function
     */
    public void connectMongoDB() {
        try{
            MongoClientURI uri = new MongoClientURI(DatabaseTags.MONGOCONNECTION_MONGOLAB);
            mongoClient = new MongoClient(uri);
            database = mongoClient.getDB(uri.getDatabase());
            if( database == null ){
                System.out.println("Cannot connect to MongoDB!....");
                System.out.println("URL: " + DatabaseTags.MONGOCONNECTION_MONGOLAB);              
            }
        }catch( UnknownHostException e ){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static MongoDBConnection getInstance(){
    	if( instance == null ){
    		instance = new MongoDBConnection();
    		instance.connectMongoDB();
    	}
    	return instance;
    }
    
    public MongoClient getMongoClient(){
    	return mongoClient;
    }
    
    public DB getDB(){
    	return database;
    }
}
