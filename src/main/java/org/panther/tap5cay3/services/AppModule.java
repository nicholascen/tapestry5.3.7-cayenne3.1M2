package org.panther.tap5cay3.services;

import static com.googlecode.tapestry5cayenne.T5CayenneConstants.PROJECT_FILE;

import java.io.IOException;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.validator.ValidatorMacro;
import org.panther.tap5cay3.encoders.RoleAssignmentEncoder;
import org.panther.tap5cay3.encoders.RoleEncoder;
import org.panther.tap5cay3.encoders.UserEncoder;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.model.RoleAssignment;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.security.AuthenticationFilter;
//	7/14/2013 - infrastructure - logging framework
import org.slf4j.Logger;

import com.googlecode.tapestry5cayenne.services.TapestryCayenneModule;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule(TapestryCayenneModule.class)
public class AppModule
{
	

    public static void bind(ServiceBinder binder)
    {

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    	
		

		binder.bind(Authenticator.class, AuthenticatorImpl.class);

		binder.bind(UserService.class, UserServiceImpl.class);
		binder.bind(RoleService.class, RoleServiceImpl.class);
		binder.bind(RoleAssignmentService.class, RoleAssignmentServiceImpl.class);
		
		
    }
   

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");

}

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        
        configuration.add(SymbolConstants.HMAC_PASSPHRASE,"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        //	configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");	//	7/5/2013 - JCen - wait until Tapestry-5.4.x.x

        configuration.add(PROJECT_FILE,"cayenne-tap5cay3.xml");
    }

    @Contribute(ValidatorMacro.class)
    public static void combineValidators(MappedConfiguration<String, String> configuration)
    {
        //	configuration.add("username", "required, minlength=3, maxlength=15");
        configuration.add("password", "required, minlength=6, maxlength=12");
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }
    

    /**	
     * 1-19-2014
     * Contribute query -> list (consequently: query -> GridDataSource) coercion and ObjEntity -> GridDataSource contributions.
     * @param conf
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void contributeTypeCoercer(Configuration<CoercionTuple> conf) {

		conf.add(new CoercionTuple<Character, String>(Character.class,
				String.class, new Coercion<Character, String>() {
					public String coerce(Character input) {
						return input.toString();
					}
				}));
		conf.add(new CoercionTuple<String, Character>(String.class,
				Character.class, new Coercion<String, Character>() {
					public Character coerce(String str) {
						return Character.valueOf((str.charAt(0)));
					}
				}));

        
    }


    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }
    
    //
    public static void contributeValueEncoderSource(MappedConfiguration<Class, ValueEncoderFactory> configuration)
	{

		configuration.addInstance(RoleAssignment.class, RoleAssignmentEncoder.class);
		configuration.addInstance(User.class, UserEncoder.class);
		configuration.addInstance(Role.class, RoleEncoder.class);

	}

    
    @Contribute(ComponentRequestHandler.class)
    public static void contributeComponentRequestHandler(
            OrderedConfiguration<ComponentRequestFilter> configuration)
    {
        configuration.addInstance("RequiresLogin", AuthenticationFilter.class);
    }

}
