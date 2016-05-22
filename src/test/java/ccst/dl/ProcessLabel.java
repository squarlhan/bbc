package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessLabel {
	
	public double[][] train_x;
	public double[] train_y;
	public double[][] train_xy;
	
	public double[][] test_x;
	public double[] test_y;
	public double[][] test_xy;
	
	public void generatelables(String inurl, String outurl)throws IOException{
		Double now = 0.0;
		int trend = 1;
		String today = null;
		InputStreamReader ir = new InputStreamReader(new FileInputStream(inurl));
		BufferedReader reader = new BufferedReader(ir);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outurl));  
		String firstline = reader.readLine();
		String[] firstlines = firstline.trim().split("\t");
		if(firstlines.length>=2){
			today = firstlines[0].trim();
			now = Double.valueOf(firstlines[1].trim());

		}
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			String[] lines = line.trim().split("\t");
			if(lines.length>=2){
				
				Double tom = Double.valueOf(lines[1].trim());
				trend = tom >= now ? 1 : 0;
				bw.write(today + "\t" + trend + "\n");
				bw.flush();
				today = lines[0].trim();
				now = tom;
			}
		}

		ir.close();
		reader.close();
		bw.close();
	}
	
	public void generatematrixbysvm4test(String indir, String trendurl, String dicturl) throws IOException{
		//construct mao
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
			if(lines.length>=2){
				trends.put(lines[0].trim(), Integer.valueOf(lines[1].trim()));
			}
		}
		List<double[]> mydata = new ArrayList();
		List<double[]> mydata1 = new ArrayList();
		List<Double> ys = new ArrayList();
		//Generate out file

		//compute the words frequency of each file
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
				
				//write the frequency vector to file
				double[] temp = new double[wordfreq.size()];
				double[] temp1 = new double[wordfreq.size()+1];
				int j = 0;
				for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {  
					temp[j] = entry.getValue();  
					temp1[j] = entry.getValue();  
					//reset the map
					wordfreq.put(entry.getKey(), 0);
					j++;
				}
				ys.add(tentry.getValue()*1.0);
				temp1[j] = tentry.getValue();
				mydata.add(temp);
				mydata1.add(temp1);
			}else{
				System.out.println("No News for this day: "+tentry.getKey());
			}
			
		}
		int h = ys.size();
		this.test_y = new double[h];
		this.test_x = new double[h][wordfreq.size()];
		this.test_xy = new double[h][wordfreq.size()+1];
		for(int i = 0; i<= h-1;i++){
			this.test_y[i] = ys.get(i);
			this.test_x[i] = mydata.get(i);
			this.test_xy[i] = mydata1.get(i);
		}
		
		ir.close();
		reader.close();
//	    return (double[][]) mydata.toArray();
		
	}
	
	public void generatematrixbysvm(String indir, String trendurl, String dicturl) throws IOException{
		//construct mao
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
			if(lines.length>=2){
				trends.put(lines[0].trim(), Integer.valueOf(lines[1].trim()));
			}
		}
		List<double[]> mydata = new ArrayList();
		List<double[]> mydata1 = new ArrayList();
		List<Double> ys = new ArrayList();
		//Generate out file

		//compute the words frequency of each file
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
				
				//write the frequency vector to file
				double[] temp = new double[wordfreq.size()];
				double[] temp1 = new double[wordfreq.size()+1];
				int j = 0;
				for (Map.Entry<String, Integer> entry : wordfreq.entrySet()) {  
					temp[j] = entry.getValue();  
					temp1[j] = entry.getValue();  
					//reset the map
					wordfreq.put(entry.getKey(), 0);
					j++;
				}
				ys.add(tentry.getValue()*1.0);
				temp1[j] = tentry.getValue();
				mydata.add(temp);
				mydata1.add(temp1);
			}else{
				System.out.println("No News for this day: "+tentry.getKey());
			}
			
		}
		int h = ys.size();
		this.train_y = new double[h];
		this.train_x = new double[h][wordfreq.size()];
		this.train_xy = new double[h][wordfreq.size()+1];
		for(int i = 0; i<= h-1;i++){
			this.train_y[i] = ys.get(i);
			this.train_x[i] = mydata.get(i);
			this.train_xy[i] = mydata1.get(i);
		}
		
		ir.close();
		reader.close();
//	    return (double[][]) mydata.toArray();
		
	}
	
	public void gettrainsetfromfile(String x, String y) throws IOException{
		InputStreamReader xi = new InputStreamReader(new FileInputStream(x));
		BufferedReader xr = new BufferedReader(xi);
		InputStreamReader yi = new InputStreamReader(new FileInputStream(y));
		BufferedReader yr = new BufferedReader(yi);
		
		List<Double> ys = new ArrayList();
		for (String line = yr.readLine(); line != null; line = yr.readLine()) {
			if(line.trim().length()>=1){
				ys.add(Double.valueOf(line.trim()));
			}
		}
		int h = ys.size();
		this.train_y = new double[h];
		for(int i = 0; i<= h-1;i++){
			this.train_y[i] = ys.get(i);
		}
		int i = 0;
		for (String line = xr.readLine(); line != null; line = xr.readLine()) {
			line = line.trim();
			if(line.length()>1){
				String[] lines = line.split(",");
				int ii = lines.length;
				double[] temp;
				int l = 0;
				if(lines[ii-1].trim().length()>=1){
					l = ii;
				}else{
					l = ii-1;
				}
				this.train_x = new double[h][l];
				this.train_xy = new double[h][l+1];
				for(int j = 0; j<=l-1;j++){
					this.train_x[i][j] = Double.valueOf(lines[j].trim());
					this.train_xy[i][j] = Double.valueOf(lines[j].trim());
				}
				this.train_xy[i][l-1] = this.train_y[i];
				i++;
			}
			
		}
		xi.close();
		xr.close();
		yi.close();
		yr.close();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProcessLabel pl = new ProcessLabel();
		SVModel svm = new SVModel();
		try {
//			pl.generatelables("C:/Users/install/Desktop/hxs/bbc/bbcdata/price20140428.txt", "C:/Users/install/Desktop/hxs/bbc/bbcdata/trend20140428.txt");
			 pl.generatematrixbysvm("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
					"C:/Users/install/Desktop/hxs/bbc/bbcdata/trend20140428.txt",
					"C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt");
			 pl.generatematrixbysvm4test("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfinfo/",
						"C:/Users/install/Desktop/hxs/bbc/bbcdata/trend20160428.txt",
						"C:/Users/install/Desktop/hxs/bbc/bbcdata/dict10000.txt");
//			pl.gettrainsetfromfile("C:/Users/install/Desktop/hxs/bbc/bbcdata/freq20140428.txt", "C:/Users/install/Desktop/hxs/bbc/bbcdata/y20140428.txt");
			double[][]  tainset = pl.train_xy;
		    double[][]  testset =pl.test_xy;
//			tainset = svm.scale(-1, 1, pl.train_xy);
//			testset = svm.scale(-1, 1, pl.test_xy);
			long middle = System.currentTimeMillis();
			svm.computemodel(tainset,"C:/Users/install/Desktop/hxs/bbc/bbcdata/svmodel1.mod", 0.01, 0.1);
//			svm.loadmodel("C:/Users/install/Desktop/hxs/bbc/bbcdata/freqsvm.txt.model");
			svm.predict(testset);
			long stop = System.currentTimeMillis();
			System.out.println(" Time:"+(stop-middle)/1000+"s.");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
