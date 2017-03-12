import java.io.*;

public class Game {

	public enum Color {BLACK, WHITE};

	// Note: Black goes first in this game

	// Anti-cheating design:
	// Pass defensive copies of Move/Board objects to players to prevent unauthorized
	// changes to current game state.
	// Hide Player objects from their opponents to prevent players from running opponent's
	// chooseNextMove() method, which would reveal the opponent's strategy.

	private Board board;
	private Player p1; // BLACK player
	private Player p2; // WHITE player
	private Color turn;

	public static Color flipColor(Color color) {
		if (color == Game.Color.BLACK) {
			return Game.Color.WHITE;
		} else if (color == Game.Color.WHITE) {
			return Game.Color.BLACK;
		} else {
			return null;
		}
	}

	// Succinct symbols "B" and "W" for e.g., printing board state to stdout.
	public static String colorToString(Color color) {
		if (color == Game.Color.BLACK) {
			return "B";
		} else if (color == Game.Color.WHITE) {
			return "W";
		} else {
			return "-";
		}
	}

	private Game(Player p1, Player p2) {
		board = new Board();
		this.p1 = p1;
		this.p2 = p2;
		turn = Game.Color.BLACK;
	}

	private Player getCurrentPlayer() {
		if (turn == Game.Color.BLACK) {
			return p1;
		} else if (turn == Game.Color.WHITE) {
			return p2;
		} else {
			return null;
		}
	}

	private Game.Color findWinner() {
		int advantage = board.countDifference(Game.Color.BLACK);
		if (advantage > 0) {
			return Game.Color.BLACK;
		} else if (advantage < 0) {
			return Game.Color.WHITE;
		} else {
			return null;
		}
	}

	private void announceWinner(Color c) {
		if (c != null) {
			System.out.println("The winner is " + c);
		} else {
			System.out.println("It's a tie game!");
		}
	}

	// Notify players of each turn that happens. Players may use
	// this information to e.g., help them choose future moves.
	private void broadcastTurn(Move m, Board b) {
		p1.acknowledgeTurn(m, b);
		p2.acknowledgeTurn(m, b);
	}

	private static boolean areValidArguments(String[] args) {
		if (args.length != 2) {
			System.err.println("Players must be entered as two arguments, e.g., HUMAN CPU");
			return false;
		}
		if (args[0] == null || args[1] == null || !Game.isValidPlayer(args[0]) || !Game.isValidPlayer(args[1])) {
			System.err.println("Players must be entered as two arguments, e.g., HUMAN CPU");
			return false;
		}
		return true;
	}

	private static boolean isValidPlayer(String s) {
		if (s == null) {
			return false;
		}
		return s.equals("HUMAN") || s.equals("CPU");
	}

	private static Player constructPlayer(String s, Game.Color c) {
		if (s.equals("HUMAN")) {
			return new Human(c);
		} else if (s.equals("CPU")) {
			return new MinimaxAI(c);
		} else {
			return null;
		}
	}

	private void printBoard() {
		System.out.println(this.board);
	}

	public static void main(String[] args) {

		// Parse/validate input, etc.

		if (!areValidArguments(args)) {
			System.exit(1);
		}

		Player p1, p2;

		p1 = constructPlayer(args[0], Game.Color.BLACK);
		p2 = constructPlayer(args[1], Game.Color.WHITE);

		Game game = new Game(p1, p2);
		game.printBoard();

		// Game loop
		while (true) {

			Player currentPlayer = game.getCurrentPlayer();
			System.out.println("It is " + game.turn + "'s turn");

			// Case 1: currentPlayer cannot make a move
			if (!game.board.existsActiveMove(game.turn)) {
				// if deadlock or board is filled, then announce end of game
				if (!game.board.existsActiveMove(flipColor(game.turn))) {
					Color winner = game.findWinner();
					game.announceWinner(winner);
					return;
				}
				game.broadcastTurn(new Move(game.turn), new Board(game.board));
				game.turn = Game.flipColor(game.turn);
				continue;
			}

			// Case 2: currentPlayer is asked to give his next move
			Move nextMove;
			while (true) {
				try {
					if (currentPlayer == null) {System.out.println("null player");}
					nextMove = currentPlayer.chooseNextMove(game.board);
					game.board.makeMove(nextMove);
					break;
				} catch (InvalidMoveException e) {
					System.err.println(e);
				}
			}

			game.turn = Game.flipColor(game.turn);
			game.broadcastTurn(nextMove, new Board(game.board));

			game.printBoard();
		}
	}
}