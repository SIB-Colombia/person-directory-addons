package net.unicon.persondir;

import static org.junit.Assert.*;

import org.jasig.services.persondir.IPersonAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MongoPersonAttributeDaoTests {
	@Test
    public void testMongoPersonAttributeDaoTest() throws Exception {
		final MongoPersonAttributeDao g = new MongoPersonAttributeDao("localhost", "27017", "cas", "users");
		//final IPersonAttributes attrs = g.getPerson("valegrajales");
		
		//assertTrue(attrs.getAttributes().isEmpty());
	}
}
