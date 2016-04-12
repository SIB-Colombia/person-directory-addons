package net.unicon.persondir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.BasePersonAttributeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import static com.mongodb.client.model.Filters.*;

public class MongoPersonAttributeDao extends BasePersonAttributeDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${mongodb.attributes.host}")
	private String mongoHostUrl;
	
	@Value("${mongodb.attributes.port}")
	private String mongoPort;
	
	@Value("${mongodb.attributes.db}")
	private String mongoDB;
	
	@Value("${mongodb.attributes.collection}")
	private String mongoCollection;
	
	public MongoPersonAttributeDao(final String hostUrl, final String port, final String db, final String collection) throws Exception {
		super();
		this.mongoHostUrl = hostUrl;
		this.mongoPort = port;
		this.mongoDB = db;
		this.mongoCollection = collection;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> getAvailableQueryAttributes() {
		return Collections.EMPTY_SET;
	}

	@Override
	public Set<IPersonAttributes> getPeople(Map<String, Object> arg0) {
		throw new UnsupportedOperationException("This method is not implemented.");
	}

	@Override
	public Set<IPersonAttributes> getPeopleWithMultivaluedAttributes(Map<String, List<Object>> arg0) {
		throw new UnsupportedOperationException("This method is not implemented.");
	}

	@Override
	public IPersonAttributes getPerson(final String userId) {
		// Create onnection string
		MongoClientURI connectionString = new MongoClientURI("mongodb://"+this.mongoHostUrl+":"+this.mongoPort);
		MongoClient mongoClient = new MongoClient(connectionString);

		MongoDatabase database = mongoClient.getDatabase(this.mongoDB);

		MongoCollection<Document> collection = database.getCollection(this.mongoCollection);
		
		Document user = collection.find(eq("email", userId)).projection(Projections.exclude("password", "_id")).first();
		
		final Map<String, List<Object>> personAttributesMap = new HashMap<String, List<Object>>();
		final List<Object> attributeList = new ArrayList<Object>();
		personAttributesMap.put(userId, attributeList);
		//grouperGroupsAsAttributesMap.put("grouperGroups", groupsList);
		
		for(Map.Entry<String, Object> entry : user.entrySet()) {
			Map<String, List<Object>> personAttributes = new HashMap<String, List<Object>>();
			if (entry.getValue() instanceof List) {
				personAttributes.put(entry.getKey(), (List) entry.getValue()); 
			} else {
				personAttributes.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}
			attributeList.add(personAttributes);
		}
		
		final IPersonAttributes personAttributes = new AttributeNamedPersonImpl(personAttributesMap);
		
		mongoClient.close();
		
		return personAttributes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getPossibleUserAttributeNames() {
		return Collections.EMPTY_SET;
	}

}
