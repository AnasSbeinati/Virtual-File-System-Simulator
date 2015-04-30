import java.util.ArrayList;


public class File1 {
	String name;
	ArrayList<Integer> allocatedBlocks;
	public File1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Integer> getAllocatedBlocks() {
		return allocatedBlocks;
	}
	public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
		this.allocatedBlocks = allocatedBlocks;
	}
	boolean deleted;
	public File1(String name, ArrayList<Integer> allocatedBlocks) {
		this.name = name;
		this.allocatedBlocks = allocatedBlocks;
		deleted=false;
	}
}
