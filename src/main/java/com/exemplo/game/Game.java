package com.exemplo.game;

import com.exemplo.model.Board;
import com.exemplo.model.Position;
import com.exemplo.model.Piece;
import com.exemplo.model.Rook;
import com.exemplo.model.Lady;
import com.exemplo.model.Bishop;
import com.exemplo.model.King;
import com.exemplo.model.Pawn;
import java.util.List;
import java.util.ArrayList;

 public class Game {
    private Board board;
    private boolean isWhiteTurn;
    private boolean isGameOver;
    private Piece selectedPiece;
    // Outros atributos conforme necessário
    public Game() {
        board = new Board();
        isWhiteTurn = true; // As brancas começam
        isGameOver = false; // O jogo não terminou
        selectedPiece = null; // Nenhuma peça selecionada inicialmente
        setupPieces(); // Configurar peças no tabuleiro
    }
    // Configura as peças na posição inicial
    private void setupPieces() {
        // Peças brancas    
        board.placePiece(new Rook(board, true), new Position(7, 0));
        board.placePiece(new Bishop(board, true), new Position(7, 2));  
        board.placePiece(new Lady(board, true), new Position(7, 3));
        board.placePiece(new King(board, true), new Position(7, 4));
        board.placePiece(new Bishop(board, true), new Position(7, 5));
        board.placePiece(new Rook(board, true), new Position(7, 7));

        for (int i = 0; i < 8; i++) {
            board.placePiece(new Pawn(board, true), new Position(6, i));
        }
        
        // Peças pretas
        board.placePiece(new Rook(board, false), new Position(0, 0));
        board.placePiece(new Bishop(board, false), new Position(0, 2));
        board.placePiece(new Lady(board, false), new Position(0, 3));
        board.placePiece(new King(board, false), new Position(0, 4));
        board.placePiece(new Bishop(board, false), new Position(0, 5));
        board.placePiece(new Rook(board, false), new Position(0, 7));

        for (int i = 0; i < 8; i++) {
            board.placePiece(new Pawn(board, false), new Position(1, i));
        }
    }
        
        // Outras peças conforme necessário
        public Board getBoard() {
            return board;
        }   
        // Outros métodos conforme necessário
        public boolean isWhiteTurn() {
            return isWhiteTurn;
        }
        // Muda a vez do jogador
        public boolean isGameOver() {
            return isGameOver;
        }
        // Verifica se o jogo terminou
        public Piece getSelectedPiece() {
            return selectedPiece;
        }
        // Retorna a peça selecionada
        public void selectPiece(Position position) {
            Piece piece = board.getPieceAt(position);
            if (piece != null && piece.isWhite() == isWhiteTurn) {
                selectedPiece = piece;
            }
        }
        public boolean movePiece(Position destination){
            if(selectedPiece == null || isGameOver){
                return false;
            }
            //verifica se o movimento é válido
            if(!selectedPiece.canMoveTo(destination)){
                return false;
            }
            //verifica se o movimento deixa o rei em xeque
            if(moveCausesCheck(selectedPiece,destination)){
                return false;
            }
            //armazenar a posição original
            Position originalPosition = selectedPiece.getPosition(); //posição original da peça

            //captura a peça adversária, se necessário
            Piece capturedPiece = board.getPieceAt(destination);

            //fazer o movimento
            board.removePiece(originalPosition);
            board.placePiece(selectedPiece,destination);

            //verificar a condição especial (promoção de peão, etc)
            checkSpecialConditions(selectedPiece,destination);

            //verificar se o oponente está em xeque ou xeque-mate
            checkGameStatus();

            //Passa o turno
            isWhiteTurn = !isWhiteTurn;
            selectedPiece = null;
            //
            return true;
        }
        private boolean moveCausesCheck(Piece piece, Position destination){
            // Implementar lógica para verificar se o movimento coloca o rei em xeque
            return false; // Placeholder    
        }   
        private void checkSpecialConditions(Piece piece, Position destination){
            // Implementar lógica para condições especiais (promoção de peão, roque, en passant)
        }
        private void checkGameStatus(){
            // Implementar lógica para verificar xeque e xeque-mate
        }
 }
