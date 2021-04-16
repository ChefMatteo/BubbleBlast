import java.util.*;
import java.util.stream.*;

public class GameGrid {
    private static GameGrid instance = null;
    public static GameGrid getGameGrid() {
        if (instance == null) {
            instance = new GameGrid();
        }
        return instance;
    }

    //Attributes
    private List<List<Bubble>> grid;
    private List<List<Bubble>> gridClone;
    private int movesLeft;
    private StringBuilder movesOfGame = new StringBuilder();

    //Constructor
    private GameGrid() {
        gridGeneration();
    }

    //Getters and setters
    public int getMovesLeft() {
        return movesLeft;
    }
    public List<List<Bubble>> getGrid() {
        return grid;
    }
    public StringBuilder getMovesOfGame() {
        return movesOfGame;
    }
    public int numberedList = 1;

    //Grid methods
    private void gridGeneration() {
        //Stream che crea 5 List.size()=5
        grid = IntStream.range(1, 6).mapToObj(a ->
                //Stream che crea 6 bolle casuali e le inserisce nelle liste del primo stream
                IntStream.range(1, 7)
                        .mapToObj(b -> new Bubble((new Random().nextInt(3)), a, b))
                        .toList())
                .toList();
        gridClone = IntStream.range(0, 5).mapToObj(a ->
                //Stream che crea 6 bolle casuali e le inserisce nelle liste del primo stream
                IntStream.range(0, 6)
                        .mapToObj(b -> grid.get(a).get(b).bubbleClone())
                        .toList())
                .toList();

    }

    public void gridStamp(){
        //Stream per ogni lista
        grid.stream()
                .map(a -> a.stream()
                        //Per ogni lista prende solo la view delle bolle
                        .map(Bubble::getBubbleView).collect(Collectors.toList()))
                .forEach(System.out::println);
        System.out.println();
    }

    public boolean move(String coordinates) {
        String [] ACCEPTED_GRID_COORDINATES = {
                "A1","A2","A3","A4","A5","A6",
                "B1","B2","B3","B4","B5","B6",
                "C1","C2","C3","C4","C5","C6",
                "D1","D2","D3","D4","D5","D6",
                "E1","E2","E3","E4","E5","E6"};
/*
        Controllo per movimenti rimanenti
*/
        if (movesLeftController()) {
            int y;
            int x;
/*
        Controllo per la validità delle coordinate
*/
            if (Arrays.asList(ACCEPTED_GRID_COORDINATES).contains(coordinates.toUpperCase())) {
/*
            Trasformazione coordinate in numeri validi per matrice
*/
                switch (coordinates.toUpperCase().charAt(0)) {
                    case 'A' -> x = 0;
                    case 'B' -> x = 1;
                    case 'C' -> x = 2;
                    case 'D' -> x = 3;
                    case 'E' -> x = 4;
                    default -> throw new IllegalStateException("Unexpected value: " + coordinates.toLowerCase().charAt(0));
                }
                y = Integer.parseInt(coordinates.substring(1)) - 1;
                movesOfGame
                        .append(numberedList)
                        .append(") Inserite le coordinate x: ")
                        .append(x)
                        .append(" y: ")
                        .append(y)
                        .append("\n");
                numberedList++;

/*
            Chiamata per il cambio stato della bolla selezionata
*/
/*
                Verifica esistenza bolla
*/
                if (grid.get(x).get(y).touched(x, y, grid, true)) {
/*
                Controllo per individuare una vittoria
*/
                    if (!checkNonExplodedBubbles(grid)) {
                        System.out.println("Hai vinto!!!");
                        movesOfGame.append("Partita vinta.\n");
                        return false;
                    } else {
                        gridStamp();
                        movesLeft--;
/*
                Controllo per l'ortografia
*/
                        if (movesLeft == 1) {
                            System.out.println("Hai " + movesLeft + " tentativo rimasto");
                        } else {
                            System.out.println("Hai " + movesLeft + " tentativi rimasti");
                        }
                        return true;
                    }
                } else {
                    System.out.println("Nessuna bolla presente, riprova");
                    movesOfGame
                            .append(numberedList)
                            .append(") Inserite coordinate di una bolla esplosa.\n");
                    numberedList++;
                    return true;
                }
            } else {
                System.out.println("Inserire coordinate valide");
                movesOfGame
                        .append(numberedList)
                        .append(") Inserite coordinate non valide.\n");
                numberedList++;
                return true;
            }

        } else return false;
    }

    public boolean checkNonExplodedBubbles(List<List<Bubble>> grid) {
/*
        Controllo per individuare bolle non esplose
*/
        return grid.stream()
                .flatMap(Collection::stream)
                .anyMatch(a->a.getStatement() != BubbleStatement.EXPLODED);
    }

    public void touchAloneBubbles(List<List<Bubble>> grid) {
        grid.stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .map(i -> i.touched(i.getX(), i.getY(), grid, false));
    }

    public boolean movesLeftController() {
/*
            Controllo per individuare le mosse rimanenti
*/
        if (movesLeft == 0 && checkNonExplodedBubbles(grid)) {
            System.out.println("Hai perso, ritenta!");
            movesOfGame.append("Partita persa.\n");
            return false;
        }
        else return true;
    }

    public void movesLeft() {
        while (checkNonExplodedBubbles(gridClone)) {
            double counterREADY;
            double counterPUFFY;
            double counterEMPTY;
            int [] bestREADY = new int [2];
            int [] bestPUFFY = new int [2];
            int [] bestEMPTY = new int [2];
            counterREADY = bestCounterMethod(BubbleStatement.READY_TO_EXPLODE,
                    bestREADY, bestPUFFY, bestEMPTY);
            counterPUFFY = bestCounterMethod(BubbleStatement.PUFFY,
                    bestREADY, bestPUFFY, bestEMPTY);
            counterEMPTY = bestCounterMethod(BubbleStatement.EMPTY,
                    bestREADY, bestPUFFY, bestEMPTY);
            double bestCounter = Math.max(Math.max(counterEMPTY, counterPUFFY), counterREADY);
            if(bestCounter <= 0 && checkNonExplodedBubbles(gridClone)){
                touchAloneBubbles(gridClone);
                movesLeft++;
            }
            else {
                if (bestCounter == counterREADY) {
                    gridClone.get(bestREADY[0]).get(bestREADY[1]).touched(bestREADY[0], bestREADY[1], gridClone, false);
                    movesLeft++;
                } else if (bestCounter == counterPUFFY) {
                    gridClone.get(bestPUFFY[0]).get(bestPUFFY[1]).touched(bestPUFFY[0], bestPUFFY[1], gridClone, false);
                    gridClone.get(bestPUFFY[0]).get(bestPUFFY[1]).touched(bestPUFFY[0], bestPUFFY[1], gridClone, false);
                    movesLeft += 2;
                } else {
                    gridClone.get(bestEMPTY[0]).get(bestEMPTY[1]).touched(bestEMPTY[0], bestEMPTY[1], gridClone, false);
                    gridClone.get(bestEMPTY[0]).get(bestEMPTY[1]).touched(bestEMPTY[0], bestEMPTY[1], gridClone, false);
                    gridClone.get(bestEMPTY[0]).get(bestEMPTY[1]).touched(bestEMPTY[0], bestEMPTY[1], gridClone, false);
                    movesLeft += 3;
                }
            }
        }
    }

    public double bestCounterMethod(BubbleStatement statementToTouch, int [] bestREADY, int [] bestPUFFY, int [] bestEMPTY){
        int maxCol = 5;
        int maxRow = 4;
        int x = 0;
        int y = 0;
        double counter = 0;
        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxCol; col++) {
                if (gridClone.get(row).get(col).getStatement() == statementToTouch) {
                    if (gridClone.get(row).get(col).checkExplosion(row, col, BubbleStatement.READY_TO_EXPLODE) > counter) {
                        counter = gridClone.get(row).get(col).checkExplosion(row, col, BubbleStatement.READY_TO_EXPLODE);
                        if(statementToTouch == BubbleStatement.READY_TO_EXPLODE) {
                            bestREADY[0] = row;
                            bestREADY[1] = col;
                        }
                        else if(statementToTouch == BubbleStatement.PUFFY) {
                            bestPUFFY[0] = row;
                            bestPUFFY[1] = col;
                        }
                        if(statementToTouch == BubbleStatement.EMPTY) {
                            bestEMPTY[0] = row;
                            bestEMPTY[1] = col;
                        }
                    }
                }
            }
        }
        //controllo stato bolla selezionata
        if(statementToTouch == BubbleStatement.PUFFY){
            counter--;
        }
        else if(statementToTouch == BubbleStatement.EMPTY){
            counter -= 2;
        }
        return counter;
    }
}
