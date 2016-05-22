package ccst.dl;
/**
 * lemmation and revoming stop words of a whole file
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class PreLemmaStop {

	private static Logger log = LoggerFactory.getLogger(training.class);

	public List<String> lemmaline(String line) {
		List<String> res = new ArrayList();
		Document doc = new Document(line);
		for (Sentence sent : doc.sentences()) {
			res.addAll(sent.lemmas());
		}
		return res;
	}

	public List<String> rmstop(List<String> input, Set<String> stops) {
		List<String> res = new ArrayList();
		for (String s : input) {
			if (!stops.contains(s.trim())) {
				res.add(s);
			}
		}
		return res;
	}
	
	public void preprocess(File txt, File stopf, File resultf) throws IOException {
		Set<String> stops = new HashSet();
		
		if(resultf.exists()){
			resultf.delete();
		}else{
			resultf.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultf));  

		InputStreamReader ir = new InputStreamReader(new FileInputStream(stopf));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	        line = line.trim();
	        stops.add(line);
	        // do something here
	    }
		
		
		ir = new InputStreamReader(new FileInputStream(txt));
		reader = new BufferedReader(ir);
		
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	        line = line.trim();
	        List<String> res = lemmaline(line);
	        List<String> linelist = rmstop(res, stops);
	        for(String s : linelist){
	        	bw.write(s+" ");
	        }
	        bw.write("\n");
	        bw.flush();
	    }
		
		bw.close();
		ir.close();
		reader.close();
	}

	public void preprocess(String txturl, String stopurl, String resulturl) throws IOException {
		File txt = new File(txturl);
		File stopf = new File(stopurl);
		File resultf = new File(resulturl);
		Set<String> stops = new HashSet();
		
		if(resultf.exists()){
			resultf.delete();
		}else{
			resultf.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultf));  

		InputStreamReader ir = new InputStreamReader(new FileInputStream(stopurl));
		BufferedReader reader = new BufferedReader(ir);
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	        line = line.trim();
	        stops.add(line);
	        // do something here
	    }
		
		
		ir = new InputStreamReader(new FileInputStream(txt));
		reader = new BufferedReader(ir);
		
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
	        line = line.trim().toLowerCase();
	        List<String> res = lemmaline(line);
	        List<String> linelist = rmstop(res, stops);
	        for(String s : linelist){
	        	bw.write(s+" ");
	        }
	        bw.write("\n");
	        bw.flush();
	    }
		
		bw.close();
		ir.close();
		reader.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String txturl = "C:/Users/install/Desktop/hxs/bbc/bbcdata/all.txt";
		String stopurl = "C:/Users/install/Desktop/hxs/bbc/bbcdata/stopwords.txt";
		String resulturl = "C:/Users/install/Desktop/hxs/bbc/bbcdata/result.txt";

		PreLemmaStop pls = new PreLemmaStop();
		pls.preprocess(txturl, stopurl, resulturl);

	}

}
