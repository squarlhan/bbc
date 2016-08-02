package ccst.dl;

import java.io.File;

import com.jujutsu.tsne.FastTSne;
import com.jujutsu.utils.MatrixOps;
import com.jujutsu.utils.MatrixUtils;
import com.jujutsu.tsne.TSne;

public class TSneTest {
  public static void main(String [] args) {
    int initial_dims = 55;
    double perplexity = 20.0;
    double [][] X = MatrixUtils.simpleRead2DMatrix(new File("src/test/resources/datasets/mnist2500_X.txt"), ",");
    System.out.println(MatrixOps.doubleArrayToPrintString(X, ", ", 50,10));
    TSne tsne = new FastTSne();
    double [][] Y = tsne.tsne(X, 2, initial_dims, perplexity);   

    // Plot Y or save Y to file and plot with some other tool such as for instance R

  }
}