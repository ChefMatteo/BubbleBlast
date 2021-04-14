import java.util.*;
import java.util.stream.*;

public class GameGrid {
    //Singleton
    private static GameGrid instance = null;
    public static GameGrid getGameGrid() {
        if (instance == null) {
            instance = new GameGrid();
        }
        return instance;
    }

    //Attributes
    private List<List<Bubble>> grid;
    private int movesLeft;
    private StringBuilder movesOfGame = new StringBuilder();
    private final String [] ACCEPTED_GRID_COORDINATES = {
            "A1","A2","A3","A4","A5","A6",
            "B1","B2","B3","B4","B5","B6",
            "C1","C2","C3","C4","C5","C6",
            "D1","D2","D3","D4","D5","D6",
            "E1","E2","E3","E4","E5","E6"};

    //Attibutes for MovesLeft() controlles
    private HashMap<Integer, Integer> coordinatesToBeExcluded = new HashMap<>();


    //Constructor
    private GameGrid() {
        GridGeneration();
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
    private void GridGeneration() {

/*
        //***Test**
        List<Bubble> test = new ArrayList<Bubble>();
        test.add(new Bubble(2));
        List<List<Bubble>> gridTest = new ArrayList<>();
        gridTest.add(test);
        grid = gridTest;
*/
        //Stream che crea 5 List.size()=5
        grid = IntStream.range(1, 6).mapToObj(a ->
                //Stream che crea 6 bolle casuali e le inserisce nelle liste del primo stream
                IntStream.range(1, 7)
                        .mapToObj(b -> new Bubble((new Random().nextInt(3))))
                        .toList())
                .toList();

        MovesLeft(grid);
    }

    public void GridStamp(){
        //Stream per ogni lista
        grid.stream()
                .map(a -> a.stream()
                        //Per ogni lista prende solo la view delle bolle
                        .map(Bubble::getBubbleView).collect(Collectors.toList()))
                .forEach(System.out::println);
        System.out.println();
    }

    private boolean MoveMovesLeft(){

        return true;
    }

    public boolean Move(String coordinates) {
/*
        Controllo per movimenti rimanenti
*/
        if (MovesLeftController()) {
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
                if (grid.get(x).get(y).Touched(x, y)) {
/*
                Controllo per individuare una vittoria
*/
                    if (!CheckNonExplodedBubbles()) {
                        System.out.println("Hai vinto!!!");
                        movesOfGame.append("Partita vinta.\n");
                        return false;
                    } else {
                        GridStamp();
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

    public boolean CheckNonExplodedBubbles() {
/*
        Controllo per individuare bolle non esplose
*/
        return grid.stream()
                .flatMap(Collection::stream)
                .anyMatch(a->a.getStatement() != BubbleStatement.EXPLODED);
    }

    public boolean MovesLeftController() {
/*
            Controllo per individuare le mosse rimanenti
*/
        if (movesLeft == 0 && CheckNonExplodedBubbles()) {
            System.out.println("Hai perso, ritenta!");
            movesOfGame.append("Partita persa.\n");
            return false;
        }
        else return true;
    }

    //TODO MovesLeft
    public void MovesLeft(List<List<Bubble>> grid){
        movesLeft = 5;
    }

    public void HeadTest(){
        GameGrid gridTest = getGameGrid();
        int movesCounter = Integer.MAX_VALUE;
        test(coordinatesToBeExcluded, gridTest);
    }

    public boolean test(HashMap<Integer, Integer> coordinatesToBeExcluded, GameGrid gridTest){
        int maxCol = 5;
        int maxRow = 4;
        int x = 0;
        int y = 0;
        int counter = Integer.MIN_VALUE;
        boolean flag = false;
        for(int row = 0; row <= maxRow; row++){
            for(int col = 0; col <= maxCol; col++){
                System.out.println("x: " + row + " y: " + col + " check: " + gridTest.grid.get(row).get(col).CheckExplosion(row,col));
                if(gridTest.grid.get(row).get(col).CheckExplosion(row,col) > counter
                        && !coordinatesToBeExcluded.containsKey(row)
                        && !coordinatesToBeExcluded.containsValue(col)){
                    counter = gridTest.grid.get(row).get(col).CheckExplosion(row,col);
                    y = col;
                    x = row;
                    flag = true;
                }
            }
        }
        System.out.println("Best: x: " + x + " y: " + y + " counter: " + counter);
        gridTest.grid.get(x).get(y).Touched(x,y);
        coordinatesToBeExcluded.put(x ,y);
        return flag;
    }
}
