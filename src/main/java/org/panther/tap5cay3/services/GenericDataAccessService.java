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

import java.io.Serializable;
import java.util.List;

import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.panther.tap5cay3.model.Role;

/**
 * A generic interface for all database services.
 * 
 */
public interface GenericDataAccessService<T, ID extends Serializable> extends
		Serializable
{

	public T newEntity();
	/**
	 * Find and return all occurrences in the database, sorted by the given
	 * field case-insensitively and in the given direction.
	 * 
	 * @param name
	 *            of the field to use for sorting
	 * @param ascending
	 *            if true sort ascending else descending
	 * @return
	 */
	public T create();
	
	/**
	 * Find and return all occurrences in the database, sorted by the given
	 * field case-insensitively and in the given direction.
	 * 
	 * @param name
	 *            of the field to use for sorting
	 * @param ascending
	 *            if true sort ascending else descending
	 * @return
	 */
	public T findById(ID id);

	/**
	 * Create or update an instance of the entity to the database.
	 * 
	 * @param trans
	 * @return
	 */
	public T save(T entity);
	
	public void saveAll(List<T> entities);

	/**
	 * Delete an entity from the database.
	 * 
	 * @param entity
	 */
	public void delete(T entity);

	public List<T>	findAll();

	public List<T>	findAllWithOrderings(List<Ordering> orderings);
	
	public List<T>	findAllWithQualifier(Expression qualifier);
	
	public List<T>	findAllWithQualifierAndOrderings(Expression qualifier,List<Ordering> orderings);
	
	public List<T>	findAllWithQueryString(String sqlString);
	
    public T findInList(ID id, List<T> entities);

	
}