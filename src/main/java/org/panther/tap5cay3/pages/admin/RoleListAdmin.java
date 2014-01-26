package org.panther.tap5cay3.pages.admin;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Secure;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.panther.tap5cay3.domains.SearchCriteria;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.services.RoleService;

/**
 * Start page of application app.
 */
//	@Secure	
//	1-23-2014 - 5.4-alpha-4 seems to ignore this annotation. 5.3.7 enforces it
public class RoleListAdmin{
    @Inject
    private BeanModelSource beanModelSource;
    
    @Inject
    private ComponentResources resources;
 
    @Inject
    private Messages	beanModelMessages;
    
    @Inject
    private RoleService roleService;
	
	@Inject
	private Block roleDetailBlock;
	
	@Property
	private Role role;
	
	@Property(write=false)
	private Role detailRole;
	
    @SessionState
    @Property
	private SearchCriteria searchCriteria;

	public List<Role> getRoles(){
		return roleService.findAllRoles();
	}
	
	public BeanModel<Role> getRoleModel() {
		BeanModel<Role> roleModel = beanModelSource.createDisplayModel(Role.class, beanModelMessages);
		return roleModel;
	}
	
	void onActionFromDelete(Integer id){
		
		roleService.delete(roleService.findById(id));
		
	}
	
	Object onActionFromView(Integer id){
		
		detailRole = roleService.findById(id);
		return roleDetailBlock;
	}

}