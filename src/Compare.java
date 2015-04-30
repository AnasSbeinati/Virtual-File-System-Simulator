import java.util.Comparator;


public class Compare implements Comparator<Space>{

	@Override
	public int compare(Space o1, Space o2) {
		if(o1.size>o2.size)
			return 1;
		if(o1.size<o2.size)
			return -1;
		return 0;
	}

}
