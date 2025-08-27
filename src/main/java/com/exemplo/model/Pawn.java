package com.exemplo.model;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Board board, boolean isWhite) {
        super(isWhite, board);
    }

    @Override
    public List getPossibleMoves() {
        List moves = new ArrayList<>();

        int direction = isWhite ? -1 : 1; // Peças brancas movem para cima (-1), pretas para baixo (+1)
        int startRow = isWhite ? 6 : 1; // Linha inicial para peões brancos e pretos

        // Movimento para frente
        Position oneStepForward = new Position(position.getRow() + direction, position.getColumn());
        if (oneStepForward.isValido() && board.getPieceAt(oneStepForward) == null) {
            moves.add(oneStepForward);

            // Movimento de dois passos na primeira jogada
            Position twoStepsForward = new Position(position.getRow() + 2 * direction, position.getColumn());
            if (position.getRow() == startRow && board.getPieceAt(twoStepsForward) == null) {
                moves.add(twoStepsForward);
            }
        }

        // Capturas diagonais
        int[] diagonalOffsets = {-1, 1};
        for (int offset : diagonalOffsets) {
            Position diagonalCapture = new Position(position.getRow() + direction, position.getColumn() + offset);
            if (diagonalCapture.isValido()) {
                Piece pieceAtDiagonal = board.getPieceAt(diagonalCapture);
                if (pieceAtDiagonal != null && pieceAtDiagonal.isWhite() != isWhite) {
                    moves.add(diagonalCapture);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}