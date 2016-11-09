package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class FindTopDis {

	public List<String> readtops(String addr){
		List<String> res = new ArrayList();
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(addr));
			BufferedReader reader = new BufferedReader(ir);
		    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		    		res.add(line.trim());
		    	
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
		
		return res;
	}
	
	public Set<String> getids(Mesh mesh, List<String> res){
		Set<String> ids = new HashSet<String>();
		for(String s : res){
			ids.add(mesh.getIndex().get(s));
		}
		return ids;
	}
	
	public void writeselectedentries(Mesh mesh, Set<String> abcall, String addr) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(addr + "centry.map"));
			for(String ss : abcall){
				bw.write(ss + "=");
				String t = "";
				for (String s : mesh.getEntry().get(ss)) {
					t += s + "|";
				}
				t = t.substring(0, t.length() - 1);
				bw.write(t + "\n");
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
		FindTopDis ft = new FindTopDis();
		Mesh mesh = new Mesh("c");
		mesh.initfromfile("C:/Users/install/Desktop/hxs/bbc/MeSH/");
		String cnnaddr = "C:/Users/install/Desktop/hxs/bbc/Check/cnn.txt";
		String bbcaddr = "C:/Users/install/Desktop/hxs/bbc/Check/bbc.txt";
		String aljaddr = "C:/Users/install/Desktop/hxs/bbc/Check/alja.txt";
		String addr = "C:/Users/install/Desktop/hxs/bbc/Check/";
		List<String> cnnlist = ft.readtops(cnnaddr);
		List<String> bbclist = ft.readtops(bbcaddr);
		List<String> aljalist = ft.readtops(aljaddr);
		Set<String> cnnset = ft.getids(mesh, cnnlist);
		Set<String> bbcset = ft.getids(mesh, bbclist);
		Set<String> aljaset = ft.getids(mesh, aljalist);
		
//		Set<String> bc = cnnset;
//		bc.retainAll(bbcset);
//		System.out.println("BC:"+bc.size());
//		
//		Set<String> ba = bbcset;
//		bc.retainAll(aljaset);
//		System.out.println("BA:"+ba.size());
//		
//		Set<String> ac = aljaset;
//		bc.retainAll(cnnset);
//		System.out.println("AC:"+ac.size());
//		
//		Set<String> abc = aljaset;
//		abc.retainAll(cnnset);
//		abc.retainAll(bbcset);
//		System.out.println("ABC:"+abc.size());
		
		Set<String> abcall = aljaset;
		abcall.addAll(cnnset);
		abcall.addAll(bbcset);
		System.out.println("ABCALL:"+abcall.size());

		ft.writeselectedentries(mesh, abcall, addr);
	}

}
