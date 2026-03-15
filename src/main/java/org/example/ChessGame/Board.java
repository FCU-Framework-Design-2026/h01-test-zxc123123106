package org.example.ChessGame;

public class Board {

    private Chess[][] board;
    private int row;
    private int col;


    public Board(int row, int col){
        this.board = new Chess[row][col];
        this.row = row;
        this.col = col;

        //init board
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.board[i][j] = new Chess('_');
            }
        }
    }

    public void showBoard() {
        int x = 0;
        System.out.println("  1  2  3  4  5  6  7  8");
        for (Chess[] rows : this.board) {
            System.out.printf("%c ", 'A'+(x++));
            for (Chess ele : rows) {
                System.out.printf("%c  ", ele.getInfo());
            }
            System.out.println();
        }
    }

    public void updateBoard(int row, int col, Chess item){
        this.board[row][col] = item;
        item.updateChess(row, col);
    }

    public char getPosInfo(int row, int col){
        return board[row][col].getInfo();
    }

    public Chess getPosChess(int row, int col){
        return board[row][col];
    }

    public int checkOver(Player p1, Player p2){
        if(p1.isDie()) return 1;
        else if(p2.isDie()) return 2;
        else return 0;
    }

}
