package org.example.ChessGame;

import java.util.HashMap;

public class Chess {

    private final char chess;
    private int row;
    private int col;
    private static final HashMap<Character, Integer> LVL = new HashMap<>();    //chess rank

    public Chess(char name){
        this.chess = name;
        this.row = 0;
        this.col = 0;

        //init chess rank
        LVL.put('帥', 7);
        LVL.put('仕', 6);
        LVL.put('相', 5);
        LVL.put('俥', 4);
        LVL.put('碼', 3);
        LVL.put('炮', 2);
        LVL.put('兵', 1);
        LVL.put('將', 7);
        LVL.put('士', 6);
        LVL.put('象', 5);
        LVL.put('車', 4);
        LVL.put('馬', 3);
        LVL.put('砲', 2);
        LVL.put('卒', 1);
    }

    @Override
    public String toString(){
        return "" + this.chess;
    }

    public char getInfo(){
        return this.chess;
    }

    public boolean checkRank(char oppo){

        switch (this.chess){
            case '帥': case '將':
                return LVL.get(this.chess) >= LVL.get(oppo) &&
                        oppo != '卒' &&
                        oppo != '兵';
            case '仕': case '士':
            case '相': case '象':
            case '俥': case '車':
            case '碼': case '馬':
            case '炮': case '砲':
                return LVL.get(this.chess) >= LVL.get(oppo);
            case '兵': case '卒':
                return oppo == '帥' || oppo == '將';
            default:
                return false;
        }
    }

    public void updateChess(int nRow, int nCol){
        this.row = nRow;
        this.col = nCol;
    }

    public boolean nextDistIsAvail(int distR, int distC){
        int dr = Math.abs(distR - this.row);
        int dc = Math.abs(distC - this.col);
        int dt = dr + dc;

        return dr <= 1 && dc <= 1 && dt < 2 &&
                (dr + dc != 0);
    }
}
