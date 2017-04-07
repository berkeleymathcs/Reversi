import java.io.*;
import java.util.*;

public class Human extends Player {

	public Human(Game.Color side) {
		this.side = side;
	}

	public void acknowledgeTurn(Move m, Board b) {
		System.out.println(m.getSide() + " just played a piece on (" + m.getX() + ", " + m.getY() + ")");
	}

	public Move chooseNextMove(Board b) {
		if (!b.existsActiveMove(this.side)) {
			return new Move(this.side);
		}
		
		while (true) {
			try {
				BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
				Move move;
				System.out.print("Enter a move: ");
				String input[] = keyboard.readLine().split(" ");

				if (input.length != 2) {
					System.err.println("Move must be two space separated integers");
					continue;
				}

				int x = Integer.parseInt(input[0]);
				int y = Integer.parseInt(input[1]);

				move = new Move(x, y, this.side);

				return move;
			} catch (NumberFormatException e) {
				System.err.println(e);
				continue;
			} catch (IOException e) {
				System.err.println(e);
				continue;
			}
		}
	}


}