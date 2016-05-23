package ccst.dl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/**
 * import mesh file and get all the id and its names and synonyms
 * @author install
 *
 */
public class Mesh {

	private HashMap<String, String> index;
	private HashMap<String, Set<String>> entry;

	public Mesh() {
		super();
		// TODO Auto-generated constructor stub
		index = new HashMap<String, String>();
		entry = new HashMap<String, Set<String>>();
	}

	public HashMap<String, String> getIndex() {
		return index;
	}

	public void setIndex(HashMap<String, String> index) {
		this.index = index;
	}

	public HashMap<String, Set<String>> getEntry() {
		return entry;
	}

	public void setEntry(HashMap<String, Set<String>> entry) {
		this.entry = entry;
	}

	public void init(String addr) {
		try {
			File meshfile = new File(addr);
			InputStreamReader ir = new InputStreamReader(new FileInputStream(meshfile));
			BufferedReader reader = new BufferedReader(ir);
			Set<String> names = null;
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				if (line.startsWith("*NEWRECORD")) {
					names = new HashSet<String>();
				}
				if (line.startsWith("MH") && !line.startsWith("MH_")) {
					String[] lines = line.split("=");
					names.add(lines[1].trim().toLowerCase().replaceAll(",", ""));
				}
				if (line.startsWith("ENTRY") || line.startsWith("PRINT ENTRY")) {
					String[] lines = line.split("=");
					String[] sublines = lines[1].trim().split("\\|");
					names.add(sublines[0].trim().toLowerCase().replaceAll(",", ""));
				}
				if (line.startsWith("UI")) {
					String[] lines = line.split("=");
					String id = lines[1].trim();
					entry.put(id, names);
					for (String s : names) {
						index.put(s, id);
					}
				}
			}

			reader.close();
			ir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writemaps(String addr) {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(addr + "index.map"));
			for (Entry<String, String> i : index.entrySet()) {
				bw.write(i.getKey() + "=" + i.getValue() + "\n");
				bw.flush();
			}
			bw = new BufferedWriter(new FileWriter(addr + "entry.map"));
			for (Entry<String, Set<String>> e : entry.entrySet()) {
				bw.write(e.getKey() + "=");
				String t = "";
				for (String s : e.getValue()) {
					t += s + "|";
				}
				t = t.substring(0, t.length() - 1);
				bw.write(t + "\n");
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initfromfile(String addr) {
		File indexfile = new File(addr + "index.map");
		File entryfile = new File(addr + "entry.map");

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(indexfile));
			BufferedReader reader = new BufferedReader(ir);

			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String[] lines = line.split("=");
				index.put(lines[0], lines[1]);
			}

			ir = new InputStreamReader(new FileInputStream(entryfile));
			reader = new BufferedReader(ir);
			Set<String> names = null;
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				names = new HashSet<String>();
				String[] lines = line.split("=");
				String[] sublines = lines[1].trim().split("\\|");
				for (String s : sublines) {
					names.add(s);
				}
				entry.put(lines[0], names);
			}
			ir.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void findsynonyms(String inaddr, String outaddr){
		
		
		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(inaddr));
			BufferedReader reader = new BufferedReader(ir);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outaddr));
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				String id = index.get(line.toLowerCase());
				if(id!=null){
					
					bw.write(id + "=");
					String t = "";
					for (String s : entry.get(id)) {
						t += s + "|";
					}
					t = t.substring(0, t.length() - 1);
					bw.write(t + "\n");
					bw.flush();
					
				}else{
					bw.write("D000000" + "="+ line.toLowerCase()+ "\n");
				    bw.flush();
//				    System.out.println(line);
				}
			}
			
			bw.close();
			reader.close();
			ir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public Set<Set<String>> readds(String addr){
		
		Set<Set<String>> rs = new HashSet<Set<String>>();
		File dsfile = new File(addr);

		try {
			InputStreamReader ir = new InputStreamReader(new FileInputStream(dsfile));
			BufferedReader reader = new BufferedReader(ir);

			Set<String> names = null;
			int i = 0;
			for (String line = reader.readLine().trim(); line != null; line = reader.readLine()) {
				names = new HashSet<String>();
				String[] lines = line.split("=");
				String[] sublines = lines[1].trim().split("\\|");
				for (String s : sublines) {
					s.replaceAll(" ", "");
					names.add(s);
				}
				rs.add(names);
				i++;
//				System.out.println(i +"set size: "+rs.size());
			}
			ir.close();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		Mesh mesh = new Mesh();
//		mesh.init("C:/Users/install/Desktop/hxs/bbc/MeSH/d2016.bin");
//		mesh.writemaps("C:/Users/install/Desktop/hxs/bbc/MeSH/");
		mesh.initfromfile("C:/Users/install/Desktop/hxs/bbc/MeSH/");
		mesh.findsynonyms("C:/Users/install/Desktop/hxs/bbc/MeSH/disease205.txt",
		"C:/Users/install/Desktop/hxs/bbc/MeSH/ds.txt");
		System.out.println(mesh.readds("C:/Users/install/Desktop/hxs/bbc/MeSH/ds.txt").size());

	}

}
