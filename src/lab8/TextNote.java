package lab8;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.Serializable;

public class TextNote extends Note implements Serializable {
	public String content;
	
	public TextNote(String title){
		super(title);
	}
	
	public TextNote (File f){
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	public TextNote(String title, String content){
		super(title);
		this.content = content;
	}
	
	private String getTextFromFile(String absolutePath){
		String result = "";
		FileInputStream fis;
		try {
			fis = new FileInputStream(absolutePath);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			try {
				result = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public void exportTextToFile(String pathFolder){
		FileWriter fw = null;
		BufferedWriter bw = null;
		if (pathFolder.equals("")){
			pathFolder = ".";
		}
		File file = new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}