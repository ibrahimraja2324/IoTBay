package iotbay.dao;

import java.sql.Connection;


public abstract class DB {   
    protected String URL = "jdbc:sqlite:" + System.getProperty("catalina.base") + "/webapps/assignment1/WEB-INF/database/database.db";
    protected String driver = "org.sqlite.JDBC";
    protected Connection conn;


}