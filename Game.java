import java.io.*;

public class Game {

	public enum Color {BLACK, WHITE};


	// Black goes first.
	// If a player cannot make a legal move, play passes
	// to the other player.

	// The board for the game and the player information must be private
	// 

	private Board board;
	private Player p1;
	private Player p2;
	private Color turn;

	public static abstract class HiddenMove {

	}

	public static Color flipColor(Color color) {
		if (color == Game.Color.BLACK) {
			return Game.Color.WHITE;
		} else if (color == Game.Color.WHITE) {
			return Game.Color.BLACK;
		} else {
			return null;
		}
	}

	public static String colorToString(Color color) {
		if (color == Game.Color.BLACK) {
			return "B";
		} else if (color == Game.Color.WHITE) {
			return "W";
		} else {
			return " ";
		}
	}

	private Game(Player p1, Player p2) {
		board = new Board();
		this.p1 = p1;
		this.p2 = p2;
		turn = Game.Color.BLACK;
	}

	private Player turnToPlayer(Game.Color turn) {
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
			System.out.print("The winner is " + c);
		} else {
			System.out.println("It's a tie game!");
		}
	}

	private void broadcastTurn(Move m, Board b) {
		p1.acknowledgeTurn(m, b);
		p2.acknowledgeTurn(m, b);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Must enter two players as arguments");
			return;
		}

		Player p1;
		Player p2;

		if (args[0].equals("HUMAN")) {
			p1 = new Human(Game.Color.BLACK);
		} else if (args[0].equals("CPU")) {
			p1 = new MinimaxAI(Game.Color.BLACK);
		} else {
			System.out.println("input 1 must be HUMAN or CPU");
			return;
		}

		if (args[1].equals("HUMAN")) {
			p2 = new Human(Game.Color.WHITE);
		} else if (args[1].equals("CPU")) {
			p2 = new MinimaxAI(Game.Color.WHITE);
		} else {
			System.out.println("input 2 must be HUMAN or CPU");
			return;
		}

		Game game = new Game(p1, p2);
		System.out.println(game.board.toString());
		while (true) {

			Player p = game.turnToPlayer(game.turn);

			System.out.println("It is " + game.turn + "'s turn");

			if (!game.board.existsActiveMove(game.turn)) {
				if (!game.board.existsActiveMove(flipColor(game.turn))) {
					Color winner = game.findWinner();
					game.announceWinner(winner);
					return;
				}
				game.broadcastTurn(new Move(game.turn), new Board(game.board));
				game.turn = Game.flipColor(game.turn);
				continue;
			}

			Move nextMove;
			while (true) {
				try {
					nextMove = p.getNextMove(game.board);
					game.board.makeMove(nextMove);
					break;
				} catch (InvalidMoveException e) {
					System.err.println(e);
				}
			}

			game.turn = Game.flipColor(game.turn);
			game.broadcastTurn(nextMove, new Board(game.board));

			System.out.println(game.board.toString());
		}
	}
}