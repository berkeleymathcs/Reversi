import java.util.*;

public class Board {
	public static final int SIDE_LENGTH = 8;

	private Game.Color[][] spaces;

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

	public boolean existsActiveMove(Game.Color side) {
		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				if (spaces[i][j] == null && existsSandwich(side, i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Move> getAllValidMoves(Game.Color side) {
		List<Move> validMoves = new ArrayList<Move>();
		if (!existsActiveMove(side)) {
			if (existsActiveMove(Game.flipColor(side))) {
				validMoves.add(new Move(side));
			}
			return validMoves;
		}

		for (int i = 0; i < SIDE_LENGTH; i++) {
			for (int j = 0; j < SIDE_LENGTH; j++) {
				if (spaces[i][j] == null && existsSandwich(side, i, j)) {
					validMoves.add(new Move(i, j, side));
				}
			}
		}

		return validMoves;
	}

	public boolean isValidMove(Move move) {
		Game.Color side = move.getSide();
		if (move.isPass()) {
			return !existsActiveMove(side) && existsActiveMove(Game.flipColor(side));
		}

		int x = move.getX();
		int y = move.getY();

		if (!Board.onBoard(x, y) || this.spaces[x][y] != null) {
			return false;
		}

		return existsSandwich(side, x, y);
	}


	public static boolean onBoard(int x, int y) {
		return (x >= 0 && x < SIDE_LENGTH && y >= 0 && y < SIDE_LENGTH);
	}

  public void makeMove(Move move) throws InvalidMoveException {
  	if (move.isPass()) {
  		return;
  	}
  	
  	int x = move.getX();
  	int y = move.getY();

  	if (isValidMove(move)) {
  		this.spaces[x][y] = move.getSide();
  		this.flipChips(x,y);
  		return;
  	}
  	throw new InvalidMoveException("Invalid Move");
  }

  private boolean existsSandwich(Game.Color side, int x, int y) {
  	for (int i = -1; i <= 1; i++) {
  		for (int j = -1; j <= 1; j++) {
  			if ((i != 0 || j != 0) && existsSandwichInDir(side, x, y, i, j)) {
  				return true;
  			}
  		}
  	}
  	return false;
  }

	private boolean existsSandwichInDir(Game.Color side, int x, int y, int changeX, int changeY) {

		if (!onBoard(x+changeX, y+changeY) || spaces[x+changeX][y+changeY] != Game.flipColor(side)) {
			return false;
		}

		int n = 2;

		while (onBoard(x+n*changeX,y+n*changeY)) {
			if (spaces[x+n*changeX][y+n*changeY] == side) {
				return true;
			}
			if (spaces[x+n*changeX][y+n*changeY] == null) {
				return false;
			}
			n++;
		}

		return false;
	}

	private void flipChips(int x, int y) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					flipChipsInDir(x, y, i, j);
				}
			}
		}
	}

	private void flipChipsInDir(int x, int y, int changeX, int changeY) {
		int n = 1;
		while (onBoard(x+n*changeX,y+n*changeY) && spaces[x+n*changeX][y+n*changeY] == Game.flipColor(spaces[x][y])) {
			n++;
		}
		if (onBoard(x+n*changeX,y+n*changeY) && spaces[x+n*changeX][y+n*changeY] == spaces[x][y]) {
			for (n--; n > 0; n--) {
				spaces[x+n*changeX][y+n*changeY] = spaces[x][y];
			}
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
		String output = " ";
		for (int i = 0; i < SIDE_LENGTH; i++) {
			output += "   " + i;
		}
		String horizLine = "  ---------------------------------";
		output += "\n" + horizLine;
		for (int i = 0; i < SIDE_LENGTH; i++) {
			output += "\n";
			output += i + " ";
			for (int j = 0; j < SIDE_LENGTH; j++) {
				output += "| " + Game.colorToString(spaces[i][j]) + " ";
			}
			output += "|";
			output += "\n" + horizLine;
		}
		return output;
	}
}
