package ccst.dl;

import java.io.FileNotFoundException;

import java.util.Collection;
import java.util.Arrays;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import com.github.fommil.netlib.BLAS;

public class TestCase {
	
    private static Logger log = LoggerFactory.getLogger(training.class);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println(BLAS.getInstance().getClass().getName());
		Document doc = new Document("add your text here! It can contain multiple sentences.");
		 for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
	            // We're only asking for words -- no need to load any models yet
	            System.out.println("The second word of the sentence '" + sent + "' is " + sent.word(1));
	            // When we ask for the lemma, it will load and run the part of speech tagger
	            System.out.println("The third lemma of the sentence '" + sent + "' is " + sent.lemmas());
	            // When we ask for the parse, it will load and run the parser
	            System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
	            // ...
	        }

		System.setProperty("treetagger.home", "C:/Users/install/Desktop/hxs/bbc/tt/");
		 TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		 try {
		     tt.setModel("C:/Users/install/Desktop/hxs/bbc/tt/lib/english-utf8.par:utf8");
		     tt.setHandler(new TokenHandler<String>() {
		         public void token(String token, String pos, String lemma) {
		             System.out.println(token+"\t"+pos+"\t"+lemma);
		         }
		     });
		     tt.process(Arrays.asList(new String[] {"This", "is", "a", "test", "."}));
		 }
		 finally {
		     tt.destroy();
		 }
		
		Word2Vec word2Vec = WordVectorSerializer.loadFullModel("C:/Users/install/Desktop/hxs/bbc/pathToSaveModel.txt");
//		String filePath = "C:/Users/install/Desktop/hxs/bbc/info/all.txt";
		/*
		 * PLEASE NOTE: after model is restored, it's still required to set
		 * SentenceIterator and TokenizerFactory, if you're going to train this
		 * model
		 */
//		SentenceIterator iterator = new BasicLineIterator(filePath);
//		TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
//		tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
//
//		word2Vec.setTokenizerFactory(tokenizerFactory);
//		word2Vec.setSentenceIter(iterator);
//
//		log.info("Word2vec uptraining...");
//
//		word2Vec.fit();
		Collection<String> lst = word2Vec.wordsNearest("protein", 5);
		log.info("Closest words to 'protein' on 2nd run: " + lst);
		
		lst = word2Vec.wordsNearest("gold", 5);
		log.info("Closest words to 'gold' on 2nd run: " + lst);
		
		lst = word2Vec.wordsNearest("petrol", 5);
		log.info("Closest words to 'petrol' on 2nd run: " + lst);
		
		lst = word2Vec.wordsNearest("gas", 5);
		log.info("Closest words to 'gas' on 2nd run: " + lst);
		
		lst = word2Vec.wordsNearest("nobel", 5);
		log.info("Closest words to 'nobel' on 2nd run: " + lst);
		
		lst = word2Vec.wordsNearest("jihua", 5);
		log.info("Closest words to 'jihua' on 2nd run: " + lst);

		/*
		 * Model can be saved for future use now
		 */

	}

}
