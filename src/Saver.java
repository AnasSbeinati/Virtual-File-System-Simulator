import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
public class Saver {
	String filePath;

	public Saver(String path) {
		this.filePath = path;
	}

	public void write(system sys) {
		if(new File(filePath).isDirectory()) {
			try {
				FileOutputStream os=new FileOutputStream(new File(filePath));
				ObjectOutputStream ob=new ObjectOutputStream(os);
				ob.writeInt(sys.sizeKB);
				ob.writeInt(sys.allspace);
				if(sys.tech1.equals("con"))
					ob.writeInt(1);
				else
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
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void read(system sys) {

	}
}
