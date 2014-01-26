package com.googlecode.tapestry5cayenne.services;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

import com.googlecode.tapestry5cayenne.annotations.CommitAfter;

/**
 * Exactly analogous to the tapestry-hibernate CommitAfterWorker.
 * That is: any method annotated with @CommitAfer in a tapestry-controlled class
 * (component, page, mixin) will automatically call context.commitChanges() on completion of
 * the method; should commit fail, rollback is performed automatically.
 */
public class CayenneCommitAfterWorker implements ComponentClassTransformWorker2
{
    
    private ObjectContextProvider provider;
    
    private final MethodAdvice advice = new MethodAdvice()
    {
	public void advise(MethodInvocation invocation)
	{
	    try
	    {
		invocation.proceed();
		provider.currentContext().commitChanges();

	    } catch (RuntimeException e)
	    {
		provider.currentContext().rollbackChanges();
		throw e;
	    }
	}
    };

    public void CommitAfterWorker(ObjectContextProvider provider)
    {
	this.provider = provider;
    }

    public void transform(PlasticClass plasticClass,
	    TransformationSupport support, MutableComponentModel model)
    {
	for (PlasticMethod method : plasticClass
		.getMethodsWithAnnotation(CommitAfter.class))
	{
	    method.addAdvice(advice);
        }
    }

}
