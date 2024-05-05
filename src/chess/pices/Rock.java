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
	
	@Override
	public boolean[][] possibleMoves(){
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		return mat;
	}
}
