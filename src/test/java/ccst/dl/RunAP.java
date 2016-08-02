package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccst.dl.ap.affinitymain.InteractionData;
import ccst.dl.ap.affinitymain.NewRunAlgorithm;
import ccst.dl.ap.algorithm.abs.AffinityPropagationAlgorithm.AffinityConnectingMethod;

public class RunAP {
	
	private HashMap<Integer, String> dicts;
	private Collection<InteractionData> dists;
	private double preferences;
	private double lambda;
	private int iterations;
	private Integer convits;
	
	

	public RunAP(String dictaddr, int num) {
		super();
		// TODO Auto-generated constructor stub
		dicts = new HashMap<Integer, String>();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(dictaddr));
			BufferedReader reader = new BufferedReader(ir);
			int i = 0;
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String l = line.trim();
				if (l.length() > 0) {
					dicts.put(i, l);
					i++;
					if(i>=num){
						break;
					}
				}
			}
			ir.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public double getPreferences() {
		return preferences;
	}



	public void setPreferences(double preferences) {
		this.preferences = preferences;
	}



	public double getLambda() {
		return lambda;
	}



	public void setLambda(double lambda) {
		this.lambda = lambda;
	}



	public int getIterations() {
		return iterations;
	}



	public void setIterations(int iterations) {
		this.iterations = iterations;
	}



	public Integer getConvits() {
		return convits;
	}



	public void setConvits(Integer convits) {
		this.convits = convits;
	}



	public Collection<InteractionData> getDists() {
		return dists;
	}



	public void setDists(Collection<InteractionData> dists) {
		this.dists = dists;
	}



	public HashMap<Integer, String> getDicts() {
		return dicts;
	}



	public void setDicts(HashMap<Integer, String> dicts) {
		this.dicts = dicts;
	}
	
	public void write2file(double[][] x, String addr) throws IOException{
		int r = x.length;
		int c = x[0].length;
		File f = new File(addr);
		if(f.exists()){
			f.delete();
			f.createNewFile();
		}else{
			f.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for(int i = 0; i<= r-1; i++){
			for(int j = 0; j<=c-1; j++){
				bw.write(String.valueOf(x[i][j])+'\t');
				bw.flush();
			}
			bw.write('\n');
			bw.flush();
		}
		bw.close();
	}
	
	public Collection<InteractionData> trans2apin(double[][] input, String addr) throws IOException{
		double[][] dis = EucDistance.calcEucMatrix(input);
		Collection<InteractionData> ints = new HashSet<InteractionData>();
//		int row = dis.length;
//		for(int i = 0; i<= row-1; i++){
//			for(int j = 0; j<= row-1; j++){
//				ints.add(new InteractionData(dicts.get(i), dicts.get(j), dis[i][j]));
//			}
//		}
		write2file(input, addr);
		ints = EucDistance.transEucMatrix(dis);
		this.dists = ints;
		this.preferences = dis[0][0];
		return ints;
	}
	
	public void run(){
		String kind = "clusters";
		AffinityConnectingMethod connMode = AffinityConnectingMethod.ORIGINAL;
		boolean takeLog = false;
		boolean refine = true;
		Integer steps = null;

		NewRunAlgorithm alg = new NewRunAlgorithm(dists, lambda, iterations,
				convits, preferences, kind);
		alg.setTakeLog(takeLog);
		alg.setConnMode(connMode);
		alg.setSteps(steps);
		alg.setRefine(refine);

		alg.setParemeters();
		List<Integer> results = alg.run();
		if(results==null||results.size()==0){
			System.err.println("Cluster Error, 0 result!");	
		}
		Set<Integer> centers = new HashSet<Integer>();
		centers.addAll(results);
		HashMap<String, Set<String>> rs= new HashMap<String, Set<String>>();
		for(int i = 0; i<= results.size()-1;i++){
			System.out.println(results.get(i));
			String c = dicts.get(results.get(i));
			if(rs.get(c)==null){
				Set<String> names = new HashSet<String>();
				names.add(dicts.get(i));
				rs.put(c, names);
			}else{
				Set<String> names = rs.get(c);
				names.add(dicts.get(i));
				rs.put(c, names);
			}
		}
		System.out.println(rs);
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num = 150;
		RunAP ra = new RunAP("C:/Users/install/Desktop/hxs/bbc/MeSH/top150cn.txt", num);
		Normalize normal = new Normalize(2010, 2016, 4, 4);
		normal.getmatrixfromfils("C:/Users/install/Desktop/hxs/bbc/MeSH/top150.txt", num,
				"C:/Users/install/Desktop/hxs/bbc/bbcdata/topfrqswithspace/");
		normal.normal("C:/Users/install/Desktop/hxs/bbc/bbcdata/newsmonth.txt");
		try {
			ra.trans2apin(normal.getNormalfrqs(), 
					"C:/Users/install/Desktop/hxs/bbc/MeSH/apin"+String.valueOf(num)+".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ra.setConvits(null);
		ra.setIterations(100);
		ra.setLambda(0.8);
		ra.run();

	}

}
