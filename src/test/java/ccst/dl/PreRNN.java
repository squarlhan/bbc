package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreRNN {

	private Long startday;
	private Long endday;
	private Long oneday;
	private double[][] disfrqs;
	private double[][] normalfrqs;
//	private HashMap<Integer, Integer> monthdays;

	public PreRNN(int yst, int ysp, int mst, int msp, int dst, int dsp) {
		super();
		Calendar start = Calendar.getInstance();  
	    start.set(yst, mst-1, dst);  
	    startday = start.getTimeInMillis();  
	  
	    Calendar end = Calendar.getInstance();  
	    end.set(ysp, msp-1, dsp);  
	    endday = end.getTimeInMillis();  
	    
	    oneday = 1000 * 60 * 60 * 24l; 
//		monthdays = new HashMap<Integer, Integer>();
//		monthdays.put(1, 31);
//		monthdays.put(2, 28);
//		monthdays.put(3, 31);
//		monthdays.put(4, 30);
//		monthdays.put(5, 31);
//		monthdays.put(6, 30);
//		monthdays.put(7, 31);
//		monthdays.put(8, 31);
//		monthdays.put(9, 30);
//		monthdays.put(10, 31);
//		monthdays.put(11, 30);
//		monthdays.put(12, 31);
	}

	public double[][] getNormalfrqs() {
		return normalfrqs;
	}

	public void setNormalfrqs(double[][] normalfrqs) {
		this.normalfrqs = normalfrqs;
	}

	public double[][] getDisfrqs() {
		return disfrqs;
	}

	public void setDisfrqs(double[][] disfrqs) {
		this.disfrqs = disfrqs;
	}
	

	public void dir2monthly(String topfile, String dailydir, String monthlydir) {
		File meshfile = new File(topfile);
		List<String> tops = new ArrayList<String>();
		File od = new File(monthlydir) ;
		if(!od.exists()){
			od.mkdirs();
		}
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String l = line.trim();
				if (l.length() > 0) {
					tops.add(l);
					newsnum_daily2monthly(dailydir + l + ".txt", monthlydir + l + ".txt");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String num_daily2monthly(String dailyaddr, String monthlyaddr) {

		String res = "";
		HashMap<String, Integer> newsmap = new HashMap<String, Integer>();
		try {
			File meshfile = new File(dailyaddr);
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			BufferedWriter bw = new BufferedWriter(new FileWriter(monthlyaddr));

			
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {

				if (line.trim().length() > 0) {
					String[] lines = line.trim().split("\t");
					String key = lines[0].trim();
					int num = Integer.parseInt(lines[1].trim());
					newsmap.put(key, num);
				}
			}

			int former = Integer.MAX_VALUE;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
			for(long t = startday; t<= endday; t+= oneday){
			    Date d = new Date(t);  
		        String key = df.format(d);
		        Integer freq = newsmap.get(key);
		        if(freq == null){
		        	res+="N";
		        }else if(former == Integer.MAX_VALUE){
		        	res+="N";
		        	former = freq;
		        }else if(freq>former){
		        	res+="U";
		        	former = freq;
		        }else if(freq<former){
		        	res+="D";
		        	former = freq;
		        }else{
		        	res+="M";
		        	former = freq;
		        }
			}

			bw.write(df.format(new Date(startday))+"\t"+df.format(new Date(endday))+"\n");
			bw.write(res+"\n");
			
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
		return res;

	}

	public void newsnum_daily2monthly(String dailyaddr, String monthlyaddr) {

		HashMap<String, Integer> newsmap = new HashMap<String, Integer>();
		try {
			File meshfile = new File(dailyaddr);
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			BufferedWriter bw = new BufferedWriter(new FileWriter(monthlyaddr));

			
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {

				if (line.trim().length() > 0) {
					String[] lines = line.trim().split("\t");
					String key = lines[0].trim();
					int num = Integer.parseInt(lines[1].trim());
					newsmap.put(key, num);
				}
			}

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
			for(long t = startday; t<= endday; t+= oneday){
			    Date d = new Date(t);  
		        String key = df.format(d);
		        Integer freq = newsmap.get(key);
		        if(freq == null){
		        	bw.write(key+"\t"+"-1"+"\n");
		        	bw.flush();
		        }else{
		        	bw.write(key+"\t"+freq+"\n");
		        	bw.flush();
		        }
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

	public HashMap<String, Integer> readmontlynums(String monthlyaddr) {
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

//		Normalize normal = new Normalize(2010, 2016, 4, 4);
		PreRNN normal = new PreRNN(2010, 2016, 4, 4, 27, 28);
		// normal.newsnum_daily2monthly("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsnum.txt",
		// "C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		// normal.readmontlynums("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		 normal.dir2monthly("C:/Users/install/Desktop/hxs/test/BBC/tops/top111.txt",
		 "C:/Users/install/Desktop/hxs/test/BBC/allfrq/",
		 "C:/Users/install/Desktop/hxs/test/BBC/rnn/");
//		normal.getmatrixfromfils("C:/Users/install/Desktop/hxs/bbc/MeSH/top120.txt",
//				"C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqswithspace/");
//		normal.normal("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
	}

}
