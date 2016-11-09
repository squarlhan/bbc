
package ccst.dl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrintDirs {

	public static void main(String[] args) {
		try {
			File root = new File("C:/Users/install/Desktop/hxs/test/cnd/data");
			File[] sublists = root.listFiles();
			List<File> files = new ArrayList();
			File result = new File("C:/Users/install/Desktop/hxs/test/cnd/all.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(result));
			System.out.println("Directory: " + sublists.length);
			long start = System.currentTimeMillis();
			for (File f : sublists) {
				bw.write(f.getName());
                bw.write("\n");
                bw.flush();
                System.out.println(f.getName());
			}
			bw.close();
			long stop = System.currentTimeMillis();
			System.out.println("Files: " + result.getFreeSpace() + " Time:" + (stop - start) / 1000 + "s.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void merge(List<File> files, File fout) throws Exception {
		// 构建文件输出流
		FileOutputStream out = new FileOutputStream(fout);
		for (File f : files) {
			// 打开文件输入流
			FileInputStream in = new FileInputStream(f);
			// 从输入流中读取数据，并写入到文件数出流中
			int c;
			int pre = (int) ' ';
			while ((c = in.read()) != -1) {
				if (!isSignal(c) || (Character.isDigit(pre) && ((c == (int) ',')) || (c == (int) '.'))) {
					out.write(Character.toLowerCase(c));
				} else {
					out.write((int) ' ');
					out.write(Character.toLowerCase(c));
					out.write((int) ' ');
				}
				pre = c;
			}
			in.close();
		}
		out.close();
	}

	public static boolean isSignal(int i) {
		Set<Integer> sigset = new HashSet();
		sigset.add((int) ',');
		sigset.add((int) '.');
		sigset.add((int) '?');
		sigset.add((int) ':');
		sigset.add((int) ';');
		sigset.add((int) '\'');
		sigset.add((int) '\"');
		sigset.add((int) '(');
		sigset.add((int) ')');
		sigset.add((int) '[');
		sigset.add((int) ']');
		sigset.add((int) '#');
		sigset.add((int) '@');
		sigset.add((int) '_');
		sigset.add((int) '&');
		sigset.add((int) '/');
		return sigset.contains(i);
	}

}
