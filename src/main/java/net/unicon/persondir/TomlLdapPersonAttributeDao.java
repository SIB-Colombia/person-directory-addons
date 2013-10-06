package net.unicon.persondir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import me.grison.jtoml.impl.Toml;

import org.jasig.services.persondir.support.QueryType;
import org.jasig.services.persondir.support.ldap.LdapPersonAttributeDao;
import org.springframework.core.io.Resource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * This is an implementation of the {@link LdapPersonAttributeDao} that is able
 * to insert the ldap and context source settings from an external Toml file. To learn
 * more about the Toml format, please see this link:
 * <a href="https://github.com/mojombo/toml/blob/master/README.md">
 * https://github.com/mojombo/toml/blob/master/README.md
 * </a>
 * <p>The configuration that is read and parsed is encapsulated inside the
 * {@link TomlLdapConfiguration} class. This dao will noy only prepare
 * for the resolution of person attributes, but also is able to construct
 * the necessary {@link LdapContextSource} via the settings that is provided in the file.
 * @author Misagh Moayyed
 * @since 0.2
 */
public class TomlLdapPersonAttributeDao extends LdapPersonAttributeDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Resource tomlConfigFile;

    public TomlLdapPersonAttributeDao(final Resource tomlConfigFile) throws Exception {
        super();
        this.tomlConfigFile = tomlConfigFile;

        validateTomlResource();

        final TomlLdapConfiguration config = buildTomlLdapConfiguration();
        applyTomlConfigurationToDao(config);
    }

    private void applyTomlConfigurationToDao(final TomlLdapConfiguration config) {
        this.setBaseDN(config.getBaseDN());
        this.setQueryAttributeMapping(config.getQueryAttributeMappings());
        this.setQueryType(QueryType.valueOf(config.getQueryType()));
        this.setRequireAllQueryAttributes(config.isRequireAllQueryAttributes());
        this.setResultAttributeMapping(config.getResultAttributeMappings());
        this.setUnmappedUsernameAttribute(config.getUnmappedUsernameAttribute());
        this.setQueryTemplate(config.getQueryTemplate());
        this.setUseAllQueryAttributes(config.isUseAllQueryAttributes());
        
        final LdapContextSource ctxSource = new LdapContextSource();
        ctxSource.setPooled(config.isPooled());
        ctxSource.setBaseEnvironmentProperties(config.getBaseEnvironmentSettings());
        ctxSource.setPassword(config.getPassword());
        ctxSource.setUrls(config.getUrls().toArray(new String[] {}));
        ctxSource.setUserDn(config.getUserDN());
        ctxSource.setCacheEnvironmentProperties(config.isCacheEnvironmentProperties());
        ctxSource.setReferral(config.getReferral());
        
       final LdapTemplate template = new LdapTemplate(ctxSource);
       template.setIgnoreNameNotFoundException(config.isIgnoreNameNotFoundException());
       template.setIgnorePartialResultException(config.isIgnorePartialResultException());
       
       this.setLdapTemplate(template);
    }

    private TomlLdapConfiguration buildTomlLdapConfiguration() throws Exception {
        try {
            final Toml toml = Toml.parse(this.tomlConfigFile.getFile());
               
            final TomlLdapConfiguration config = toml.getAs("ldap", TomlLdapConfiguration.class);
            return config;
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void validateTomlResource() throws IOException {
        if (!this.tomlConfigFile.exists()) {
            throw new RuntimeException("Toml configuration file cannot be found at the specified path");
        }
        if (this.tomlConfigFile.isOpen()) {
            throw new RuntimeException("Another process/application has opened the Toml configuration file");
        }
        if (!this.tomlConfigFile.isReadable()) {
            throw new RuntimeException("Toml configuration file cannot be read at the specified path");
        }
        if (this.tomlConfigFile.contentLength() == 0) {
            throw new RuntimeException("Toml configuration file is empty");
        }
    }

    /**
     * The Toml configuration file is encapsulated by this class.
     * @author Misagh
     * @since 0.2
     */
    public static final class TomlLdapConfiguration {

        private String baseDN;
        private Boolean requireAllQueryAttributes;
        private Boolean useAllQueryAttributes;
        private Map<String, String> queryAttributeMappings;
        private Map<String, String> resultAttributeMappings;
        private String queryType;
        private Boolean pooled;
        private List<String> urls;
        private String userDN;
        private String password;
        private Map<String, String> baseEnvironmentSettings;
        private String unmappedUsernameAttribute;
        private String queryTemplate;
        private Boolean cacheEnvironmentProperties;
        private String referral;
        private Boolean ignoreNameNotFoundException;
        private Boolean ignorePartialResultException;
        
        public String getBaseDN() {
            return this.baseDN;
        }

        public String getUnmappedUsernameAttribute() {
            return this.unmappedUsernameAttribute;
        }

        public String getQueryTemplate() {
            return this.queryTemplate;
        }

        public void setBaseDN(final String baseDN) {
            this.baseDN = baseDN;
        }

        public Boolean isRequireAllQueryAttributes() {
            return this.requireAllQueryAttributes;
        }

        public void setRequireAllQueryAttributes(final Boolean requireAllQueryAttributes) {
            this.requireAllQueryAttributes = requireAllQueryAttributes;
        }

        public Boolean isUseAllQueryAttributes() {
            return this.useAllQueryAttributes;
        }

        public void setUseAllQueryAttributes(final Boolean useAllQueryAttributes) {
            this.useAllQueryAttributes = useAllQueryAttributes;
        }

        public Map<String, String> getQueryAttributeMappings() {
            return this.queryAttributeMappings;
        }

        public void setQueryAttributeMappings(final Map<String, String> queryAttributeMappings) {
            this.queryAttributeMappings = queryAttributeMappings;
        }

        public Map<String, String> getResultAttributeMappings() {
            return this.resultAttributeMappings;
        }

        public void setResultAttributeMappings(final Map<String, String> resultAttributeMappings) {
            this.resultAttributeMappings = resultAttributeMappings;
        }

        public String getQueryType() {
            return this.queryType;
        }

        public void setQueryType(final String queryType) {
            this.queryType = queryType;
        }

        public Boolean isPooled() {
            return this.pooled;
        }

        public void setPooled(final Boolean pooled) {
            this.pooled = pooled;
        }

        public List<String> getUrls() {
            return this.urls;
        }

        public void setUrls(final List<String> urls) {
            this.urls = urls;
        }

        public String getUserDN() {
            return this.userDN;
        }

        public void setUserDN(final String userDN) {
            this.userDN = userDN;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }

        public Map<String, String> getBaseEnvironmentSettings() {
            return this.baseEnvironmentSettings;
        }

        public void setBaseEnvironmentSettings(final Map<String, String> baseEnvironmentSettings) {
            this.baseEnvironmentSettings = baseEnvironmentSettings;
        }

        public void setUnmappedUsernameAttribute(final String unmappedUsernameAttribute) {
            this.unmappedUsernameAttribute = unmappedUsernameAttribute;
        }

        public void setQueryTemplate(final String queryTemplate) {
            this.queryTemplate = queryTemplate;
        }

        public Boolean isCacheEnvironmentProperties() {
            return this.cacheEnvironmentProperties;
        }

        public void setCacheEnvironmentProperties(final Boolean cacheEnvironmentProperties) {
            this.cacheEnvironmentProperties = cacheEnvironmentProperties;
        }

        public String getReferral() {
            return this.referral;
        }

        public void setReferral(final String referral) {
            this.referral = referral;
        }

        public Boolean isIgnoreNameNotFoundException() {
            return this.ignoreNameNotFoundException;
        }

        public void setIgnoreNameNotFoundException(final Boolean ignoreNameNotFoundException) {
            this.ignoreNameNotFoundException = ignoreNameNotFoundException;
        }

        public Boolean isIgnorePartialResultException() {
            return this.ignorePartialResultException;
        }

        public void setIgnorePartialResultException(final Boolean ignorePartialResultException) {
            this.ignorePartialResultException = ignorePartialResultException;
        }

    }
}
