package com.googlecode.tapestry5cayenne;

import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.dba.db2.DB2Adapter;
import org.apache.cayenne.dba.derby.DerbyAdapter;
import org.apache.cayenne.dba.frontbase.FrontBaseAdapter;
import org.apache.cayenne.dba.h2.H2Adapter;
import org.apache.cayenne.dba.hsqldb.HSQLDBAdapter;
import org.apache.cayenne.dba.ingres.IngresAdapter;
import org.apache.cayenne.dba.mysql.MySQLAdapter;
import org.apache.cayenne.dba.openbase.OpenBaseAdapter;
import org.apache.cayenne.dba.oracle.Oracle8Adapter;
import org.apache.cayenne.dba.oracle.OracleAdapter;
import org.apache.cayenne.dba.postgres.PostgresAdapter;
import org.apache.cayenne.dba.sqlite.SQLiteAdapter;
import org.apache.cayenne.dba.sqlserver.SQLServerAdapter;

/**
 * 
 */
public enum CayenneDBAdapter {
    H2(1), HSQL(2), FRONTBASE(3), DERBY(4), DB2(5), INGRES(6), MYSQL(6), OPENBASE(
	    7), ORACLE8(8), ORACLE(9), POSTGRESQL(10), SQLITE(11), SQLSERVER(12), SYBASE(
	    13);

    public int value_;

    CayenneDBAdapter(int value)
    {
	value_ = value;
    }
    
    public static DbAdapter get(CayenneDBAdapter cayenneDBAdpater)
	    throws InstantiationException, IllegalAccessException
    {
	switch (cayenneDBAdpater)
	{
	case H2:
	    return H2Adapter.class.newInstance();
	case HSQL:
	    return HSQLDBAdapter.class.newInstance();
	case FRONTBASE:
	    return FrontBaseAdapter.class.newInstance();
	case DERBY:
	    return DerbyAdapter.class.newInstance();
	case DB2:
	    return DB2Adapter.class.newInstance();
	case INGRES:
	    return IngresAdapter.class.newInstance();
	case MYSQL:
	    return MySQLAdapter.class.newInstance();
	case OPENBASE:
	    return OpenBaseAdapter.class.newInstance();
	case ORACLE8:
	    return Oracle8Adapter.class.newInstance();
	case ORACLE:
	    return OracleAdapter.class.newInstance();
	case POSTGRESQL:
	    return PostgresAdapter.class.newInstance();
	case SQLITE:
	    return SQLiteAdapter.class.newInstance();
	case SQLSERVER:
	    return SQLServerAdapter.class.newInstance();
	case SYBASE:
	    return SQLiteAdapter.class.newInstance();
	default:
	    return null;
	}
    }
}