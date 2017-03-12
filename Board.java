import java.util.*;

public class Board {
	public static final int SIDE_LENGTH = 8;

	private Game.Color[][] spaces;

	// Sets up the board to the standard starting configuration. See Reversi rules
	public Board() {
		spaces = new Game.Color[SIDE_LENGTH][SIDE_LENGTH];

		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				spaces[i][j] = null;
			}
		}

		int lower = SIDE_LENGTH / 2 - 1;
		int upper = SIDE_LENGTH / 2;

		spaces[lower][lower] = Game.Color.WHITE;
		spaces[upper][upper] = Game.Color.WHITE;

		spaces[lower][upper] = Game.Color.BLACK;
		spaces[upper][lower] = Game.Color.BLACK;
	}

	public Board(Board b) {
		this.spaces = new Game.Color[SIDE_LENGTH][SIDE_LENGTH];
		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				this.spaces[i][j] = b.spaces[i][j];
			}
		}
	}

	public Game.Color getSpace(int x, int y) {
		return spaces[x][y];
	}

	// returns false if current player must pass or board is filled
	public boolean existsActiveMove(Game.Color side) {
		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				if (spaces[i][j] == null && formsSandwich(new Move(i, j, side))) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidMove(Move move) {
		Game.Color side = move.getSide();
		
		// Declare passing to be valid only when the current player cannot play an
		// active move and the board state is not a deadlock
		if (move.isPass()) {
			return !existsActiveMove(side) && existsActiveMove(Game.flipColor(side));
		}

		int x = move.getX();
		int y = move.getY();

		if (Board.onBoard(x, y) && this.spaces[x][y] == null) {
			return formsSandwich(new Move(x, y, side));
		}

		return false;
	}

	public List<Move> findAllValidMoves(Game.Color side) {
		boolean playerMustPass = !existsActiveMove(side);
		boolean opponentMustPass = !existsActiveMove(Game.flipColor(side));
		boolean deadlock = playerMustPass && opponentMustPass;

		if (deadlock) {
			return new ArrayList<Move>();
		}

		if (playerMustPass) {
			List<Move> pass = new ArrayList<Move>();
			pass.add(new Move(side));
			return pass;
		}

		List<Move> activeMoves = new ArrayList<Move>();

		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				if (spaces[i][j] == null && formsSandwich(new Move(i, j, side))) {
					activeMoves.add(new Move(i, j, side));
				}
			}
		}

		return activeMoves;
	}

	public static boolean onBoard(int x, int y) {
		return (x >= 0 && x < SIDE_LENGTH && y >= 0 && y < SIDE_LENGTH);
	}

  public void makeMove(Move move) throws InvalidMoveException {

  	if (!isValidMove(move)) {
  		throw new InvalidMoveException("Invalid Move");
  	}

  	if (move.isPass()) {
  		return;
  	}

  	int x = move.getX();
  	int y = move.getY();

  	this.spaces[x][y] = move.getSide();
  	this.flipChips(x,y);
  }

  // A piece can be placed on the board only if it would form a "sandwich" with
  // the pieces in an outward pointing ray (horizontal, vertical, diagonal).
  // Example: _ W W W B. A black move on the _ forms a sandwich with the pieces to the right.
  private boolean formsSandwich(Move m) {
  	for (int dirX = -1; dirX <= 1; dirX++) {
  		for (int dirY = -1; dirY <= 1; dirY++) {
  			if ((dirX != 0 || dirY != 0) && formsSandwichInDir(m, dirX, dirY)) {
  				return true;
  			}
  		}
  	}
  	return false;
  }

  // Checks for a sandwich in the direction of the vector (dirX, dirY).
  // dirX and dirY have value -1, 0, or 1, and are not both 0.
	private boolean formsSandwichInDir(Move m, int dirX, int dirY) {

		int x = m.getX();
		int y = m.getY();

		Game.Color side = m.getSide();

		// neighboring piece must belong to opponent
		if (!onBoard(x + dirX, y + dirY) || spaces[x+dirX][y+dirY] != Game.flipColor(side)) {
			return false;
		}

		x += dirX;
		y += dirY;

		while (onBoard(x, y)) {
			if (spaces[x][y] == side) {
				return true;
			}
			if (spaces[x][y] == null) {
				return false;
			}
			x += dirX;
			y += dirY;
		}

		return false;
	}

	// Assumes a piece that forms a sandwich has just been placed at (x,y).
	private void flipChips(int x, int y) {
		for (int dirX = -1; dirX <= 1; dirX++) {
			for (int dirY = -1; dirY <= 1; dirY++) {
				if (dirX != 0 || dirY != 0) {
					flipChipsInDir(x, y, dirX, dirY);
				}
			}
		}
	}

	// follow the direction (dirX, dirY) to verify that a sandwich exists,
	// then iterate back and flip the pieces.
	private void flipChipsInDir(int x, int y, int dirX, int dirY) {
		Game.Color targetColor = spaces[x][y];
		Game.Color oppositeColor = Game.flipColor(targetColor);

		x += dirX;
		y += dirY;

		while (onBoard(x, y) && spaces[x][y] == oppositeColor) {
			x += dirX;
			y += dirY;
		}

		// checks if fell off board or did not form a sandwich
		if (!onBoard(x,y) || spaces[x][y] != targetColor) {
			return;
		}

		x -= dirX;
		y -= dirY;
		while (spaces[x][y] == oppositeColor) {
			spaces[x][y] = targetColor;
			x -= dirX;
			y -= dirY;
		}
	}

	public int countDifference(Game.Color side) {
		int countDifference = 0;
		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				if (spaces[i][j] == side) {
					countDifference++;
				} else if (spaces[i][j] == Game.flipColor(side)) {
					countDifference--;
				}
			}
		}
		return countDifference;
	}

	public String toString() {
		String boardASCII = " ";
		for (int i = 0; i < SIDE_LENGTH; i++) {
			boardASCII += "   " + i;
		}
		String horizLine = "  ---------------------------------";
		boardASCII += "\n" + horizLine;
		for (int i = 0; i < SIDE_LENGTH; i++) {
			boardASCII += "\n";
			boardASCII += i + " ";
			for (int j = 0; j < SIDE_LENGTH; j++) {
				boardASCII += "| " + Game.colorToString(spaces[i][j]) + " ";
			}
			boardASCII += "|";
			boardASCII += "\n" + horizLine;
		}
		return boardASCII;
	}
}
