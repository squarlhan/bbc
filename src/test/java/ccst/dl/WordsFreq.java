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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsFreq {

	public void generatefreqbykey(String indir, String result, String keyword) throws IOException {
		Map<String, Integer> wordfreq = new HashMap();
		// get all input files
		List<File> files = new ArrayList();
		File root = new File(indir);
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
		for (File f : files){
			String fname = f.getName();
			InputStreamReader ir = new InputStreamReader(new FileInputStream(f));
			BufferedReader reader = new BufferedReader(ir);
			int count = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.trim().split(" ");
				for (String l : lines) {
					if (l.trim().equals(keyword)) {
						count++;
					}

				}
			}
			wordfreq.put(fname, count);
			ir.close();
			reader.close();
		}
		

		BufferedWriter bw = new BufferedWriter(new FileWriter(result));
		for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
			bw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			bw.flush();
		}
		
		bw.close();
	}
	
	public void getfreqbykey(String indir, String result, String keyword) throws IOException {
		Map<String, Integer> wordfreq = new HashMap();
		// get all input files
		List<File> files = new ArrayList();
		File root = new File(indir);
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
		for (File f : files){
			String fname = f.getName();
			
			int count = 0;
			for (File fs : f.listFiles()) {
				InputStreamReader ir = new InputStreamReader(new FileInputStream(fs));
				BufferedReader reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					String[] lines = line.trim().split(" ");
					for (String l : lines) {
//						if (l.trim().toLowerCase().equals(keyword)) {
						if (l.trim().toLowerCase().contains(keyword)) {
							count++;
						}

					}
				}
				ir.close();
				reader.close();
			}
			wordfreq.put(fname, count);
			
		}
		

		BufferedWriter bw = new BufferedWriter(new FileWriter(result+keyword+".txt"));
		for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
			bw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			bw.flush();
		}
		
		bw.close();
	}

	public void generatefullfreq(String inall, String result) throws IOException {
		Map<String, Integer> wordfreq = new HashMap();
		InputStreamReader ir = new InputStreamReader(new FileInputStream(inall));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] lines = line.trim().split(" ");
			for (String l : lines) {
				if (l.trim().length() > 0) {
					if (wordfreq.get(l.trim()) == null) {
						wordfreq.put(l.trim(), 1);
					} else {
						wordfreq.put(l.trim(), wordfreq.get(l.trim()) + 1);
					}
				}

			}
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(result));
		for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
			bw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			bw.flush();
		}
		ir.close();
		reader.close();
		bw.close();
	}

	public void generatematrix(String indir, String dicturl, String outurl) throws IOException {
		// construct mao
		Map<String, Integer> wordfreq = new HashMap();
		InputStreamReader ir = new InputStreamReader(new FileInputStream(dicturl));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			wordfreq.put(line, 0);
		}
		// get all input files
		List<File> files = new ArrayList();
		File root = new File(indir);
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
		// Generate out file
		BufferedWriter bw = new BufferedWriter(new FileWriter(outurl));
		// compute the words frequency of each file
		for (File f : files) {
			ir = new InputStreamReader(new FileInputStream(f));
			reader = new BufferedReader(ir);
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				String[] lines = line.trim().split(" ");
				for (String l : lines) {
					if (l.trim().length() > 0) {
						if (wordfreq.get(l.trim()) != null) {
							wordfreq.put(l.trim(), wordfreq.get(l.trim()) + 1);
						}
					}

				}
			}
			// write the frequency vector to file
			for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
				bw.write(entry.getValue() + ", ");
				// reset the map
				wordfreq.put(entry.getKey(), 0);
			}
			bw.write("\n");
			bw.flush();
		}
		ir.close();
		reader.close();
		bw.close();

	}

	public void generatematrixwithlabel(String indir, String trendurl, String dicturl, String outurl)
			throws IOException {
		// construct mao
		Map<String, Integer> wordfreq = new HashMap();
		InputStreamReader ir = new InputStreamReader(new FileInputStream(dicturl));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			wordfreq.put(line, 0);
		}
		Map<String, Integer> trends = new HashMap();
		ir = new InputStreamReader(new FileInputStream(trendurl));
		reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] lines = line.trim().split("\t");
			if (lines.length >= 2) {
				trends.put(lines[0].trim(), Integer.valueOf(lines[1].trim()));
			}
		}
		// Generate out file
		BufferedWriter bw = new BufferedWriter(new FileWriter(outurl));
		// compute the words frequency of each file
		for (Map.Entry<String, Integer> tentry : trends.entrySet()) {
			File f = new File(indir + tentry.getKey() + ".txt");
			if (f.exists()) {
				ir = new InputStreamReader(new FileInputStream(f));
				reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					String[] lines = line.trim().split(" ");
					for (String l : lines) {
						if (l.trim().length() > 0) {
							if (wordfreq.get(l.trim()) != null) {
								wordfreq.put(l.trim(), wordfreq.get(l.trim()) + 1);
							}
						}

					}
				}

				// write the frequency vector to file

				for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
					bw.write(entry.getValue() + ",");
					// reset the map
					wordfreq.put(entry.getKey(), 0);
				}
				bw.write(tentry.getValue() + "\n");
				bw.flush();
			}

		}
		ir.close();
		reader.close();
		bw.close();

	}

	public void generatematrixbylable(String indir, String trendurl, String dicturl, String outurl) throws IOException {
		// construct mao
		Map<String, Integer> wordfreq = new HashMap();
		InputStreamReader ir = new InputStreamReader(new FileInputStream(dicturl));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			wordfreq.put(line, 0);
		}
		List<String> dates = new ArrayList();
		ir = new InputStreamReader(new FileInputStream(trendurl));
		reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] lines = line.trim().split("\t");
			if (lines.length >= 2) {
				dates.add(lines[0].trim());
			}

		}
		// Generate out file
		BufferedWriter bw = new BufferedWriter(new FileWriter(outurl));
		// compute the words frequency of each file
		for (String d : dates) {
			File f = new File(indir + d + ".txt");
			if (f.exists()) {
				ir = new InputStreamReader(new FileInputStream(f));
				reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					String[] lines = line.trim().split(" ");
					for (String l : lines) {
						if (l.trim().length() > 0) {
							if (wordfreq.get(l.trim()) != null) {
								wordfreq.put(l.trim(), wordfreq.get(l.trim()) + 1);
							}
						}

					}
				}

				// write the frequency vector to file
				for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
					bw.write(entry.getValue() + ", ");
					// reset the map
					wordfreq.put(entry.getKey(), 0);
				}
				bw.write("\n");
				bw.flush();
			}

		}
		ir.close();
		reader.close();
		bw.close();

	}

	public void generatematrixbysvm(String indir, String trendurl, String dicturl, String outurl) throws IOException {
		// construct mao
		Map<String, Integer> wordfreq = new HashMap();
		InputStreamReader ir = new InputStreamReader(new FileInputStream(dicturl));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			wordfreq.put(line, 0);
		}
		Map<String, Integer> trends = new HashMap();
		ir = new InputStreamReader(new FileInputStream(trendurl));
		reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] lines = line.trim().split("\t");
			if (lines.length >= 2) {
				trends.put(lines[0].trim(), Integer.valueOf(lines[1].trim()));
			}
		}
		// Generate out file
		BufferedWriter bw = new BufferedWriter(new FileWriter(outurl));
		// compute the words frequency of each file
		for (Map.Entry<String, Integer> tentry : trends.entrySet()) {
			File f = new File(indir + tentry.getKey() + ".txt");
			if (f.exists()) {
				ir = new InputStreamReader(new FileInputStream(f));
				reader = new BufferedReader(ir);
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					String[] lines = line.trim().split(" ");
					for (String l : lines) {
						if (l.trim().length() > 0) {
							if (wordfreq.get(l.trim()) != null) {
								wordfreq.put(l.trim(), wordfreq.get(l.trim()) + 1);
							}
						}

					}
				}

				// write the frequency vector to file
				int i = 1;
				bw.write(tentry.getValue() + " ");
				System.out.println(tentry.getValue());
				for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {
					bw.write(i + ":" + entry.getValue() + " ");
					// reset the map
					wordfreq.put(entry.getKey(), 0);
					i++;
				}
				bw.write("\n");
				bw.flush();
			}

		}
		ir.close();
		reader.close();
		bw.close();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordsFreq wf = new WordsFreq();
		try {
			// wf.generatefullfreq("C:/Users/install/Desktop/hxs/bbc/bbcdata/result.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/allfq.txt");
			// wf.generatematrix("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/freqcsv.txt");
			// wf.generatematrixbylable("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/trend20140428.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/freq20140428.txt");
			// wf.generatematrixbysvm(
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/trend20160428.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt",
			// "C:/Users/install/Desktop/hxs/bbc/bbcdata/freqsvmtest.txt");
//			wf.generatematrixwithlabel("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
//					"C:/Users/install/Desktop/hxs/bbc/bbcdata/trend.txt",
//					"C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt",
//					"C:/Users/install/Desktop/hxs/bbc/bbcdata/freqs.txt");
//			 wf.generatefreqbykey("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
//			 "C:/Users/install/Desktop/hxs/bbc/bbcdata/tumor.txt", "cancer");
			 
//			String[] kws = {"ovarian", "bowel", "colon", "melanoma", "leukaemia", "pancreatic", "prostate", "breast"};
			String[] kws = {"neoplasm","carcinoma","tumor"};
			for(String k: kws){
				long st = System.currentTimeMillis();
				wf.getfreqbykey("C:/Users/install/Desktop/hxs/bbc/bbcdata/info/",
						 "C:/Users/install/Desktop/hxs/bbc/bbcdata/", k);
				long sp = System.currentTimeMillis();
				System.out.println("Proceessing time: "+ 1.0*(sp-st)/1000 + "S!");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
