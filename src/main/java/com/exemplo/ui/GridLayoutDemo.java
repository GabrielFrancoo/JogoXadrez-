package com.exemplo.ui;

import java.awt.*;
import javax.swing.*;


public class GridLayoutDemo {
        public static void main(String[] args){
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
                }
                    
                frame.add(panel);
                frame.setVisible(true);
            }
}
