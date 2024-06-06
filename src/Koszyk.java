import java.util.ArrayList;
import java.util.List;

public class Koszyk {
    private List<Game> games;

    public Koszyk() {
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public List<Game> getGames() {
        return games;
    }
}
