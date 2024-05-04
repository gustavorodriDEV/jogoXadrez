package bordgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardExeception(
					"Erro ao criar o tabuleiro: é necessario ter pelo menos 1 linha(row) e 1 coluna(column)");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardExeception("Position not on the board");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if (!positionExists(position)) {
			throw new BoardExeception("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardExeception("Existe um perça nesse posição" + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}

	public boolean positionExists(int row, int column) {
	    return row >= 0 && row < 8 && column >= 0 && column < 8;
	}


	private boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}

	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardExeception("Position not on the board");
		}
		return piece(position) != null;
	}

	

}
