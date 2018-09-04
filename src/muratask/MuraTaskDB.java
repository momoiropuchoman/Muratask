/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muratask;

/**
 *
 * @author osakana
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class MuraTaskDB extends DB
{

    Map<Integer, Task> getAllTask()
    {
	Map<Integer, Task> lstTask = new HashMap<>();
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "select * from tasks"
	    );
	    //preparedStatement.setString(1, key);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    while (resultSet.next())
	    {
		System.out.println(resultSet.getString("name") + ", " + resultSet.getBoolean("isCmp"));
		Task task = new Task();
		task.name = resultSet.getString("name");
		task.isCompleted = resultSet.getBoolean("isCmp");
		task.id = resultSet.getInt("id");
		lstTask.put(task.id, task);
	    }
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
		if (connection != null)
		{
		    connection.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}
	return lstTask;
    }

    Task addTask(String name)
    {
	Task task = new Task(name);
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "insert into tasks(name, isCmp) values(?, ?)"
	    );
	    preparedStatement.setString(1, name);
	    preparedStatement.setBoolean(2, false);
	    int successNum = preparedStatement.executeUpdate();
	    if (successNum != 1)
	    {
		return null;
	    }
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "select max(id) from tasks"
	    );
	    ResultSet resultSet = preparedStatement.executeQuery();
	    task.id = resultSet.getInt(1);
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
		if (connection != null)
		{
		    connection.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}

	return task;
    }

    boolean renameTask(int id, String name)
    {
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "update tasks set name = ? where id = ?"
	    );
	    preparedStatement.setString(1, name);
	    preparedStatement.setInt(2, id);
	    int i = preparedStatement.executeUpdate();
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
		if (connection != null)
		{
		    connection.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}

	return true;
    }

    boolean deleteTask(int id)
    {
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "delete from tasks where id = ?"
	    );
	    preparedStatement.setInt(1, id);
	    int i = preparedStatement.executeUpdate();
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
		if (connection != null)
		{
		    connection.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}

	return true;
    }

    String getInfo(String key)
    {
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "select * from charas where fieldID in (select ID from fields where name = ?)"
	    );
	    preparedStatement.setString(1, key);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    System.out.println(resultSet.getInt("x") + ", " + resultSet.getInt("y"));
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}

	return "hoge";
    }

    List<Note> getNotesByTaskID(int taskID)
    {
	System.out.println("into db");
	List<Note> lstNote = new ArrayList<>();
	Connection connection = this.getConnection();
	PreparedStatement preparedStatement = null;

	try
	{
	    preparedStatement = connection.prepareStatement(
		    "select * from notes where task_id = ? order by id"
	    );
	    preparedStatement.setInt(1, taskID);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    System.out.println("aaaa");
	    while (resultSet.next())
	    {
		//System.out.println(resultSet.getString("name") + ", " + resultSet.getBoolean("isCmp"));
		Note note = new Note();
		note.text = resultSet.getString("note");
		note.id = resultSet.getInt("id");
		note.taskID = resultSet.getInt("task_id");
		lstNote.add(note);
	    }
	} catch (SQLException e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (preparedStatement != null)
		{
		    preparedStatement.close();
		}
		if (connection != null)
		{
		    connection.close();
		}
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	}

	return lstNote;
    }

}
