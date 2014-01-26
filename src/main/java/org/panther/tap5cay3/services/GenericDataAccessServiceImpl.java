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
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.Log;
import org.panther.tap5cay3.model.Role;
import org.testng.Assert;


/**
 * Abstract class that implements the {@link GenericDataAccessService} interface, using
 * Apache Cayenne. Services can extend this class to get Cayenne-based
 * implementations of the most commonly-needed database access methods.
 * 
 * @param <T>
 * @param <ID>
 */
public abstract class GenericDataAccessServiceImpl<T extends CayenneDataObject, ID extends Serializable>
		implements GenericDataAccessService<T, ID>
{

	private static final long serialVersionUID = 99136895883299725L;
	
    public static final String COMMON_MANDATORY_ID_PROPERTY = "id";

	/**
	 * When this is not null, queries should be cached within the named region.
	 * Subclasses may set the region name.
	 */
	protected String queryCacheRegion;

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public GenericDataAccessServiceImpl()
	{
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T newEntity() {
		try {
			return getPersistentClass().newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void delete(T entity)
	{
		Assert.assertNotNull(entity, "No Entity Specified");

		ObjectContext context = DataContext.getThreadObjectContext();
		context.deleteObject(entity);
		context.commitChanges();
	}

	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}

	public String getQueryCacheRegion()
	{
		return queryCacheRegion;
	}

	/**
	 * Performs a commit to the database of an instance of
	 * {@link CayenneDataObject}.
	 * <p>
	 * IMPORTANT NOTE: Cayenne commits all changes in the context, not just the
	 * changes in this one entity. Most of the time this is what you want, but
	 * if not, then you need to ensure that what you want to commit is in a
	 * different context from what you don't.
	 * 
	 * @param entity
	 */
	public T save(T entity)
	{

		ObjectContext context;
		if (entity.getObjectContext() == null)
		{
			context = DataContext.getThreadObjectContext();
			context.registerNewObject(entity);
		}
		else
		{
			context = entity.getObjectContext();
		}
		
		context.commitChanges();
		return entity;
	}
	
	@Log
	public void saveAll(List<T> entities)
	{
		
		for (T entity : entities) {entity = save(entity);	}
	}

	public T create()
	{
		ObjectContext context = DataContext.getThreadObjectContext();
		return create(context);
	}

	/**
	 * Create a new {@link T} object, attached to the given
	 * Cayenne ObjectContext but not yet persisted to the database
	 * 
	 * @param context
	 * @return the new object
	 */
	private T create(ObjectContext context)
	{
		T entity = context.newObject(getPersistentClass());
		return entity;
	}


	/**
	 * Performs a lookup from the database of an instance of
	 * {@link CayenneDataObject}.
	 * <p>
	 * 
	 * @param id
	 */
	public T findById(ID id)
	{

		Expression exp = ExpressionFactory.inExp(COMMON_MANDATORY_ID_PROPERTY, id);
		//	SelectQuery query = new SelectQuery(getPersistentClass(), exp);

		@SuppressWarnings("unchecked")
//		List<T> entities = DataContext.getThreadObjectContext().performQuery(query);
		List<T> entities = findAllWithQualifier(exp);

		if (entities.size() == 0) {
			return null;
		}
		
		return entities.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<T>	findAll() {
		
		SelectQuery query = new SelectQuery(getPersistentClass());
		
		return DataContext.getThreadObjectContext().performQuery(query);
	}

	
	@SuppressWarnings("unchecked")
	public List<T>	findAllWithOrderings(List<Ordering> orderings) {
		
		SelectQuery query = new SelectQuery(getPersistentClass());

		if (orderings.size() > 0)	{
			for (int i = 0; i < orderings.size(); i++)	{
				query.addOrdering(orderings.get(i));				
			}
		}
		
		return DataContext.getThreadObjectContext().performQuery(query);
	}

	@SuppressWarnings("unchecked")
	public List<T>	findAllWithQualifier(Expression qualifier) {
		
		SelectQuery query = new SelectQuery(getPersistentClass(),qualifier);

		return DataContext.getThreadObjectContext().performQuery(query);
	}

	@SuppressWarnings("unchecked")
	public List<T>	findAllWithQualifierAndOrderings(Expression qualifier,List<Ordering> orderings) {
		
		SelectQuery query = new SelectQuery(getPersistentClass(),qualifier);
		
		if (orderings.size() > 0)	{
			for (int i = 0; i < orderings.size(); i++)	{
				query.addOrdering(orderings.get(i));				
			}
		}

		return DataContext.getThreadObjectContext().performQuery(query);
	}

	@SuppressWarnings("unchecked")
	public List<T>	findAllWithQueryString(String sqlString) {
		
		SQLTemplate query = new SQLTemplate(getPersistentClass(),sqlString);
//		query.setCacheStrategy(QueryCacheStrategy.LOCAL_CACHE);
//		query.setCacheGroups(Component.class.getSimpleName(), Component.class.getSimpleName()+".root");
		
		return DataContext.getThreadObjectContext().performQuery(query);
	}
	
	//	2014-1-7 generic object finder with an Id from a list of objects of the same type
	public T findInList(ID id, List<T> entities)
	 {
	        for (T entity : entities) {
	            if (entity.equals(findById(id))) {
	                return entity;
	            }
	        }
	        return null;
	    }
}