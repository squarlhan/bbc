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
import java.util.HashMap;
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
//		set.add("orf");
		set.add("pain");
//		set.add("pus");
//		set.add("stis");
		set.add("sti");
		set.add("sid");
		set.add("tic");
		if(set.contains(s))return false;
		return true;
	}
	
	public String rmpunctuation(String str){
		str = str.replaceAll("[.,\"\\?!:';\\(\\)]", " ");
		return str;
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
			    	count += StringUtils.countMatches(rmpunctuation(" "+line+" "), " "+keyword+" ");
			    	
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
	
	public HashMap<String, Integer> readtongji(String addr){
		HashMap<String, Integer> map  =  new HashMap<String, Integer>();

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
		    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
//		    	keyword = keyword.replaceAll(" ", "");	
		    	String[] lines = line.split("\t");
		    	if(lines.length==2){
		    		String name = lines[0];
		    		int freq = Integer.parseInt(lines[1]);
		    		map.put(name, freq);
		    	}
		    	
		    }
		    ir.close();
		    reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return map;
	}
	
	public List<String> checkbalance(String alldir, String topdir, String topaddr, String tongjiaddr){
		List<String> list = new ArrayList();
		
		try {
			List<String> names = new ArrayList();
			InputStreamReader ir = new InputStreamReader(new FileInputStream(topaddr));
			BufferedReader reader = new BufferedReader(ir);
		    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
//		    	keyword = keyword.replaceAll(" ", "");	
		    	names.add(line.trim());		    	
		    }
		    HashMap<String, Integer> map = this.readtongji(tongjiaddr);
		    
		    for(String n : names){
		    	String dayaddr = alldir+n+".txt";
		    	String monaddr = topdir+n+".txt";
		    	int daycount = 0;
		    	int moncount = 0;
		    	
		    	ir = new InputStreamReader(new FileInputStream(dayaddr));
				reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {	
					String[] lines = line.split("\t");
			    	if(lines.length==2){
			    		int freq = Integer.parseInt(lines[1]);
			    		daycount+=freq;
			    	}			    	
			    }
				
				ir = new InputStreamReader(new FileInputStream(monaddr));
				reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {	
					String[] lines = line.split("\t");
			    	if(lines.length==2){
			    		int freq = Integer.parseInt(lines[1]);
			    		moncount+=freq;
			    	}			    	
			    }
				
				if(map.get(n)!=daycount||map.get(n)!=moncount){
					String l = n+"\t"+map.get(n)+"\t"+daycount+"\t"+moncount;
					list.add(l);
					System.out.println(l);
				}
		    }
		    
		    ir.close();
		    reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String freqaddr = "C:/Users/install/Desktop/hxs/bbc/bbcdata/mergeinfo/";
		String synaddr = "C:/Users/install/Desktop/hxs/bbc/MeSH/";
		String outaddr = "C:/Users/install/Desktop/hxs/bbc/Check/";
		String alldir = "C:/Users/install/Desktop/hxs/bbc/bbcdata/allfrqs/";
		String topdir = "C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqs/";
		String topaddr = "C:/Users/install/Desktop/hxs/bbc/MeSH/20160922tops.txt";
		String tongjiaddr = "C:/Users/install/Desktop/hxs/bbc/bbcdata/allfrqs/20160922tongji.txt";
		String kw = "avian flu";
		String year = "2013";
		DoubleCheck c = new DoubleCheck();
		
		c.check(freqaddr, synaddr, outaddr, kw, year);
//		c.checkbalance(alldir, topdir, topaddr, tongjiaddr);
	}

}
