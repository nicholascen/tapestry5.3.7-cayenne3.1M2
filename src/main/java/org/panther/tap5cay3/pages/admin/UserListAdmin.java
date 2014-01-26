package org.panther.tap5cay3.pages.admin;

import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.services.UserService;

/**
 * Start page of application app.
 */
public class UserListAdmin{
    @Inject
    private BeanModelSource beanModelSource;
    
    @Inject
    private ComponentResources resources;
 
    @Inject
    private Messages	beanModelMessages;
    
    @Inject
    private UserService userService;
	
	@Inject
	private Block userDetails;
	
	@Property
	private User user;
	
	@Property(write=false)
	private User detailUser;

	public List<User> getUsers(){
		return userService.findAllUsers();
	}
	
	public BeanModel<User> getUserModel() {
		BeanModel<User> userModel = beanModelSource.createDisplayModel(User.class, beanModelMessages);

		return userModel;
	}
	
	void onActionFromDelete(Integer id){
		
		userService.delete(userService.findByUserId(id));
		
	}
	
	Object onActionFromView(Integer id){
		detailUser = userService.findByUserId(id);
		return userDetails;
	}

}