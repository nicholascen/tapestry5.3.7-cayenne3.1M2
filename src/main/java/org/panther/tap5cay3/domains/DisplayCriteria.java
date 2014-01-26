package org.panther.tap5cay3.domains;

import java.io.Serializable;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

/**
 * This class has been extracted from JBoss seam example applications
 */
public class DisplayCriteria implements Serializable
{
    @Property
    @SessionState
	private int rowsPerPage = 5;	//	default to 5; 
    
    
    //	7/14/2013 - END beginning of a session data store to carry forward what's needed between requests

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        this.rowsPerPage = rowsPerPage;
    }

}