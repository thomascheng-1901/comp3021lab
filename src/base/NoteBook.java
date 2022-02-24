package base;

import java.util.ArrayList;

public class NoteBook {
	
	private ArrayList<Folder> folders;
	
	public NoteBook(){
		folders = new ArrayList<Folder>();
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
}
