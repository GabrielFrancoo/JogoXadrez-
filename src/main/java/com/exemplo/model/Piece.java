package com.exemplo.model;
//importa java util lista, coleção de dados, armazena os possiveis movimentos
import java.util.List;

//classe abstrata, não pode ser instanciada diretamente
public abstract class Piece {
    protected Position position;
    //true se a peça for branca, false se for preta
    protected boolean isWhite;
    //tabuleiro onde a peça está localizada
    protected Board board;
    
    //construtor da peça, recebe a cor e o tabuleiro
    public Piece(boolean isWhite,Board board){
        this.board = board;
        this.isWhite = isWhite;
    }
    //executar o movimento no tabuleiro
    
    //retorna a cor da peça
    public boolean isWhite(){
        return isWhite;
    }
    //retorna a posição atual da peça
    public Position getPosition(){
        return position;
    }
    //define a posição atual da peça
    public void setPosition(Position position){
        this.position = position;
    }
    //método abstrato que deve ser implementado pelas subclasses, retorna os movimentos possíveis da peça
    public abstract List getPossibleMoves();

    //verifica se a peça pode se mover para a posição especificada
    public boolean canMoveTo(Position position){
        List possibleMoves = getPossibleMoves();
        return possibleMoves.contains(position);
    }
    
    //método abstrato que deve ser implementado pelas subclasses, retorna o símbolo da peça
    public abstract String getSymbol();
}

