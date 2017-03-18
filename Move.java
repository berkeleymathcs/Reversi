public class Move {
	// This class simply encapsulates the information associated with a move into a single object.
	private final int x;
	private final int y;
	private final Game.Color side;
	// (below) score is a value used by the MinimaxAI.
	// A better design would hide this value in the MinimaxAI class.
	public int score;
	private final boolean pass;

	public Move(Game.Color side) {
		x = -1;
		y = -1;
		this.side = side;
		pass = true;
	}

	public Move(int x, int y, Game.Color side) {
		this.x = x;
		this.y = y;
		this.side = side;
		this.pass = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Game.Color getSide() {
		return side;
	}

	public boolean isPass() {
		return pass;
	}

	public String toString() {
		return "(" + side + ", x: " + x + ", y: " + y + ")";
	}

}
