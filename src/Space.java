
public class Space {
	public int start,end,size;
	boolean state;
	public Space(int start, int end, boolean state) {
		super();
		this.start = start;
		this.end = end;
		this.state = state;
		size=(end-start);
	}
	
}
