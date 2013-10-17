package net.unicon.persondir.config;

import net.unicon.persondir.JsonBackedComplexStubPersonAttributeDao;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * {@link NamespaceHandler} for convenient CAS configuration namespace.
 *
 * @author Dmitriy Kopylenko
 * @author Unicon, inc.
 * @since 0.2
 */
public class PersonDirectoryNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("json-attribute-repository", new JsonAttributesRepositoryBeanDefinitionParser());
    }

    /**
     * Parses <pre>json-attribute-repository</pre> elements into bean definitions of type {@link net.unicon.cas.addons.persondir.JsonBackedComplexStubPersonAttributeDao}
     */
    private static class JsonAttributesRepositoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return JsonBackedComplexStubPersonAttributeDao.class;
        }

        @Override
        protected void doParse(Element element, BeanDefinitionBuilder builder) {
            builder.addConstructorArgValue(element.getAttribute("config-file"));
            builder.setInitMethodName("init");
        }

        @Override
        protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
            return "attributeRepository";
        }
    }
}
