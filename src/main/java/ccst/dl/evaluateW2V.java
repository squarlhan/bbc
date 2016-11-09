package ccst.dl;

import java.util.Collection;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
/**
 * Created by niless on 4/19/16.
 */
public class evaluateW2V {
    private static Logger log = LoggerFactory.getLogger(applyW2V.class);

    public static void main(String[] args) throws Exception {
    	
    	 Word2Vec wordVectors = WordVectorSerializer.loadFullModel("C:/Users/install/Desktop/hxs/bbc/bbcdata/wordembeddingodel.mod");

//        WordVectors wordVectors = WordVectorSerializer.loadTxtVectors(new File("C:/Users/install/Desktop/hxs/bbc/bbcdata/wordembeddingodel.mod"));
        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
       // double[] wordVector = wordVectors.getWordVector("myword");

        log.info("Evaluate model...");
//        double sim = wordVectors.similarity("cancer", "tumor");
//        log.info("Similarity between cancer and disease" + sim);
        Collection<String> similar = wordVectors.wordsNearest("h1n1", 10);
        log.info("Similar words to cancer " + similar);
    }
}
