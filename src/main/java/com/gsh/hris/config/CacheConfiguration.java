package com.gsh.hris.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.gsh.hris.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.gsh.hris.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.gsh.hris.domain.User.class.getName());
            createCache(cm, com.gsh.hris.domain.Authority.class.getName());
            createCache(cm, com.gsh.hris.domain.User.class.getName() + ".authorities");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName());
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".positions");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".dutySchedules");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".dailyTimeRecords");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".benefits");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".dependents");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".educations");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".trainingHistories");
            createCache(cm, com.gsh.hris.domain.Employee.class.getName() + ".leaves");
            createCache(cm, com.gsh.hris.domain.Cluster.class.getName());
            createCache(cm, com.gsh.hris.domain.Cluster.class.getName() + ".departments");
            createCache(cm, com.gsh.hris.domain.Department.class.getName());
            createCache(cm, com.gsh.hris.domain.Department.class.getName() + ".employees");
            createCache(cm, com.gsh.hris.domain.EmploymentType.class.getName());
            createCache(cm, com.gsh.hris.domain.EmploymentType.class.getName() + ".employees");
            createCache(cm, com.gsh.hris.domain.Position.class.getName());
            createCache(cm, com.gsh.hris.domain.DutySchedule.class.getName());
            createCache(cm, com.gsh.hris.domain.DailyTimeRecord.class.getName());
            createCache(cm, com.gsh.hris.domain.Benefits.class.getName());
            createCache(cm, com.gsh.hris.domain.Dependents.class.getName());
            createCache(cm, com.gsh.hris.domain.Education.class.getName());
            createCache(cm, com.gsh.hris.domain.TrainingHistory.class.getName());
            createCache(cm, com.gsh.hris.domain.Leave.class.getName());
            createCache(cm, com.gsh.hris.domain.LeaveType.class.getName());
            createCache(cm, com.gsh.hris.domain.LeaveType.class.getName() + ".leaves");
            createCache(cm, com.gsh.hris.domain.Holiday.class.getName());
            createCache(cm, com.gsh.hris.domain.HolidayType.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
