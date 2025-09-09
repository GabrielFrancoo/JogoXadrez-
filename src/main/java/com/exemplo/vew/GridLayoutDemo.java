package com.exemplo.vew;
import com.exemplo.model.Bishop;
import com.exemplo.model.Board;
import com.exemplo.model.Horse;
import com.exemplo.model.King;
import com.exemplo.model.Lady;
import com.exemplo.model.Pawn;
import com.exemplo.model.Position;
import com.exemplo.model.Piece;
import com.exemplo.model.Rook;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import java.util.List;

public class GridLayoutDemo {
    private Board board;
    private JButton[][] buttons;
    private Position selectedPosition;
    private boolean isWhiteTurn = true; // Começa com as brancas

    public GridLayoutDemo(Board board){
        this.board = board;
    }
    
    private String getImagePath(Piece piece) {
        // Usar as imagens apenas para peças pretas
        if (!piece.isWhite()) {
            if (piece instanceof King) return "/King.png";
            if (piece instanceof Horse) return "/Horse.png";
            if (piece instanceof Bishop) return "/Bishop.png";
            if (piece instanceof Lady) return "/Lady.png";
            if (piece instanceof Rook) return "/Rook.png";
            if (piece instanceof Pawn) return "/Pawn.png";
        }
        // Para peças brancas e outras peças, usar símbolos Unicode
        return null;
    }
    
    public static void main(String[] args){
        Board board = new Board();
        GridLayoutDemo game = new GridLayoutDemo(board);
        game.setupBoard();
        game.createAndShowGUI();
    }
    
    private void setupBoard() {
        // Torres (Rooks) - posições corretas
        board.placePiece(new Rook(board, true), new Position(7,0) );
        board.placePiece(new Rook(board, false), new Position(0,0) );
        board.placePiece(new Rook(board, true), new Position(7,7) );
        board.placePiece(new Rook(board, false), new Position(0,7) );

        // Cavalos (Horses) - coremass corrigidas
        board.placePiece(new Horse(board, true), new Position(7, 1));
        board.placePiece(new Horse(board, false), new Position(0, 1));
        board.placePiece(new Horse(board, true), new Position(7, 6));
        board.placePiece(new Horse(board, false), new Position(0, 6));  

        // Bispos (Bishops) - cores corrigidas
        board.placePiece(new Bishop(board, true), new Position(7, 2));
        board.placePiece(new Bishop(board, false), new Position(0, 2));
        board.placePiece(new Bishop(board, true), new Position(7, 5));
        board.placePiece(new Bishop(board, false), new Position(0, 5));

        // Rainhas (Ladies) - posições corretas
        board.placePiece(new Lady(board, true), new Position(7, 3));
        board.placePiece(new Lady(board, false), new Position(0, 3));

        // Reis (Kings) - posições corretas
        board.placePiece(new King(board, true), new Position(7, 4));
        board.placePiece(new King(board, false), new Position(0, 4));

        // Peões pretos (fileira 1)
        board.placePiece(new Pawn(board, false),new Position(1, 0));
        board.placePiece(new Pawn(board, false),new Position(1, 1));
        board.placePiece(new Pawn(board, false),new Position(1, 2));
        board.placePiece(new Pawn(board, false),new Position(1, 3));
        board.placePiece(new Pawn(board, false),new Position(1, 4));
        board.placePiece(new Pawn(board, false),new Position(1, 5));
        board.placePiece(new Pawn(board, false),new Position(1, 6));    
        board.placePiece(new Pawn(board, false),new Position(1, 7));

        // Peões brancos (fileira 6)
        board.placePiece(new Pawn(board, true),new Position(6, 0));
        board.placePiece(new Pawn(board, true),new Position(6, 1));
        board.placePiece(new Pawn(board, true),new Position(6, 2));
        board.placePiece(new Pawn(board, true),new Position(6, 3));
        board.placePiece(new Pawn(board, true),new Position(6, 4));
        board.placePiece(new Pawn(board, true),new Position(6, 5));
        board.placePiece(new Pawn(board, true),new Position(6, 6));
        board.placePiece(new Pawn(board, true),new Position(6, 7));
    }
    
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Jogo de Xadrez");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        JPanel panel = new JPanel(new GridLayout(8,8));
        buttons = new JButton[8][8];
        
        boolean isWhite = true;
        for (int i = 0; i<64; i++){
            JButton button = new JButton();
            button.setBackground(isWhite? Color.WHITE: Color.GRAY);
            panel.add(button);
            isWhite = (i % 8 ==7)? isWhite :!isWhite;

            int row = i / 8;
            int column = i % 8;
            buttons[row][column] = button;
            
            // Adicionar evento de clique
            final int finalRow = row;
            final int finalCol = column;
            button.addActionListener(e -> handleButtonClick(finalRow, finalCol));
            
            // Configurar peça inicial
            updateButton(row, column);
        }
                    
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private void handleButtonClick(int row, int col) {
        Position clickedPosition = new Position(row, col);
        Piece clickedPiece = board.getPieceAt(clickedPosition);
        
        // Se nenhuma peça está selecionada
        if (selectedPosition == null) {
            // Verificar se clicou em uma peça válida do turno atual
            if (clickedPiece != null && clickedPiece.isWhite() == isWhiteTurn) {
                selectedPosition = clickedPosition;
                buttons[row][col].setBackground(Color.YELLOW); // Destaque da seleção
            }
        } 
        // Se uma peça já está selecionada
        else {
            // Se clicou na mesma peça, deseleciona
            if (clickedPosition.equals(selectedPosition)) {
                clearSelection();
                return;
            }
            
            // Se clicou em outra peça do mesmo jogador, seleciona ela
            if (clickedPiece != null && clickedPiece.isWhite() == isWhiteTurn) {
                clearSelection();
                selectedPosition = clickedPosition;
                buttons[row][col].setBackground(Color.YELLOW);
                return;
            }
            
            // Tentar mover a peça
            Piece selectedPiece = board.getPieceAt(selectedPosition);
            if (selectedPiece != null && selectedPiece.canMoveTo(clickedPosition)) {
                // Executar o movimento
                executeMove(selectedPosition, clickedPosition);
                clearSelection();
                // Alternar turno
                isWhiteTurn = !isWhiteTurn;
            } else {
                // Movimento inválido
                clearSelection();
            }
        }
    }
    
    private void clearSelection() {
        if (selectedPosition != null) {
            int row = selectedPosition.getRow();
            int col = selectedPosition.getColumn();
            // Restaurar cor original
            boolean isWhite = (row + col) % 2 == 0;
            buttons[row][col].setBackground(isWhite ? Color.WHITE : Color.GRAY);
            selectedPosition = null;
        }
    }
    
    private void executeMove(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece != null) {
            // Remover peça da posição anterior
            board.removePiece(from);
            // Colocar peça na nova posição
            board.placePiece(piece, to);
            
            // Atualizar interface
            updateButton(from.getRow(), from.getColumn());
            updateButton(to.getRow(), to.getColumn());
        }
    }
    
    private void updateButton(int row, int col) {
        Position pos = new Position(row, col);
        Piece piece = board.getPieceAt(pos);
        JButton button = buttons[row][col];

    
        if (piece != null) {
            String imagePath = getImagePath(piece);
            if (imagePath != null) {
                URL imageURL = getClass().getResource(imagePath);
                if (imageURL != null) {
                    ImageIcon icon = new ImageIcon(imageURL);
                    Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));
                    button.setText("");
                } else {
                    // fallback seguro caso a imagem não exista
                    button.setIcon(null);
                    button.setText(piece.getSymbol());
                }
            } else {
                // fallback seguro caso getImagePath retorne null
                button.setIcon(null);
                button.setText(piece.getSymbol());
            }
        } else {
            // célula vazia
            button.setIcon(null);
            button.setText("");
        }
    }   
}
 