package org.panther.tap5cay3.pages.admin;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.pages.Index;
import org.panther.tap5cay3.services.UserService;

/**
 * Page used for editing/creating users
 */
public class UserEdit {

	@Component
	private BeanEditForm form;

	@Inject
	private UserService userService;


	@Property
	private User user;

	private Integer userId = 0;

	public void onActivate(Integer id) {
		if (id == 0) {
			user = userService.newEntity();
			}
		else
		 {
			user = userService.findByUserId(id);
			this.userId = id;
		}
	}
	
	public Object onSuccess() {

		user =  userService.save(user);
		return UserListAdmin.class;
	}
	
	public void onValidateForm() {
		User anotherUser = userService.findUserByName(user.getLoginEmail());
		if (anotherUser != null && anotherUser.getId() != user.getId()) {
			form.recordError("User with the name '" + user.getLoginEmail() + "' already exists");
		}
	}

	public long onPassivate() {
		return userId;
	}

}
