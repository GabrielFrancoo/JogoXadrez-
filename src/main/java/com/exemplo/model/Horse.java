package com.exemplo.model;
import java.util.ArrayList;
import java.util.List;
// Importações necessárias
public class Horse extends Piece {
    public Horse(Board board, boolean isWhite) {
        super(isWhite, board);
    }
    @Override
    // Implementação do método para obter os movimentos possíveis do Cavalo
    public List getPossibleMoves() {
        // Lista para armazenar os movimentos possíveis
        List moves = new ArrayList<>();

        int[][] directions = {
            {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
            {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
        };
        int row = position.getRow();
        int col = position.getColumn();

        for(int[] direction : directions){
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            Position newPos = new Position(newRow, newCol);

            if(newPos.isValido()){
                Piece pieceAtPosition = board.getPieceAt(newPos);
                if(pieceAtPosition == null || pieceAtPosition.isWhite() != isWhite){
                    moves.add(newPos);
                }
            }
        } 
            return moves;  
        }
    //cavalo    
    @Override
    public String getSymbol() {
    return isWhite ? "♘" : "♞";
    }
    
}
