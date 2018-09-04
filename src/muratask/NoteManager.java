/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muratask;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author osakana
 */
public class NoteManager implements Common
{

    MuraTaskDB db;
    
    NoteManager()
    {
	db = new MuraTaskDB();
    }

    List<Note> getNotesByTaskID(int taskID)
    {
	System.out.println("into db 2");
	List<Note> lstNote = db.getNotesByTaskID(taskID);
	System.out.println("into db3");
	// setY(lstNote);

	return lstNote;
    }

    // 未完成
    void setY(List<Note> lstNote)
    {
	for (int i = 0; i < lstNote.size(); i++)
	{
	    lstNote.get(i).pane.relocate(10, 150 * i);
	    lstNote.get(i).pane.setPrefSize(TASK_DETAIL_AREA_X - 20, 150);
	}
    }
}
