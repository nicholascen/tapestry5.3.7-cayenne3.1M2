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
package org.panther.tap5cay3.encoders;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.panther.tap5cay3.model.Role;
import org.panther.tap5cay3.services.RoleService;

/**
 * A ValueEncoder for the Entry entity. This makes it easy for Tapestry
 * to convert a Entry ID to a fully-populated object, and vice-versa.
 * See http://tapestry.apache.org/using-select-with-a-list.html
 * 
 * @author bharner
 *
 */
public class RoleEncoder implements ValueEncoder<Role>,
		ValueEncoderFactory<Role> {

    @Inject
    private RoleService roleService;
    
    public String toClient(Role value) {
        // return the given object's ID
    	String cval = String.valueOf(value.getId()); 
    	//logger.error("here i am with cval=" + cval);
        return cval;
    }

    @Log
//	@Override
//	public Role toValue(String idAsString) {
//		Role role = null;
//
//		if (idAsString == null) {
//			role = roleService.newEntity();
//		} else {
//			Integer id = new Integer(idAsString);
//			role = roleService.findById(id);
//
//			// If Role has since been deleted from the DB. Create a skeleton
//			// Role.
//			if (role == null) {
//				role = roleService.findById(id);
//			}
//		}
//
//		return role;
//	}
//
    public Role toValue(String id) { 
        // find the Entry object of the given ID in the database
        try {
			return roleService.findById(Integer.parseInt(id));
		}
		catch (NumberFormatException e) {
			throw new RuntimeException("ID " + id + " is not a number", e);
		}
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    public ValueEncoder<Role> create(Class<Role> type) {
        return this; 
    }
} 
