package com.exemplo.model;

public class Position {
    private int row;
    private int column;
    private Position position;

    public Position(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public boolean isValido(){
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return row == other.row && column == other.column;
    }
    
}
