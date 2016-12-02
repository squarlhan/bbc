
package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MergeFiles {
	
	public static void main(String[] args) {
		
		MergeFiles mf = new MergeFiles();
		
		List<String> ins = new ArrayList();
		List<String> outs = new ArrayList();
		
		ins.add("C:/Users/install/Desktop/hxs/test/AljazeeraSpider/data/");
		ins.add("C:/Users/install/Desktop/hxs/test/Beast/data/");
		ins.add("C:/Users/install/Desktop/hxs/test/CNNSpider/data/");
		ins.add("C:/Users/install/Desktop/hxs/test/FOX/data/");
		ins.add("C:/Users/install/Desktop/hxs/test/Reuters/data/");
		ins.add("C:/Users/install/Desktop/hxs/test/Telegraph/data/");
		
		outs.add("C:/Users/install/Desktop/hxs/test/AljazeeraSpider/all.txt");
		outs.add("C:/Users/install/Desktop/hxs/test/Beast/all.txt");
		outs.add("C:/Users/install/Desktop/hxs/test/CNNSpider/all.txt");
		outs.add("C:/Users/install/Desktop/hxs/test/FOX/all.txt");
		outs.add("C:/Users/install/Desktop/hxs/test/Reuters/all.txt");
		outs.add("C:/Users/install/Desktop/hxs/test/Telegraph/all.txt");
		
		int ll = ins.size();
		
		for(int i = 0; i<=ll-1;i++){
			mf.mergeall(ins.get(i), outs.get(i));
		}
	}
	
	public void mergeall(String inaddr, String outaddr) {
		File root = new File(inaddr);
		File[] sublists = root.listFiles();
		List<File> files = new ArrayList();
		File result = new File(outaddr);
		System.out.println("Directory: "+sublists.length);
		long start = System.currentTimeMillis();
		for(File f: sublists){
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
		}
		long middle = System.currentTimeMillis();
		System.out.println("Files: "+files.size()+" Time:"+(middle-start)/1000+"s.");
		try {
			mergerbyday(files, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long stop = System.currentTimeMillis();
		System.out.println("Files: "+result.getFreeSpace()+" Time:"+(stop-middle)/1000+"s.");
	}
	
	public static void merge(List<File> files, File fout) throws Exception {
		// 构建文件输出流
		FileOutputStream out = new FileOutputStream(fout);
		for (File f : files) {
			// 打开文件输入流
			FileInputStream in = new FileInputStream(f);
			// 从输入流中读取数据，并写入到文件数出流中
			int c;
			int pre = (int)' ';
			while ((c = in.read()) != -1) {
				if(!isSignal(c)||(Character.isDigit(pre)&&((c==(int)','))||(c==(int)'.'))){
					out.write(Character.toLowerCase(c));
				}else{
					out.write((int)' ');
					out.write(Character.toLowerCase(c));
					out.write((int)' ');
				}
				pre = c;
			}
			in.close();
		}
		out.close();
	}
	
	public void mergerbyday(List<File> files, File fout) throws IOException{
		
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(fout));
			
			
			for (File fs : files) {
				InputStreamReader ir = new InputStreamReader(new FileInputStream(fs));
				BufferedReader reader = new BufferedReader(ir);
			    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			    	String myline = line.trim().toLowerCase();
			    	if(myline.startsWith("name")){
			    		bw2.write(myline+"\n");
			    	}else if(myline.startsWith("link")){
			    		;
			    	}else{
			    		if(myline.length()>1)bw2.write(myline);
			    	}
			    		
			    }
			    bw2.write("\n");
			    ir.close();
			    reader.close();
			}
			bw2.close();
			
	}
	
	public static boolean isSignal(int i){
		Set<Integer> sigset = new HashSet();
		sigset.add((int)',');
		sigset.add((int)'.');
		sigset.add((int)'?');
		sigset.add((int)':');
		sigset.add((int)';');
		sigset.add((int)'\'');
		sigset.add((int)'\"');
		sigset.add((int)'(');
		sigset.add((int)')');
		sigset.add((int)'[');
		sigset.add((int)']');
		sigset.add((int)'#');
		sigset.add((int)'@');
		sigset.add((int)'_');
		sigset.add((int)'&');
		sigset.add((int)'/');
		return sigset.contains(i);
	}

}
