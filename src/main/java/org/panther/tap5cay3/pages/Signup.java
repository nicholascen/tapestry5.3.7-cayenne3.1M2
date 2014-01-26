package org.panther.tap5cay3.pages;

import java.util.Date;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.annotations.AnonymousAccess;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.security.AuthenticationException;
import org.panther.tap5cay3.services.Authenticator;
import org.panther.tap5cay3.services.UserService;
import org.panther.tap5cay3.utils.QueryParameters;

/**
 * This page the user can create an account
 * 
 * @author karesti
 */
@AnonymousAccess
public class Signup
{

    @Property
    @Validate("required, minlength=1, maxlength=25")
    private String firstName;

    @Property
    @Validate("required, minlength=3, maxlength=25")
    private String lastName;

    @Property
    @Validate("required")
    private Date dateOfBirth;

    @Property
    @Validate("required,email")
    private String loginEmail;

    @Property
    @Validate("password")
    private String password;

    @Property
    @Validate("password")
    private String verifyPassword;

    @SuppressWarnings("unused")
    @Property
    private String kaptcha;

    @Inject
    private UserService userService;

    @Component
    private Form registerForm;

    @Inject
    private Messages messages;

    @Inject
    private Authenticator authenticator;

    @SuppressWarnings("unused")
    @InjectPage
    private Signin signin;

    @OnEvent(value = EventConstants.VALIDATE, component = "RegisterForm")
    public void checkForm()
    {
        if (!verifyPassword.equals(password))
        {
            registerForm.recordError(messages.get("error.verifypassword"));
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "RegisterForm")
    public Object proceedSignup()
    {

//        User userVerif = crudServiceDAO.findUniqueWithNamedQuery(
//                User.BY_USERNAME_OR_EMAIL,
//                QueryParameters.with("username", username).and("email", email).parameters());
        User userVerif = userService.findUserByName(loginEmail);

        if (userVerif != null)
        {
            registerForm.recordError(messages.get("error.userexists"));

            return null;
        }

        User user = new User();
        
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setLoginEmail(loginEmail);
        user.setPassword(password);
        userService.save(user);

//        crudServiceDAO.create(user);

        try
        {
            authenticator.login(loginEmail, password);
        }
        catch (AuthenticationException ex)
        {
            registerForm.recordError("Authentication process has failed");
            return this;
        }

        return Index.class;
    }
}
