package com.exemplo.ui;
import com.exemplo.model.Board;
import com.exemplo.model.Position;
import com.exemplo.model.Piece;
import com.exemplo.model.Rook;

import java.awt.*;
import javax.swing.*;


public class GridLayoutDemo {
    private Board board;

        public GridLayoutDemo(Board board){
            this.board = board;
        }
        public static void main(String[] args){
            Board board = new Board();

            board.placePiece(new Rook(board, true), new Position(7,0) );
            

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400,400);

            JPanel panel = new JPanel(new GridLayout(8,8));
            
            boolean isWhite = true;
            for (int i = 0; i<64; i++){
                JButton button = new JButton();
                button.setBackground(isWhite? Color.WHITE: Color.GRAY);
                panel.add(button);
                isWhite = (i % 8 ==7)? isWhite :!isWhite;

                int row = i / 8;
                int column = i % 8;

                Position pos = new Position(row, column);
                Piece piece = board.getPieceAt(pos);
                if (piece != null){
                    button.setText(piece.getSymbol());
                }
                }
                    
                frame.add(panel);
                frame.setVisible(true);
            }
}
