// ========================= src/view/ChessGUI.java =========================
package view;

import controller.Game;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.text.StyledDocument;
import model.board.Position;
import model.pieces.Pawn;
import model.pieces.Piece;

public class ChessGUI extends JFrame {
    
    private static final long serialVersionUID = 1L; // evita warning de serialização

    // --- Config de cores/styles ---
    //branco
    private static final Color LIGHT_SQ = new Color(240, 217, 181); // Bege claro
    private static final Color DARK_SQ  = new Color(128, 128, 128); // Cinza
    private static final Color HILITE_SELECTED = new Color(50, 120, 220);
    private static final Color HILITE_LEGAL    = new Color(20, 140, 60);
    private static final Color HILITE_LASTMOVE = new Color(220, 170, 30);

    private static final Border BORDER_SELECTED = new MatteBorder(3,3,3,3, HILITE_SELECTED);
    private static final Border BORDER_LEGAL    = new MatteBorder(3,3,3,3, HILITE_LEGAL);
    private static final Border BORDER_LASTMOVE = new MatteBorder(3,3,3,3, HILITE_LASTMOVE);

    private final Game game;
    private final  CapturedPiecesPanel whiteCapturedPanel;
    private final  CapturedPiecesPanel blackCapturedPanel; 
    private final JPanel boardPanel;
    private final JButton[][] squares = new JButton[8][8];

    private final JLabel status;
    private final JLabel whiteTimeLabel;
    private final JLabel blackTimeLabel;
    private final JTextPane history;
    private final JScrollPane historyScroll;
        



    // Menu / controles
    private JCheckBoxMenuItem pcAsBlack;
    private JCheckBoxMenuItem pcAsWhite;
    private JSpinner whiteDepSpinner;
    private JSpinner blackDepSpinner;
    private JMenuItem newGameItem, undoItem, quitItem;

    // Seleção atual e movimentos legais
    private Position selected = null;
    private List<Position> legalForSelected = new ArrayList<>();

    // Realce do último lance
    private Position lastFrom = null, lastTo = null;

    // IA
    private boolean aiThinking = false;
    private final Random rnd = new Random();
    
    // Timer
    private Timer gameTimer;

    public ChessGUI() {
        super("ChessGame");

        whiteCapturedPanel = new CapturedPiecesPanel();
        blackCapturedPanel = new CapturedPiecesPanel();
        // Look&Feel Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        this.game = new Game();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));


        // Menu
        setJMenuBar(buildMenuBar());

        // Painel do tabuleiro (8x8)
        boardPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        boardPanel.setBackground(Color.DARK_GRAY);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        // Cria botões das casas
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                final int rr = r;
                final int cc = c;
                JButton b = new JButton();
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setFocusPainted(false);
                b.setOpaque(true);
                b.setBorderPainted(true);
                b.setContentAreaFilled(true);
                b.setFont(b.getFont().deriveFont(Font.BOLD, 24f)); // fallback com Unicode
                b.addActionListener(e -> handleClick(new Position(rr, cc)));
                squares[r][c] = b;
                boardPanel.add(b);
            }
        }

        // Barra inferior de status
        status = new JLabel("Vez: Brancas");
        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        
        // Timer labels
        whiteTimeLabel = new JLabel("Brancas: 00:00");
        blackTimeLabel = new JLabel("Pretas: 00:00");
        whiteTimeLabel.setFont(whiteTimeLabel.getFont().deriveFont(Font.BOLD, 14f));
        blackTimeLabel.setFont(blackTimeLabel.getFont().deriveFont(Font.BOLD, 14f));
        whiteTimeLabel.setForeground(Color.BLUE);
        blackTimeLabel.setForeground(Color.RED);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
       //Painel de peças capturadas
       JPanel capturedPanel = new JPanel();
       capturedPanel.setLayout(new BoxLayout(capturedPanel, BoxLayout.Y_AXIS));
       capturedPanel.setBorder(BorderFactory.createTitledBorder("Capturadas"));
       
       whiteCapturedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       blackCapturedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

       whiteCapturedPanel.setMaximumSize((new Dimension(Integer.MAX_VALUE, whiteCapturedPanel.getPreferredSize().height)));
       blackCapturedPanel.setMaximumSize((new Dimension(Integer.MAX_VALUE, whiteCapturedPanel.getPreferredSize().height)));
      
       capturedPanel.add(whiteCapturedPanel);
       capturedPanel.add(Box.createVerticalStrut(10)); // Espaço entre os painéis
       capturedPanel.add(blackCapturedPanel);
       
       capturedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
       leftPanel.add(capturedPanel);
        
        // Histórico
        history = new JTextPane();
        history.setEditable(false);
        history.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        history.setPreferredSize(new Dimension(200, 300));
        historyScroll = new JScrollPane(history);
    
        
        // Layout principal: tabuleiro à esquerda, histórico à direita
        JPanel rightPanel = new JPanel(new BorderLayout(6, 6));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        JLabel histLabel = new JLabel("Histórico de lances:");
        histLabel.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));
        rightPanel.add(histLabel, BorderLayout.NORTH);
        rightPanel.add(historyScroll, BorderLayout.CENTER);
        rightPanel.add(buildSideControls(), BorderLayout.SOUTH);

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
        timerPanel.add(whiteTimeLabel);
        timerPanel.add(blackTimeLabel);
        timerPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(leftPanel, BorderLayout.WEST);// Painel esquerdo
        add(boardPanel, BorderLayout.CENTER);
        add(timerPanel, BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);


        // Atualiza ícones conforme a janela/painel muda de tamanho
        boardPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                refresh(); // recarrega ícones ajustando o tamanho
            }
        });

        setMinimumSize(new Dimension(1200, 800));    
        setLocationRelativeTo(null);
        pack(); 
        setVisible(true);
        // Atalhos: Ctrl+N, Ctrl+Q
        setupAccelerators();

        setVisible(true);
        startGameTimer();
        refresh();
        pcAsBlack.setSelected(false);
        pcAsWhite.setSelected(false);
        startAITimer(); // inicia o timer da IA
        maybeTriggerAI(); // caso o PC jogue primeiro
    }

    // ----------------- Menus e controles -----------------

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu gameMenu = new JMenu("Jogo");

        newGameItem = new JMenuItem("Novo Jogo");
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        newGameItem.addActionListener(e -> doNewGame());

        undoItem = new JMenuItem("Desfazer");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        undoItem.addActionListener(e -> doUndo());

        pcAsWhite = new JCheckBoxMenuItem("PC joga com as Brancas");
        pcAsWhite.setSelected(false);

        pcAsBlack = new JCheckBoxMenuItem("PC joga com as Pretas");
        pcAsBlack.setSelected(false);

        JMenu depthMenu = new JMenu("Profundidade IA");
        whiteDepSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        whiteDepSpinner.setToolTipText("Profundidade da IA para as Brancas (1-4)");
        depthMenu.add(whiteDepSpinner);

        blackDepSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        blackDepSpinner.setToolTipText("Profundidade da IA para as Pretas (1-4)");
        depthMenu.add(blackDepSpinner);

        quitItem = new JMenuItem("Sair");
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        quitItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        gameMenu.add(newGameItem);
        gameMenu.add(undoItem);
        gameMenu.addSeparator();
        gameMenu.add(pcAsBlack);
        gameMenu.add(pcAsWhite);
        gameMenu.add(depthMenu);
        gameMenu.addSeparator();
        gameMenu.add(quitItem);

        mb.add(gameMenu);
        return mb;
    }

    private JPanel buildSideControls() {
        JPanel panel = new JPanel(new GridLayout(3,2,10,5));

        JCheckBox cb = new JCheckBox("PC (Pretas)");
        cb.setSelected(pcAsBlack.isSelected());
        cb.addActionListener(e -> pcAsBlack.setSelected(cb.isSelected()));
        panel.add(cb);

        panel.add(new JLabel("LVL Pretas:"));
        blackDepSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        panel.add(blackDepSpinner);

        JCheckBox cb2 = new JCheckBox("PC (Brancas)");
        cb2.setSelected(pcAsWhite.isSelected());
        cb2.addActionListener(e -> pcAsWhite.setSelected(cb2.isSelected()));
        panel.add(cb2);

        panel.add(new JLabel("LVL Brancas:"));
        whiteDepSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        panel.add(whiteDepSpinner);



        JButton btnNew = new JButton("Novo Jogo");
        btnNew.addActionListener(e -> doNewGame());
        panel.add(btnNew);

        return panel;
    }

    private void setupAccelerators() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "newGame");
        getRootPane().getActionMap().put("newGame", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { doNewGame(); }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "undo");
        getRootPane().getActionMap().put("undo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { doUndo(); }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "quit");
        getRootPane().getActionMap().put("quit", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(ChessGUI.this, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    private void doNewGame() {
        selected = null;
        legalForSelected.clear();
        lastFrom = lastTo = null;
        aiThinking = false;
        game.newGame();
        startGameTimer();
        whiteCapturedPanel.clear();
        blackCapturedPanel.clear();

        startGameTimer();
        refresh();
        maybeTriggerAI();
    }
    
    private void doUndo() {
        if (game.undoLastMove()) {
            selected = null;
            legalForSelected.clear();
            lastFrom = lastTo = null;
            refresh();
            status.setText("Movimento desfeito");
        } else {
            status.setText("Nenhum movimento para desfazer");
        }
    }

    // ----------------- Interação de tabuleiro -----------------

private void handleClick(Position clicked) {
    if (game.isGameOver() || aiThinking) return;

    if ((pcAsBlack.isSelected() && !game.whiteToMove()) ||
        (pcAsWhite.isSelected() && game.whiteToMove()))
    return;

    Piece p = game.board().get(clicked);

    if (selected == null) {
        if (p != null && p.isWhite() == game.whiteToMove()) {
            selected = clicked;
            legalForSelected = game.legalMovesFrom(selected);
        }
    } else {
        List<Position> legals = game.legalMovesFrom(selected);
        if (legals.contains(clicked)) {
            Character promo = null;
            Piece moving = game.board().get(selected);
            Piece captured = game.board().get(clicked); // PEÇA QUE SERÁ CAPTURADA

            if (moving instanceof Pawn && game.isPromotion(selected, clicked)) {
                promo = askPromotion();
            }

            lastFrom = selected;
            lastTo = clicked;

            game.move(selected, clicked, promo);

            // ===== ADICIONA AO PAINEL DE PEÇAS CAPTURADAS =====
        if (captured != null) {
            int iconSize = 32; 
            ImageIcon icon = ImageUtil.getPieceIcon(captured.isWhite(), captured.getSymbol().charAt(0), iconSize);
            if (captured.isWhite()) {
                whiteCapturedPanel.addCapturedPiece(icon);
            } else {
                blackCapturedPanel.addCapturedPiece(icon);
            }
        }
            // ================================================

            selected = null;
            legalForSelected.clear();

            refresh();
            maybeAnnounceEnd();
            maybeTriggerAI();
            return;
        } else if (p != null && p.isWhite() == game.whiteToMove()) {
            selected = clicked;
            legalForSelected = game.legalMovesFrom(selected);
        } else {
            selected = null;
            legalForSelected.clear();
        }
    }
    refresh();
}

    private Character askPromotion() {
        String[] opts = {"Rainha", "Torre", "Bispo", "Cavalo"};
        int ch = JOptionPane.showOptionDialog(
                this,
                "Escolha a peça para promoção:",
                "Promoção",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]
        );
        return switch (ch) {
            case 1 -> 'R';
            case 2 -> 'B';
            case 3 -> 'N';
            default -> 'Q';
        };
    }

    // ----------------- IA (não bloqueante) -----------------

private void maybeTriggerAI() {
    if (game.isGameOver()) return;

    boolean aiWhite = pcAsWhite.isSelected();
    boolean aiBlack = pcAsBlack.isSelected();

    // Verifica se é a vez da IA
    if ((game.whiteToMove() && !aiWhite) || (!game.whiteToMove() && !aiBlack)) return;

    aiThinking = true;
    status.setText("Vez: " + (game.whiteToMove() ? "Brancas" : "Pretas") + " — PC pensando...");
    
    int depth;
        if (game.whiteToMove()){
            depth = (Integer) whiteDepSpinner.getValue();
        } else {
            depth = (Integer) blackDepSpinner.getValue();

        }
    
    boolean isWhiteTurn = game.whiteToMove();


    new SwingWorker<Void, Void>() {
        Position aiFrom, aiTo;


       /* @Override
         Subsittuir por minimax depois
        /*protected Void doInBackground() {
            var allMoves = collectAllLegalMovesForSide(isWhiteTurn); // agora pega lado correto
            if (allMoves.isEmpty()) return null;

            int bestScore = Integer.MIN_VALUE;
            List<Move> bestList = new ArrayList<>();

            for (Move mv : allMoves) {
                int score = 0;
                Piece target = game.board().get(mv.to);
                if (target != null) score += pieceValue(target); // valor captura
                score += centerBonus(mv.to);
                score += (depth - 1) * 2;

                if (score > bestScore) {
                    bestScore = score;
                    bestList.clear();
                    bestList.add(mv);
                } else if (score == bestScore) {
                    bestList.add(mv);
                }
          
            
            if (!bestList.isEmpty()) {
                Move chosen = bestList.get(rnd.nextInt(bestList.size()));
                aiFrom = chosen.from;
                aiTo = chosen.to;
            }
            

            return null;
        }  }*/

        @Override
        protected Void doInBackground() {
            var allMoves = collectAllLegalMovesForSide(isWhiteTurn); // agora pega lado correto
            if (allMoves.isEmpty()) return null;
            int bestScore = Integer.MIN_VALUE;
            List<Move> bestList = new ArrayList<>();

            for (Move mv : allMoves) {
                int score = evaluateMove(mv.from, mv.to, depth, isWhiteTurn);

                if (score > bestScore) {
                    bestScore = score;
                    bestList.clear();
                    bestList.add(mv);
                } else if (score == bestScore) {
                    bestList.add(mv);
                }
            }
            if (depth == 1 && !bestList.isEmpty()) {
                Collections.shuffle(bestList); // IA nível 1 escolhe aleatório entre melhores
            }

            if (!bestList.isEmpty()) {
                Move chosen = bestList.get(rnd.nextInt(bestList.size()));
                aiFrom = chosen.from;
                aiTo = chosen.to;
            }

            return null;
        }

            @Override
            protected void done() {
                try { get(); } catch (Exception ignored) {}
                if (aiFrom != null && aiTo != null && !game.isGameOver() && game.whiteToMove() == isWhiteTurn) {
                    Character promo = null;
                    Piece moving = game.board().get(aiFrom);
                    if (moving instanceof Pawn && game.isPromotion(aiFrom, aiTo)) promo = 'Q';
                   
                    Piece captured = game.board().get(aiTo); // PEÇA QUE SERÁ CAPTURADA
                    game.move(aiFrom, aiTo, promo);

                    if (captured != null) {
                        ImageIcon icon = ImageUtil.getPieceIcon(captured.isWhite(), captured.getSymbol().charAt(0), 32);
                        if (captured.isWhite()) {
                            whiteCapturedPanel.addCapturedPiece(icon);
                        } else {
                            blackCapturedPanel.addCapturedPiece(icon);
                        }
                    }

                    lastFrom = aiFrom;
                    lastTo = aiTo;
                }
                try{
                    Thread.sleep(1);
                }catch (InterruptedException ignored) {}

                aiThinking = false;
                refresh();
                maybeAnnounceEnd();
                maybeTriggerAI(); // próxima vez da IA
            }
        }.execute();
    }

    private static class Move {
        final Position from, to;
        Move(Position f, Position t) { this.from = f; this.to = t; }
    }

    private List<Move> collectAllLegalMovesForSide(boolean whiteSide) {
        List<Move> moves = new ArrayList<>();
        if (whiteSide != game.whiteToMove()) return moves;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position from = new Position(r, c);
                Piece piece = game.board().get(from);
                if (piece != null && piece.isWhite() == whiteSide) {
                    for (Position to : game.legalMovesFrom(from)) {
                        moves.add(new Move(from, to));
                    }
                }
            }
        }
        return moves;
    }

    private int pieceValue(Piece p) {
        if (p == null) return 0;
        switch (p.getSymbol()) {
            case "P": return 100;
            case "N":
            case "B": return 300;
            case "R": return 500;
            case "Q": return 900;
            case "K": return 20000;
        }
        return 0;
    }
    

    private int centerBonus(Position pos) {
        int r = pos.getRow(), c = pos.getColumn();
        if ((r==3 || r==4) && (c==3 || c==4)) return 10;
        if ((r>=2 && r<=5) && (c>=2 && c<=5)) return 4;
        return 0;
    }

    // ----------------- Atualização de UI -----------------

    private void refresh() {
        // 1) Cores base e limpa bordas
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean light = (r + c) % 2 == 0;
                Color base = light ? LIGHT_SQ : DARK_SQ;
                JButton b = squares[r][c];
                b.setBackground(base);
                b.setBorder(null);
                b.setToolTipText(null);
            }
        }

        // 2) Realce último lance
        if (lastFrom != null) squares[lastFrom.getRow()][lastFrom.getColumn()].setBorder(BORDER_LASTMOVE);
        if (lastTo   != null) squares[lastTo.getRow()][lastTo.getColumn()].setBorder(BORDER_LASTMOVE);

        // 3) Realce seleção e movimentos legais
        if (selected != null) {
            squares[selected.getRow()][selected.getColumn()].setBorder(BORDER_SELECTED);
            for (Position d : legalForSelected) {
                squares[d.getRow()][d.getColumn()].setBorder(BORDER_LEGAL);
            }
        }

        // 4) Ícones das peças (ou Unicode como fallback)
        int iconSize = computeSquareIconSize();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = game.board().get(new Position(r, c));
                JButton b = squares[r][c];

                if (p == null) {
                    b.setIcon(null);
                    b.setText("");
                    continue;
                }

                char sym = p.getSymbol().charAt(0);
                ImageIcon icon = ImageUtil.getPieceIcon(p.isWhite(), sym, iconSize);
                if (icon != null) {
                    b.setIcon(icon);
                    b.setText("");
                } else {
                    b.setIcon(null);
                    b.setText(toUnicode(p.getSymbol(), p.isWhite()));
                }
            }
        }

        // 5) Status e histórico
        String side = game.whiteToMove() ? "Brancas" : "Pretas";
        String chk = game.inCheck(game.whiteToMove()) ? " — Xeque!" : "";
        if (aiThinking) chk = " — PC pensando...";
        status.setText("Vez: " + side + chk);
        
        // Update undo button state
        undoItem.setEnabled(game.canUndo());

        // Limpar histórico
        history.setText("");
        
        var hist = game.history();
        StyledDocument doc = history.getStyledDocument();
        
        // Estilos para cores
        javax.swing.text.Style whiteStyle = history.addStyle("white", null);
        javax.swing.text.StyleConstants.setForeground(whiteStyle, Color.BLUE);
        javax.swing.text.StyleConstants.setBold(whiteStyle, true);
        
        javax.swing.text.Style blackStyle = history.addStyle("black", null);
        javax.swing.text.StyleConstants.setForeground(blackStyle, Color.RED);
        javax.swing.text.StyleConstants.setBold(blackStyle, true);
        
        javax.swing.text.Style numberStyle = history.addStyle("number", null);
        javax.swing.text.StyleConstants.setForeground(numberStyle, Color.BLACK);
        javax.swing.text.StyleConstants.setBold(numberStyle, true);
        
        try {
            for (int i = 0; i < hist.size(); i++) {
                if (i % 2 == 0) {
                    // Número do movimento
                    String moveNumber = (i / 2) + 1 + ". ";
                    doc.insertString(doc.getLength(), moveNumber, numberStyle);
                }
                
                // Movimento com cor
                String move = hist.get(i) + " ";
                if (i % 2 == 0) {
                    // Movimento das brancas - azul
                    doc.insertString(doc.getLength(), move, whiteStyle);
                } else {
                    // Movimento das pretas - vermelho
                    doc.insertString(doc.getLength(), move, blackStyle);
                    doc.insertString(doc.getLength(), "\n", null);
                }
            }
        } catch (Exception e) {
            // Fallback para texto simples
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hist.size(); i++) {
                if (i % 2 == 0) sb.append((i / 2) + 1).append('.').append(' ');
                sb.append(hist.get(i)).append(' ');
                if (i % 2 == 1) sb.append('\n');
            }
            history.setText(sb.toString());
        }
        
        history.setCaretPosition(history.getDocument().getLength());
    }

    private void maybeAnnounceEnd() {
        if (!game.isGameOver()) return;
        String msg;
        if (game.inCheck(game.whiteToMove())) {
            msg = "Xeque-mate! " + (game.whiteToMove() ? "Brancas" : "Pretas") + " estão em mate.";
        } else {
            msg = "Empate por afogamento (stalemate).";
        }
        JOptionPane.showMessageDialog(this, msg, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
    }

    private String toUnicode(String sym, boolean white) {
        return switch (sym) {
            case "K" -> white ? "\u2654" : "\u265A";
            case "Q" -> white ? "\u2655" : "\u265B";
            case "R" -> white ? "\u2656" : "\u265C";
            case "B" -> white ? "\u2657" : "\u265D";
            case "N" -> white ? "\u2658" : "\u265E";
            case "P" -> white ? "\u2659" : "\u265F";
            default -> "";
        };
    }

    private int computeSquareIconSize() {
        JButton b = squares[0][0];
        int w = Math.max(1, b.getWidth());
        int h = Math.max(1, b.getHeight());
        int side = Math.min(w, h);
        if (side <= 1) return 64;
        return Math.max(24, side - 8);
    }

    // ----------------- Timer methods -----------------
    
    private void startGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        gameTimer = new Timer(1000, e -> updateTimerDisplay()); // Update every second
        gameTimer.start();
    }
    
    private void updateTimerDisplay() {
        if (game.isGameOver()) {
            gameTimer.stop();
            return;
        }
        
        long whiteTime = game.getCurrentPlayerTime();
        long blackTime = game.getInactivePlayerTime();
        
        if (game.whiteToMove()) {
            whiteTime = game.getCurrentPlayerTime();
            blackTime = game.getInactivePlayerTime();
        } else {
            whiteTime = game.getInactivePlayerTime();
            blackTime = game.getCurrentPlayerTime();
        }
        
        whiteTimeLabel.setText("Brancas: " + game.formatTime(whiteTime));
        blackTimeLabel.setText("Pretas: " + game.formatTime(blackTime));
        
        // Highlight current player's timer
        if (game.whiteToMove()) {
            whiteTimeLabel.setFont(whiteTimeLabel.getFont().deriveFont(Font.BOLD, 16f));
            blackTimeLabel.setFont(blackTimeLabel.getFont().deriveFont(Font.PLAIN, 14f));
        } else {
            whiteTimeLabel.setFont(whiteTimeLabel.getFont().deriveFont(Font.PLAIN, 14f));
            blackTimeLabel.setFont(blackTimeLabel.getFont().deriveFont(Font.BOLD, 16f));
        }
    }
//------------------ AI Timer -----------------
    private void startAITimer() {
    Timer aiTimer = new Timer(500, e -> {
        if (!aiThinking && !game.isGameOver()) {
            // Só dispara se for vez de uma IA marcada
            boolean aiWhite = pcAsWhite.isSelected();
            boolean aiBlack = pcAsBlack.isSelected();
            if ((game.whiteToMove() && aiWhite) || (!game.whiteToMove() && aiBlack)) {
                maybeTriggerAI();
            }
        }
    });
    aiTimer.start();
}
    // ----------------- Minimax Evaluation -----------------
private int evaluateBoard(boolean forWhite) {
    if (game.isGameOver()) {
        if (game.inCheck(forWhite)) return -100000; // Nosso lado perdeu
        else return 0; // Empate por afogamento ou repetição
    }

    int score = 0;
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            Piece p = game.board().get(new Position(r, c));
            if (p != null) {
                int value = pieceValue(p);
                score += p.isWhite() == forWhite ? value : -value;
            }
        }
    }

    // Penaliza se o rei está em xeque
    if (game.inCheck(forWhite)) score -= 50;
    if (game.inCheck(!forWhite)) score += 50;

    return score;
}

private int evaluateMove(Position from, Position to, int depth, boolean isWhiteTurn) {
    Piece captured = game.board().get(to);
    game.move(from, to, null); // faz o movimento

    int score;
    if (depth == 1 || game.isGameOver()) {
        score = evaluateBoard(isWhiteTurn);
        if (captured != null) score += pieceValue(captured); // captura vale mais
    } else {
        int best = isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<Position> moves = game.legalMovesFrom(to);
        for (Position next : moves) {
            int val = evaluateMove(to, next, depth - 1, !isWhiteTurn);
            if (isWhiteTurn) best = Math.max(best, val);
            else best = Math.min(best, val);
        }
        score = best;
    }

    game.undoLastMove(); // desfaz o movimento
    return score;
}
        public static void main(String[] args) {
            SwingUtilities.invokeLater(ChessGUI::new);
        }
    }  
