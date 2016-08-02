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
	private double[][] disfrqs;
	private double[][] normalfrqs;

	public Normalize(int yst, int ysp, int mst, int msp) {
		super();
		this.yst = yst;
		this.ysp = ysp;
		this.mst = mst;
		this.msp = msp;
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

	public double[][] getmatrixfromfils(String topaddr, int num, String diraddr) {
		List<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(topaddr));
			BufferedReader reader = new BufferedReader(ir);
			int i = 0;
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String l = line.trim();
				if (l.length() > 0) {
					list.add(readmontlynums(diraddr + l + ".txt"));
					i++;
				}
				if(i>=num){
					break;
				}
			}
			ir.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ii = list.size();
		int jj = list.get(0).size();
		double[][] re = new double[ii][jj];
		for (int i = 0; i <= ii - 1; i++) {
			int j = 0;
			for (int y = yst; y <= ysp; y++) {
				for (int m = 1; m <= 12; m++) {
					if (!((y == yst && m < mst) || (y == ysp && m > msp))) {
						String sy = String.valueOf(y);
						String sm = String.valueOf(m);
						sm = sm.length() < 2 ? "-0" + sm : "-" + sm;
						re[i][j] = list.get(i).get(sy + sm);
						j++;
					}

				}

			}
		}
		this.disfrqs = re;
		return re;

	}
	
	public double[][] getmatrixfromfils(String topaddr, String diraddr) {
		List<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(topaddr));
			BufferedReader reader = new BufferedReader(ir);
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String l = line.trim();
				if (l.length() > 0) {
					list.add(readmontlynums(diraddr + l + ".txt"));
				}
			}
			ir.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ii = list.size();
		int jj = list.get(0).size();
		double[][] re = new double[ii][jj];
		for (int i = 0; i <= ii - 1; i++) {
			int j = 0;
			for (int y = yst; y <= ysp; y++) {
				for (int m = 1; m <= 12; m++) {
					if (!((y == yst && m < mst) || (y == ysp && m > msp))) {
						String sy = String.valueOf(y);
						String sm = String.valueOf(m);
						sm = sm.length() < 2 ? "-0" + sm : "-" + sm;
						re[i][j] = list.get(i).get(sy + sm);
						j++;
					}

				}

			}
		}
		this.disfrqs = re;
		return re;

	}
	
	public double[][] normal(String newsmonth){
		int ii = disfrqs.length;
    	int jj = disfrqs[0].length;
    	double[][] output = new double[ii][jj];
    	HashMap<String, Integer> numm = readmontlynums(newsmonth);
    	for (int i = 0; i <= ii - 1; i++) {
			int j = 0;
			for (int y = yst; y <= ysp; y++) {
				for (int m = 1; m <= 12; m++) {
					if (!((y == yst && m < mst) || (y == ysp && m > msp))) {
						String sy = String.valueOf(y);
						String sm = String.valueOf(m);
						sm = sm.length() < 2 ? "-0" + sm : "-" + sm;
						output[i][j] = disfrqs[i][j] / numm.get(sy + sm);
						j++;
					}
				}
			}
		}
    	this.normalfrqs = scalebyx(0,1,output);
    	return normalfrqs;
	}
	
	public double[][] scalebyx(double lower, double upper, double[][] input){
    	int m = input.length;
    	int n = input[0].length;
    	double[] min = new double[m];
    	double[] max = new double[m];
    	double[][] output = new double[m][n];
    	for(int i = 0;i<=m-1;i++){
    		min[i] = Double.MAX_VALUE;
    		max[i] = -Double.MAX_VALUE;
    		for(int j = 0;j<=n-1;j++){
    			min[i] = Math.min(min[i],input[i][j]);
    			max[i] = Math.max(max[i],input[i][j]);
    		}
    	}
    	for(int i = 0;i<=m-1;i++){
    		for(int j = 0;j<=n-1;j++){
    			output[i][j] = ((upper-lower)*(input[i][j]-min[i])/(max[i]-min[i]))+lower;
    		}
    	}
    	return output;
    }
	
    public double[][] scalebyy(double lower, double upper, double[][] input){
    	int m = input.length;
    	int n = input[0].length;
    	double[] min = new double[n];
    	double[] max = new double[n];
    	double[][] output = new double[m][n];
    	for(int j = 0;j<=n-1;j++){
    		min[j] = Double.MAX_VALUE;
    		max[j] = -Double.MAX_VALUE;
    		for(int i = 0; i<=m-1; i++){
    			min[j] = Math.min(min[j],input[i][j]);
    			max[j] = Math.max(max[j],input[i][j]);
    		}
    	}
    	for(int j = 0;j<=n-1;j++){
    		for(int i = 0; i<=m-1; i++){
    			output[i][j] = ((upper-lower)*(input[i][j]-min[j])/(max[j]-min[j]))+lower;
    		}
    	}
    	return output;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Normalize normal = new Normalize(2010, 2016, 4, 4);
		// normal.newsnum_daily2monthly("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsnum.txt",
		// "C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		// normal.readmontlynums("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		 normal.dir2monthly("C:/Users/install/Desktop/hxs/bbc/MeSH/top150.txt",
		 "C:/Users/install/Desktop/hxs/bbc/bbcdata/allfrqswithspace/",
		 "C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqswithspace/");
//		normal.getmatrixfromfils("C:/Users/install/Desktop/hxs/bbc/MeSH/top120.txt",
//				"C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqswithspace/");
//		normal.normal("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
	}

}
