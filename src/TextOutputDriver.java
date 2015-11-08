
public class TextOutputDriver {

	final static boolean DEBUG=true;	
	
	public void main() {
		if (DEBUG) { System.out.println("TextOutputDriver.main() start"); }
		Parser p=new Parser();
		p.print(6.0);	
		if (DEBUG) { System.out.println("TextOutputDriver.main() end"); }
	}

}
