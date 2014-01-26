package org.panther.tap5cay3.model;

import org.panther.tap5cay3.model.auto._Role;

import com.googlecode.tapestry5cayenne.annotations.Label;

public class Role extends _Role {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 578108255167009893L;
	public static final String NAME_PROPERTY = "name";
   /**
	 * 
	 */

	@Label
	public String getName() {
		return	super.getName();
		
	}

}
