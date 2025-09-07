package com.exemplo.model;

public class Position {
    private int row;
    private int column;

    // Construtor
    public Position(int row, int column){
        this.row = row;
        this.column = column;
    }
    // Getters 
    public int getRow(){
        return row;
    }
    // Getters
    public int getColumn(){
        return column;
    }
    //setters
    public void setRow(int row){
        this.row = row;
    }
    //setters
    public void setColumn(int column){
        this.column = column;
    }
    // Verifica se a posição está dentro dos limites do tabuleiro
    public boolean isValido(){
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }
    @Override
    public int hashCode(){
    return 31 * row + column;
    }
    @Override
    public String toString() {
    return "(" + row + ", " + column + ")";
    }
    // Sobrescreve o método equals para comparar posições
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return row == other.row && column == other.column;
    }
    
}
