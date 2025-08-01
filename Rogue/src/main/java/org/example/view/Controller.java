package org.example.view;

import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.datalayer.DataManager;
import org.example.domain.Backpack;
import org.example.domain.GameSession;
import org.example.domain.characters.Player;
import org.example.domain.enums.Direction;
import org.example.domain.enums.ItemType;
import org.example.domain.generation.LevelFactory;
import org.example.domain.items.Item;

import java.io.IOException;
import java.util.List;

public class Controller {
    private final GameSession gameSession;
    private final LevelFactory lf;
    private final View view;
    private Backpack backpack;
    private Player player;

    final int START_MAX_HEALTH = 200;
    final int START_DEXTERITY = 35;
    final int START_STRENGTH = 35;


    public Controller(View view, GameSession restoredSession, String playerName) {
        this.view = view;
        if (restoredSession != null) {
            this.gameSession = restoredSession;
            this.player = restoredSession.getPlayer();
            this.backpack = restoredSession.getBackpack();
            this.lf = restoredSession.getLevelFactory();
        } else {
            this.lf = new LevelFactory(9, 10);
            this.backpack = new Backpack();
            this.player = new Player(null, playerName, START_MAX_HEALTH, START_MAX_HEALTH, START_DEXTERITY, START_STRENGTH, null);
            this.gameSession = new GameSession(player, backpack, null, lf, false);
        }
    }


    public void userInput(int input) {
        if (input == 'Q' || input == 'q') {
            gameSession.setGameOver(true);
            gameSession.endGameHandler();
            DataManager.saveGame(gameSession);
            if (!player.isAlive()) {
                gameSession.endGameHandler();
            }
            return;
        }
        if (gameSession.getBackpack().isOpen()) {
            gameSession.setUsedItemMessage(useItemHandler(input));
        } else {
            gameSession.setUsedItemMessage(null);
            gameTurnHandler(input);
            backpackItemsShowHandler(input);
        }
    }

    private void gameTurnHandler(int input) {
        switch (input) {
            case 'w', 'W' -> gameSession.gameTurnHandler(Direction.UP);
            case 'a', 'A' -> gameSession.gameTurnHandler(Direction.LEFT);
            case 's', 'S' -> gameSession.gameTurnHandler(Direction.DOWN);
            case 'd', 'D' -> gameSession.gameTurnHandler(Direction.RIGHT);
        }
    }

    public String useItemHandler(int input) {
        Backpack backpack = gameSession.getBackpack();
        Player player = gameSession.getPlayer();
        List<Item> items = gameSession.getBackpack().getItemsOfType();
        if (items == null) {
            view.cleanSelectMenu();
            return null;
        }
        if (input >= '1' && input <= '9') {
            int itemIndex = input - '0' - 1;
            if (itemIndex >= items.size()) {
                view.cleanSelectMenu();
                return null;
            }
            Item itemToUse = items.get(itemIndex);
            if (itemToUse != null) {
                view.cleanSelectMenu();
                backpack.setOpen(false);
                return backpack.useItem(itemToUse, gameSession);
            }
        } else if (backpack.isOpenWeaponList() && input == '0' && player.getWeapon() != null) {
            backpack.add(player.getWeapon());
            player.setWeapon(null);
            backpack.setOpenWeaponList(false);
            view.cleanSelectMenu();
            return "You disarmed";
        }
        backpack.setOpen(false);
        view.cleanSelectMenu();
        return null;
    }

    private void backpackItemsShowHandler(int input) {
        Backpack backpack = gameSession.getBackpack();
        switch (input) {
            case 'h', 'H' -> {
                view.printWeapon(backpack);
                backpack.setItemsToShowByType(ItemType.WEAPON);
            }
            case 'j', 'J' -> {
                view.printFood(backpack);
                backpack.setItemsToShowByType(ItemType.FOOD);
            }
            case 'k', 'K' -> {
                view.printElixir(backpack);
                backpack.setItemsToShowByType(ItemType.ELIXIR);
            }
            case 'e', 'E' -> {
                view.printScroll(backpack);
                backpack.setItemsToShowByType(ItemType.SCROLL);
            }
        }
    }

    public void gameCycle() throws IOException, InterruptedException {
        Screen screen = view.getScreen();
        startState(screen);
        if (gameSession.isGameOver()) {
            return;
        }
        view.drawMenu();
        view.cleanSelectMenu();
        if(gameSession.getCurrentLevel() == null){
            gameSession.startNewGame();
        }

        userInput('w');
        userInput('s');
        while (!gameSession.isGameOver()) {
            executeDrawing(gameSession, screen);
            var key = screen.readInput();
            if (!KeyType.ArrowDown.equals(key.getKeyType()) && !KeyType.ArrowUp.equals(key.getKeyType()) && !KeyType.ArrowLeft.equals(key.getKeyType()) && !KeyType.ArrowRight.equals(key.getKeyType())) {
                userInput(key.getCharacter());
            }
        }
        finishState(screen);
    }

    public void startState(Screen screen) throws IOException, InterruptedException {
        view.drawLabel();
        screen.refresh();
        while (true) {
            var key = screen.readInput();
            if (key != null && key.getKeyType() == KeyType.Escape) {
                gameSession.setGameOver(true);
                break;
            } else if (key != null && key.getKeyType() == KeyType.Enter) {
                gameSession.setGameOver(false);
                break;
            }
        }
    }

    public void finishState(Screen screen) throws InterruptedException, IOException {
        try {
            view.drawStatistic(DataManager.loadRecords());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            var key = screen.readInput();
            if (key != null && key.getKeyType() == KeyType.Escape) {
                break;
            }
        }
        view.drawGameOver();
        while (true) {
            var key = screen.readInput();
            if (key != null && key.getKeyType() == KeyType.Escape) {
                break;
            }
        }
    }

    public void executeDrawing(GameSession gameSession, Screen screen) throws IOException {
        view.cleanMessage();
        view.drawField(gameSession.getCurrentLevel().getField());
        view.drawWarFog(gameSession.getCurrentLevel().getField());
        view.printDamageAndItemInfo(gameSession.getTakeItemMessage(), gameSession.getAttackMessages(), gameSession.getUsedItemMessage());
        view.printAdditionalInfo(gameSession);
        screen.refresh();
    }
}
