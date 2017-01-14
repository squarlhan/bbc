package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class COSArticle {
	
	private Integer[][] matrix;
	private HashMap<String, Integer> idindex;
	private HashMap<String, Set<String>> mesh_items;
	
	

	public COSArticle(String mesh_addr) {
		super();
		Mesh mesh = new Mesh("c");
		mesh.initentryfile(mesh_addr);
		mesh_items = mesh.getEntry();
		idindex = new HashMap<String, Integer>();
		int i = 0;
		for (Entry<String, Set<String>> e : mesh_items.entrySet()) {
			idindex.put(e.getKey(), i);
			i++;
		}
		matrix = new Integer[i][i];
		for(int j = 0;j<=i-1;j++){
			for(int k = 0;k<=i-1;k++){
				matrix[j][k] = 0;
			}
		}
	}



	public COSArticle() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Integer[][] getMatrix() {
		return matrix;
	}



	public void setMatrix(Integer[][] matrix) {
		this.matrix = matrix;
	}



	public HashMap<String, Integer> getIdindex() {
		return idindex;
	}



	public void setIdindex(HashMap<String, Integer> idindex) {
		this.idindex = idindex;
	}



	public HashMap<String, Set<String>> getMesh_items() {
		return mesh_items;
	}



	public void setMesh_items(HashMap<String, Set<String>> mesh_items) {
		this.mesh_items = mesh_items;
	}
	
	public boolean doesget(String s){
		Set<String> set = new HashSet<String>();
		set.add("ache");
		set.add("arc");
		set.add("bite");
//		set.add("orf");
		set.add("pain");
//		set.add("pus");
//		set.add("stis");
		set.add("sti");
		set.add("sid");
		set.add("tic");
		if(set.contains(s))return false;
		return true;
	}
	
	public String rmpunctuation(String str){
		str = str.replaceAll("[.,\"\\?!:';\\(\\)]", " ");
		return str;
	}

	public void descartes(List<Set<Integer>> dimvalue, List<List<Integer>> result, int layer, List<Integer> curstring)
    {
        //大于一个集合时：
        if (layer < dimvalue.size() - 1)
        {
            //大于一个集合时，第一个集合为空
            if (dimvalue.get(layer).size() == 0)
            	descartes(dimvalue, result, layer + 1, curstring);
            else
            {
                for (Integer str: dimvalue.get(layer))
                {
                	List<Integer> s1 = new ArrayList<Integer>();
                    if(curstring!=null){
                    	s1.addAll(curstring);
                    }
                    s1.add(str);
                    descartes(dimvalue, result, layer + 1, s1);
                }
            }
        }
        //只有一个集合时：
        else if (layer == dimvalue.size() - 1)
        {
            //只有一个集合，且集合中没有元素
            if (dimvalue.get(layer).size() == 0)
                result.add(curstring);
            //只有一个集合，且集合中有元素时：其笛卡尔积就是这个集合元素本身
            else
            {
                for (Integer str: dimvalue.get(layer))
                {
                	List<Integer> s1 = new ArrayList<Integer>();
                	s1.addAll(curstring);
                	s1.add(str);
                    result.add(s1);
                }
            }
        }
        else{
        	;
        }
    }

	public void count_co_occurrency(String rooturl) throws IOException{
		File root = new File(rooturl);
		File[] sublists = root.listFiles();
		List<File> files;

		for(File f: sublists){
			//merge files
			files = new ArrayList<File>();
			files.addAll(Arrays.asList(f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(dir.isDirectory()){
                        return true;
                    }
                    name = dir.getName();
                    return name.endsWith(".txt")|| name.endsWith(".TXT");
                }
            })));
			
			
			
			for (File fs : files) {
				Set<Integer> indexes = new HashSet<Integer>();
				InputStreamReader ir = new InputStreamReader(new FileInputStream(fs));
				BufferedReader reader = new BufferedReader(ir);
			    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			    	String myline = line.trim().toLowerCase();
			    	for (Entry<String, Set<String>> e : mesh_items.entrySet()) {
			    		String id = e.getKey();
			    		Set<String> keywords = e.getValue();
			    		for (String s : keywords) {
							if(this.doesget(s)&&rmpunctuation(" "+myline+" ").indexOf(" "+s+" ")>=0){
								indexes.add(idindex.get(id));
								break;
							}
							
						}
					}
			    		
			    }
			    
			    List<Set<Integer>> dimvalue = new ArrayList<Set<Integer>>();
			    List<List<Integer>> result = new ArrayList<List<Integer>>();
			    dimvalue.add(indexes);
			    dimvalue.add(indexes);
			    descartes(dimvalue, result, 0, null);
			    
			    if(result.size()>0&&result.get(0)!=null){
			    	for(List<Integer> pos : result){
			    		matrix[pos.get(0)][pos.get(1)]++;
			    	}
			    }
			    
			    ir.close();
			    reader.close();
			}
			
		}
	}
	
	public void writematrix(String maddr, String iaddr){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(maddr));
			int s = matrix.length;
			for(int i = 0;i<=s-1;i++){
				for(int j = 0;j<=s-1;j++){
					bw.write(matrix[i][j]+"\t");
				}
				bw.write("\n");
				bw.flush();
			}
			
			BufferedWriter bwi = new BufferedWriter(new FileWriter(iaddr));
			for(Entry<String,Integer> e : idindex.entrySet()){
				bwi.write(e.getKey()+"\t"+e.getValue()+"\n");
				bwi.flush();
			}
			
			bw.close();
			bwi.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test_Descartes(){
		List<Set<Integer>> dimvalue = new ArrayList<Set<Integer>>();
        Set<Integer> v1 = new HashSet<Integer>();
        v1.add(1);
        v1.add(2);

         
        dimvalue.add(v1);
        dimvalue.add(v1);
         
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        
        
        descartes(dimvalue, result, 0, null);
         
        int i = 1;
        for (List<Integer> s : result)
        {
            System.out.println(i++ + ":" +s.get(0)+"\t"+s.get(1));
        }
	}
	
	public void pipeline(){
		
		List<String> root_dir_name = new ArrayList<String>();
		
		root_dir_name.add("AljazeeraSpider");
		root_dir_name.add("BBC");
//		root_dir_name.add("Beast");
		root_dir_name.add("CNNSpider");
		root_dir_name.add("FOX");
		root_dir_name.add("Telegraph");
		root_dir_name.add("Reuters");
		
		int s = root_dir_name.size();
		for(int i = 0; i<=s-1;i++){
			String mesh_addr = "C:/Users/install/Desktop/hxs/test/"+root_dir_name.get(i)+"/centry.map";
			String rooturl = "C:/Users/install/Desktop/hxs/test/"+root_dir_name.get(i)+"/data/";
			String maddr = "C:/Users/install/Desktop/hxs/test/"+root_dir_name.get(i)+"/matrix.txt";
			String iaddr = "C:/Users/install/Desktop/hxs/test/"+root_dir_name.get(i)+"/indexid.txt";
			COSArticle ca = new COSArticle(mesh_addr);
//			ca.test_Descartes();
			try {
				System.out.println(new Date()+" : "+ root_dir_name.get(i)+"START!");
				ca.count_co_occurrency(rooturl);
				ca.writematrix(maddr, iaddr);
				System.out.println(new Date()+" : "+ root_dir_name.get(i)+"DONE!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	public static void main(String[] args) {
		
		COSArticle ca = new COSArticle();
		ca.pipeline();
		// TODO Auto-generated method stub
//		String mesh_addr = "C:/Users/install/Desktop/hxs/test/Beast/centry.map";
//		String rooturl = "C:/Users/install/Desktop/hxs/test/Beast/data/";
//		String maddr = "C:/Users/install/Desktop/hxs/test/Beast/matrix.txt";
//		String iaddr = "C:/Users/install/Desktop/hxs/test/Beast/indexid.txt";
//		COSArticle ca = new COSArticle(mesh_addr);
////		ca.test_Descartes();
//		try {
//			ca.count_co_occurrency(rooturl);
//			ca.writematrix(maddr, iaddr);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
    }

}
