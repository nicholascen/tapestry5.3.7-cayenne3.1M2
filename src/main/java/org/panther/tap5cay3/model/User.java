package org.panther.tap5cay3.model;

import org.panther.tap5cay3.model.auto._User;

import com.googlecode.tapestry5cayenne.annotations.Label;

public class User extends _User {
	
    public static final String NAME_PROPERTY = "name";
   /**
	 * 
	 */
	private static final long serialVersionUID = -6250189696101889494L;

	@Label
	public String getName() {
    	String	fn = getFirstName();
    	String  ln = getLastName();
    	
    	if (fn == null && ln == null )	{return null;}
    	
    	if (fn == null) {return ln;}
    	
    	if (ln == null) {return fn;}
    	
    	return fn + " " + ln;
    		
    }

}
