package org.panther.tap5cay3.components;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.model.RoleAssignment;
import org.panther.tap5cay3.services.Authenticator;
import org.panther.tap5cay3.services.RoleAssignmentService;
import org.panther.tap5cay3.services.RoleService;

/**
 * List all the roleAssignments of the current user.
 * 
 * @author ccordenier
 */
public class YourRoles
{
    @Inject
    private RoleAssignmentService roleAssignmentService;

    @Inject
    private Authenticator authenticator;

    @Property
    private List<RoleAssignment> roleAssignments;

    @SuppressWarnings("unused")
    @Property
    private RoleAssignment current;
    
    @Property
    private SelectModel rolesModel;

    @Property
    private Role role;

    // Generally useful bits and pieces

    @Inject
    private SelectModelFactory selectModelFactory;

    @Inject
    private RoleService roleService;

    /**
     * Prepare the list of roleAssignment to display, extract all the roleAssignment associated to the current
     * logged user.
     * 
     * @return
     */
    @SetupRender
    boolean listRoleAssignments()
    {
        roleAssignments = roleAssignmentService.findByUser( authenticator.getLoggedUser());
  
        return roleAssignments.size() > 0 ? true : false;
    }

    void onPrepareForRender() {
        // Get all persons - ask business service to find them (from the database)
        List<Role> roles = roleService.findAll();

        rolesModel = selectModelFactory.create(roles, "name");
    }

    /**
     * Simply cancel the roleAssignment
     * 
     * @param roleAssignment
     */
//    @OnEvent(value = "cancelRoleAssignment")
//    void cancelRoleAssignment(RoleAssignment roleAssignment)
    //	@OnEvent(value = "cancelRoleAssignment")
    @OnEvent(value="cancelRoleAssignment")
    void cancelRoleAssignment(Integer	id)
    {
    	roleAssignmentService.delete(roleAssignmentService.findById(id));
    }

}
