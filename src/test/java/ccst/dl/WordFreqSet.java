package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class WordFreqSet {

	public void getfreq(String rootdir, String outdir, Set<Set<String>> keywords) throws IOException {
		
		File od = new File(outdir) ;
		if(!od.exists()){
			od.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outdir+"20160922tongji.txt"));
		for(Set<String> set : keywords){
			String name = (String) set.toArray()[0];
			int c = getfreqbyset(rootdir,  outdir+name+".txt", set);		
			bw.write(name+"\t"+c + "\n");
			bw.flush();
			System.out.println(name+"\t"+c);
		}
		bw.close();

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

	public int getfreqbyset(String rootdir, String result, Set<String> keywords) throws IOException {
		Map<String, Integer> wordfreq = new HashMap();
		// get all input files
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
		int c = 0;
		for (File f : files) {
			String fname = f.getName().substring(0, 10);

			int count = 0;
			InputStreamReader ir = new InputStreamReader(new FileInputStream(f));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				for (String s : keywords) {
					if(this.doesget(s)){
						count += StringUtils.countMatches(rmpunctuation(" "+line+" "), " "+s+" ");
					}
					
				}
			}
			ir.close();
			reader.close();
			wordfreq.put(fname, count);
			c+=count;

		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(result));
		for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
			bw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			bw.flush();
		}

		bw.close();
		return c;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordFreqSet wfs = new WordFreqSet();
//		String s = wfs.rmpunctuation("asdfashg.asfd,(as)df'asdfasdf?{sdaf]");
		Mesh mesh = new Mesh();
		Set<Set<String>> keywords = mesh.readds("C:/Users/install/Desktop/hxs/bbc/MeSH/centry.map");
		long st = System.currentTimeMillis();
		try {
			wfs.getfreq("C:/Users/install/Desktop/hxs/bbc/bbcdata/mergeinfo/",
					 "C:/Users/install/Desktop/hxs/bbc/bbcdata/allfrqswithspace/", keywords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long sp = System.currentTimeMillis();
		System.out.println("Proceessing time: "+ 1.0*(sp-st)/1000 + "S!");

	}

}
