package com.exemplo.model;
// Classe que representa a peça Torre no xadrez
import java.util.ArrayList;
// Importações necessárias
import java.util.List;

// Classe que representa a peça Torre no xadrez
public class Rook extends Piece {
    
    // Construtor da peça Torre
    public Rook(Board board, boolean isWhite) {
        super(isWhite, board);
    }
    
    // Implementação do método para obter os movimentos possíveis da Torre
    @Override
    public List getPossibleMoves() {
        List moves = new ArrayList<>();
        
        // Direções:
        int[][] directions = {
            {-1, 0},//cima
            {0, 1},//direita
            {1, 0},//baixo
            {0, -1}//esquerda
        };

        // Percorre todas as direções possíveis
        for (int[] direction : directions) {
            int row = position.getRow();//posição atual linha
            int col = position.getColumn();//posição atual coluna

            // Move-se na direção até encontrar uma peça ou sair do tabuleiro    
            while (true) {
                // Atualiza a posição
                row += direction[0];
                // Atualiza a posição
                col += direction[1];
                // Cria uma nova posição
                Position newPos = new Position(row, col);
                
                // Verifica se a nova posição é válida
                if (!newPos.isValido()) break;
                
                // Verifica se há uma peça na nova posição
                Piece pieceAtPosition = board.getPieceAt(newPos);
                
                // Se a casa está vazia ou contém uma peça adversária, o movimento é válido
                if (pieceAtPosition == null) {
                    // Casa vazia, movimento válido
                    moves.add(newPos);
                    // Continua na mesma direção
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
        // Retorna a lista de movimentos possíveis
        return moves;
    }
    // Implementação do método para obter o símbolo da peça Torre
    @Override
    public String getSymbol() {
        // Retorna o símbolo Unicode da Torre dependendo da cor
        return isWhite ? "♖" : "♜";
    }
 }