abstract class Player {

	public Game.Color side;

	abstract public void acknowledgeTurn(Move m, Board b);

	abstract public Game.HiddenMove getNextMove(Board b);

}