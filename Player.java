abstract class Player {

	public Game.Color side;

	// Game object notifies the players at each turn with the move
	// just played and the resulting board configuration. Players can
	// use/store this information to e.g. help them choose moves in the future.
	abstract public void acknowledgeTurn(Move m, Board b);

	abstract public Move chooseNextMove(Board b);

}