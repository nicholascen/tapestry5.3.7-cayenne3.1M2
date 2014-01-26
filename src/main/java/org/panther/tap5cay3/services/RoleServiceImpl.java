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

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SortOrder;
import org.panther.tap5cay3.model.Role;

/**
 * Service for all {@link Role} related functionality.
 * See also {@link GenericDataAccessService}
 *
 */
public class RoleServiceImpl 
	extends GenericDataAccessServiceImpl<Role, Integer> 
	implements RoleService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.apache.tapestry.unicorn.services.RoleService#getAllRoles()
	 */
	public List<Role> findAllRoles() {

		List<Ordering>	orderings = new ArrayList<Ordering>();

		Ordering order = new Ordering(Role.NAME_PROPERTY,
					SortOrder.ASCENDING_INSENSITIVE);
		
		orderings.add(order);
		
		return findAllWithOrderings(orderings);

	}

	/**
	 * Returns a {@link Role} with the specified ID or it returns null.
	 *
	 * @param uid
	 * @return
	 */


	public Role findRoleByName(String name) {
		
		Expression qualifier = ExpressionFactory.likeIgnoreCaseExp(
                Role.NAME_PROPERTY,name);
                
        List<Role> matchedRoles = findAllWithQualifier(qualifier);
        
        if (	matchedRoles.size() == 0) {
        	return null;
        }

        return	matchedRoles.get(0);
	}

}
