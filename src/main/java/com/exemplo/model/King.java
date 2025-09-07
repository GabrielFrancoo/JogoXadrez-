package com.exemplo.model;
import java.util.ArrayList;
import java.util.List;

// Classe que representa a peça Rei no xadrez
public class King extends Piece{
    public King(Board board, boolean isWhite) {
        super(isWhite, board);
    }
    // Implementação do método para obter os movimentos possíveis do Rei
    @Override      
    public List getPossibleMoves() {
        List moves = new ArrayList<>();

        // Direções:
        int[][] directions = {
            {-1, 0},//cima
            {-1, 1},//cima direita
            {0, 1},//direita
            {1, 1},//baixo direita
            {1, 0},//baixo
            {1, -1},//baixo esquerda
            {0, -1},//esquerda
            {-1, -1}//cima esquerda
        };
        // Percorre todas as direções possíveis
        int row = position.getRow();
        int col = position.getColumn();

        for(int[] direction : directions){
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            Position newPos = new Position(newRow, newCol);

            // Verifica se a nova posição é válida
            if(newPos.isValido()){
                Piece pieceAtPosition = board.getPieceAt(newPos);
                // Se a casa está vazia ou contém uma peça adversária, o movimento é válido
                if(pieceAtPosition == null || pieceAtPosition.isWhite() != isWhite){
                    moves.add(newPos);
                }
            }
        }
        return moves;
    }
    // Retorna o símbolo do Rei
    @Override
    public String getSymbol() {
        return isWhite ? "♔" : "♚";
    }
    
}
