package com.luvsoft.utils;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBConnection {
	// Database URLs
	public static final String MONGOCONNECTION_MONGOLAB = "mongodb://root:root@ds041992.mongolab.com:41992/coffeeshop";
	public static final String MONGOCONNECTION_LOCALHOST = "mongodb://localhost:27017/coffeeshop";
	    
	private static MongoDBConnection instance;
    private static MongoClient mongoClient;
    private static DB database;
    
    /*
     * @name connectMongoDB function
     */
    public void connectMongoDB() {
        try{
            MongoClientURI uri = new MongoClientURI(MONGOCONNECTION_LOCALHOST);
            mongoClient = new MongoClient(uri);
            database = mongoClient.getDB(uri.getDatabase());
            if( database == null ){
                System.out.println("Cannot connect to MongoDB!....");
                System.out.println("URL: " + MONGOCONNECTION_LOCALHOST);              
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
