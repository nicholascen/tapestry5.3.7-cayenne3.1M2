package org.panther.tap5cay3.pages.admin;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.services.RoleService;

/**
 * Page used for editing/creating roles
 */
public class RoleEdit {

	@Component
	private BeanEditForm form;

	@Inject
	private RoleService roleService;

	@Property
	private Role role;

	private Integer roleId = 0;

	public void onActivate(Integer id) {
		if (id == 0) {
			role = new Role();
		} else {
			role = roleService.findById(id);
			this.roleId = id;
		}
	}

	public Object onSuccess() {

		role = roleService.save(role);
		return RoleListAdmin.class;
	}

	public void onValidateForm() {
		Role anotherRole = roleService.findRoleByName(role.getName());
		if (anotherRole != null && anotherRole.getId() != role.getId()) {
			form.recordError("Role with the name '" + role.getName()
					+ "' already exists");
		}
	}

	public long onPassivate() {
		return roleId;
	}

}
