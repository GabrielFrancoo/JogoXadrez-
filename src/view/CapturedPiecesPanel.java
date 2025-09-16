package view;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CapturedPiecesPanel extends JPanel {
    private final JLabel[][] slots = new JLabel[8][2];
    private final int ICON_SIZE = 32;
    private int nextIndex = 0;

//===================PEÇAS CAPTURADAS NO MENU LATERAL=========================== 
    public CapturedPiecesPanel() {
        // 8 linhas, 2 colunas
        setLayout(new GridLayout(8, 2, 2, 2));
        setBorder(BorderFactory.createTitledBorder(""));

        for( int r = 0; r < 8; r++ ) {
            for( int c = 0; c < 2; c++ ) {
                JLabel  lbl = new JLabel();
                lbl.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                slots[r][c] = lbl;
                add(lbl);
            }
        }
        // Tamanho fixo do painel (2 colunas x 8 linhas)
        int width = 2 * ICON_SIZE + 4;   // 2 colunas + espaçamento
        int height = 8 * ICON_SIZE + 14; // 8 linhas + espaçamento + título
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
    }
//================= MÉTODO DE CAPTURA DE PEÇAS ====================
    public void addCapturedPiece(ImageIcon pieceIcon) {
        if(nextIndex >= 18) return;
        
        int r = nextIndex / 2;
        int c = nextIndex % 2;
        Image img = pieceIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        slots[r][c].setIcon(new ImageIcon(img));
        nextIndex++;
        revalidate();
        repaint();
    }

    public void clear() {
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 2; c++) {
                slots[r][c].setIcon(null);
            }
        }
    
        nextIndex = 0;
        revalidate();
        repaint();
    }
}