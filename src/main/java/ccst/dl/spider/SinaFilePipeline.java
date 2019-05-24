package ccst.dl.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class SinaFilePipeline extends FilePersistentBase implements Pipeline {

	public static int count = 0;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * create a FilePipeline with default path"/data/webmagic/"
	 */
	public SinaFilePipeline() {
		setPath("F:/Downloads/weibo/");
	}

	public SinaFilePipeline(String path) {
		setPath(path);
	}

	public boolean isSignal(int i){
		Set<Integer> sigset = new HashSet();
		sigset.add((int)'|');
		sigset.add((int)'*');
		sigset.add((int)'?');
		sigset.add((int)':');
		sigset.add((int)'\'');
		sigset.add((int)'\"');
		sigset.add((int)'/');
		sigset.add((int)'<');
		sigset.add((int)'>');
		return sigset.contains(i);
	}
	
	public String removechar(String s) {

		String r = "";
		for(char c : s.toCharArray()){
			if(!isSignal(c)){
				r+=c;
			}
		}
		return r;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		try {
//			String ds = resultItems.get("date").toString().trim();
//			long milsecs = 0;
//			try{
//			    milsecs = Long.parseLong(ds);
//			}catch(Exception  exception) {
//		   		// handle passing in the wrong type of URL.
//				System.err.println("url:\t" + resultItems.getRequest().getUrl());
//		   	}
//			Date d = new Date(milsecs*1000);
//			SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd  HH:mm:ss");
//			String date = ft.format(d);
			
			PrintWriter printWriter = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(getFile(path + "tufa.txt")), "UTF-8"));

			List<String> outer1 = new ArrayList<String>();
			List<String> outer2 = new ArrayList<String>();
			
			if (resultItems.get("content") instanceof Iterable) {
				Iterable value = (Iterable) resultItems.get("content");
				Iterable dates = (Iterable) resultItems.get("date");
				for (Object o : dates) {
					if (o.toString().trim().length() >= 1) {
						outer1.add(o.toString().trim());
					}
				}
				for (Object o : value) {
					if (o.toString().trim().length() >= 1) {
						outer2.add(o.toString().trim());
					}
				}
			} else {
				printWriter.println(resultItems.get("date").toString().trim());
				printWriter.println(resultItems.get("content").toString().trim());
			}
			int s = outer1.size();
			for(int i = 0;i<=s-1;i++){
				printWriter.println(outer1.get(i)+" : "+outer2.get(i));
				printWriter.flush();
				count++;
			}
			
			
			printWriter.close();
		} catch (IOException e) {
			logger.warn("write file error", e);
		}
	}

	public static void main(String[] args) {
		SinaFilePipeline m = new SinaFilePipeline();
		System.out.println(m.removechar("asdf:as|df/*sgf?<kjfg>"));
	}
}
