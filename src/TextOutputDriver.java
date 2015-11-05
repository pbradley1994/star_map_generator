
public class TextOutputDriver {

	final static boolean DEBUG=true;	
	
	public static void main(String[] args) {
		if (DEBUG) { System.out.println("TextOutputDriver.main() start"); }
		Parser p=new Parser();
		p.print(6.0);	
		if (DEBUG) { System.out.println("TextOutputDriver.main() end"); }
	}

}
