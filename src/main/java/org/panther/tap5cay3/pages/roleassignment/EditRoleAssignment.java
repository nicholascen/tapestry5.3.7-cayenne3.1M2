/* Copyright 2011 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.panther.tap5cay3.pages.roleassignment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.model.RoleAssignment;
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.pages.RoAssEditableGrid;
import org.panther.tap5cay3.services.RoleAssignmentService;
import org.panther.tap5cay3.services.RoleService;
import org.panther.tap5cay3.services.UserService;
import org.slf4j.Logger;

/**
 * A Tapestry Page for editing an roleAssignment or creating a new one. For editing an
 * existing roleAssignment, first call setEntry() to set the roleAssignment to be edited.
 * 
 */
public class EditRoleAssignment
{

	@PageActivationContext
	// tell Tapestry to generate onActivate() & onPassivate()
	@Property
	private RoleAssignment roleAssignment;

	@Inject
	private RoleAssignmentService roleAssignmentService;

	
	@Property
	private String description;

	@Property
	private Role role; // used in a loop
	
	@Property
	private List<Role> roles;

	@Property
	private SelectModel roleSelectModel;

	@Inject
	private RoleService roleService;

	@Property
	private User user;

	@Property
	private List<User> users;

	@Property
	private SelectModel userSelectModel;

	@Inject
	private UserService userService;
	
	@Property
	private Date effectiveDate;

	@Property
	private Date expirationDate;


	@Property
	private String pageHeading;


	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private AlertManager alertManager;

	@Component
	private Form editForm;

	@Inject
	private Messages messages;

	@Inject
	private Logger logger;

	@InjectPage
	private RoAssEditableGrid roAssEditableGrid;

	/**
	 * Do setup actions prior to the form being rendered.
	 */
	@OnEvent(value = EventConstants.PREPARE_FOR_RENDER, component = "editForm")
	void setupFormData() {
		// copy to temporary properties (so we don't pollute our entities
		// with potentially invalid/incomplete data)
		if (roleAssignment==null) {
			
			this.description = "Enter Description...";
			Date today = new Date();
			this.effectiveDate = today;
			
			Calendar cal = Calendar.getInstance();
            cal.set(9999, 11, 31);
            Date future = new Date(cal.getTimeInMillis()); 
			this.expirationDate = future;

		}
		
		if (roleAssignment!=null) {
			
			this.role = roleAssignment.getRole();
			this.user = roleAssignment.getUser();
	
			this.description = roleAssignment.getDescription();
	
			this.effectiveDate = roleAssignment.getEffectiveDate();
			this.expirationDate = roleAssignment.getExpirationDate();

		}
		// populate the lists for select menus:
		users = userService.findAll();

		roles = roleService.findAll();

		// create SelectModels for the select menus:
		userSelectModel = selectModelFactory.create(users, User.NAME_PROPERTY);
		roleSelectModel = selectModelFactory.create(roles, Role.NAME_PROPERTY);
	}

	/**
	 * Save the submitted changes to the database.
	 * 
	 * @return the saved object
	 */
	@OnEvent(value = EventConstants.SUCCESS, component = "editForm")
	Object saveSubmittedRoleAssignment()
	{
		if (roleAssignment == null)
		{
			// create a new, empty roleAssignment object
			roleAssignment = roleAssignmentService.newEntity();
		}

		// Copy the submitted form values into the (new or existing) object.
		// (Inserting the values *after* validation ensures that we don't
		// pollute our entity set with invalid or abandoned objects.)
		roleAssignment.setRole(this.role);
		roleAssignment.setUser(this.user);
		roleAssignment.setDescription(this.description);
		roleAssignment.setEffectiveDate(this.effectiveDate);
		roleAssignment.setExpirationDate(this.expirationDate);

		// save to the database
		roleAssignment = roleAssignmentService.save(roleAssignment);

		// TODO: if user is under-privileged, send e-mail notifications about
		// roleAssignment needing to be approved/enabled

		logger.info("Saved " + roleAssignment.getDescription());

		return roAssEditableGrid;
	}

	/**
	 * Perform initializations (not necessarily form-related) needed before the
	 * page renders
	 */
	@SetupRender
	void initPage(final MarkupWriter writer)
	{
		if ((roleAssignment == null) || (roleAssignment.getId() == null))
		{
			pageHeading = "New RoleAssignment";
		}
		else
		{
			pageHeading = "Edit RoleAssignment";
		}
	}
}
