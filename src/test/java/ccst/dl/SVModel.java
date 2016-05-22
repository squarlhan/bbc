package ccst.dl;

import java.io.IOException;
import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVModel {
	
    private svm_model model;
    
	
	public svm_model getModel() {
		return model;
	}

	public void setModel(svm_model model) {
		this.model = model;
	}
	
	

	public SVModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void getDefaultModel(){
		double[][] results = generatetestdata();
		int lenth = results[0].length;
		trian(results, 1.0/(lenth-1.0), 50);
	}

	public void loadmodel(String svmurl){
		try {
			model = svm.svm_load_model(svmurl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double predict(double[] data){
		double pre_y = 0.0;
		int lenth = data.length;
		svm_node[] datanode = new svm_node[lenth];		
		for(int i=1;i<=lenth;i++){
			datanode[i-1] = new svm_node();
			datanode[i-1].index = i;
			datanode[i-1].value = data[i-1];			
		}
		pre_y = svm.svm_predict(model,datanode);
//		data[lenth-1] = pre_y;
		return pre_y;
	}
	
	public double predict(double[][] data){
		int h = data.length;
		int lenth = data[0].length;
		int count = 0;
		double[] ys = new double[h];
		for(int i = 0; i<= h-1;i++){			
			ys[i] = predict(data[i]);
			count = ys[i]==data[i][lenth-1]?count+1:count;
		}
		System.out.println(count+" / "+h+" = "+(double)count/h);
		return (double)count/h;
		
	}
	
	public double trian(double[][] results, double gamma, double c){
		svm_problem prob_train = getdata(results);
		svm_parameter param = initsvm(gamma, c);
		model = svm.svm_train(prob_train,param);
		return predict(results);
	}
	
	public void computemodel(double[][] results, String svmurl, double gamma, double c){
		trian(results,  gamma,  c);
		try {
			svm.svm_save_model(svmurl, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public svm_parameter initsvm(double gamma, double c){
		
		svm_parameter param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.NU_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = gamma;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = c;
		param.eps = 0.1;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		return param;
	}
    
    public double[][] scale(double lower, double upper, double[][] input){
    	int m = input.length;
    	int n = input[0].length;
    	double[] min = new double[n-1];
    	double[] max = new double[n-1];
    	double[][] output = new double[m][n];
    	for(int j = 0;j<=n-2;j++){
    		min[j] = Double.MAX_VALUE;
    		max[j] = -Double.MAX_VALUE;
    		for(int i = 0; i<=m-1; i++){
    			min[j] = Math.min(min[j],input[i][j]);
    			max[j] = Math.max(max[j],input[i][j]);
    			output[i][n-1] = input[i][n-1]==0?lower:upper;
//    			output[i][n-1] = input[i][n-1];
    		}
    	}
    	for(int j = 0;j<=n-2;j++){
    		for(int i = 0; i<=m-1; i++){
    			output[i][j] = ((upper-lower)*(input[i][j]-min[j])/(max[j]-min[j]))+lower;
    		}
    	}
    	return output;
    }
	
	public svm_problem getdata(double[][] results){
		svm_problem prob = new svm_problem();
		prob.l = results.length;
		double[] y = new double[prob.l];
		int lenth = results[0].length;
		svm_node[][] x = new svm_node[prob.l][lenth-1];
		for(int i=0;i<=prob.l-1;i++){
			for(int j = 1;j<=lenth-1;j++){
				x[i][j-1] = new svm_node();
				x[i][j-1].index = j;
				x[i][j-1].value = results[i][j-1];
			}
			y[i] = results[i][lenth-1];
		}
		prob.x = x;
		prob.y = y;
		return prob;
	}
	
	public double[][] generatetestdata(){
		
		double[][] temp_arr = {
				{0.05, 7.0, 0.1, 0.08574},
				{0.05, 7.0, 0.2, 0.05789},
				{0.05, 7.0, 0.3, 0.05002},
				{0.05, 7.0, 0.4, 0.06347},
				{0.05, 7.0, 0.5, 0.06603},
				{0.05, 7.0, 0.6, 0.07771},
//				{0.05, 7.0, 0.7, 0.082},
				{0.05, 7.0, 0.8, 0.13},
				{0.05, 7.0, 0.9, 0.17},
				{0.05, 5.0, 0.7, 0.079},
				{0.05, 5.5, 0.7, 0.069},
				{0.05, 6.0, 0.7, 0.064},
				{0.05, 6.5, 0.7, 0.0608},
				{0.05, 7.0, 0.7, 0.06649},
				{0.05, 7.5, 0.7, 0.1514},
				{0.05, 8.0, 0.7, 0.1682},
//				{0.05, 6.5, 0.7, 0.06675},
				{0.10, 6.5, 0.7, 0.0517},
				{0.20, 6.5, 0.7, 0.082},
				{0.30, 6.5, 0.7, 0.2445},
				{0.40, 6.5, 0.7, 0.3275},
				{0.50, 6.5, 0.7, 0.468}};
		
		return temp_arr;
	}

}
