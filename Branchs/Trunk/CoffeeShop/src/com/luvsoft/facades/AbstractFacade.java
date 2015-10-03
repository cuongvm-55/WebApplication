package com.luvsoft.facades;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.utils.DatabaseTags;
import com.luvsoft.utils.MongoDBConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class AbstractFacade {	
    // ///////////////////////////////////////////////////////////
    // ! Function is used to get all document in a collection
    // !
    // ! @return ArrayList list of documents in this collection
    // ///////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
	public <T extends AbstractEntity> boolean findAll(List<T> retList) {
    	MongoDBConnection dbConnection = MongoDBConnection.getInstance();
    	if( dbConnection.getDB() == null )
    	{
    		System.err.printf("Cannot get Database...");
        	return false;		
    	}
    	
    	DBCollection collection = dbConnection.getDB().getCollection(getCollectionName());
        if( collection == null )
        {
        	System.err.printf("Collection is NULL: %s", getCollectionName());
        	return false;
        }
        if( retList == null )
        {
        	retList = new ArrayList<T>();
        }
        try{	
	        DBCursor cursor = collection.find();
	        while (cursor.hasNext()) {
	        	retList.add((T) mapObject(cursor.next()));
	        }
	        return true;
        }catch(Exception e)
        {
        	return false;
        }
    }

    // ///////////////////////////////////////////////////////////
    // ! Function is used to get all document in a collection by Id
    // !
    // ! @return An Object in the collection who has the Id
    // ///////////////////////////////////////////////////////////
	public <T extends AbstractEntity> boolean findById(String id, T retobject) {
    	MongoDBConnection dbConnection = MongoDBConnection.getInstance();
    	if( dbConnection.getDB() == null )
    	{
    		System.out.printf("Cannot get Database...");
        	return false;		
    	}
    	
    	DBCollection collection = dbConnection.getDB().getCollection(getCollectionName());
        if( collection == null )
        {
        	System.out.printf("Collection is NULL: %s", getCollectionName());
        	return false;
        }
        
        try{
	        BasicDBObject query = new BasicDBObject(DatabaseTags.TAG_ID, new ObjectId(id));
	        DBObject dbobj = collection.findOne(query);
	        if( dbobj != null )
	        {
	        	retobject.setObject(dbobj);
	        	return true;
	        }
	        return false;
        }catch(Exception e){
        	return false;
        }
    }

    // ///////////////////////////////////////////////////////////
    // ! Function is used to get all document in a collection by a query
    // !
    // ! @return ArrayList list of documents in this collection
    // ///////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
	public <T extends AbstractEntity> boolean findByQuery(BasicDBObject query, List<T> retList) throws Exception{
    	MongoDBConnection dbConnection = MongoDBConnection.getInstance();
    	if( dbConnection.getDB() == null )
    	{
    		System.out.printf("Cannot get Database...");
        	return false;		
    	}
    	DBCollection collection = dbConnection.getDB().getCollection(getCollectionName());
        if( collection == null )
        {
        	System.out.printf("Collection is NULL: %s", getCollectionName());
        	return false;
        }
        if( retList == null )
        {
        	retList = new ArrayList<T>();
        }
        try{
	        DBCursor cursor = collection.find(query);
	        while (cursor.hasNext()) {
	        	retList.add((T) mapObject(cursor.next()));
	        }
	        return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }   
    }
    
    public <T extends AbstractEntity> boolean save(BasicDBObject object){
    	MongoDBConnection dbConnection = MongoDBConnection.getInstance();
    	if( dbConnection.getDB() == null )
    	{
    		System.out.printf("Cannot get Database...");
        	return false;		
    	}
    	DBCollection collection = dbConnection.getDB().getCollection(getCollectionName());
        if( collection == null )
        {
        	System.out.printf("Collection is NULL: %s", getCollectionName());
        	return false;
        }
        
        collection.insert(object);
        
    	return true;
    }
    
    public abstract String getCollectionName();

    public abstract AbstractEntity mapObject(DBObject dbobject);
    
    public boolean mapObject(AbstractEntity entity, DBObject dbobject)
    {
    	entity.setObject(dbobject);
    	return true;
    }
}
