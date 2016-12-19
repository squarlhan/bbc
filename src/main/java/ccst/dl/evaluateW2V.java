package ccst.dl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Created by niless on 4/19/16.
 */
public class evaluateW2V {
    private static Logger log = LoggerFactory.getLogger(applyW2V.class);
    
    public HashMap<String, String> readmost(String addr){
    	HashMap<String, String> maps = new HashMap<String, String>();
    	File dsfile = new File(addr);

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(dsfile));
			BufferedReader reader = new BufferedReader(ir);

			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String[] lines = line.split("=");
				maps.put(lines[0].trim(), lines[1].trim());

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
    	return maps;
    }
    
    public void evaluatempas(HashMap<String, String> maps, String ourdir,Word2Vec wordVectors){
    	File od = new File(ourdir) ;
		if(!od.exists()){
			od.mkdirs();
		}
		for (Entry<String, String> en : maps.entrySet()){
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(ourdir+en.getKey()+".txt"));
				Collection<String> similar = wordVectors.wordsNearest(en.getValue(), 100);
		        log.info("Similar words to " + en.getValue() +" : " + similar);
		        for(String s:similar){
		        	double dis = wordVectors.similarity(en.getValue(), s);
		        	log.info("Similarity between "+en.getValue()+" and "+s+" is: " + dis);
		        	bw.write(en.getValue()+"\t"+s+"\t"+dis+"\n");
		        }
		        bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    }
    
    public void pipeline() throws FileNotFoundException{
    	List<String> mostlist = new ArrayList();
    	List<String> modlist = new ArrayList();
    	List<String> outlist = new ArrayList();
    	
    	modlist.add("./alj.mod");
    	outlist.add("./alj/");
    	mostlist.add("./alj.txt");
    	
    	modlist.add("./bbc.mod");
    	outlist.add("./bbc/");
    	mostlist.add("./bbc.txt");
    	
    	modlist.add("./beast.mod");
    	outlist.add("./beast/");
    	mostlist.add("./beast.txt");
    	
    	modlist.add("./cnn.mod");
    	outlist.add("./cnn/");
    	mostlist.add("./cnn.txt");
    	
    	modlist.add("./telegraph.mod");
    	outlist.add("./telegraph/");
    	mostlist.add("./telegraph.txt");
    	
    	modlist.add("./fox.mod");
    	outlist.add("./fox/");
    	mostlist.add("./fox.txt");
    	
    	modlist.add("./reuters.mod");
    	outlist.add("./reuters/");
    	mostlist.add("./reuters.txt");
    	
    	int lenth = modlist.size();
    	for(int i=0;i<=lenth-1;i++){
    		log.info("start to loading " + modlist.get(i)+" ...:");
    		HashMap<String, String> maps = readmost(mostlist.get(i));
    		Word2Vec wordVectors = WordVectorSerializer.loadFullModel(modlist.get(i));
    		evaluatempas(maps, outlist.get(i), wordVectors);
    	}
    }

    public static void main(String[] args) throws Exception {
    	
//    	 Word2Vec wordVectors = WordVectorSerializer.loadFullModel("C:/Users/install/Desktop/hxs/test/AljazeeraSpider/alj.mod");

//        WordVectors wordVectors = WordVectorSerializer.loadTxtVectors(new File("C:/Users/install/Desktop/hxs/bbc/bbcdata/wordembeddingodel.mod"));
//        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
       // double[] wordVector = wordVectors.getWordVector("myword");

//        log.info("Evaluate model...");
//        double sim = wordVectors.similarity("cancer", "tumor");
//        log.info("Similarity between cancer and disease" + sim);

//        String key = "infarction";
//        Collection<String> similar = wordVectors.wordsNearest(key, 50);
//        log.info("Similar words to " + key +" : " + similar);
//        for(String s:similar){
//        	log.info("Similarity between "+key+" and "+s+" is: " + wordVectors.similarity(key, s));
//        }
    	
    	evaluateW2V eva = new evaluateW2V();
    	eva.pipeline();
    }
}
