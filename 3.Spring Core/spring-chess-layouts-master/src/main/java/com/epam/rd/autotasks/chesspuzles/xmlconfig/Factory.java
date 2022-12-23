package com.epam.rd.autotasks.chesspuzles.xmlconfig;

import com.epam.rd.autotasks.chesspuzles.Cell;
import com.epam.rd.autotasks.chesspuzles.ChessPiece;

public class Factory {
    public ChessPiece createChessPiece(char letter,int number,char value){
        return new ChessPiece() {
            @Override
            public Cell getCell() {
                return Cell.cell(letter, number);
            }

            @Override
            public char toChar() {
                return value;
            }
        };
    }
}
