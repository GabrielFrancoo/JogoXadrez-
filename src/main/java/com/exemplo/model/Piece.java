package com.exemplo.model;
import java.util.List;


public abstract class Piece {
    protected Position position;
    protected boolean isWhite;
    protected Board board;

    public Piece(boolean isWhite,Board board){
        this.board = board;
        this.isWhite = isWhite;
    }

    public boolean isWhite(){
        return isWhite;
    }

    public Position getPosition(){
        return position;
    }

    public void setPosition(Position position){
        this.position = position;
    }
    public abstract List getPossibleMoves();

    public boolean canMoveTo(Position position){
        List possibleMoves = getPossibleMoves();
        return possibleMoves.contains(position);
    }

    public abstract String getSymbol();
}

