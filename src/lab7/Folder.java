package lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.Serializable;

public class Folder implements Comparable<Folder>, Serializable {
	private ArrayList<Note> notes;
	private String name;

	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public String getName() {
		return name;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

	public String toString() {
		int nText = 0;
		int nImage = 0;

		for (Note n1 : notes) {
			if (n1 instanceof TextNote) {
				nText += 1;
			} else if (n1 instanceof ImageNote) {
				nImage += 1;
			}
		}
		return name + ":" + nText + ":" + nImage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Folder){
			if (((Folder) obj).getName().equals(this.getName())){
				return true;
			}
		}
		return false;
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Folder other = (Folder) obj;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		return true;
	}

	@Override
	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		return this.name.compareTo(o.name);
	}
	
	public void sortNotes(){
		Collections.sort(notes);
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> return_list = new ArrayList<>();
		
		String [] keys = keywords.split(" ");
		for (Note a : notes){
			boolean add = true;
			if (a instanceof ImageNote){
				for (int i = 0; i < keys.length; ++i){
					if (i+2<keys.length && (keys[i+1].equals("OR") || keys[i+1].equals("oR") || keys[i+1].equals("Or")
							|| keys[i+1].equals("or"))){
						if (a.getTitle().toLowerCase().contains(keys[i].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i].toUpperCase()) 
								|| a.getTitle().toLowerCase().contains(keys[i+2].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i+2].toUpperCase())){
							i += 2;
						} else {
							add = false;
							break;
						}
					} else {
						if (a.getTitle().toLowerCase().contains(keys[i].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i].toUpperCase())){
						
						} else {
							add = false;
							break;
						}
					}
				}
				if (add){
					return_list.add(a);
				}
			} else if (a instanceof TextNote) {
				for (int i = 0; i < keys.length; ++i){
					if (i+2<keys.length && (keys[i+1].equals("OR") || keys[i+1].equals("oR") || keys[i+1].equals("Or")
							|| keys[i+1].equals("or"))){
						if (a.getTitle().toLowerCase().contains(keys[i].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i].toUpperCase()) 
								|| a.getTitle().toLowerCase().contains(keys[i+2].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i+2].toUpperCase())
								|| ((TextNote) a).content.toLowerCase().contains(keys[i].toLowerCase()) || ((TextNote) a).content.toUpperCase().contains(keys[i].toUpperCase()) 
								|| ((TextNote) a).content.toLowerCase().contains(keys[i+2].toLowerCase()) || ((TextNote) a).content.toUpperCase().contains(keys[i+2].toUpperCase())){
							i += 2;
						} else {
							add = false;
							break;
						}
					} else {
						if (a.getTitle().toLowerCase().contains(keys[i].toLowerCase()) || a.getTitle().toUpperCase().contains(keys[i].toUpperCase()) 
								|| ((TextNote) a).content.toLowerCase().contains(keys[i].toLowerCase()) || ((TextNote) a).content.toUpperCase().contains(keys[i].toUpperCase()) ){
						
						} else {
							add = false;
							break;
						}
					}
				}
				if (add){
					return_list.add(a);
				}
			}
		}
		return return_list;
	}
}
