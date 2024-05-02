package application;

import chess.ChessPiece;

public class UI {

    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Use print para evitar nova linha depois do número da linha
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]);
            }
            System.out.println(); // Quebra de linha após o fim da linha do tabuleiro
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print("-"); // Muda para print para evitar quebra de linha
        } else {
            System.out.print(piece); // Muda para print para manter na mesma linha
        }
        System.out.print(" "); // Adiciona um espaço após a peça ou hífen, sem quebra de linha
    }
}
