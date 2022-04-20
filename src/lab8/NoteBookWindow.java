package lab8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import lab4.NoteBook;
import lab8.Folder;
import lab8.Note;
import lab8.NoteBook;
import lab8.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	String currentNote = "";
	
	Stage stage;
	
	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(centerVBox());
//		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	
	private HBox addHBox() {
		HBox hbox = new HBox();
		
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setDisable(false);
		
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              FileChooser filechooser = new FileChooser();
              filechooser.setTitle("Please Choose An File which Contains a NoteBook Object!");
              
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
              filechooser.getExtensionFilters().add(extFilter);
              
              File file = filechooser.showOpenDialog(stage);
              
              if (file != null){
            	  loadNoteBook(file);
              }
            }
        });
		
		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setDisable(false);
		
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              FileChooser filechooser = new FileChooser();
              filechooser.setTitle("Please Choose An File to save the NoteBook Object!");
              
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
              filechooser.getExtensionFilters().add(extFilter);
              
              File file = filechooser.showOpenDialog(stage);
              

            	  if (noteBook.save(file.getName())){
            		  Alert alert = new Alert(AlertType.INFORMATION);
            		  alert.setTitle("Successfully saved");
            		  alert.setContentText("You file has been saved to file " + file.getName());
            		  alert.showAndWait().ifPresent(rs -> {
            		      if (rs == ButtonType.OK) {
            		          System.out.println("Pressed OK.");
            		      }
            		  });

            	  } else {
            		  System.out.println("failed to save");
            	  }
            	  
              
            }
        });

		hbox.getChildren().addAll(buttonLoad, buttonSave);
		
		Label search_label = new Label("Search: ");
		TextField input = new TextField();
		Button search_button = new Button("Search");
		
		ArrayList<String> list = new ArrayList<String>();
		
		search_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              currentSearch = input.getText();
              textAreaNote.setText("");
              for (Folder f : noteBook.getFolders()){
            	  if (f.getName().equals(currentFolder)){
            		  list.clear();
            		  List<Note> return_list = f.searchNotes(currentSearch);
            		  for (Note n : return_list){
            			  list.add(n.getTitle());
            		  }
                      ObservableList<String> search_notes = FXCollections.observableArrayList(list);
            		  titleslistView.setItems(search_notes);
            		  break;
            	  }
              }
            }
        });

		
		Button clear_button = new Button("Clear Search");
		
		clear_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	input.clear();
            	currentSearch = "";
            	textAreaNote.clear();
            	textAreaNote.setText("");
            	for (Folder f : noteBook.getFolders()){
            		if (f.getName().equals(currentFolder)){
            			list.clear();
            			List<Note> return_list = f.getNotes();
            		 	for (Note n : return_list){
            		 		list.add(n.getTitle());
            		 	}
            		 	ObservableList<String> search_notes = FXCollections.observableArrayList(list);
            		 	titleslistView.setItems(search_notes);
            		 	break;
            		}
            	}
            }
        });
		
		hbox.getChildren().addAll(search_label, input, search_button, clear_button);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		String name1 = this.noteBook.getFolders().get(0).getName();
		String name2 = this.noteBook.getFolders().get(1).getName();
		String name3 = this.noteBook.getFolders().get(2).getName();
		ArrayList<String> a = new ArrayList();
		for (Folder f : this.noteBook.getFolders()){
			a.add(f.getName());
		}
//		foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
//		foldersComboBox.getItems().addAll(name1, name2 , name3);
		foldersComboBox.getItems().addAll(a);

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView(t1);
			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				
				currentNote = title;
				System.out.println("currentNote = " + currentNote);

				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				
				String content = null;
				
				for (Folder f : noteBook.getFolders()){
					for (Note n : f.getNotes()){
						if (n.getTitle().equals(title)){
							TextNote tn = (TextNote) n;
							content = tn.content;
						}
					}
				}
				
				textAreaNote.setText(content);

			}
		});
		
		Button addFolder = new Button("Add a Folder");
		
		addFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	TextInputDialog dialog = new TextInputDialog("Add a Folder");
                dialog.setTitle("Input");
                dialog.setHeaderText("Add a new folder for your notebook:");
                dialog.setContentText("Please enter the name you want to create:");

                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    // TODO
                	boolean duplicate = false;
                	System.out.println("result = " + result);
                	for (Folder f : noteBook.getFolders()){
                		if (result.equals(Optional.of(f.getName()))){
                			duplicate = true;
                			Alert alert = new Alert(AlertType.INFORMATION);
                  		  	alert.setTitle("Warning");
                  		  	alert.setContentText("You already have a folder named with " + result.get());
                  		  	alert.showAndWait().ifPresent(rs -> {
                  		    if (rs == ButtonType.OK) {
                  		         System.out.println("Pressed OK.");
                  		     }
                  		  	});
                		} else {
                    		System.out.println("not equal");
                		}
                	}
                	if (!duplicate){
                		if (result.equals(" ")){
                    		Alert alert = new Alert(AlertType.INFORMATION);
                  		  	alert.setTitle("Warning");
                  		  	alert.setContentText("Please enter a valid folder name");
                  		  	alert.showAndWait().ifPresent(rs -> {
                  		    if (rs == ButtonType.OK) {
                  		         System.out.println("Pressed OK.");
                  		     }
                  		  	});
                    	} else {
                    		noteBook.addFolder(result.get());
                    		foldersComboBox.getItems().add(result.get());
                    		System.out.println("add folder");
                    		Alert alert = new Alert(AlertType.INFORMATION);
                  		  	alert.setTitle("Successful");
                  		  	alert.setContentText("You have successfully created a folder named " + result.get());
                  		  	alert.showAndWait().ifPresent(rs -> {
                  		    if (rs == ButtonType.OK) {
                  		         System.out.println("Pressed OK.");
                  		     }
                  		  	});
                    	}
                	}
                	
                	
                	
                }
            }
        });
		
		Button addNote = new Button("Add a Note");
		
		addNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("currentfolder = " + currentFolder);
            	if (currentFolder.isEmpty() || currentFolder.equals("-----")){
        			Alert alert = new Alert(AlertType.INFORMATION);
          		  	alert.setTitle("Warning");
          		  	alert.setContentText("Please choose a folder first");
          		  	alert.showAndWait().ifPresent(rs -> {
          		    if (rs == ButtonType.OK) {
          		         System.out.println("Pressed OK.");
          		     }
          		  	});
            	} else {
            		TextInputDialog dialog = new TextInputDialog("Add a Note");
            	    dialog.setTitle("Input");
            	    dialog.setHeaderText("Add a new folder for your notebook:");
            	    dialog.setContentText("Please enter the name of your note:");

            	    // Traditional way to get the response value.
            	    Optional<String> result = dialog.showAndWait();
            	    if (result.isPresent()){
            	        // TODO 
            	    	noteBook.createTextNote(currentFolder, result.get());
            	    	titleslistView.getItems().add(result.get());
            	    	currentNote = result.get();
            	    	Alert alert = new Alert(AlertType.INFORMATION);
              		  	alert.setTitle("Successful!");
              		  	alert.setContentText("Insert note " + result.get() + " to folder " + currentFolder + " successfully!");
              		  	alert.showAndWait().ifPresent(rs -> {
              		    if (rs == ButtonType.OK) {
              		         System.out.println("Pressed OK.");
              		     }
              		  	});
            	    }
            	}
            }
        });
		
		HBox hbox2 = new HBox();
		hbox2.setSpacing(8);
		
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox2);
		hbox2.getChildren().add(foldersComboBox);
		hbox2.getChildren().add(addFolder);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNote);
		

		return vbox;
	}

	private void updateListView(Object t1) {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		
		for (Folder f : this.noteBook.getFolders()){
			if (f.getName().equals(t1.toString())){
				for (Note n : f.getNotes()){
					list.add(n.getTitle());
				}
			}
		}
		
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	
	private VBox centerVBox(){
		VBox v = new VBox();
		HBox h = new HBox();
		h.setSpacing(8); 
		Button button = new Button ("Save Note");
		Button a = new Button ("Delete Note");
		
		 ImageView iv = new ImageView();
		 Image saveImage = new Image("save.png");
         iv.setImage(saveImage);
         iv.setFitHeight(18);
         iv.setFitWidth(18);
         
         button.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent event) {
            	 if (currentFolder.isEmpty() || currentFolder.equals("-----")){
            	  Alert alert = new Alert(AlertType.INFORMATION);
           		  alert.setTitle("Successful");
           		  alert.setContentText("Please select a folder and a note");
           		  alert.showAndWait().ifPresent(rs -> {
           		      if (rs == ButtonType.OK) {
           		          System.out.println("Pressed OK.");
           		      }
           		  });
            	 } else{
            		 for (Folder f : noteBook.getFolders()){
            			 for (Note n : f.getNotes()){
            				 if (n.getTitle().equals(currentNote)){
            					 TextNote tn = (TextNote) n;
            					 tn.content = textAreaNote.getText();
            					 System.out.println("content changed");
            					 Alert alert = new Alert(AlertType.INFORMATION);
            					 alert.setTitle("Successful");
            	           		  alert.setContentText("Content of the note has been suceccessfully saved");
            	           		  alert.showAndWait().ifPresent(rs -> {
            	           		      if (rs == ButtonType.OK) {
            	           		          System.out.println("Pressed OK.");
            	           		      }
            	           		  });
            					 break;
            				 }
            			 }
            		 }
            	 }
             }
         });
         
         a.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent event) {
            	 if (currentFolder.isEmpty() || currentFolder.equals("-----")){
            		 System.out.println("empty currentfolder");
            	  Alert alert = new Alert(AlertType.INFORMATION);
           		  alert.setTitle("Warning");
           		  alert.setContentText("Please select a folder and a note");
           		  alert.showAndWait().ifPresent(rs -> {
           		      if (rs == ButtonType.OK) {
           		          System.out.println("Pressed OK.");
           		      }
           		  });
            	 } else{
            		 System.out.println("pressed delete 2");
            		 for (Folder f : noteBook.getFolders()){
            			 if (f.getName().equals(currentFolder)){
            				 System.out.println("found folder");
            				 System.out.println("currentNote = " + currentNote);
            				 for (Note n : f.getNotes()){
            					 System.out.println("note name = " + n.getTitle());
            					 if (n.getTitle().equals(currentNote)){
            						 System.out.println("found note");
            						 textAreaNote.setText("");
                        			 f.removeNote(currentNote);
                        			 titleslistView.getItems().remove(currentNote);
                        			 System.out.println("note deleted");
                        			 Alert alert = new Alert(AlertType.INFORMATION);
                        			 alert.setTitle("Successful");
                              		  alert.setContentText("Content of the note has been suceccessfully deleted");
                              		  alert.showAndWait().ifPresent(rs -> {
                              		      if (rs == ButtonType.OK) {
                              		          System.out.println("Pressed OK.");
                              		      }
                              		  });
                              		  break;
                    			 }
            				 }
            			 }
            			 
            			 
            		 }
            	 }
             }
         });
         
         ImageView iv2 = new ImageView();
         Image rubbishImage = new Image("delete.png");
         iv2.setImage(rubbishImage);
         iv2.setFitHeight(18);
         iv2.setFitWidth(18);

//         Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);
//         iv.setViewport(viewportRect);
         iv.setPreserveRatio(true);
         iv2.setPreserveRatio(true);
		
//		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
//		saveView.setFitHeight(18);
//		saveView.setFitWidth(18);
//		saveView.setPreserveRatio(true);

		h.getChildren().addAll(iv, button,iv2, a);
		v.getChildren().add(h);
		v.getChildren().add(addGridPane());
		
		return v;
	}
	
	private GridPane addGridPane() {
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid

		grid.add(textAreaNote, 0, 1);

		return grid;
	}
	
	private void loadNoteBook(File file){
		System.out.println("func loadNoteBook");
		NoteBook nb = new NoteBook(file.getName());
		noteBook = nb;
		
		ArrayList<String> list = new ArrayList<String>();
              textAreaNote.setText("");
              for (Folder f : noteBook.getFolders()){
            	  if (f.getName().equals(currentFolder)){
            	  for (Note n : f.getNotes()){
            		 
            		  list.add(n.getTitle());
            	  }
            	  }
              }
              ObservableList<String> search_notes = FXCollections.observableArrayList(list);
    		  titleslistView.setItems(search_notes);
	}
	
	private boolean save(File file){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try{
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(noteBook);
			out.close();
			return true;
		} catch (Exception e){
			return false;
		}
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		
		
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
}