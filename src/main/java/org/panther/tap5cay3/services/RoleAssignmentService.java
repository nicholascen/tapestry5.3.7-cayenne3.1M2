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
package org.panther.tap5cay3.services;

import java.util.List;

import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.model.RoleAssignment;
import org.panther.tap5cay3.model.User;

/**
 * Service for creating, managing and searching for {@link RoleAssignment}
 * entities. See also {@link GenericDataAccessService}
 * 
 */
public interface RoleAssignmentService 
	extends GenericDataAccessService<RoleAssignment, Integer> {

	/**
	 * Find all {@link RoleAssignment}s that match the given {@link User}
	 *  
	 * @param user the User to match, or null to match all
	 * @return a list of matching RoleAssignment objects, in alphabetical order by role name
	 */
	public List<RoleAssignment> findByUser(User user);

	/**
	 * Find all {@link RoleAssignment}s that match the given {@link Role}
	 *  
	 * @param role the Role to match, or null to match all
	 * @return a list of matching RoleAssignment objects, in alphabetical order by user name
	 */
	public List<RoleAssignment> findByRole(Role role);
}