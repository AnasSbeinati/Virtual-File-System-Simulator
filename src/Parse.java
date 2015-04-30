import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Parse {
	public boolean main(system sys) {
		String command = "";
		while (command.equals("exit")) {
			command = new Scanner(System.in).nextLine();
			String str[] = command.trim().split(" ");
			switch (str[0]) {
			case "createfile":
				sys.createFile(str[1], Integer.parseInt(str[2]));
				break;
			case "createfolder":
				sys.createFolder(str[1]);
				break;
			case "deletefolder":
				sys.deleteFolder(str[1]);
				break;
			case "deletefile":
				sys.deleteFile(str[1]);
				break;
			default:
				System.out.println("->No match command");
			}
			System.out.println("->");
		}
		return false;
	}

	public system read(String filePath) throws Exception {
		FileInputStream is;
		ObjectInputStream os;
		system sys;
		is = new FileInputStream(new File(filePath));
		os = new ObjectInputStream(is);
		int sizeKB, allspace = 0;
		int t;
		tech tech1;
		Directory root;
		ArrayList<Boolean> state = new ArrayList<>();
		ArrayList<Space> spaces = new ArrayList<>();
		int currentSize = 0;
		sizeKB = os.readInt();
		allspace = os.readInt();
		t = os.readInt();
		if (t == 1)
			tech1 = new Contiguous();
		else
			tech1 = new Indexed();
		int spa = os.readInt();
		for (int i = 0; i < spa; i++) {
			int start = os.readInt();
			int end = os.readInt();
			boolean b = os.readBoolean();
			spaces.add(new Space(start, end, b));
		}
		spa = os.readInt();
		for (int i = 0; i < spa; i++) {
			boolean b = os.readBoolean();
			state.add(b);
		}
		Directory dir = new Directory();
		sys = new system(sizeKB, tech1);
		tech1.readTree(sys, os, 0, allspace);
		sys.setAllspace(allspace);
		sys.setSpaces(spaces);
		sys.setState(state);
		// sys.setRoot(dir);
		return sys;
	}
}
