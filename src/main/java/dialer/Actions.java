package dialer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Actions implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6808015566074110726L;
	private List<Action> l;
	
	public Actions(){
		l = new ArrayList<Action>();
	}
	
	@Override
	public String toString() {
		return "Actions [l=" + l + "]";
	}

	public List<Action> getL() {
		return l;
	}

	public void setL(List<Action> l) {
		this.l = l;
	}

	public void addAction(Action a){
		l.add(a);
	}

}
