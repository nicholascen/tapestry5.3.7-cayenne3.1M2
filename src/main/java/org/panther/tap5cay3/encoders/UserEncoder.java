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
import org.panther.tap5cay3.model.User;
import org.panther.tap5cay3.services.UserService;

/**
 * A ValueEncoder for the Entry entity. This makes it easy for Tapestry
 * to convert a Entry ID to a fully-populated object, and vice-versa.
 * See http://tapestry.apache.org/using-select-with-a-list.html
 * 
 * @author bharner
 *
 */
public class UserEncoder implements ValueEncoder<User>,
		ValueEncoderFactory<User> {

    @Inject
    private UserService UserService;
    
    public String toClient(User value) {
        // return the given object's ID
    	String cval = String.valueOf(value.getId()); 
    	//logger.error("here i am with cval=" + cval);
        return cval;
    }

    @Log
    public User toValue(String id) { 
        // find the Entry object of the given ID in the database
        try {
			return UserService.findByUserId(Integer.parseInt(id));
		}
		catch (NumberFormatException e) {
			throw new RuntimeException("ID " + id + " is not a number", e);
		}
    }

    // let this ValueEncoder also serve as a ValueEncoderFactory
    public ValueEncoder<User> create(Class<User> type) {
        return this; 
    }
} 
