package ccst.dl;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.models.embeddings.WeightLookupTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

/**
 * Created by efsun on 4/18/16.
 */
public class applyW2V {

    private static Logger log = LoggerFactory.getLogger(applyW2V.class);
    private static String outputPath = "/Users/efsun/Documents/GWU/Results/Word2Vec/";
    public static void main(String[] args) throws Exception {

        //File f = new File("/Users/efsun/Documents/GWU/Data/Writing/");

        //String filePath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();


       /* File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            processFile(files[i]); //new File("/Users/efsun/Documents/GWU/Data/Writing/file.txt")
        }*/

        Word2Vec wordVectors = processFile(new File("/Users/efsun/Documents/GWU/Data/writingAll.txt"));

        //log.info("Closest Words:");
        //Collection<String> lst = vec.wordsNearest("day", 10);
        //System.out.println(lst);

        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
       // Iterator<INDArray> vectors = weightLookupTable.vectors();
       // INDArray wordVector = wordVectors.getWordVectorMatrix("myword");
        double[] wordVector = wordVectors.getWordVector("myword");
     }
    private static Word2Vec processFile(File f) throws Exception
    {

        log.info("Load data...."+f.getName());
        SentenceIterator iter = new LineSentenceIterator(f);
        /*iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });*/
        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
       // SentenceIterator iter = new BasicLineIterator(f);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)//1
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");

        // Write word vectors
        WordVectorSerializer.writeWordVectors(vec, outputPath+f.getName());
        return vec;

    }
}


