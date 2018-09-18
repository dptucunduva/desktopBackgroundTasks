package br.org.dpt.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import br.org.dpt.entity.Entry;

public class FileLoader {
	
	private static final String fileDataSource = System.getProperty("user.dir") + File.separator + "tasks.lst";
	public static final ArrayList<Entry> entries = new ArrayList<Entry>();
	
	private static Long lastRead = null; 
	
	public static boolean refresh() {
		Boolean changed = Boolean.FALSE;
		
		File tasksFile = new File (fileDataSource);
		if (lastRead == null || 
				(tasksFile.exists() && lastRead < tasksFile.lastModified())) {
			lastRead = tasksFile.lastModified();
			changed = Boolean.TRUE;
			
			entries.clear();
			try(BufferedReader br = new BufferedReader(new FileReader(tasksFile))) {
			    for(String line; (line = br.readLine()) != null; ) {
			    	if (!line.startsWith("#") && !line.trim().isEmpty()) {
			    		entries.add(new Entry(line));
			    	}
			    }
			} catch (Exception e) {
				//TODO
				e.printStackTrace();
			}
		}
		
		return changed;
	}
}
