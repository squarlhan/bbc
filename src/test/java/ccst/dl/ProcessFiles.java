/**
 * merge to one file day by day; lemmation and removing stop words also done.
 */
package ccst.dl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author install
 *
 */
public class ProcessFiles {
	
	private String rooturl;
	private String outurl;
	private String tempurl;
	private String stopurl;
	
	
	public ProcessFiles() {
		super();
	}

	public String getTempurl() {
		return tempurl;
	}

	public void setTempurl(String tempurl) {
		this.tempurl = tempurl;
	}


	public ProcessFiles(String rooturl, String outurl, String tempurl, String stopurl) {
		super();
		this.rooturl = rooturl;
		this.outurl = outurl;
		this.tempurl = tempurl;
		this.stopurl = stopurl;
	}

	public String getStopurl() {
		return stopurl;
	}

	public void setStopurl(String stopurl) {
		this.stopurl = stopurl;
	}

	public String getRooturl() {
		return rooturl;
	}



	public void setRooturl(String rooturl) {
		this.rooturl = rooturl;
	}



	public String getOuturl() {
		return outurl;
	}



	public void setOuturl(String outurl) {
		this.outurl = outurl;
	}

	//delete stop words and lemmation, and merge the files from same month
	public void process() throws IOException{
		PreLemmaStop pls = new PreLemmaStop();
		File root = new File(rooturl);
		File stopf = new File(stopurl);
		File[] sublists = root.listFiles();
		File tempdir = new File(tempurl);
		File outdir = new File(outurl);
		List<File> files;
		if(!tempdir.exists()){
			tempdir.mkdirs();
		}
		if(!outdir.exists()){
			outdir.mkdirs();
		}
		for(File f: sublists){
			//merge files
			files = new ArrayList();
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
			String outname = f.getName();
			File ftemp = new File(tempdir, outname+".txt");
			File fout = new File(outdir, outname+".txt");
			FileOutputStream out = new FileOutputStream(ftemp);
			for (File fs : files) {
				// 打开文件输入流
				FileInputStream in = new FileInputStream(fs);
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
			//preprocess file
			pls.preprocess(ftemp, stopf, fout);
		}
	}
	//delete stop words and lemmation
	public void processnomerge() throws IOException{
		PreLemmaStop pls = new PreLemmaStop();
		File root = new File(rooturl);
		File stopf = new File(stopurl);
		File[] sublists = root.listFiles();
		File tempdir = new File(tempurl);
		File outdir = new File(outurl);
		List<File> files;
		if(!tempdir.exists()){
			tempdir.mkdirs();
		}
		if(!outdir.exists()){
			outdir.mkdirs();
		}
		for(File f: sublists){
			//merge files
			files = new ArrayList();
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
			String outname = f.getName();
			File tempdatedir = new File(tempurl+outname+"/");
			File outdatedir = new File(outurl+outname+"/");
			if(!tempdatedir.exists()){
				tempdatedir.mkdirs();
			}
			if(!outdatedir.exists()){
				outdatedir.mkdirs();
			}
			for (File fs : files) {
				
				File ftemp = new File(tempdatedir, fs.getName()+".txt");
				File fout = new File(outdatedir, fs.getName()+".txt");
				FileOutputStream out = new FileOutputStream(ftemp);
				// 打开文件输入流
				FileInputStream in = new FileInputStream(fs);
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
				out.close();
				pls.preprocess(ftemp, stopf, fout);
			}
			
			//preprocess file
			
		}
	}
	
	public void mergerbymonth(String newsnum) throws IOException{
		File root = new File(rooturl);
		File[] sublists = root.listFiles();
		File outdir = new File(outurl);
		BufferedWriter bw = new BufferedWriter(new FileWriter(newsnum));
		List<File> files;
		if(!outdir.exists()){
			outdir.mkdirs();
		}
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
			
			String outname = f.getName();
			File fout = new File(outdir, outname+".txt");
			FileOutputStream out = new FileOutputStream(fout);
			
			bw.write(outname+"\t"+files.size()+"\n");
			bw.flush();
			
			for (File fs : files) {
				FileInputStream in = new FileInputStream(fs);
				int c;
				while ((c = in.read()) != -1) {
					if(!(c==(int)'\n')){
//					if(!(c==(int)' ')&&!(c==(int)'\n')){
						out.write(Character.toLowerCase(c));
					}					
				}
				in.close();
			}
			out.close();
			
		}
		bw.close();
	}
	
	public boolean isSignal(int i){
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


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ProcessFiles pf = new ProcessFiles();
		pf.setRooturl("C:/Users/install/Desktop/hxs/test/Beast/data/");
		pf.setOuturl("C:/Users/install/Desktop/hxs/test/Beast/mergedata/");
//		pf.setOuturl("C:/Users/install/Desktop/hxs/bbc/bbcdata/pfdateinfo/");
//		pf.setStopurl("C:/Users/install/Desktop/hxs/bbc/bbcdata/stopwords.txt");
//		pf.setTempurl("C:/Users/install/Desktop/hxs/bbc/bbcdata/tempdateinfo/");
		try {
//			pf.process();
//			pf.processnomerge();
			pf.mergerbymonth("C:/Users/install/Desktop/hxs/test/Beast/newsnum.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
