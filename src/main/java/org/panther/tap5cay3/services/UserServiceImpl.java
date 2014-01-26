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
import org.panther.tap5cay3.model.User;

/**
 * Service for all {@link User} related functionality.
 * See also {@link GenericDataAccessService}
 */
public class UserServiceImpl 
	extends GenericDataAccessServiceImpl<User, Integer> 
	implements UserService {

	private static final long serialVersionUID = -6305278621616029751L;
	

	/* (non-Javadoc)
	 * @see org.apache.tapestry.unicorn.services.UserService#findByUserId(java.lang.String)
	 */

	

	/**
	 * Find and return the {@link User} instance with the given userId
	 * 
	 * returns the User, if found, or else null
	 */
	public User findByUserId(Integer userId) {

		return	findById(userId);
		
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tapestry.unicorn.services.UserService#getAllUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<User> findAll() {
		
		List<Ordering>	orderings = new ArrayList<Ordering>();

		Ordering order = new Ordering(User.LAST_NAME_PROPERTY,
					SortOrder.ASCENDING_INSENSITIVE);
		
		orderings.add(order);

		return findAllWithOrderings(orderings);

	}

	public List<User> findAllUsers()	{	
			return	findAll();
	}
	
	public User find(Integer id)	{
		User	user	=	findByUserId(id);
		return	user;
	}
	

	public User findUserByName(String name) {
		
		Expression qualifier = ExpressionFactory.likeIgnoreCaseExp(
                User.LOGIN_EMAIL_PROPERTY,
                name);
                
        List<User> matchedUsers = findAllWithQualifier(qualifier);
        
        if (	matchedUsers.size() == 0) {
        	return null;
        }

        return	matchedUsers.get(0);
		
	}

	public User findUserByCredentials(String username,String password) {
		Expression qualifier = ExpressionFactory.likeIgnoreCaseExp(
                User.LOGIN_EMAIL_PROPERTY,
                username);
		
		//	AND the second criterion -> exact match on password case sensitive 7/8/2013
		qualifier =	qualifier.andExp(ExpressionFactory.likeExp(User.PASSWORD_PROPERTY, password));
		
        List<User> matchedUsers = findAllWithQualifier(qualifier);
        
        if (	matchedUsers.size() == 0) {
        	return null;
        }

        return	matchedUsers.get(0);
		
	}
}
