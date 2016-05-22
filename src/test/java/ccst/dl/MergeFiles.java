
package ccst.dl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MergeFiles {
	
	public static void main(String[] args) {
		File root = new File("C:/Users/install/Desktop/hxs/bbc/info");
		File[] sublists = root.listFiles();
		List<File> files = new ArrayList();
		File result = new File("C:/Users/install/Desktop/hxs/bbc/info/all.txt");
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
			merge(files, result);
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
