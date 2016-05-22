package ccst.dl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.canova.api.records.reader.RecordReader;
import org.canova.api.records.reader.impl.CSVRecordReader;
import org.canova.api.split.FileSplit;
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RBM;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Gibson
 */
public class CSVDBNExample {

    private static Logger log = LoggerFactory.getLogger(CSVDBNExample.class);

    public static void main(String[] args) throws  Exception {

    	long start = System.currentTimeMillis();
        //First: get the dataset using the record reader. CSVRecordReader handles loading/parsing
        int numLinesToSkip = 0;
        String delimiter = ",";
      String filepre = "C:/Users/install/Desktop/hxs/bbc/bbcdata/";
      String filePath = filepre+"freqs.txt";
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
        recordReader.initialize(new FileSplit(new File(filePath)));

        //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
        int labelIndex = 10000;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
        int numClasses = 2;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2
        int batchSize = 1500;    //Iris data set: 150 examples total. We are loading all of them into one DataSet (not recommended for large data sets)
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchSize,labelIndex,numClasses);


        DataSet next = iterator.next();

        final int numInputs = 10000;
        int outputNum = 2;
        int iterations = 1000;
        long seed = 23;


        log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iterations)
                .learningRate(1e-6f)
                .optimizationAlgo(OptimizationAlgorithm.LBFGS)
                .l1(1e-1).regularization(true).l2(2e-4)		
                .useDropConnect(true)	
                .list(6)
                .layer(0, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)		
                        .nIn(numInputs).nOut(5000) // # fully connected hidden layer nodes. Add list if multiple layers.		
                        .weightInit(WeightInit.XAVIER) // Weight initialization		
                        .k(1) // # contrastive divergence iterations		
                        .activation("relu") // Activation function type		
                        .lossFunction(LossFunctions.LossFunction.RMSE_XENT) // Loss function type		
                        .updater(Updater.ADAGRAD)		
                        .dropOut(0.5)		
                        .build()) // NN layer type		
                .layer(1, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)		
                        .nIn(5000).nOut(1000) // # fully connected hidden layer nodes. Add list if multiple layers.		
                        .weightInit(WeightInit.XAVIER) // Weight initialization		
                        .k(1) // # contrastive divergence iterations		
                        .activation("relu") // Activation function type		
                        .lossFunction(LossFunctions.LossFunction.RMSE_XENT) // Loss function type		
                        .updater(Updater.ADAGRAD)		
                        .dropOut(0.5)		
                        .build()) // NN layer type	
                .layer(2, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)		
                        .nIn(1000).nOut(500) // # fully connected hidden layer nodes. Add list if multiple layers.		
                        .weightInit(WeightInit.XAVIER) // Weight initialization		
                        .k(1) // # contrastive divergence iterations		
                        .activation("relu") // Activation function type		
                        .lossFunction(LossFunctions.LossFunction.RMSE_XENT) // Loss function type		
                        .updater(Updater.ADAGRAD)		
                        .dropOut(0.5)		
                        .build()) // NN layer type	
                .layer(3, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)		
                        .nIn(500).nOut(100) // # fully connected hidden layer nodes. Add list if multiple layers.		
                        .weightInit(WeightInit.XAVIER) // Weight initialization		
                        .k(1) // # contrastive divergence iterations		
                        .activation("relu") // Activation function type		
                        .lossFunction(LossFunctions.LossFunction.RMSE_XENT) // Loss function type		
                        .updater(Updater.ADAGRAD)		
                        .dropOut(0.5)		
                        .build()) // NN layer type	
                .layer(4, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)		
                        .nIn(100).nOut(20) // # fully connected hidden layer nodes. Add list if multiple layers.		
                        .weightInit(WeightInit.XAVIER) // Weight initialization		
                        .k(1) // # contrastive divergence iterations		
                        .activation("relu") // Activation function type		
                        .lossFunction(LossFunctions.LossFunction.RMSE_XENT) // Loss function type		
                        .updater(Updater.ADAGRAD)		
                        .dropOut(0.5)		
                        .build()) // NN layer type	
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .weightInit(WeightInit.XAVIER)
                        .activation("softmax")
                        .nIn(20).nOut(outputNum).build())
                .backprop(true).pretrain(false)
                .build();

        //run the model
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        //Normalize the full data set. Our DataSet 'next' contains the full 150 examples
        next.normalizeZeroMeanZeroUnitVariance();
        next.shuffle();
        //split test and train
        SplitTestAndTrain testAndTrain = next.splitTestAndTrain(0.75);  //Use 65% of data for training

        DataSet trainingData = testAndTrain.getTrain();
        model.fit(trainingData);

        //evaluate the model on the test set
        Evaluation eval = new Evaluation(outputNum);
        DataSet test = testAndTrain.getTest();
        INDArray output = model.output(trainingData.getFeatureMatrix());
        eval.eval(trainingData.getLabels(), output);
        log.info(eval.stats());
        output = model.output(test.getFeatureMatrix());
        eval.eval(test.getLabels(), output);
        log.info(eval.stats());
        long stop = System.currentTimeMillis();
        
        log.info("Running time: "+ (stop-start)/1000+"s.");
        
        log.info("****************Example finished********************");		
		
        String modelpath = filepre+"dbnmodel.mod";
        String confpath = filepre+"dbnconf.json";
        OutputStream fos = new FileOutputStream(modelpath);		
        DataOutputStream dos = new DataOutputStream(fos);		
        Nd4j.write(model.params(), dos);		
        dos.flush();		
        dos.close();		
        FileUtils.writeStringToFile(new File(confpath), model.getLayerWiseConfigurations().toJson());		
   		
//        MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File(confpath)));		
//        DataInputStream dis = new DataInputStream(new FileInputStream(modelpath));		
//        INDArray newParams = Nd4j.read(dis);		
//        dis.close();		
//        MultiLayerNetwork savedNetwork = new MultiLayerNetwork(confFromJson);		
//        savedNetwork.init();		
//        savedNetwork.setParams(newParams);		
//        System.out.println("Original network params " + model.params());		
//        System.out.println(savedNetwork.params());	
    }

}
