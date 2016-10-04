package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DoubleCheck {
	
	public void check(String freqaddr, String synaddr, String outaddr, String keyword, String year){
		Mesh mesh = new Mesh("c");
		mesh.initfromfile(synaddr);
		String id = mesh.getIndex().get(keyword);
		Set<String> syns = mesh.getEntry().get(id);
		List<String> res = new ArrayList(); 
		for(String s : syns){
			
			try {
				if(doesget(s)){
					res.addAll(countbykw(freqaddr, s, year));
				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}	
		writemaps(res, outaddr+keyword+".txt");
	}
	
	public boolean doesget(String s){
		Set<String> set = new HashSet<String>();
		set.add("ache");
		set.add("arc");
		set.add("bite");
		set.add("orf");
		set.add("pain");
		set.add("pus");
		set.add("stis");
		set.add("sti");
		set.add("sid");
		set.add("tic");
		if(set.contains(s))return false;
		return true;
	}
	
	public List<String> countbykw(String rootdir, String keyword, String year) throws IOException{
		List<String> wordfreq = new ArrayList();
		List<File> files = new ArrayList();
		File root = new File(rootdir);
		files.addAll(Arrays.asList(root.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (dir.isDirectory()) {
					return true;
				}
				name = dir.getName();
				return name.endsWith(".txt") || name.endsWith(".TXT");
			}
		})));

		for (File f : files) {
			String fname = f.getName().substring(0, 10);
			if(true){
//			if(fname.startsWith(year)){
			    int count = 0;
			    InputStreamReader ir = new InputStreamReader(new FileInputStream(f));
			    BufferedReader reader = new BufferedReader(ir);
			    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
//			    	keyword = keyword.replaceAll(" ", "");	
			    	count += StringUtils.countMatches(line, keyword);
			    	
			    }
			    ir.close();
			    reader.close();
			    if(count>0){
			    	wordfreq.add(fname+"	"+keyword+"	"+count);
			    }			    
			}
		}
		return wordfreq;
	}
	
	public void writemaps(List<String> map, String addr) {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(addr));
			for (String i : map) {
				bw.write(i + "\n");
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String freqaddr = "C:/Users/install/Desktop/hxs/bbc/bbcdata/mergeinfo/";
		String synaddr = "C:/Users/install/Desktop/hxs/bbc/MeSH/";
		String outaddr = "C:/Users/install/Desktop/hxs/bbc/Check/";
		String kw = "sclerosing panencephalitides subacute";
		String year = "2013";
		DoubleCheck c = new DoubleCheck();
		
		c.check(freqaddr, synaddr, outaddr, kw, year);
		
	}

}
