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
public class Task
{
    int id;
    String name;
    boolean isCompleted;
    boolean isDeleted;
    
    Task()
    {
        
    }
    
    Task(String name)
    {
        this.name = name;
        isCompleted = false;
        isDeleted = false;
    }
}
