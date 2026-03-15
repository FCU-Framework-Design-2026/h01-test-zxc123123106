package org.example.ChessGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Player {

    int number;
    private final Character[] pieces;
    private final ArrayList<Chess> collection = new ArrayList<Chess>();
    private final HashMap<Character, Integer> alive = new HashMap<>();

    public Player(int num){
        this.number = num;
        if (num == 1) { //紅方
            this.pieces = new Character[]{'帥','仕','相','俥','碼','炮','兵'};
            this.alive.put('帥', 1);
            this.alive.put('仕', 2);
            this.alive.put('相', 2);
            this.alive.put('俥', 2);
            this.alive.put('碼', 2);
            this.alive.put('炮', 2);
            this.alive.put('兵', 5);
        }
        else {  //黑方
            this.pieces = new Character[]{'將','士','象','車','馬','砲','卒'};
            this.alive.put('將', 1);
            this.alive.put('士', 2);
            this.alive.put('象', 2);
            this.alive.put('車', 2);
            this.alive.put('馬', 2);
            this.alive.put('砲', 2);
            this.alive.put('卒', 5);
        }
    }

    public void showFaction(){
        System.out.println("Player" + this.number + " faction: " + Arrays.toString(this.pieces));
    }

    public boolean checkChessIsYour(char item){
        ArrayList<Character> pieceList = new ArrayList<>(Arrays.asList(this.pieces));
        return pieceList.contains(item);
    }

    public void addCollection(Chess item){
        this.collection.add(item);
    }

    public void showCollection(){
        System.out.println("Player" + this.number + " collections: " + this.collection);
    }

    public void chessDie(char die){
        this.alive.put(die, this.alive.get(die)-1);
        if(this.alive.get(die) == 0) {this.alive.remove(die);}
    }

    public boolean isDie(){
        return this.alive.isEmpty();
    }
}
