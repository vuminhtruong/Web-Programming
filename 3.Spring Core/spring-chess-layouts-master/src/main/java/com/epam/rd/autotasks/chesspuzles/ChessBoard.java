package com.epam.rd.autotasks.chesspuzles;

import java.util.*;
import java.util.stream.Collectors;

public interface ChessBoard {

    List<List<Character>> board = new ArrayList<>();

    static ChessBoard of(Collection<ChessPiece> pieces){
        board.clear();
        for(int i=0;i<8;i++){
            List<Character> row = new ArrayList<>();
            for(int j=0;j<8;j++){
                row.add('.');
            }
            board.add(row);
        }
        for(ChessPiece chessPiece : pieces){
            Cell position = chessPiece.getCell();
            char chess = chessPiece.toChar();
            board.get(8-position.number).set(position.letter-'A', chess);
        }
        return () -> board.stream().map(x->{
            String s="";
            for(Character c : x)
                s+=c;
            return s;
        }).collect(Collectors.joining("\n"));
    }

    String state();
}
