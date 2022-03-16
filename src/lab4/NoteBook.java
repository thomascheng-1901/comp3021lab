package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements Serializable {
	
	private ArrayList<Folder> folders;
	private static final long serialVersionUID = 1L;
	
	public NoteBook(){
		folders = new ArrayList<Folder>();
	}
	
	public NoteBook(String file){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			NoteBook nb = (NoteBook) ois.readObject();
			this.folders = nb.folders;
			ois.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean createTextNote(String folderName, String title, String content){
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	public boolean createTextNote(String folderName, String title){
		TextNote tnote = new TextNote(title);
		return insertNote(folderName, tnote);
	}
	
	public boolean createImageNote(String folderName, String title){
		ImageNote inote = new ImageNote(title);
		return insertNote(folderName, inote);
	}
	
	public ArrayList<Folder> getFolders(){
		return folders;
	}
	
	public boolean insertNote(String foldername, Note note){
		Folder f = null;
		for (Folder f1 : folders){
			if (f1.getName().equals(foldername)){
				f = f1;
			}
		}
		if (f == null){
			f = new Folder(foldername);
			folders.add(f);
		}
//		f = null;
//		for (Folder f1 : folders){
//			if (f1.getName().equals(foldername)){
//				f = f1;
//				break;
//			}
//		}
		
		for (Note n1 : f.getNotes()){
			if (n1.equals(note)){
				System.out.println("Creating note " + note.getTitle() + " under folder " + foldername + " failed");
				return false;
			}
		}
		f.addNote(note);
		return true;
	}
	
	public void sortFolders(){
		for (Folder f : folders){
			f.sortNotes();
		}
		Collections.sort(folders);
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> return_list = new ArrayList<>();
		for (Folder f : folders){
			return_list.addAll(f.searchNotes(keywords));
		}
		return return_list;
	}
	
	public boolean save(String file){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try{
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
			return true;
		} catch (Exception e){
			return false;
		}
	}
}
