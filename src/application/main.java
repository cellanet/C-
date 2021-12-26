/*
 * Creator: prof Q
 * Modify: Gia Minh
 * 12/06/2021
 * Exam 3: practicing database
 */
package application;
	
import java.sql.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Exam3 extends Application {
	static Connection connection;
	static String database;
	static Label result;
	static TextArea sqlTextArea, resultTextArea;
	
	@Override
	public void start(Stage primaryStage){
		connection = null;
		database = "";		
		try {
			
			primaryStage.setTitle("Database Client using Derby DBMS");
			//Create menu bar with menu options and their click-events
			MenuBar menuBar = new MenuBar();
			Menu fileMenu = new Menu("_Database");
			MenuItem DBCreateItem = new MenuItem("_Create...");
			fileMenu.getItems().add(DBCreateItem);
			DBCreateItem.setOnAction(e ->{
				//perform creating a database
				DatabaseWindow("Create Database");
				System.out.println("Creating Database...");
			});

			fileMenu.getItems().add(new SeparatorMenuItem());
			MenuItem DBConnectItem = new MenuItem("C_onnect...");
			fileMenu.getItems().add(DBConnectItem);
			DBConnectItem.setOnAction(e ->{
				//perform connecting a database
				DatabaseWindow("Connect Database");
				System.out.println("Connecting database...");
			});

			MenuItem DBDisconnectItem = new MenuItem("_Disconnect...");
			fileMenu.getItems().add(DBDisconnectItem);
			DBDisconnectItem.setOnAction(e ->{
				//perform disconnect a database
				DatabaseWindow("Disconnect Database");
				System.out.println("Disconnecting database..");
			});

			fileMenu.getItems().add(new SeparatorMenuItem());
			MenuItem exitItem = new MenuItem("E_xit");
			fileMenu.getItems().add(exitItem);
			exitItem.setOnAction(e ->{
				try{
					connection.close();
					System.out.println("Closing database connection...");
				}
				catch(Exception ex){
				}
				primaryStage.close();
			});

			// ============================ TABLE ============================
			menuBar.getMenus().add(fileMenu);
			
			Menu tableMenu = new Menu("_Table");
			MenuItem tableCreateItem = new MenuItem("_Create...");
			tableMenu.getItems().add(tableCreateItem);
			tableCreateItem.setOnAction(e ->{
				//perform create a table
				TableExecuteWindow("CREATE TABLE");
				System.out.println("creating table...");
			});
		
			MenuItem tableDropItem = new MenuItem("_Drop...");
			tableMenu.getItems().add(tableDropItem);
			tableDropItem.setOnAction(e ->{
				//perform drop a table
				TableExecuteWindow("DROP TABLE");
				System.out.println("dropping table...");
			});

			tableMenu.getItems().add(new SeparatorMenuItem());

			MenuItem tableInsertItem = new MenuItem("_Insert a record...");
			tableMenu.getItems().add(tableInsertItem);
			tableInsertItem.setOnAction(e ->{
				//perform insert a record to a table
				TableExecuteWindow("INSERT TABLE");
				System.out.println("Inserting record(s)..");
			});

			MenuItem tableUpdateItem = new MenuItem("_Update record(s)...");
			tableMenu.getItems().add(tableUpdateItem);
			tableUpdateItem.setOnAction(e ->{
				//perform update a record to a table
				TableExecuteWindow("UPDATE TABLE");
				System.out.println("Updating record(s)..");
			});
			
			MenuItem tableDeleteItem = new MenuItem("_Delete record(s)...");
			tableMenu.getItems().add(tableDeleteItem);
			tableDeleteItem.setOnAction(e ->{
				//perform delete a record from a table
				TableExecuteWindow("DELETE TABLE");
				System.out.println("Deleting record(s)..");
			});
			
			menuBar.getMenus().add(tableMenu);

			Menu queryMenu = new Menu("_Query");
			MenuItem sqlItem = new MenuItem("_Retrieve...");
			queryMenu.getItems().add(sqlItem);
			sqlItem.setOnAction(e ->{
				//perform query a table;
				QueryExecuteWindow();
				System.out.println("SQL Querying//Retreiving information..");
			});

			menuBar.getMenus().add(queryMenu);

			Menu helpMenu = new Menu("_Help");
			MenuItem aboutItem = new MenuItem("_About...");
			helpMenu.getItems().add(aboutItem);
			aboutItem.setOnAction(e ->{
				AboutWindow();
			});

			menuBar.getMenus().add(helpMenu);
			BorderPane root = new BorderPane();
			root.setTop(menuBar);
			
			Scene scene = new Scene(root,700,400);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Display the about window that contains:
	 * 		project/application name
	 * 		developer name
	 * 		department name
	 * 		Company name
	 * 		Date
	 * 		version number
	 */
	public void AboutWindow()
	{
		Stage aboutStage = new Stage();

		aboutStage.setResizable(false);
		aboutStage.setTitle("About Database Client...");
		aboutStage.setFullScreen(false);
		aboutStage.initModality(Modality.APPLICATION_MODAL);

		Image developer = new Image("FILE:src\\student.gif"); //insert your own picture file

		ImageView imageView = new ImageView(developer);
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);

		HBox hbox = new HBox(10, new Label(), imageView, new Label("Database Client\nGia Minh\nCMPR113 - Advanced Java\nSanta Ana College\nDecember 6th, 2021\nversion: 1.0.0"));
		VBox vbox1 = new VBox(10, new Label(), hbox);
		
		Button OKbutton = new Button("_OK");
		OKbutton.setOnAction(e ->{
			aboutStage.close();
		});
		
		VBox vbox2 = new VBox(10,OKbutton, new Label());
		vbox2.setAlignment(Pos.CENTER);
		
		BorderPane root = new BorderPane();
		root.setCenter(vbox1);
		root.setBottom(vbox2);
		
		Scene scene = new Scene(root,300,200);
		aboutStage.setScene(scene);
		aboutStage.show();
			
	}
	// ========================================= DATABASE ====================================
	public void DatabaseWindow(String title)
	{
		Stage DBNameStage = new Stage();

		DBNameStage.setTitle(title);
		DBNameStage.setResizable(false);
		DBNameStage.setFullScreen(false);
		DBNameStage.initModality(Modality.APPLICATION_MODAL);

		TextField databaseName = new TextField();
		databaseName.setPrefWidth(250);
	
		HBox hbox1 = new HBox(10, new Label(" "), new Label("Database name: "), databaseName );
		VBox vbox1 = new VBox(10, new Label(), hbox1, new Label());
			
		result = new Label();
		
		Button OKbutton = new Button("_OK");
		OKbutton.setOnAction(e ->{
			// perform create database, connect database or disconnect data base
			if(title == "Create Database")
			{
				//create database
				try 
				{
					database = "jdbc:derby:" + databaseName.getText() + ";create=true";
					connection = DriverManager.getConnection(database);
					connection.close();
					System.out.println("Created database: " + databaseName.getText());
					DBNameStage.close();
				} catch (Exception e1) 
				{
					result.setText("ERROR: " + e1.getMessage());
				}
				
			}
			//connecting
			else if(title == "Connect Database")
			{
				try 
				{
					database = "jdbc:derby:" + databaseName.getText();
					connection = DriverManager.getConnection(database);
					System.out.println("Connected to database: " + database);
					DBNameStage.close();
				} catch (Exception e1) 
				{
					result.setText("ERROR: " + e1.getMessage());
				}
			}
			//disconnecting
			else
			{
				try 
				{					
					connection.close();
					System.out.println("------------");
					System.out.println("Disconnected to database: " + database);	
					DBNameStage.close();
				} catch (Exception e1) 
				{
					result.setText("ERROR: " + e1.getMessage());
				}			
			}
		});
			
		Button Cancelbutton = new Button("_Cancel");
		Cancelbutton.setOnAction(e ->{
			DBNameStage.close();
		});
			
		HBox hbox2 = new HBox(10, OKbutton, Cancelbutton);
		hbox2.setAlignment(Pos.CENTER);
		
		VBox vbox2 = new VBox(10,hbox2, new Label());
		vbox2.setAlignment(Pos.CENTER);
			
		BorderPane root = new BorderPane();
		root.setTop(vbox1);
		root.setCenter(vbox2);
		root.setBottom(result);	
		Scene scene = new Scene(root,400,150);
		DBNameStage.setScene(scene);
		DBNameStage.show();
	}
	
	// ========================================== TABLE =======================================

	/**
	 * Displays a window (GUI) for executing CREATE, DROP, INSERT, UPDATE, or DELETE SQL statement on  a table
	 */
	public void TableExecuteWindow(String title)
	{
		Stage TableStage = new Stage();
		TableStage.setResizable(false);
		TableStage.setTitle(title);
		TableStage.setFullScreen(false);
		TableStage.initModality(Modality.APPLICATION_MODAL);
	
		sqlTextArea = new TextArea();
		sqlTextArea.setPrefColumnCount(30);
		sqlTextArea.setPrefRowCount(10);
		
		if(title == "CREATE TABLE")
			sqlTextArea.setText("CREATE TABLE TableName");
		else if(title == "DROP TABLE")
			sqlTextArea.setText("DROP TABLE TableName");
		else if(title == "INSERT TABLE")
			sqlTextArea.setText("INSERT INTO TableName VALUES (value1-int, 'value2'-string, value3-double)");
		else if(title == "UPDATE TABLE")
			sqlTextArea.setText("UPDATE TableName SET column1, column2... WHERE condition");
		else
			sqlTextArea.setText("DELETE FROM TableName WHERE condition");
	//	sqlTextArea.setText("CREATE, DROP, INSERT, UPDATE, or DELETE sql syntax");
	
		VBox vbox = new VBox(new Label(" SQL Statement"),sqlTextArea);
		BorderPane root = new BorderPane();
		root.setTop(vbox);

		result = new Label();

		Button executeButton = new Button("_Execute...");
		executeButton.setOnAction(e ->{
			// perform execute sql 
			try 
			{				
				Statement stmt = connection.createStatement();			
				stmt.execute(sqlTextArea.getText());				
				
			} catch (Exception e1) 
			{
				result.setText("ERROR: " + e1.getMessage());
			}
		});
		
		Button saveButton = new Button("_Save...");
		saveButton.setOnAction(e ->{
			writeSQLFile(TableStage);
		});

		Button openButton = new Button("_Open...");
		openButton.setOnAction(e ->{
			readSQLFile(TableStage);
		});

		Button cancelButton = new Button("_Cancel");
		cancelButton.setOnAction(e ->{
			TableStage.close();
		});

		HBox hbox1 = new HBox(10,executeButton, saveButton, openButton, cancelButton);
		root.setCenter(hbox1);
		
		HBox hbox2 = new HBox(10,new Label(), result);
		root.setBottom(hbox2);
		
		Scene scene = new Scene(root,600,250);
		TableStage.setScene(scene);
		TableStage.show();
	}

	/**
	 * Displays a window (GUI) for performing SQL Query (executeQuery) statement
	 */
	public void QueryExecuteWindow()
	{
		Stage QueryStage = new Stage();
		
		QueryStage.setResizable(false);
		QueryStage.setTitle("SELECT FROM TABLE SQL");
		QueryStage.setFullScreen(false);
		QueryStage.initModality(Modality.APPLICATION_MODAL);
	
		sqlTextArea = new TextArea();
		sqlTextArea.setPrefColumnCount(30);
		sqlTextArea.setPrefRowCount(10);
		sqlTextArea.setText("SELECT * FROM TableName\n\tWHERE Criteria");
		
		resultTextArea = new TextArea();
		resultTextArea.setPrefColumnCount(30);
		resultTextArea.setPrefRowCount(10);
		
		BorderPane root = new BorderPane();
	
		result = new Label();

		Button executeButton = new Button("_Execute...");
		executeButton.setOnAction(e ->{
			// perform execute query SQL
			try 
			{
				Statement stmt = connection.createStatement();		
				ResultSet resultSet = stmt.executeQuery(sqlTextArea.getText());				
				while (resultSet.next())
				{
					resultTextArea.setText("Student ID: " + resultSet.getString(1)
										+ "\nStudent name: " + resultSet.getString(2) +
										"\nScores: "	+ resultSet.getDouble(3));
				}
					
			} catch (Exception e1) 
			{
				result.setText("ERROR: " + e1.getMessage());
			}
		});
		
		Button saveButton = new Button("_Save...");
		saveButton.setOnAction(e ->{
			writeSQLFile(QueryStage);
		});

		Button openButton = new Button("_Open...");
		openButton.setOnAction(e ->{
		 	 readSQLFile(QueryStage);
		});

		Button cancelButton = new Button("_Cancel");
		cancelButton.setOnAction(e ->{
			QueryStage.close();
		});
		
		HBox hbox1 = new HBox(10,executeButton, saveButton, openButton, cancelButton);
		
		VBox vbox = new VBox(10, new Label(" SQL Statement"),sqlTextArea, hbox1, new Label(" Result"),resultTextArea);
		root.setTop(vbox);

		HBox hbox2 = new HBox(10,new Label(), result);
		root.setBottom(hbox2);
		
		Scene scene = new Scene(root,600,500);
		QueryStage.setScene(scene);
		QueryStage.show();
	}

	/**
	 * Prompts for an SQL file to read and displays onto the textArea 	
	 * @param currentStage
	 */
	public void readSQLFile(Stage currentStage)
	{
		try{
			FileChooser fileOpenChooser = new FileChooser();

			fileOpenChooser.setTitle("Open SQL file...");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
			fileOpenChooser.getExtensionFilters().add(extFilter);
			File selectedFile = fileOpenChooser.showOpenDialog(currentStage);
			if (selectedFile.exists()){
				BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
				String strCurrentLine;
				String buffer = "";
				while ((strCurrentLine = reader.readLine()) != null) 
					buffer += strCurrentLine + '\n';
				reader.close();
				sqlTextArea.setText(buffer);
				result.setText("Completed opening SQL file: " + selectedFile + ".");
			}
		}
		catch(Exception ex){
			result.setText("ERROR: " + ex.getMessage());
		}
	}
	
	/**
	 * Prompts for a name ofe SAL file to save the content of the textArea 
	 * @param currentStage
	 */
	public void writeSQLFile(Stage currentStage)
	{
		try{
			 FileChooser fileSaveChooser = new FileChooser();
			 fileSaveChooser.setTitle("Save SQL file...");
			 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
             fileSaveChooser.getExtensionFilters().add(extFilter);
			 File selectedFile = fileSaveChooser.showSaveDialog(currentStage);
			 if (sqlTextArea.getText() != null){
					 BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
					 writer.write(sqlTextArea.getText());
					 writer.close();
					result.setText("Completed saving SQL file: " + selectedFile + ".");
				 }
				 else
					 result.setText("ERROR: cannot not save an empty " + selectedFile + " file.");
		}
		catch(Exception ex){
			result.setText("ERROR: " + ex.getMessage());
		}
	}
}
