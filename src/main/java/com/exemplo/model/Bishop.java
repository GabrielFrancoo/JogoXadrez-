package com.exemplo.model;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Board board, boolean isWhite) {
        super(isWhite, board);
    }
    @Override
    public List getPossibleMoves() {
        List moves = new ArrayList<>();

        int[][] directions = {{ -1, -1}, { -1, 1}, {1, 1}, {1, -1}};
        
        for(int[] direction : directions){
            int row = position.getRow();
            int col = position.getColumn();
            
            while (true) {
                row += direction[0];
                col += direction[1];
                Position newPos = new Position(row, col);
                
                if (!newPos.isValido()) break;
                
                Piece pieceAtPosition = board.getPieceAt(newPos);
                if (pieceAtPosition == null) {
                    // Casa vazia, movimento válido
                    moves.add(newPos);
                } else if (pieceAtPosition.isWhite() != isWhite) {
                    // Casa com peça adversária, movimento válido (captura)
                    moves.add(newPos);
                    break;
                } else {
                    // Casa com peça da mesma cor, movimento inválido
                    break;
                }
            }
        }
            return moves;
    }
    //bispo
    @Override
    public String getSymbol() {
        return isWhite ? "♗" : "♝";
    }
}
