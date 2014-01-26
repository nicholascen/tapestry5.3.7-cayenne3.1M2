package org.panther.tap5cay3.pages;

import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.panther.tap5cay3.model.Role;

public class RoleDisplayGrid {

    // Screen fields

    @Property
    @Persist(PersistenceConstants.FLASH)
    private List<Role> roles;

    // The code

    public void set(List<Role> roles) {
        this.roles = roles;
    }

}
