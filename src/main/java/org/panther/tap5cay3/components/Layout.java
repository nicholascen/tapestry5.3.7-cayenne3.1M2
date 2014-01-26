package org.panther.tap5cay3.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Border of the application
 */
@Import(stylesheet = "context:layout/layout.css")
public class Layout {
	@Inject
	private ComponentResources resources;

	/** The page title, for the <title> element and the <h1>element. */
	@SuppressWarnings("unused")
	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	/**Optional CSS rules to place into the page head */
	@SuppressWarnings("unused")
	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private Block style;

	@SuppressWarnings("unused")
	@Property
	private String pageName;

    @SuppressWarnings("unused")
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String section;

    /**
     * A block that displays in the upper-right corner of the page
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.PROP)
    private Block tools;

	void SetupRender()
	{
		pageName = resources.getPageName();
	}

	public String[] getPageNames()
	{
		return new String[] { "Index", "Index", "Contact" };
	}


}
