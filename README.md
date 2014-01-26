A sample project illustrating the use of tapestry5-cayenne project for 'select' columns populated from database tables, 
on top of Tapestry 5.3.7 and Cayenne 3.1M2.

The database information is encapsulated in the Cayenne project and mapping files cayenne-tap5cay3.xml and tap5cay3.map.xml.

Cayenne Modeler is used define and maintain database and entity information.  Any database that Cayenne can connect to can be used.

Once you have the database/schema/tables created, run the user_load.sql to load the first user with which to login.

To login once the app is up, use q/q.

The "User Admin" and "Role Admin" pages can be used to define/update/view/delete users and roles.

The "Role Editable Grid" page illustrates how multiple entries can edited and saved with the same page.

The "Role Assignment Editable Grid" page illustrates the use of drop downs populated from entries in the Role and User tables.

For further information, please refer to http://t5cayenne.saiwai-solutions.com/ and http://t5cayenne.saiwai-solutions.com/tutorial.

Many thanks to Robert Zeigler for making the tapestry5-cayenne project available that provides an elegant bonding agent between the 
two elegant frameworks, Tapestry and Cayenne, and for his support and guidance in the process of learning how to leverage the built-in 
functionality provided in the tapestry5-cayenne project.