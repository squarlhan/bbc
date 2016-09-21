package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class DataAnalysis {

	public static List<Set<String>> getfile(String addr){
		File file = new File(addr);
		List<Set<String>> result = new ArrayList();
		try {
			InputStreamReader insr = new InputStreamReader(new FileInputStream(file), "gb2312");
			BufferedReader br = new BufferedReader(insr);
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.trim().length() >= 1) 
				{				 
					String[] lines = line.split(",");
					Set<String> myset = new HashSet();
					for(String str:lines){
						if(str.trim().length()>1)myset.add(str);
					}
					result.add(myset);
					//System.out.println("myset:"+myset.size());
				}
			}
			br.close();
			insr.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Gene:"+result.size());
		return result;	
	}
	
	 /** Çó½»¼¯ */  
    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {  
        Set<T> setIntersection;  
        T item;  
  
        if (setA instanceof TreeSet)  
            setIntersection = new TreeSet<T>();  
        else  
            setIntersection = new HashSet<T>();  
  
        Iterator<T> iterA = setA.iterator();  
        while (iterA.hasNext()) {  
            item = iterA.next();  
            if (setB.contains(item))  
                setIntersection.add(item);  
        }  
        return setIntersection;  
    }  
    
    public static int setfile(List<Set<String>> result, String addr){
    	int count = 0;
		
		try {
			File outer = new File(addr);
			if (outer.exists()) {
				outer.delete();
				if (outer.createNewFile()) {
					System.out.println("result file create success!");
				} else {
					System.out.println("result file create failed!");
				}
			} else {
				if (outer.createNewFile()) {
					System.out.println("result file create success!");
				} else {
					System.out.println("result file create failed!");
				}

			}

			BufferedWriter output = new BufferedWriter(new FileWriter(outer));
			for(int i=0;i<=result.size()-1;i++){
				for(Iterator it = result.get(i).iterator();it.hasNext();){
					String ele = (String)it.next();
					output.write(ele+"\t");
				}
				count += result.get(i).size();
				output.write("\n");
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return count;
    }
    
    public static void similarity(String[] addrs){
    	
        NumberFormat format = new DecimalFormat("#0.00");
		List mylist = new ArrayList();
		for(String add: addrs){
			mylist.add(getfile(add));
		}
		
		String mydata = "";
		String fydata = "";
		
		for (int i = 0; i<= mylist.size()-1; i++){
			List<Set<String>> A = (List<Set<String>>)mylist.get(i);
			if(i>0){
				for(int a = 0; a<= i-1; a++){
					int ft = 0;
					List<Set<String>> B = (List<Set<String>>)mylist.get(a);
					List<Set<String>> res = new ArrayList();
					for(Set<String> Aele : A){
						int intersize = 0;
						Set<String> finalset = new HashSet();
						for(Set<String> Bele : B){
							Set<String> tempset = intersection(Aele, Bele);
							if(tempset.size()>intersize){
								intersize = tempset.size();
								finalset = tempset;
							}							
						}
						res.add(finalset);
						ft = ft+Aele.size()-intersize;
					}
					int totel = setfile(res, i+"VS"+a+".txt");
					System.out.println(i+" VS "+a+" : "+totel+" ; "+ft);
					mydata = mydata + String.valueOf(Double.valueOf(format.format((double)totel*100/2421)))+"%"+"\t";
					fydata = fydata + String.valueOf(Double.valueOf(format.format((double)ft*100/2421)))+"%"+"\t";
				}
			}
			mydata = mydata+"-"+"\t";
			fydata = fydata+"-"+"\t";
			if(i!=mylist.size()-1){
				for(int a = i+1; a<= mylist.size()-1; a++){
					int ft = 0;
					List<Set<String>> B = (List<Set<String>>)mylist.get(a);
					List<Set<String>> res = new ArrayList();
					for(Set<String> Aele : A){
						int intersize = 0;
						Set<String> finalset = new HashSet();
						for(Set<String> Bele : B){
							Set<String> tempset = intersection(Aele, Bele);
							if(tempset.size()>intersize){
								intersize = tempset.size();
								finalset = tempset;
							}
						}
						res.add(finalset);
						ft = ft+Aele.size()-intersize;
					}
					int totel = setfile(res, i+"VS"+a+".txt");
					System.out.println(i+" VS "+a+" : "+totel+" ; "+ft);
					mydata = mydata + String.valueOf(Double.valueOf(format.format((double)totel*100/2421)))+"%"+"\t";
					fydata = fydata + String.valueOf(Double.valueOf(format.format((double)ft*100/2421)))+"%"+"\t";
				}
			}
			mydata = mydata+"\n";
			fydata = fydata+"\n";
		}
		
		System.out.println(mydata);
		System.out.println("*******************************************");
		System.out.println(fydata);
    }
    
public static void similarity2(String[] addrs){
    	
        NumberFormat format = new DecimalFormat("#0.00");
		List mylist = new ArrayList();
		for(String add: addrs){
			mylist.add(getfile(add));
		}
		
		String mydata = "";
		String fydata = "";
		
		for (int i = 0; i<= mylist.size()-1; i++){
			List<Set<String>> A = (List<Set<String>>)mylist.get(i);
			if(i>0){
				for(int a = 0; a<= i-1; a++){
					double ft = 0;
					double tt = 0;
					List<Set<String>> B = (List<Set<String>>)mylist.get(a);
					List<Set<String>> res = new ArrayList();
					for(Set<String> Aele : A){
						int intersize = 0;
						Set<String> finalset = new HashSet();
						int Blen = 0;
						for(Set<String> Bele : B){
							Set<String> tempset = intersection(Aele, Bele);
							if(tempset.size()>intersize){
								intersize = tempset.size();
								finalset = tempset;
								Blen = Bele.size();
							}							
						}
						res.add(finalset);
						tt = tt+(double)intersize*2/(Blen+Aele.size());
						ft = ft+(double)(Blen+Aele.size()-2*intersize)/(Blen+Aele.size());
					}
					int totel = setfile(res, i+"VS"+a+".txt");
					System.out.println(i+" VS "+a+" : "+tt+" ; "+ft);
					mydata = mydata + String.valueOf(Double.valueOf(format.format(tt*100/A.size())))+"%"+"\t";
					fydata = fydata + String.valueOf(Double.valueOf(format.format(ft*100/A.size())))+"%"+"\t";
				}
			}
			mydata = mydata+"-"+"\t";
			fydata = fydata+"-"+"\t";
			if(i!=mylist.size()-1){
				for(int a = i+1; a<= mylist.size()-1; a++){
					double ft = 0;
					double tt = 0;
					List<Set<String>> B = (List<Set<String>>)mylist.get(a);
					List<Set<String>> res = new ArrayList();
					for(Set<String> Aele : A){
						int intersize = 0;
						Set<String> finalset = new HashSet();
						int Blen = 0;
						for(Set<String> Bele : B){
							Set<String> tempset = intersection(Aele, Bele);
							if(tempset.size()>intersize){
								intersize = tempset.size();
								finalset = tempset;
								Blen = Bele.size();
							}
						}
						res.add(finalset);
						tt = tt+(double)intersize*2/(Blen+Aele.size());
						ft = ft+(double)(Blen+Aele.size()-2*intersize)/(Blen+Aele.size());
					}
					int totel = setfile(res, i+"VS"+a+".txt");
					System.out.println(i+" VS "+a+" : "+tt+" ; "+ft);
					mydata = mydata + String.valueOf(Double.valueOf(format.format(tt*100/A.size())))+"%"+"\t";
					fydata = fydata + String.valueOf(Double.valueOf(format.format(ft*100/A.size())))+"%"+"\t";
				}
			}
			mydata = mydata+"\n";
			fydata = fydata+"\n";
		}
		
		System.out.println(mydata);
		System.out.println("*******************************************");
		System.out.println(fydata);
    }
	
	public static void main(String[] args){
				
//		String[] addrs = {"95.74.txt",
//				          "95.124 (2).txt",
//				          "95.124.txt",
//				          "95.397.txt",
//				          "95.713.txt",
//				          "95.829 (2).txt",
//				          "95.829.txt",
//				          "95.885.txt",
//				          "96.025.txt",
//				          "96.182.txt",
//				          "96.206.txt",
//				          "96.376.txt",
//				          "96.385.txt",
//				          "96.497.txt",
//				          "96.503.txt",
//				          "96.584.txt",
//				          "96.703.txt",
//				          "96.978.txt",};	
//		
		String[] addrs = new String[8];
		for(int i = 1; i<=8;i++){
			addrs[i-1] = "E:\\supercoli\\exe1\\rs\\"+String.valueOf(i)+".tab";
		}
		similarity2(addrs);
	}
}
