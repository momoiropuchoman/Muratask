/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muratask;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollPane;

/**
 *
 * @author osakana
 */
public class Note implements Common
{

    int id;
    int taskID;
    String text;
    Pane pane;
    TextArea textArea;

    Note()
    {
	pane = new Pane();
	pane.setPrefSize(TASK_DETAIL_AREA_X - 15, 200);

	textArea = new TextArea();
	//textArea.setEditable(false);
	textArea.relocate(10, 10);
	textArea.setPrefSize(pane.getPrefWidth() - 20, pane.getPrefHeight() - 20);
	//textArea.setPrefRowCount();

	textArea.setOnKeyTyped(new EventHandler<KeyEvent>()
	{
	    public void handle(KeyEvent event)
	    {
		String text = textArea.getText();
		int rowCount = 1;
		for (int i = 0; i < text.length() ; i++)
		{
		    if((int)(textArea.getText().charAt(i)) == 10)
		    {
			rowCount++;
		    }
		}
		//int rowCount = textArea.getText().split(System.getProperty("line.separator")).length;
		//int rowCount = textArea.getText().split("¥n").length;
		System.out.println(rowCount + "行 " + textArea.getText().length() + "文字");
		if (rowCount > 8)
		{
		    textArea.setPrefHeight(rowCount * 22);
		    pane.setPrefHeight(textArea.getPrefHeight() + 20);
		}
	    }
	});

	pane.setId("pane2");
	pane.getChildren().add(textArea);
    }

    Note(String text)
    {
	this.text = text;
	pane.setPrefSize(TASK_DETAIL_AREA_X - 15, 100);
	textArea.setText(text);
    }

    void setText(String text)
    {
	this.text = text;
	textArea.setText(text);
    }
}
