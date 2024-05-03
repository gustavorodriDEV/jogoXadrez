package bordgame;

public class BoardExeception extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BoardExeception(String msg) {
		super(msg);
	}
}
