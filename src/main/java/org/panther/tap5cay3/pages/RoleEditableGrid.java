package org.panther.tap5cay3.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.common.FieldCopy;
//import org.panther.tap5cay3.encoders.RoleEncoder;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.services.RoleService;
import org.panther.tap5cay3.utils.ExceptionUtil;

//	import org.panther.tap5cay3.encoders.RoleEncoder;	//	2014-1-8 injection apparently does not work within the RoleEncoder 

public class RoleEditableGrid {
    //	static private final int MAX_RESULTS = 30;

    // Screen fields

    @Property
    private List<Role> roles;

    private Role role;
    
    @Property
    private final RoleEncoder roleEncoder = new RoleEncoder();

    @Property
    private final String BAD_NAME = "Acme";

    // Work fields

    private List<Role> rolesInDB;

    private boolean inFormSubmission;

    private List<Role> rolesSubmitted;

    // This carries the list of submitted roles through the redirect that follows a server-side validation failure.
    // We do this to compensate for the fact that Form doesn't carry Hidden component values through a redirect.
    @Persist(PersistenceConstants.FLASH)
    private List<Role> rolesSubmittedFlash;

    private int rowNum;
    private Map<Integer, FieldCopy> nameCopyByRowNum;

    // Other pages

    @InjectPage
    private RoleDisplayGrid page2;

    // Generally useful bits and pieces

    @Component(id = "rolesEdit")
    private Form form;

    @InjectComponent
    private TextField name;

    @Inject
    private RoleService roleService;

    // The code

    void onActivate() {
        inFormSubmission = false;
    }

    // Form bubbles up the PREPARE_FOR_RENDER event during form render.

    void onPrepareForRender() {

        // If fresh start, populate screen with all roles from the database

        if (form.isValid()) {
            // Get all roles - ask business service to find them (from the database)
            rolesInDB = roleService.findAll();

            roles = new ArrayList<Role>();

            for (Role roleInDB : rolesInDB) {
                roles.add(roleInDB);
            }
        }

        // Else, we're rendering after a redirect, so rebuild the list with the same roles as were submitted

        else {
            roles = new ArrayList<Role>(rolesSubmittedFlash);
        }
    }

    // Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

    void onPrepareForSubmit() {
        inFormSubmission = true;
        rolesSubmitted = new ArrayList<Role>();

        // Get all roles - ask business service to find them (from the database)
        rolesInDB = roleService.findAll();

        // Prepare to take a copy of each editable field.
        
        rowNum = 0;
        nameCopyByRowNum = new HashMap<Integer, FieldCopy>();
    }

    void onValidateFromName() {
        rowNum++;
        nameCopyByRowNum.put(rowNum, new FieldCopy(name));
    }

    void onValidateFromRolesEdit() {

        if (form.getHasErrors()) {
            // We get here only if a server-side validator detected an error.
            return;
        }

        // Error if any role submitted has a null id - it means toValue(...) found they are no longer in the database.

        for (Role roleSubmitted : rolesSubmitted) {
            if (roleSubmitted.getId() == null) {
                form.recordError("The list of roles is out of date. Please refresh and try again.");
                return;
            }
        }

        // Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

        rowNum = 0;

        for (Role roleSubmitted : rolesSubmitted) {
            rowNum++;

            if (roleSubmitted.getName() != null && roleSubmitted.getName().equals(BAD_NAME)) {
                // Unfortunately, at this point the field name is from the final row of the Grid.
                // Fortunately, we have a copy of the correct field, so we can record the error with that.

                Field field = nameCopyByRowNum.get(rowNum);
                form.recordError(field, "DEMO: role name cannot be " + BAD_NAME + ".");
                return;
            }
        }

        try {
            //	System.out.println(">>> rolesSubmitted = " + rolesSubmitted);
            // In a real application we would persist them to the database instead of printing them
            roleService.saveAll(rolesSubmitted);
        }
        catch (Exception e) {
            // Display the cause. In a real system we would try harder to get a user-friendly message.
            form.recordError(ExceptionUtil.getRootCauseMessage(e));
        }
    }

    Object onSuccess() {
    	return this;
        // By doing nothing the page will be displayed afresh.
//        page2.set(rolesSubmitted);
//        return page2;
    }

    void onFailure() {
        rolesSubmittedFlash = new ArrayList<Role>(rolesSubmitted);
    }

    void onRefresh() {
        // By doing nothing the page will be displayed afresh.
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;

        if (inFormSubmission) {
            rolesSubmitted.add(role);
        }
    }

    
 // This encoder is used by our Grid:
    // - during render, to convert each Role to an id (Grid then stores the ids in the form, hidden).
    // - during form submission, to convert each id back to a Role which it puts in our Role field.
    // Grid will overwrite the firstName of the Role returned.

    private class RoleEncoder implements ValueEncoder<Role> {

        public String toClient(Role role) {
            Integer id = role.getId();
            return id == null ? null : id.toString();
        }

        public Role toValue(String idAsString) {
            Role role = null;

            if (idAsString == null) {
                role = roleService.newEntity();
            }
            else {
                Integer id = new Integer(idAsString);
                role = roleService.findById(id);

                // If Role has since been deleted from the DB. Create a skeleton Role.
                if (role == null) {
                    role = roleService.newEntity();
                }
            }

            return role;
        }

    };

}
