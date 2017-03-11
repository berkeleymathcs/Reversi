import java.util.*;

public class MinimaxAI extends Player {
	
	private int minimaxDepth = 5;

	public MinimaxAI(Game.Color side) {
		this.side = side;
	}

	public void acknowledgeTurn(Move m, Board b) {
		
	}

	private int evalBoard(Board board, Game.Color side) {
		return board.countDifference(side);
	}

	private Move chooseBestMove(Board board, Game.Color turn, int depth) {
		List<Move> validMoves = board.getAllValidMoves(turn);
		if (depth == 0 || validMoves.isEmpty()) {
			Move done = new Move(turn);
			done.score = evalBoard(board, turn);
			return done;
		}

		Move bestMove = validMoves.get(0);
		bestMove.score = Integer.MIN_VALUE;
		for (Move move : validMoves) {
			Board modifiedBoard = new Board(board);
			try {
				modifiedBoard.makeMove(move);
			} catch (InvalidMoveException e) {
				System.err.println("MinimaxAI has encountered an error");
				System.exit(1);
			}
			Move reply = chooseBestMove(modifiedBoard, Game.flipColor(turn), depth - 1);
			if (-reply.score > bestMove.score) {
				bestMove = move;
				bestMove.score = -reply.score;
			}
		}
		return bestMove;
	}

	public Move getNextMove(Board board) {
		return chooseBestMove(board, this.side, minimaxDepth);
	}
}