import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public interface tech {
	public boolean creatFile(Directory dir,String name,int sizeKB,ArrayList<Space>spaces,ArrayList<Boolean> state);
	public boolean createDir(Directory dir,String name);
	public int deleteFile(Directory dir,String name,ArrayList<Space>spaces,ArrayList<Boolean> state);
	public int deleteDir(Directory dir,ArrayList<Space>spaces,ArrayList<Boolean> state);
	public void write(system sys,String filePath);
	public void readTree(system sys, ObjectInputStream os, int currentSize,
			int sizeKB) throws ClassNotFoundException, IOException;
}
