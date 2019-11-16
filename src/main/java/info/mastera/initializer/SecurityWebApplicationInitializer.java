package info.mastera.initializer;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This is to register the springSecurityFilterChain with the war
 * <p>
 * As we already use Spring in project then we need to make sure that {@link  info.mastera.config.WebSecurityConfig}
 * was loaded in our existing {@link info.mastera.config.AppInitializer}
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

}