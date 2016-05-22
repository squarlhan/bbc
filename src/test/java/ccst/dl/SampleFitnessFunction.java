package ccst.dl;


import org.jgap.*;

public class SampleFitnessFunction
  extends FitnessFunction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String CVS_REVISION = "$Revision: 1.1 $";
    
    private double[][] trianset;
    private double[][] testset;

    public SampleFitnessFunction(double[][] trianset, double[][] testset) {
      this.trianset = trianset;
      this.testset = testset;
    }


    public double evaluate(IChromosome a_subject) {
      double fitness;
      
      Double g = (Double) a_subject.getGene(0).getAllele();
      Double c = (Double) a_subject.getGene(1).getAllele();
//      int c = (int) a_subject.getGene(1).getAllele();
      SVModel svm = new SVModel();
      double trac = svm.trian(trianset, g, c);
      double teac = svm.predict(testset);
      fitness = trac + 2*teac;
      return fitness;
    }
}


