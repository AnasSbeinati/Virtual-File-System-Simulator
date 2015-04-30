import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Contiguous implements tech {

	@Override
	public boolean creatFile(Directory dir, String name, int sizeKB,
			ArrayList<Space> spaces, ArrayList<Boolean> state) {
		for (Space space : spaces) {
			if (sizeKB <= space.size && !space.state) {
				ArrayList<Integer> allocatedBlocks = new ArrayList<>();
				allocatedBlocks.add(space.start);
				allocatedBlocks.add(space.start + sizeKB - 1);
				File1 file = new File1(name, allocatedBlocks);
				dir.files.add(file);
				for (int i = space.start; i < space.start + sizeKB; i++) {
					state.set(i, true);
				}
				if (sizeKB < space.size) {
					Space newS = new Space(space.start + sizeKB, space.end,
							false);
					spaces.add(newS);
					space.end = space.start + sizeKB - 1;
				}
				space.state = true;
				Collections.sort(spaces, new Compare());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean createDir(Directory dir, String name) {
		dir.subDirectory.add(new Directory(name));
		return true;
	}

	@Override
	public int deleteFile(Directory dir, String name, ArrayList<Space> spaces,
			ArrayList<Boolean> state) {
		for (File1 file : dir.files) {
			if (file.name.equals(name)) {
				for (Space space : spaces) {
					if (space.start == file.allocatedBlocks.get(0)) {
						space.state = false;
						file.deleted = true;
						for (int i = space.start; i < space.end; i++) {
							state.set(i, false);
						}
						return space.size;
					}
				}
				return 0;
			}
		}
		return 0;
	}

	@Override
	public int deleteDir(Directory dir, ArrayList<Space> spaces,
			ArrayList<Boolean> state) {
		int totalspace = 0;
		for (File1 file : dir.files) {
			for (Space space : spaces) {
				if (space.start == file.allocatedBlocks.get(0)) {
					space.state = false;
					file.deleted = true;
					for (int i = space.start; i < space.end; i++) {
						state.set(i, false);
					}
					totalspace += space.size;
				}
			}
		}
		for (Directory dire1 : dir.subDirectory) {
			totalspace += deleteDir(dire1, spaces, state);
		}
		dir.deleted = true;
		return totalspace;
	}

	public void write(system sys, String filePath) {
		try {
			FileOutputStream os = new FileOutputStream(new File(filePath));
			ObjectOutputStream ob = new ObjectOutputStream(os);
			ob.writeInt(sys.sizeKB);
			ob.writeInt(sys.allspace);
			ob.writeInt(1);
			ob.writeInt(sys.spaces.size());
			for (Space space : sys.spaces) {
				ob.writeInt(space.start);
				ob.writeInt(space.end);
				ob.writeBoolean(space.state);
			}
			ob.writeInt(sys.state.size());
			for (Boolean bool : sys.state) {
				ob.writeBoolean(bool);
			}
			String currentPath = "root";
			writeTree(sys.root, ob, currentPath);
			ob.close();
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeTree(Directory dir, ObjectOutputStream ob,
			String currentPath) throws IOException {
		ob.writeObject(currentPath);
		ob.writeInt(dir.files.size());
		for (File1 file : dir.files) {
			ob.writeObject(currentPath + "\\" + file.name);
			ob.writeInt(file.allocatedBlocks.get(0));
			ob.writeInt(file.allocatedBlocks.get(1));
		}
		for (Directory dire : dir.subDirectory) {
			writeTree(dire, ob, currentPath + "\\" + dire.name);
		}
	}

	public void readTree(system sys, ObjectInputStream os, int currentSize,
			int sizeKB) throws ClassNotFoundException, IOException {
		if (currentSize < sizeKB-1) {
			String currentPath = (String) os.readObject();
			String paths[];
			if (currentPath.equals("root")) {
				paths = new String[1];
				paths[0] = "root";
			} else
				paths = currentPath.trim().split("\\\\");
			sys.createFolder(currentPath);
			Directory dir = sys.getDire(sys.root, paths, 0);
			int fileListSize = os.readInt();
			ArrayList<File1> files = new ArrayList<>();
			for (int i = 0; i < fileListSize; i++) {
				String temp=(String) os.readObject();
				String s[] = (temp).split("\\\\");
				ArrayList<Integer> allocatedBlocks = new ArrayList<>();
				int start=os.readInt();
				allocatedBlocks.add(start);
				allocatedBlocks.add(os.readInt());
				int size = allocatedBlocks.get(1) - allocatedBlocks.get(0);
				File1 file = new File1(s[s.length - 1], allocatedBlocks);
				files.add(file);
				currentSize += size;
			}
			dir.setFiles(files);
			readTree(sys, os, currentSize, sizeKB);
		}
	}
}
