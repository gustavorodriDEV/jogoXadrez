package chess.pices;

import bordgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Rock extends ChessPiece {

	public Rock(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}

}
