package org.panther.tap5cay3.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.panther.tap5cay3.common.FieldCopy;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.model.RoleAssignment;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.services.RoleAssignmentService;
import org.panther.tap5cay3.services.RoleService;
import org.panther.tap5cay3.services.UserService;
import org.panther.tap5cay3.utils.ExceptionUtil;

import com.googlecode.tapestry5cayenne.PersistentEntitySelectModel;
import com.googlecode.tapestry5cayenne.services.PersistentManager;

//	import org.panther.tap5cay3.encoders.RoleAssignmentEncoder;	//	2014-1-8 injection apparently does not work within the RoleAssignmentEncoder 

public class RoAssEditableGrid {
    //	static private final int MAX_RESULTS = 30;

    // Screen fields

    @Property
    private List<RoleAssignment> roleAssignments;

    private RoleAssignment roleAssignment;
    
    @Property
    private final RoleAssignmentEncoder roleAssignmentEncoder = new RoleAssignmentEncoder();

    @Property
    private final DateEncoder dateEncoder = new DateEncoder();
    
    @Property
    private ArrayList<Role> roles;
    
    @Inject
    private RoleService roleService;
    
    @Property
    private SelectModel roleSelectModel;
    
    @Property
    private ArrayList<User> users;

    @Inject
    private UserService userService;
    
    @Property
    private SelectModel userSelectModel;
    
    @Property
    private final String BAD_NAME = "Acme";

    // Work fields

    private List<RoleAssignment> roleAssignmentsInDB;

    private boolean inFormSubmission;

    private List<RoleAssignment> roleAssignmentsSubmitted;

    // This carries the list of submitted roleAssignments through the redirect that follows a server-side validation failure.
    // We do this to compensate for the fact that Form doesn't carry Hidden component values through a redirect.
    @Persist(PersistenceConstants.FLASH)
    private List<RoleAssignment> roleAssignmentsSubmittedFlash;

    private int rowNum;
    private Map<Integer, FieldCopy> nameCopyByRowNum;

    // Other pages

//    @InjectPage
//    private RoleAssignmentDisplayGrid page2;

    // Generally useful bits and pieces

    @Component(id = "roleAssignmentsEdit")
    private Form form;

    @InjectComponent
    private TextField description;

    @Inject
    private RoleAssignmentService roleAssignmentService;
    
    @Property
    private BeanModel<RoleAssignment> roleAssignmentModel;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Messages messages;


    // The code

    void onActivate() {
        inFormSubmission = false;
        roles = (ArrayList<Role>) roleService.findAll();
        users = (ArrayList<User>) userService.findAll();

    }

    void setupRender() {

    	roleSelectModel = new PersistentEntitySelectModel<Role>(Role.class,roles);
       	userSelectModel = new PersistentEntitySelectModel<User>(User.class,users);
   	       	
       	roleAssignmentModel = beanModelSource.createEditModel(RoleAssignment.class, messages);

    }

    // Form bubbles up the PREPARE_FOR_RENDER event during form render.

    void onPrepareForRender() {

        // If fresh start, populate screen with all roleAssignments from the database

        if (form.isValid()) {
            // Get all roleAssignments - ask business service to find them (from the database)
            roleAssignmentsInDB = roleAssignmentService.findAll();

            roleAssignments = new ArrayList<RoleAssignment>();

            for (RoleAssignment roleAssignmentInDB : roleAssignmentsInDB) {
                roleAssignments.add(roleAssignmentInDB);
            }
        }

        // Else, we're rendering after a redirect, so rebuild the list with the same roleAssignments as were submitted

        else {
            roleAssignments = new ArrayList<RoleAssignment>(roleAssignmentsSubmittedFlash);
        }
    }

    // Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

    void onPrepareForSubmit() {
        inFormSubmission = true;
        roleAssignmentsSubmitted = new ArrayList<RoleAssignment>();

        // Get all roleAssignments - ask business service to find them (from the database)
        roleAssignmentsInDB = roleAssignmentService.findAll();

        // Prepare to take a copy of each editable field.
        
        rowNum = 0;
        nameCopyByRowNum = new HashMap<Integer, FieldCopy>();
    }

    void onValidateFromDescription() {
        rowNum++;
        nameCopyByRowNum.put(rowNum, new FieldCopy(description));
    }

    void onValidateFromRoleAssignmentsEdit() {

        if (form.getHasErrors()) {
            // We get here only if a server-side validator detected an error.
            return;
        }

        // Error if any roleAssignment submitted has a null id - it means toValue(...) found they are no longer in the database.

        for (RoleAssignment roleAssignmentSubmitted : roleAssignmentsSubmitted) {
            if (roleAssignmentSubmitted.getId() == null) {
                form.recordError("The list of roleAssignments is out of date. Please refresh and try again.");
                return;
            }
        }

        // Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

        rowNum = 0;

        for (RoleAssignment roleAssignmentSubmitted : roleAssignmentsSubmitted) {
            rowNum++;

            if (roleAssignmentSubmitted.getDescription() != null && roleAssignmentSubmitted.getDescription().equals(BAD_NAME)) {
                // Unfortunately, at this point the field name is from the final row of the Grid.
                // Fortunately, we have a copy of the correct field, so we can record the error with that.

                Field field = nameCopyByRowNum.get(rowNum);
                form.recordError(field, "DEMO: roleAssignment Description cannot be " + BAD_NAME + ".");
                return;
            }
        }

        try {
            //	System.out.println(">>> roleAssignmentsSubmitted = " + roleAssignmentsSubmitted);
            // In a real application we would persist them to the database instead of printing them
            roleAssignmentService.saveAll(roleAssignmentsSubmitted);
        }
        catch (Exception e) {
            // Display the cause. In a real system we would try harder to get a user-friendly message.
            form.recordError(ExceptionUtil.getRootCauseMessage(e));
        }
    }

    Object onSuccess() {
    	return this;
        // By doing nothing the page will be displayed afresh.
//        page2.set(roleAssignmentsSubmitted);
//        return page2;
    }

    void onFailure() {
        roleAssignmentsSubmittedFlash = new ArrayList<RoleAssignment>(roleAssignmentsSubmitted);
    }

    void onRefresh() {
        // By doing nothing the page will be displayed afresh.
    }

    public RoleAssignment getRoleAssignment() {
        return roleAssignment;
    }

    public void setRoleAssignment(RoleAssignment roleAssignment) {
        this.roleAssignment = roleAssignment;

        if (inFormSubmission) {
            roleAssignmentsSubmitted.add(roleAssignment);
        }
    }

    // This encoder is used by our Grid:
    // - during render, to convert each roleAssignment to an id (Grid then stores the ids in the form, hidden).
    // - during form submission, to convert each id back to a roleAssignment which it puts in our roleAssignment field.
    // Grid will overwrite the name of the roleAssignment returned.


    private class DateEncoder implements ValueEncoder<Date> {

        public String toClient(Date date) {
            long timeMillis = date.getTime();
            return Long.toString(timeMillis);
        }

        public Date toValue(String timeMillisAsString) {
            long timeMillis = Long.parseLong(timeMillisAsString);
            Date date = new Date(timeMillis);
            return date;
        }

    }
    
 // This encoder is used by our Grid:
    // - during render, to convert each RoleAssignment to an id (Grid then stores the ids in the form, hidden).
    // - during form submission, to convert each id back to a RoleAssignment which it puts in our RoleAssignment field.
    // Grid will overwrite the firstName of the RoleAssignment returned.

    private class RoleAssignmentEncoder implements ValueEncoder<RoleAssignment> {

        public String toClient(RoleAssignment roleAssignment) {
            Integer id = roleAssignment.getId();
            return id == null ? null : id.toString();
        }

        public RoleAssignment toValue(String idAsString) {
            RoleAssignment roleAssignment = null;

            if (idAsString == null) {
                roleAssignment = roleAssignmentService.newEntity();
            }
            else {
                Integer id = new Integer(idAsString);
                roleAssignment = roleAssignmentService.findById(id);

                // If RoleAssignment has since been deleted from the DB. Create a skeleton RoleAssignment.
                if (roleAssignment == null) {
                    roleAssignment = roleAssignmentService.newEntity();
                }
            }

            return roleAssignment;
        }

    };

}
