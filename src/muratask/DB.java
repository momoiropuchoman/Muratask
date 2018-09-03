/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muratask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author osakana
 */
class DB 
{

    public static final String DB_NAME = "MuraTask.sqlite";

    protected Connection getConnection()
    {
        try
        {
            // Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void close(Connection connection, PreparedStatement preparedStatement)
    {
        try
        {
            if (preparedStatement != null)
            {
                preparedStatement.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
