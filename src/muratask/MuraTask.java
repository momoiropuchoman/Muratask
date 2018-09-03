/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muratask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Separator;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.geometry.Orientation;

/**
 *
 * @author osakana
 */
public class MuraTask extends Application implements Common {

    Stage stage;
    Scene scene;
    Pane root;

    // タスクリスト
    Map<Integer, Task> lstTask;
    Map<Integer, Label> lstTaskLabel;
    ScrollPane taskListArea;
    VBox taskListBase;

    // タスク入力
    Pane taskInputArea;
    TextField taskInputField;
    Button taskAddButton;
    
    // タスク詳細
    int detailedTaskID;
    Pane taskDetailArea;
    TextField taskNameField;
    Button taskCompleteEditButton;
    Button taskDeleteButton;
    // ノート
    
    
    
    MuraTaskDB db;
    
    

    public void start(Stage primaryStage) {

        db = new MuraTaskDB();
        lstTask = db.getAllTask();

        root = new Pane();
        //root.setPrefSize(WINDOW_X, WINDOW_Y);

        taskInputArea = new Pane();
        taskInputArea.relocate(0, 0);
        taskInputArea.setPrefSize(TASK_OVERVIEW_AREA_X, TASK_INPUT_AREA_Y);
        taskInputArea.setId("pane");
        root.getChildren().add(taskInputArea);

        taskInputField = new TextField();
        taskInputField.setPromptText("Enter your new task.");
        taskInputField.relocate(10, 40);
        taskInputField.setPrefSize(310, 30);
        taskInputField.setPromptText("Enter your new task.");
        taskInputArea.getChildren().add(taskInputField);

        taskAddButton = new Button();
        taskAddButton.relocate(
                taskInputField.getLayoutX() + taskInputField.getPrefWidth() + 10,
                taskInputField.getLayoutY());
        taskAddButton.setPrefSize(60, taskInputField.getPrefHeight());
        taskAddButton.setText("Add");
        taskAddButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (taskInputField.getText().length() > 0) {
                    addTask(taskInputField.getText());
                    taskInputField.setText("");
                }
            }
        });
        taskInputArea.getChildren().add(taskAddButton);

        /*
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        root.getChildren().add(separator);
         */
        taskListArea = new ScrollPane();
        taskListArea.relocate(0, TASK_INPUT_AREA_Y);
        taskListArea.setPrefSize(TASK_OVERVIEW_AREA_X, TASK_LIST_AREA_Y);
        taskListArea.setHbarPolicy(ScrollBarPolicy.NEVER);
        taskListArea.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        taskListArea.setFocusTraversable(false);
        root.getChildren().add(taskListArea);

        taskListBase = new VBox();
        //taskListBase.setMinHeight(380);
        //taskListBase.setMaxHeight(380);
        taskListBase.setPrefSize(TASK_OVERVIEW_AREA_X - 15, TASK_LIST_AREA_Y);
        taskListBase.setSpacing(1);
        taskListArea.setContent(taskListBase);

        lstTaskLabel = new HashMap<>();
        for(Map.Entry<Integer, Task> taskPair : lstTask.entrySet()) {
            createLabelFromTask(taskPair.getValue());
        }
        //taskListBase.setPrefSize(380, lstTaskLabel.size() * 30 + lstTaskLabel.size());
        
        
        taskDetailArea = new Pane();
        taskDetailArea.relocate(TASK_OVERVIEW_AREA_X, 0);
        taskDetailArea.setPrefSize(WINDOW_X - TASK_OVERVIEW_AREA_X, WINDOW_Y);
        taskDetailArea.setId("pane");
        root.getChildren().add(taskDetailArea);

        taskNameField = new TextField();
        taskNameField.relocate(30, 40);
        taskNameField.setPrefSize(430, 30);
        taskNameField.setText("");
        taskDetailArea.getChildren().add(taskNameField);

        taskCompleteEditButton = new Button();
        taskCompleteEditButton.relocate(
                taskNameField.getLayoutX() + taskNameField.getPrefWidth() + 10,
                taskNameField.getLayoutY());
        taskCompleteEditButton.setPrefSize(100, taskNameField.getPrefHeight());
        taskCompleteEditButton.setText("Rename");
        taskCompleteEditButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (taskNameField.getText().length() > 0) {
                    renameTask(taskNameField.getText());
                }
            }
        });
        taskDetailArea.getChildren().add(taskCompleteEditButton);
        
        taskDeleteButton = new Button();
        taskDeleteButton.relocate(
                taskCompleteEditButton.getLayoutX(),
                WINDOW_Y - 50
        );
        taskDeleteButton.setPrefSize(
                100, 
                taskNameField.getPrefHeight()
        );
        taskDeleteButton.setText("Delete");
        taskDeleteButton.setId("delete-button");
        taskDeleteButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(detailedTaskID != 0)
                {
                    deleteTask();
                }
                
            }
        });
        taskDetailArea.getChildren().add(taskDeleteButton);
        

        scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.getStylesheets().add(getClass().getResource("MuraTask.css").toExternalForm());

        primaryStage.setTitle("MuraTask");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    void addTask(String taskName) {
        Task task = db.addTask(taskName);
        if(task == null)
        {
            return;
        }
        lstTask.put(task.id, task);
        System.out.println("TaskNum = " + lstTask.size());
        createLabelFromTask(task);
    }
    
    void deleteTask()
    {
        lstTask.remove(detailedTaskID);
        taskListBase.getChildren().remove(lstTaskLabel.get(detailedTaskID));
        lstTaskLabel.remove(detailedTaskID);
        db.deleteTask(detailedTaskID);
    }
    
    void createLabelFromTask(Task task)
    {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3.0);
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.color(0.4, 0.5, 0.5));
        
        TaskLabel label = new TaskLabel();
        label.id = task.id;
        label.setPrefSize(TASK_OVERVIEW_AREA_X - 15, 30);
        label.setMaxHeight(30);
        label.setMinHeight(30);
        label.setText(task.name);
        label.setEffect(shadow);
        label.setOnMouseClicked((MouseEvent) -> {
            detailedTaskID = label.id;
            updateDetailArea();
        });
        
        lstTaskLabel.put(task.id, label);
        taskListBase.getChildren().add(label);
    }
    
    void renameTask(String taskName)
    {
        Task task = lstTask.get(detailedTaskID);
        task.name = taskName;
        db.renameTask(task.id, taskName);
        lstTaskLabel.get(task.id).setText(task.name);
    }
    
    void updateDetailArea()
    {
       Task task = lstTask.get(detailedTaskID);
       taskNameField.setText(task.name);
    }

    /*
    @Override
    public void start(Stage primaryStage) {

        MuraTaskDB db = new MuraTaskDB();
        lstTask = db.getAllTask();

        root = new Pane();
        //root.setPrefSize(WINDOW_X, WINDOW_Y);

        taskInputArea = new Pane();
        taskInputArea.relocate(0, 0);
        taskInputArea.setPrefSize(TASK_OVERVIEW_AREA_X, TASK_INPUT_AREA_Y);
        taskInputArea.setId("pane");

        taskInputField = new TextField();
        taskInputField.setPromptText("Enter your new task.");
        taskInputField.relocate(20, 40);
        taskInputField.setPrefSize(250, 30);
        taskInputField.setPromptText("Enter your new task.");
        taskInputArea.getChildren().add(taskInputField);

        taskAddButton = new Button();
        taskAddButton.relocate(
                taskInputField.getLayoutX() + taskInputField.getPrefWidth() + 20,
                taskInputField.getLayoutY());
        taskAddButton.setPrefSize(60, taskInputField.getPrefHeight());
        taskAddButton.setText("Add");
        taskAddButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (taskInputField.getText().length() > 0) {
                    addTask(taskInputField.getText());
                    taskInputField.setText("");
                }
            }
        });
        taskInputArea.getChildren().add(taskAddButton);

        taskListArea = new ListView<>();
        taskListArea.relocate(0, TASK_INPUT_AREA_Y);
        taskListArea.setPrefSize(TASK_OVERVIEW_AREA_X, TASK_LIST_AREA_Y);
        taskListArea.setFixedCellSize(40);

        List<String> lstStringTask = new ArrayList<>();
        for (Task t : lstTask) {
            lstStringTask.add(t.name);
        }
        lstObsTask = FXCollections.observableArrayList(lstStringTask);
        taskListArea.setItems(lstObsTask);

        taskListArea.setOnMouseClicked((MouseEvent) -> {
            onTaskListAreaClicked(taskListArea.getSelectionModel().getSelectedIndex());
        });

        // Paneを親に追加
        root.getChildren().add(taskInputArea);
        root.getChildren().add(taskListArea);

        scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.getStylesheets().add(getClass().getResource("MuraTask.css").toExternalForm());

        primaryStage.setTitle("MuraTask");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
     */
 /*
    @Override
    public void start(Stage primaryStage) {

        lstTask = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lstTask.add(new Task(String.valueOf(i)));
        }

        //root = new HBox();

        Pane root = new Pane();
        //root.setPrefSize(800, 600);
        taskManagementView = new VBox();
        taskManagementView.setPrefSize(400, 600);
        taskManagementView.relocate(0, 0);
        root.getChildren().add(taskManagementView);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        root.getChildren().add(separator);

        newTaskArea = new Pane();
        newTaskArea.setPrefSize(400, 150);
        newTaskArea.setMinHeight(150);
        newTaskArea.setStyle("-fx-background-color: white;");
        TextField newTask = new TextField();
        newTask.setPromptText("Enter your new task.");
        newTask.relocate(150, 50);
        newTaskArea.getChildren().add(newTask);
        taskManagementView.getChildren().add(newTaskArea);

        newTaskArea.setOnMouseClicked((event)->{
            System.out.println(newTask.getText());
            newTaskArea.requestFocus();
        });

        taskListScroll = new ScrollPane();
        taskListScroll.setPrefSize(400, 1000);
        taskListScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        taskListScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        taskListScroll.setFocusTraversable(false);
        taskManagementView.getChildren().add(taskListScroll);

        taskListView = new VBox();

        //taskListView.setMinHeight(380);
        //taskListView.setMaxHeight(380);
        taskListView.setSpacing(1);
        taskListScroll.setContent(taskListView);

        lstTaskLabel = new ArrayList<>();
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.color(0.4, 0.5, 0.5));

        for (int i = 0; i < lstTask.size(); i++) {
            Label label = new Label();
            label.setPrefSize(380, 30);
            label.setMaxHeight(30);
            label.setMinHeight(30);
            label.setText(lstTask.get(i).name);
            label.setEffect(shadow);
            lstTaskLabel.add(label);
            taskListView.getChildren().add(label);
        }
        taskListView.setPrefSize(380, lstTaskLabel.size() * 30 + lstTaskLabel.size());

        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("MuraTask.css").toExternalForm());

        primaryStage.setTitle("MuraTask");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
