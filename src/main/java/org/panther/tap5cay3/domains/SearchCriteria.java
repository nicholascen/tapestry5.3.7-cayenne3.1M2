package org.panther.tap5cay3.domains;

import java.io.Serializable;

/**
 * This class has been extracted from JBoss seam example applications
 */
public class SearchCriteria implements Serializable
{
    private static final long serialVersionUID = 6022601867962992033L;

    private static final char SQL_WILDCARD_CHAR = '%';

    private static final String SQL_WILDCARD_STR = String.valueOf(SQL_WILDCARD_CHAR);

    private static final String REPEAT_SQL_WIDCARD_REGEX = SQL_WILDCARD_STR + "+";

    private static final char HUMAN_WILDCARD_CHAR = '*';

    //	7/14/2013 - BEGIN beginning of a session data store to carry forward what's needed between requests

    private String searchString = null;

    private int rowsPerPage = 5;	//	default to 5; 
    
     //	7/14/2013 - END beginning of a session data store to carry forward what's needed between requests

    public String getSearchPattern()
    {
        if (searchString == null || searchString.trim().length() == 0) { return null; }

        StringBuilder pattern = new StringBuilder();
        pattern.append(searchString.toLowerCase().replace(HUMAN_WILDCARD_CHAR, SQL_WILDCARD_CHAR)
                .replaceAll(REPEAT_SQL_WIDCARD_REGEX, SQL_WILDCARD_STR));
        if (pattern.length() == 0 || pattern.charAt(0) != SQL_WILDCARD_CHAR)
        {
            pattern.insert(0, SQL_WILDCARD_CHAR);
        }
        if (pattern.length() > 1 && pattern.charAt(pattern.length() - 1) != SQL_WILDCARD_CHAR)
        {
            pattern.append(SQL_WILDCARD_CHAR);
        }
        return pattern.toString();
    }

    public String getSearchString()
    {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = (searchString != null ? searchString.trim() : null);
    }

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        this.rowsPerPage = rowsPerPage;
    }

}