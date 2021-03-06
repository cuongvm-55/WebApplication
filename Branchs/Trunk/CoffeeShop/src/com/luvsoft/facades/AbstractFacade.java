package com.luvsoft.facades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.utils.MongoDBConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

public abstract class AbstractFacade {
    public static final String TAG_ID = "_id";

    /**
     * Function is used to get all document in a collection
     * 
     * @return ArrayList list of documents in this collection
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> boolean findAll(List<T> retList) {
        DBCollection collection = getDBCollection();
        if (retList == null) {
            retList = new ArrayList<T>();
        }
        try {
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                BasicDBObject data = (BasicDBObject)cursor.next();
                retList.add((T) mapObject(data));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ///////////////////////////////////////////////////////////
    // ! Function is used to get all document in a collection by Id
    // !
    // ! @return An Object in the collection who has the Id
    // ///////////////////////////////////////////////////////////
    public <T extends AbstractEntity> boolean findById(String id, T retobject) {
        DBCollection collection = getDBCollection();
        try {
            BasicDBObject query = new BasicDBObject(TAG_ID, new ObjectId(id));
            BasicDBObject dbobj = (BasicDBObject)collection.findOne(query);
            if (dbobj != null) {
                retobject.setObject(dbobj);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // ///////////////////////////////////////////////////////////
    // ! Function is used to get all document in a collection by a query
    // !
    // ! @return ArrayList list of documents in this collection
    // ///////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> boolean findByQuery(BasicDBObject query, List<T> retList){
        DBCollection collection = getDBCollection();
        if (retList == null) {
            retList = new ArrayList<T>();
        }
        try {
            DBCursor cursor = collection.find(query);
            while (cursor.hasNext()) {
                BasicDBObject data = (BasicDBObject) cursor.next();
                retList.add((T) mapObject(data));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This function is used to find one object by query
     * @param query
     * @param ret
     * @return
     */
    public <T extends AbstractEntity> boolean findOneByQuery(BasicDBObject query, T ret){
        DBCollection collection = getDBCollection();
        try {
            BasicDBObject bdbObject = (BasicDBObject) collection.findOne(query);
            if(bdbObject != null) {
                ret.setObject(bdbObject);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T extends AbstractEntity> boolean save(T entity) {
        DBCollection collection = getDBCollection();
        HashMap<String, Object> map = entity.toHashMap();
        String id = map.get(TAG_ID).toString();
        // if Id is empty, create new id
        if( id.equals("") ){
            id = (new ObjectId()).toString();
        }

        map.remove(TAG_ID);
        BasicDBObject object = new BasicDBObject(map);
        object.append(TAG_ID, new ObjectId(id)); // we need to save _id as a ObjectId
        try{
            collection.insert(object);
        }catch(MongoException ex){
            return false;
        }
        return true;
    }

    public <T extends AbstractEntity> boolean save(BasicDBObject object) {
        DBCollection collection = getDBCollection();
        collection.insert(object);
        return true;
    }
    
    /**
     * Replace a current query Entity by a new Entity
     */
    public <T extends AbstractEntity> boolean replace(T queryEntity, T newEntity)
            throws MongoException {
        try {
            DBCollection collection = getDBCollection();
            collection.update(new BasicDBObject(queryEntity.toHashMap()),
                    new BasicDBObject(newEntity.toHashMap()));
        } catch (MongoException mge) {
            return false;
        }
        return true;
    }

    /**
     * Update field value, SET field="value" WHERE objectId="ObjectId"
     */
    public boolean updateFieldValue(String objectId, String fieldName,
            Object value) {
        try {
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject(fieldName, value));

            BasicDBObject searchQuery = new BasicDBObject().append(TAG_ID,
                    new ObjectId(objectId));
            getDBCollection().update(searchQuery, newDocument);
        } catch (MongoException mge) {
            return false;
        }
        return true;
    }
    
    /**
     * Update field value, SET field="value" WHERE objectId="ObjectId"
     */
    public <T extends AbstractEntity> boolean update(String objectId, T entity) {
        try {
            Map<String, Object> map = entity.toHashMap();
            map.remove(TAG_ID);
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject(map));

            BasicDBObject searchQuery = new BasicDBObject().append(TAG_ID,
                    new ObjectId(objectId));
            getDBCollection().update(searchQuery, newDocument);
        } catch (MongoException mge) {
            return false;
        }
        return true;
    }
    
    /**
     * Remove a document by its _id
     * Note: remove all uses "db.messages.remove({})"
     */
    public boolean removeById(String id) throws MongoException {
        try {
            BasicDBObject query = new BasicDBObject(TAG_ID, new ObjectId(id));
            DBCollection collection = getDBCollection();
            collection.remove(query);
        } catch (MongoException mge) {
            return false;
        }
        return true;
    }

    public boolean removeAll() throws MongoException{
        try{
            BasicDBObject query = new BasicDBObject("","");
            DBCollection collection = getDBCollection();
            collection.remove(query);
            return true;
        }catch(MongoException e){
            return false;
        }
    }
    /**
     * Remove a document by query
     */
    public boolean removeByQuery(BasicDBObject query) throws MongoException {
        try {
            DBCollection collection = getDBCollection();
            collection.remove(query);
        } catch (MongoException mge) {
            return false;
        }
        return true;
    }

    public abstract String getCollectionName();

    public abstract AbstractEntity mapObject(BasicDBObject dbobject);

    /**
     * Do some check on DB connection and return a DBCollection to manipulate
     * the db
     */
    private DBCollection getDBCollection() {
        MongoDBConnection dbConnection = MongoDBConnection.getInstance();
        if (dbConnection.getDB() == null) {
            System.out.printf("Cannot get Database...");
            return null;
        }
        DBCollection collection = dbConnection.getDB().getCollection(
                getCollectionName());
        if (collection == null) {
            System.out.printf("Collection is NULL: %s", getCollectionName());
            return null;
        }
        return collection;
    }
}
