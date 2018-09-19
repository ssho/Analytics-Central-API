package com.wasoftware.bigdata.rest;

/**
 * Created by wlo on 3/1/2016.
 * Authenticate user with AD or LDAP
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;


@Configuration
@EnableWebSecurity
@PropertySource("classpath:config.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Bean
    public  ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(env.getProperty("ad_ldap.domain"), env.getProperty("ad_ldap.link"));
        //ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider("BD01DC12-88.bigdata.wasoftware.com", "ldaps://BD01DC12-88.bigdata.wasoftware.com:636");
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setConvertSubErrorCodesToExceptions(true);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

         switch (env.getProperty("auth_mode").toLowerCase()) {
            case "adldap":
                auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
                break;

            case "ldap":
                auth
                        .ldapAuthentication()
                        .userDnPatterns("uid={0},ou=people")
                        .groupSearchBase("ou=groups")
                        .contextSource().ldif(env.getProperty("ldap.link"));
                break;

            case "ldap_dev":
                auth
                        .ldapAuthentication()
                        .userDnPatterns("uid={0},ou=people")
                        .groupSearchBase("ou=groups")
                        .contextSource().ldif(env.getProperty("ldap.file"));
                break;
        }

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}