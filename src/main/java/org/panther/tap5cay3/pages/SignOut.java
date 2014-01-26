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
package org.panther.tap5cay3.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.panther.tap5cay3.pages.Signin;
import org.panther.tap5cay3.services.Authenticator;

public class SignOut {

		@Inject
		private Authenticator	authenticator;

//		@Inject
//		private Signin	signin;

		public Object onActivate() {
			
			authenticator.logout();
	
			return	Signin.class;
		}
}
