public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BackEnd be = new BackEnd();
		be.openConnection();
		new GUI(be);
	}

}
