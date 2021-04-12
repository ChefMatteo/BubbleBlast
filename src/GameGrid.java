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
    private Bubble [][] grid;

    //Constructor
    private GameGrid() {
        GridGeneration();
    }

    //Getters and setters
    public void setGrid(Bubble[][] grid) {
        this.grid = grid;
    }


    private void GridGeneration(){
        //Stream che crea ogni riga
        grid = IntStream.range(1, 6).mapToObj(a ->
                //Stream che le colonne con bolle casuali
                IntStream.range(1, 7)
                        .mapToObj(b -> new Bubble((new Random().nextInt(3))))
                        .toArray(Bubble[]::new))
                .toArray(Bubble[][]::new);
    }
    public void GridStamp(){
        Stream.of(grid)
                .map(a -> Arrays.stream(a)
                        .map(Bubble::getAppearance).collect(Collectors.toList()))
                .forEach(System.out::println);
    }

    //TODO MovesLeft
    public void MovesLeft(){

    }



}
