import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.*;

@Data
public class GameGrid {
    private List<List<Bubble>> grid;
    private List<List<Bubble>> gridClone;
    private int movesLeft;
    private StringBuilder movesOfGame = new StringBuilder();
    private int numberedList = 1;

    //Constructor
    private GameGrid() {
        gridGeneration();
    }
    private static GameGrid instance = null;
    public static GameGrid getGameGrid() {
        if (instance == null) {
            instance = new GameGrid();
        }
        return instance;
    }

    //Methods

    public void reset(){
        gridGeneration();
    }
    private void gridGeneration() {
        grid = IntStream.range(1, 6).mapToObj(a ->
                IntStream.range(1, 7)
                        .mapToObj(b -> new Bubble((new Random().nextInt(3)), a-1, b-1))
                        .toList())
                .toList();
        gridClone = IntStream.range(0, 5).mapToObj(a ->
                IntStream.range(0, 6)
                        .mapToObj(b -> grid.get(a).get(b).bubbleClone())
                        .toList())
                .toList();

    }

    public void gridStamp(){
        grid.stream()
                .map(a -> a.stream()
                        .map(Bubble::getBubbleView).collect(Collectors.toList()))
                .forEach(System.out::println);
        System.out.println("");
    }

    public boolean move(String coordinates) {
        if (movesLeftController()) {
            int y;
            int x;
            switch (coordinates.toUpperCase().charAt(0)) {
                case 'A' -> x = 0;
                case 'B' -> x = 1;
                case 'C' -> x = 2;
                case 'D' -> x = 3;
                case 'E' -> x = 4;
                default -> {
                    System.out.println("Inserire coordinate valide");
                    movesOfGame
                            .append(numberedList)
                            .append(") Inserite coordinate non valide.\n");
                    numberedList++;
                    return true;
                }
            }
            if(coordinates.length() == 2) {
                y = Integer.parseInt(coordinates.substring(1)) - 1;
                if (y < 0 || y > 5) {
                    System.out.println("Inserire coordinate valide");
                    movesOfGame
                            .append(numberedList)
                            .append(") Inserite coordinate non valide.\n");
                    numberedList++;
                    return true;
                }
                movesOfGame
                        .append(numberedList)
                        .append(") Inserite le coordinate x: ")
                        .append(x)
                        .append(" y: ")
                        .append(y)
                        .append("\n");
                numberedList++;
                if (grid.get(x).get(y).touched(x, y, grid, true, 1)) {
                    if (!checkNonExplodedBubbles(grid)) {
                        System.out.println("Hai vinto!!!");
                        movesOfGame.append("Partita vinta.\n");
                        return false;
                    } else {
                        gridStamp();
                        movesLeft--;
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
            }
            else{
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
        return grid.stream()
                .flatMap(Collection::stream)
                .anyMatch(a->a.getStatement() != BubbleStatement.EXPLODED);
    }

    public void touchAloneBubbles(List<List<Bubble>> grid) {
        grid.stream()
                .flatMap(Collection::stream)
                .filter(a -> a.getStatement() != BubbleStatement.EXPLODED)
                .findFirst()
                .map(i -> i.touched(i.getX(), i.getY(), grid, false, 1));
    }

    public boolean movesLeftController() {
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
                    gridClone.get(bestREADY[0]).get(bestREADY[1]).touched(bestREADY[0], bestREADY[1], gridClone, false,1 );
                    movesLeft++;
                } else if (bestCounter == counterPUFFY) {
                    gridClone.get(bestPUFFY[0]).get(bestPUFFY[1]).touched(bestPUFFY[0], bestPUFFY[1], gridClone, false, 2);
                    movesLeft += 2;
                } else {
                    gridClone.get(bestEMPTY[0]).get(bestEMPTY[1]).touched(bestEMPTY[0], bestEMPTY[1], gridClone, false, 3);
                    movesLeft += 3;
                }
            }
        }
    }

    public double bestCounterMethod(BubbleStatement statementToTouch, int [] bestREADY, int [] bestPUFFY, int [] bestEMPTY){
        AtomicReference<Double> finalCounter = new AtomicReference<>(0.0);
        gridClone.stream()
                .flatMap(List<Bubble>::stream)
                .filter(a-> gridClone.get(a.getX()).get(a.getY()).getStatement() == statementToTouch)
                .forEach(a->{
                    if(gridClone.get(a.getX()).get(a.getY()).checkExplosion(a.getX(),a.getY(), /*BubbleStatement.READY_TO_EXPLODE,*/ gridClone) > finalCounter.get()){
                        finalCounter.set(gridClone.get(a.getX()).get(a.getY()).checkExplosion(a.getX(), a.getY(),/* BubbleStatement.READY_TO_EXPLODE,*/ gridClone));
                        switch (statementToTouch){
                            case READY_TO_EXPLODE -> {
                                bestREADY[0] = a.getX();
                                bestREADY[1] = a.getY();
                            }
                            case PUFFY -> {
                                bestPUFFY[0] = a.getX();
                                bestPUFFY[1] = a.getY();
                            }
                            case EMPTY -> {
                                bestEMPTY[0] = a.getX();
                                bestEMPTY[1] = a.getY();
                            }
                        }
                    }
                });
        double counter = finalCounter.get();
        if(statementToTouch == BubbleStatement.PUFFY){
            counter--;
        }
        else if(statementToTouch == BubbleStatement.EMPTY){
            counter -= 2;
        }
        return counter;
    }
}
