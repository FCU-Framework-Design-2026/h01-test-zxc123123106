package org.example.ChessGame;

import java.io.IOException;
import java.util.*;

public class ChessGame {

    //scanner
    Scanner s = new Scanner(System.in);

    //board
    Board board = new Board(4, 8);

    //player
    Player p1 = new Player(1);
    Player p2 = new Player(2);
    Player player;
    Player opposite;

    //chess game
    int turn = 1;
    int action;
    //self coordinate
    int yRow;
    int yCol;
    //opposite coordinate
    int oRow;
    int oCol;
    String position;
    private static final HashMap<Character, Integer> AVAILABLE = new HashMap<>();     //available piece
    private static final Character[] SPICES = {'帥', '仕', '相', '俥', '碼', '炮', '兵', '將', '士', '象', '車', '馬', '砲', '卒'};

    public ChessGame(){
        //init all available Piece
        AVAILABLE.put('帥', 1);
        AVAILABLE.put('仕', 2);
        AVAILABLE.put('相', 2);
        AVAILABLE.put('俥', 2);
        AVAILABLE.put('碼', 2);
        AVAILABLE.put('炮', 2);
        AVAILABLE.put('兵', 5);
        AVAILABLE.put('將', 1);
        AVAILABLE.put('士', 2);
        AVAILABLE.put('象', 2);
        AVAILABLE.put('車', 2);
        AVAILABLE.put('馬', 2);
        AVAILABLE.put('砲', 2);
        AVAILABLE.put('卒', 5);
    }

    public void startGame(){

        System.out.println("================================================================================ Rules ================================================================================");
        System.out.println("(1) The player who first input is Player1, and the other is Player2.");
        System.out.println("(2) The actions divide into 'flip' and 'eat'.");
        System.out.println("(3) Enter 1 is flip. Enter 2 is eat.");
        System.out.println("    * Flip: the board will open a piece chosen by player.");
        System.out.println("    * Eat: choose a piece from your faction, and then choose a piece from opposing faction. If opposite can be eaten, your will replace it.");
        System.out.println("================================================================================ Rules ================================================================================");

        while (true) {
            try {
                if (this.turn == 1) {
                    this.player = p1;
                    this.opposite = p2;
                } else {
                    this.player = p2;
                    this.opposite = p1;
                }

                //show info
                System.out.println();
                p1.showFaction();
                p1.showCollection();
                board.showBoard();
                p2.showFaction();
                p2.showCollection();
                System.out.println();

                System.out.println("# Player" + this.turn + " turns #");
                System.out.print("Action (1 flip / 2 eat / 3 move / 0 game over): ");

                //check action is right
                if (!s.hasNextInt()) {
                    System.out.println("Error action.");
                    s.next();
                    continue;
                }

                this.action = s.nextInt();

                //check whether game over
                if(this.action == 0) {throw new IOException("Termination");}

                //check action is right
                if (this.action > 3) {
                    System.out.println("Error action.");
                    continue;
                }

                switch (this.action) {

                    // flip
                    case 1:

                        do {
                            System.out.print("Enter a position (A~D, 1~8, ex.A2, 00 game over): ");

                            this.position = s.next();

                            //check whether game over
                            if(this.position.equals("00")) {throw new IOException("Termination");}

                            //check position is right
                            if (!positionIsRight(this.position)) {
                                continue;
                            }

                            break;

                        } while (true);

                        try {
                            flip(position);     //flip chess and update board
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            continue;
                        }

                        break;

                    //eat
                    case 2:

                        do {

                            //choose your chess
                            System.out.print("Choose a position from your faction (A~D, 1~8, ex.A2, 00 game over): ");

                            this.position = s.next();

                            //check whether game over
                            if(this.position.equals("00")) {throw new IOException("Termination");}

                            yRow = this.position.charAt(0) - 'A';
                            yCol = this.position.charAt(1) - '1';

                            //check position is right
                            if (!positionIsRight(this.position)) {
                                continue;
                            }

                            //check chess is your faction
                            if (!this.player.checkChessIsYour(board.getPosInfo(yRow, yCol))) {
                                System.out.println("The picked chess is not your faction");
                                continue;
                            }

                            //choose opposing chess
                            System.out.print("Choose a position from opposite faction (A~D, 1~8, ex.A2, 00 game over): ");

                            this.position = s.next();

                            //check whether game over
                            if(this.position.equals("00")) {throw new IOException("Termination");}

                            oRow = this.position.charAt(0) - 'A';
                            oCol = this.position.charAt(1) - '1';

                            if (!positionIsRight(this.position)) {continue;}     //check position is right

                            //check chess is opposite faction
                            if (!this.opposite.checkChessIsYour(board.getPosInfo(oRow, oCol))) {
                                System.out.println("The opposing chess is not opposite faction");
                                continue;
                            }

                            //check you can eat opposite
                            if(!this.board.getPosChess(yRow, yCol).nextDistIsAvail(oRow, oCol)){
                                System.out.println("The eat is invalid");
                                continue;
                            }

                            break;

                        } while (true);

                        try {
                            this.opposite.chessDie(this.board.getPosInfo(oRow, oCol));
                            eatChess(yRow, yCol, oRow, oCol);

                            switch (this.board.checkOver(p1, p2)){
                                case 1:
                                    throw new IOException("Player 2 win");
                                case 2:
                                    throw new IOException("Player 1 win");
                            }

                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            continue;
                        }

                        break;

                    //move
                    case 3:

                        do{

                            //choose a chess to move
                            System.out.print("Choose a chess you want to move (A~D, 1~8, ex.A2, 00 game over): ");

                            this.position = s.next();

                            //check whether game over
                            if(this.position.equals("00")) {throw new IOException("Termination");}

                            yRow = this.position.charAt(0) - 'A';
                            yCol = this.position.charAt(1) - '1';

                            if (!positionIsRight(this.position)) {continue;}     //check position is right

                            //check chess is your faction
                            if (!this.player.checkChessIsYour(board.getPosInfo(yRow, yCol))) {
                                System.out.println("The picked chess is not your faction");
                                System.out.println("Player: " + this.player.number);
                                continue;
                            }

                            //choose a destination
                            System.out.print("Choose a place you want to go (A~D, 1~8, ex.A2, 00 game over): ");

                            this.position = s.next();

                            //check whether game over
                            if(this.position.equals("00")) {throw new IOException("Termination");}

                            oRow = this.position.charAt(0) - 'A';
                            oCol = this.position.charAt(1) - '1';

                            //check position is right
                            if (!positionIsRight(this.position)) {continue;}

                            //check destination is not empty
                            if(this.board.getPosInfo(oRow, oCol) != 'X'){
                                System.out.println("The destination is not empty");
                                continue;
                            }

                            //check destination is available
                            if(!this.board.getPosChess(yRow, yCol).nextDistIsAvail(oRow, oCol)){
                                System.out.println("The destination is invalid");
                                continue;
                            }

                            break;

                        }while (true);

                        this.board.updateBoard(oRow, oCol, this.board.getPosChess(yRow, yCol));
                        this.board.updateBoard(yRow, yCol, new Chess('X'));

                        break;

                    default:
                        System.out.println("Error action. Please enter again.");
                        continue;
                }

                turn = (turn == 1) ? 2 : 1;

            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Game over" + e.getMessage());
                s.close();
                break;
            }
        }
    }

    private Chess pickPiece() {
        if (AVAILABLE.isEmpty()) {
            System.out.println("No more chess can be flipped.");
            throw new RuntimeException("No more piece");
        }

        ArrayList<Character> pool = new ArrayList<>();
        for(Map.Entry<Character, Integer> entry : AVAILABLE.entrySet()){
            char spice = entry.getKey();
            int count = entry.getValue();
            for(int i = 0; i < count; i++){
                pool.add(spice);
            }
        }

        //pick random one
        Random random = new Random();
        char piece = pool.get(random.nextInt(pool.size()));
        Chess chess = new Chess(piece);

        //update available piece
        AVAILABLE.put(piece, AVAILABLE.get(piece)-1);
        if(AVAILABLE.get(piece) == 0) {
            AVAILABLE.remove(piece);
        }

        return chess;
    }

    private void flip(String pos){
        int row = pos.charAt(0) - 'A';
        int col = pos.charAt(1) - '1';

        try{
            Chess flipped = pickPiece();

            //check this position is empty
            if(board.getPosInfo(row, col) != '_'){
                System.out.println("This position is not empty.");
                throw new RuntimeException("Not empty");
            }

            board.updateBoard(row, col, flipped);

        } catch (RuntimeException e){
            e.printStackTrace();
            System.out.println("Enter action again.");
            throw new RuntimeException("Enter action again");
        }
    }

    private boolean positionIsRight(String pos){
        if(pos.length() != 2){
            System.out.println("Error position");
            return false;
        } else {
            int row = pos.charAt(0) - 'A';
            int col = pos.charAt(1) - '1';
            if(row > 3 || col > 7){
                System.out.println("Error position");
                return false;
            }
            return true;
        }
    }

    public void eatChess(int yRow, int yCol, int oRow, int oCol){
        Chess you = this.board.getPosChess(yRow, yCol);
        Chess opposite = this.board.getPosChess(oRow, oCol);

        //check you can eat opposite
        if(!you.checkRank(opposite.getInfo())){
            System.out.println("You can not eat this chess, choose again.");
            throw new RuntimeException("lvl error");
        } else {
            this.board.updateBoard(yRow, yCol, new Chess('X'));
            this.board.updateBoard(oRow, oCol, you);
            player.addCollection(opposite);
        }
    }

}

