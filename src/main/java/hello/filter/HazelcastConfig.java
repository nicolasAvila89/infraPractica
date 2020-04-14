package hello.filter;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.WebFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/**
 * A conditional configuration that potentially adds the bean definitions in
 * this class to the Spring application context, depending on whether the
 * {@code @ConditionalOnExpression} is true or not.
 *
 * When true, beans are added that create a Hazelcast instance, and bind this
 * instance to Tomcat for storage of HTTP sessions, instead of Tomcat's default
 * implementation.
 */
@Configuration
@PropertySource("classpath:app.properties")
@ConditionalOnExpression("${hazelcastEnabled:true}")
public class HazelcastConfig {

    @Value( "${hazelcastMonitorURL}" )
    private String monitorURL;
    /**
     * Create a Hazelcast {@code Config} object as a bean. Spring Boot will use
     * the presence of this to determine that a {@code HazelcastInstance} should
     * be created with this configuration.
     *
     * As a simple side-step to possible networking issues, turn off multicast
     * in favour of TCP connection to the local host.
     *
     * @return Configuration for the Hazelcast instance
     */
    @Bean
    public Config config() {
        Config config = new Config();
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);
//        joinConfig.getTcpIpConfig().setEnabled(true).setMembers(singletonList("192.168.0.7"));
        ManagementCenterConfig manCenterCfg = new ManagementCenterConfig();
        manCenterCfg.setEnabled(true).setUrl(monitorURL);
        config.setManagementCenterConfig(manCenterCfg);
        return config;
    }

    /**
     * Create a web filter. Parameterize this with two properties,
     *
     * <ol>
     * <li><i>instance-name</i>
     * Direct the web filter to use the existing Hazelcast instance rather than
     * to create a new one.</li>
     * <li><i>sticky-session</i>
     * As the HTTP session will be accessed from multiple processes, deactivate
     * the optimization that assumes each user's traffic is routed to the same
     * process for that user.</li>
     * </ol>
     *
     * Spring will assume dispatcher types of {@code FORWARD}, {@code INCLUDE}
     * and {@code REQUEST}, and a context pattern of "{@code /*}".
     *
     * @param hazelcastInstance Created by Spring
     * @return The web filter for Tomcat
     */
    @Bean
    public WebFilter webFilter(HazelcastInstance hazelcastInstance) {

        Properties properties = new Properties();
        properties.put("instance-name", hazelcastInstance.getName());
        properties.put("sticky-session", "false");

        return new WebFilter(properties);
    }
}