package ccst.dl.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class CNNFilePipeline extends FilePersistentBase implements Pipeline {

	public static int count = 0;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * create a FilePipeline with default path"/data/webmagic/"
	 */
	public CNNFilePipeline() {
		setPath("/data/webmagic/");
	}

	public CNNFilePipeline(String path) {
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
			String date = resultItems.get("date").toString().trim().substring(0, 10);
			String path = this.path + PATH_SEPERATOR + date + PATH_SEPERATOR;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String title = resultItems.get("title").toString().trim();
			String filename = removechar(title);
			PrintWriter printWriter = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(getFile(path + filename + ".txt")), "UTF-8"));

			printWriter.println(title);

			if (resultItems.get("content") instanceof Iterable) {
				Iterable value = (Iterable) resultItems.get("content");
				for (Object o : value) {
					if (o.toString().trim().length() >= 1) {
						printWriter.println(o.toString().trim());
					}
				}
			} else {
				printWriter.println(resultItems.get("content").toString().trim());
			}
			count++;
			printWriter.close();
		} catch (IOException e) {
			logger.warn("write file error", e);
		}
	}

	public static void main(String[] args) {
		CNNFilePipeline m = new CNNFilePipeline();
		System.out.println(m.removechar("asdf:as|df/*sgf?<kjfg>"));
	}
}
