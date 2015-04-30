import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Indexed implements tech {

	@Override
	public boolean creatFile(Directory dir, String name, int sizeKB,
			ArrayList<Space> spaces, ArrayList<Boolean> state) {
		ArrayList<Integer> allocatedBlocks = new ArrayList<>();
		for (int i = 0; i < state.size(); i++) {
			if (!state.get(i)) {
				state.set(i, true);
				sizeKB--;
				allocatedBlocks.add(i);
			}
			if (sizeKB == 0) {
				File1 file = new File1(name, allocatedBlocks);
				dir.files.add(file);
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
				for (int i = 0; i < file.allocatedBlocks.size(); i++) {
					state.set(file.allocatedBlocks.get(i), false);
				}
				file.deleted = true;
				return file.allocatedBlocks.size();
			}
		}
		return 0;
	}

	@Override
	public int deleteDir(Directory dir, ArrayList<Space> spaces,
			ArrayList<Boolean> state) {
		int totalspace = 0;
		for (File1 file : dir.files) {
			for (int i = 0; i < file.allocatedBlocks.size(); i++) {
				state.set(file.allocatedBlocks.get(i), false);
			}
			file.deleted = true;
			totalspace += file.allocatedBlocks.size();
		}
		for (Directory dire1 : dir.subDirectory) {
			totalspace += deleteDir(dire1, spaces, state);
		}
		dir.deleted = true;
		return totalspace;
	}

	@Override
	public void write(system sys, String filePath) {
		try {
			FileOutputStream os = new FileOutputStream(new File(filePath));
			ObjectOutputStream ob = new ObjectOutputStream(os);
			ob.writeInt(sys.sizeKB);
			ob.writeInt(sys.allspace);
			ob.writeInt(2);
			ob.writeInt(sys.spaces.size());
			for (Space space : sys.spaces) {
				ob.writeInt(space.size);
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
			ob.writeInt(file.allocatedBlocks.size());
			for (Integer in : file.allocatedBlocks) {
				ob.writeInt(in);
			}
		}
		for (Directory dire : dir.subDirectory) {
			writeTree(dire, ob, currentPath + "\\" + dire.name);
		}
	}

	public void readTree(system sys, ObjectInputStream os, int currentSize,
			int sizeKB) throws ClassNotFoundException, IOException {
		if (currentSize < sizeKB - 1) {
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
				String s[] = ((String) os.readObject()).split("\\\\");
				ArrayList<Integer> allocatedBlocks = new ArrayList<>();
				int size = os.readInt();
				for (int j = 0; j < size; j++) {
					allocatedBlocks.add(os.readInt());
				}
				File1 file = new File1(s[s.length - 1], allocatedBlocks);
				files.add(file);
				currentSize += size;
			}
			dir.setFiles(files);
			readTree(sys, os, currentSize, sizeKB);
		}
	}
}
