package org.panther.tap5cay3.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

//	import org.panther.tap5cay3.services.GenericDataService;
//	import org.panther.tap5cay3.utils.QueryParameters;





import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.security.AuthenticationException;

/**
 * Basic Security Realm implementation
 * 
 * @author karesti
 * @version 1.0
 */
public class AuthenticatorImpl implements Authenticator
{

    public static final String AUTH_TOKEN = "authToken";

    @Inject
    private UserService userService;

    @Inject
    private Request request;

    public void login(String username, String password) throws AuthenticationException
    {

        User user = userService.findUserByCredentials(
                username, password);

        if (user == null) { throw new AuthenticationException("The user doesn't exist"); }

        request.getSession(true).setAttribute(AUTH_TOKEN, user);
    }

    public boolean isLoggedIn()
    {
        Session session = request.getSession(false);
        if (session != null) { return session.getAttribute(AUTH_TOKEN) != null; }
        return false;
    }

    public void logout()
    {
        Session session = request.getSession(false);
        if (session != null)
        {
            session.setAttribute(AUTH_TOKEN, null);
            session.invalidate();
        }
    }

    public User getLoggedUser()
    {
        User user = null;

        if (isLoggedIn())
        {
            user = (User) request.getSession(true).getAttribute(AUTH_TOKEN);
        }
        else
        {
            throw new IllegalStateException("The user is not logged ! ");
        }
        return user;
    }

}
