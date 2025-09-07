package com.exemplo.model;
// Classe que representa o tabuleiro de xadrez
 public class Board {
    // Matriz 8x8 para representar as peças no tabuleiro
    private Piece[][] pieces;
    // Construtor que inicializa o tabuleiro vazio
    public Board() {
        // Inicializa a matriz 8x8 com null (sem peças)
        pieces = new Piece[8][8];
    }
    // Método para obter a peça em uma posição específica
    public Piece getPieceAt(Position position) {
        // Verifica se a posição é válida
        if (!position.isValido()) return null;
        // Retorna a peça na posição especificada
        return pieces[position.getRow()][position.getColumn()];
    }
    // Método para colocar uma peça em uma posição específica
    public void placePiece(Piece piece, Position position) {
        // Verifica se a posição é válida
        if (!position.isValido()) return;
        // Coloca a peça na posição especificada
        pieces[position.getRow()][position.getColumn()] = piece;
        // Atualiza a posição da peça
        if (piece != null) {
            piece.setPosition(position);
        }
    }
    // Método para remover uma peça de uma posição específica
    public void removePiece(Position position) {
        // Verifica se a posição é válida
        if (!position.isValido()) return;
        // Remove a peça da posição especificada
        pieces[position.getRow()][position.getColumn()] = null;
    }
    // Método para verificar se uma posição está vazia
    public boolean isPositionEmpty(Position position) {
        // Verifica se a posição é válida
        return getPieceAt(position) == null;
    }
    // Método para limpar o tabuleiro (remover todas as peças)
    public void clear() {
        // Percorre toda a matriz e define todas as posições como null
        for (int row = 0; row < 8; row++) {
            // Percorre cada coluna da linha atual
            for (int col = 0; col < 8; col++) {
                // Define a posição atual como null (sem peça)
                pieces[row][col] = null;
            }
        }
    }
    
    // Método auxiliar para verificar se uma posição está sob ataque
    public boolean isUnderAttack(Position position, boolean byWhite) {
        // Percorre todas as peças no tabuleiro
        for (int row = 0; row < 8; row++) {
            // Percorre cada coluna da linha atual
            for (int col = 0; col < 8; col++) {
                // Obtém a peça na posição atual
                Piece piece = pieces[row][col];
                // Verifica se a peça pertence ao jogador que está atacando
                if (piece != null && piece.isWhite() == byWhite) {
                    // Verifica se a peça pode se mover para a posição alvo
                    if (piece.canMoveTo(position)) {

                        return true;
                    }
                }
            }
        }
        return false;
    }
}