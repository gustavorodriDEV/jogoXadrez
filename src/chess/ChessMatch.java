package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bordgame.Board;
import bordgame.Piece;
import bordgame.Position;
import chess.pices.Bishop;
import chess.pices.King;
import chess.pices.Knight;
import chess.pices.Pawn;
import chess.pices.Queen;
import chess.pices.Rook;

public class ChessMatch {

	private int turn;
	private Color currenPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currenPlayer = Color.WHITE;
		initialStup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrenPlayer() {
		return currenPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public ChessPiece[][] getPiece() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	public ChessPiece preformChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (testCheck(currenPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessExeception("You can't put yourself in check");
		}

		check = (testCheck(opponent(currenPlayer))) ? true : false;

		if (testCheckMate(opponent(currenPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		return capturedPiece;
	}

	public void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessExeception("There is no piece on source position");
		}
		if (currenPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessExeception("The chosen piece is not yuors");
		}
		if (!board.piece(position).isTherAnyPossibleMove()) {
			throw new ChessExeception("Trere is no possible moves for the chsen piece");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMoves(target)) {
			throw new ChessExeception("The chosen can't move to target position");
		}
	}

	private void nextTurn() {
		turn++;
		currenPlayer = (currenPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;

	}

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + "king on board");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> apponePieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color))
				.collect(Collectors.toList());
		for (Piece p : apponePieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialStup() {
		// Peças brancas
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		for (char c = 'a'; c <= 'h'; c++) {
			placeNewPiece(c, 2, new Pawn(board, Color.WHITE));
		}

		// Peças pretas
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		for (char c = 'a'; c <= 'h'; c++) {
			placeNewPiece(c, 7, new Pawn(board, Color.BLACK));
		}
	}
}
