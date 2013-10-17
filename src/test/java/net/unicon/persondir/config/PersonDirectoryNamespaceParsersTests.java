package net.unicon.persondir.config;

import net.unicon.persondir.JsonBackedComplexStubPersonAttributeDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dmitriy Kopylenko
 * @author Unicon, inc.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonDirectoryNamespaceParsersTests {

    @Autowired
    ApplicationContext applicationContext;

    private static final String ATTRIBUTE_REPOSITORY_BEAN_NAME = "attributeRepository";

    @Test
    public void jsonAttributeRepositoryBeanDefinitionCorrectlyParsed() {
        assertTrue(applicationContext.containsBean(ATTRIBUTE_REPOSITORY_BEAN_NAME));
        assertTrue(applicationContext.getBeansOfType(JsonBackedComplexStubPersonAttributeDao.class).size() == 1);
    }
}
