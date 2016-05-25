package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Normalize {
	
	private int yst;
	private int ysp;
	private int mst;
	private int msp;
	
	
	
	public Normalize(int yst, int ysp, int mst, int msp) {
		super();
		this.yst = yst;
		this.ysp = ysp;
		this.mst = mst;
		this.msp = msp;
	}

	public int getYst() {
		return yst;
	}

	public void setYst(int yst) {
		this.yst = yst;
	}

	public int getYsp() {
		return ysp;
	}

	public void setYsp(int ysp) {
		this.ysp = ysp;
	}

	public int getMst() {
		return mst;
	}

	public void setMst(int mst) {
		this.mst = mst;
	}

	public int getMsp() {
		return msp;
	}

	public void setMsp(int msp) {
		this.msp = msp;
	}
	
	public void dir2monthly(String topfile, String dailydir, String monthlydir){
		File meshfile = new File(topfile);
		List<String> tops = new ArrayList<String>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String l = line.trim();
				if (l.length() > 0) {
					tops.add(l);
					newsnum_daily2monthly(dailydir+l+".txt", monthlydir+l+".txt");
				}
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void newsnum_daily2monthly(String dailyaddr, String monthlyaddr){

		HashMap<String, Integer> newsmap = new HashMap<String, Integer>(); 
		try {
			File meshfile = new File(dailyaddr);
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			BufferedWriter bw = new BufferedWriter(new FileWriter(monthlyaddr));
			
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {

				if (line.trim().length() > 0) {
					String[] lines = line.trim().split("\t");
					String key = lines[0].trim().substring(0, 7);
					int num = Integer.parseInt(lines[1].trim());
					if (newsmap.get(key) == null) {
						newsmap.put(key, num);
					} else {
						newsmap.put(key, newsmap.get(key) + num);
					}

				}
			}
			
			for (Map.Entry<String, Integer> entry : newsmap.entrySet()) {
				bw.write(entry.getKey() + "\t" + entry.getValue() + "\n");
				bw.flush();
			}
			
			ir.close();
			reader.close();
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public HashMap<String, Integer> readmontlynums(String monthlyaddr){
		HashMap<String, Integer> newsmap = new HashMap<String, Integer>(); 
		try {
			File meshfile = new File(monthlyaddr);
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {

				if (line.trim().length() > 0) {
					String[] lines = line.trim().split("\t");
					String key = lines[0].trim().substring(0, 7);
					int num = Integer.parseInt(lines[1].trim());					
					newsmap.put(key, num);
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
		return newsmap;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Normalize normal = new Normalize(2010, 2016, 4, 4);
//		normal.newsnum_daily2monthly("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsnum.txt", 
//				"C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
//		normal.readmontlynums("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		normal.dir2monthly("C:/Users/install/Desktop/hxs/bbc/MeSH/top150.txt", 
				"C:/Users/install/Desktop/hxs/bbc/bbcdata/allfrqs/", 
				"C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqs/");
	}

}
