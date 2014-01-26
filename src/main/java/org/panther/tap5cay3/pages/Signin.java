package org.panther.tap5cay3.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.annotations.AnonymousAccess;
import org.panther.tap5cay3.security.AuthenticationException;
import org.panther.tap5cay3.services.Authenticator;

/**
 * User can sign up on the
 * 
 * @author karesti
 */
@AnonymousAccess
public class Signin
{
    @Property
    private String flashmessage;

    @Property
    private String username;

    @Property
    private String password;

    @Inject
    private Authenticator authenticator;

//    @InjectComponent(value="loginForm")
//    private Form form;
    
    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Log
    public Object onSuccessFromLoginForm()
    {
        try
        {
            authenticator.login(username, password);
        }
        catch (AuthenticationException ex)
        {
            loginForm.recordError(messages.get("error.login"));
            return null;
        }

        return Index.class;
    }

    public String getFlashMessage()
    {
        return flashmessage;
    }

    public void setFlashMessage(String flashmessage)
    {
        this.flashmessage = flashmessage;
    }

}
