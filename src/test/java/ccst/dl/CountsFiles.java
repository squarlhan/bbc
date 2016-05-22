package ccst.dl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class CountsFiles {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File root = new File("C:/Users/install/Desktop/hxs/bbc/info");
		File[] sublists = root.listFiles();
		System.out.println("Directory: "+sublists.length);
		long start = System.currentTimeMillis();
		int count = 0;
		for(File f: sublists){
			count+=f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(dir.isDirectory()){
                        return true;
                    }
                    name = dir.getName();
                    return name.endsWith(".txt")|| name.endsWith(".TXT");
                }
            }).length;
		}		
		long middle = System.currentTimeMillis();
		System.out.println("Files: "+count+" Time:"+(middle-start)/1000+"s.");
		count = 0;
		for(File f: sublists){
			count+=f.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(dir.isDirectory()){
                        return true;
                    }
                    name = dir.getName();
                    return name.endsWith(".txt")|| name.endsWith(".TXT");
                }
            }).length;
		}
		long stop = System.currentTimeMillis();
		System.out.println("Files: "+count+" Time:"+(stop-middle)/1000+"s.");
		

	}

}
