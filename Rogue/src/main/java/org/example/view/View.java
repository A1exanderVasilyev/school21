package org.example.view;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.example.datalayer.GameRecords;
import org.example.datalayer.PlaythroughStatistics;
import org.example.domain.Backpack;
import org.example.domain.GameSession;
import org.example.domain.enums.ItemType;
import org.example.domain.items.Item;
import org.example.domain.map.Cell;
import org.example.domain.map.Room;
import org.example.domain.space.RoomSpace;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.util.List;

public class View {
    private Terminal terminal;

    public Screen getScreen() {
        return screen;
    }

    private Screen screen;
    private final int width;
    private int height = 30;
    private final int horizontalIndent = 1;
    private final int veticalIndent = 3;
    private final int veticalIndentSub = 1;
    private final int xAddInfo = horizontalIndent;
    private final int yAddInfo = veticalIndent + veticalIndentSub + height + 2;
    private final int heightAddInfo = 5;


    public View (int height, int width) throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        this.terminal = factory.createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.height = height;
        this.width = width;
    }

     public void drawLabel() throws IOException, InterruptedException {
         var textGraphics = screen.newTextGraphics();
         textGraphics.setBackgroundColor(Color.BLACK.getCol());
         textGraphics.setForegroundColor(Color.RED.getCol());
         screen.clear();
         textGraphics.fill(' ');
         textGraphics.putString(4, 4, "                 _____    ____    _____  _    _  ______");
         textGraphics.putString(4, 5, "                |  __ \\  / __ \\  / ____|| |  | ||  ____|");
         textGraphics.putString(4, 6, " ______  ______ | |__) || |  | || |  __ | |  | || |__    ______  ______");
         textGraphics.putString(4, 7, "|______||______||  _  / | |  | || | |_ || |  | ||  __|  |______||______|");
         textGraphics.putString(4, 8, "                | | \\ \\ | |__| || |__| || |__| || |____");
         textGraphics.putString(4, 9, "                |_|  \\_\\ \\____/  \\_____| \\____/ |______|");
         textGraphics.putString(30, 15, "Press enter to start");
     }

     public void drawMenu() throws IOException {
         var textGraphics = screen.newTextGraphics();
         textGraphics.setBackgroundColor(Color.BLACK.getCol());
         textGraphics.setForegroundColor(Color.GREEN.getCol());
         screen.clear();
         drawRectangle(textGraphics, horizontalIndent, veticalIndent, width + 2, height + 2);
         textGraphics.setForegroundColor(Color.YELLOW.getCol());
         drawRectangle(textGraphics, xAddInfo, yAddInfo, width + 2, heightAddInfo);
     }


    public void drawRectangle(TextGraphics textGraphics, int x, int y, int width, int height) {
        textGraphics.drawLine(x + 1, y, x + width - 2, y, '─');
        textGraphics.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1, '─');
        textGraphics.drawLine(x, y + 1, x, y + height - 2, '│');
        textGraphics.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2, '│');
        char[] corners = {'┌', '┐', '└', '┘'};
        textGraphics.setCharacter(x, y, corners[0]);
        textGraphics.setCharacter(x + width - 1, y, corners[1]);
        textGraphics.setCharacter(x, y + height - 1, corners[2]);
        textGraphics.setCharacter(x + width - 1, y + height - 1, corners[3]);
    }

    public void drawField(Cell[][] field) throws IOException {
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        for (Cell[] cells : field) {
            for (Cell cell : cells) {
                try {
                    if (cell.getCharacter() != null){
                        textGraphics.setForegroundColor(Color.valueOf(cell.getCharacter().getColor()).getCol());
                        textGraphics.putString(horizontalIndent + 1 + cell.coords.x, veticalIndent + 1 + cell.coords.y, String.valueOf(cell.getCharacter().toDraw()));
                    } else if (cell.getItem() != null){
                        textGraphics.setForegroundColor(Color.valueOf(cell.getItem().getColor()).getCol());
                        textGraphics.putString(horizontalIndent + 1 + cell.coords.x, veticalIndent + 1 + cell.coords.y, String.valueOf(cell.getItem().toDraw()));
                    } else{
                        textGraphics.setForegroundColor(cell.getSt().getColor().getCol());
                        textGraphics.putString(horizontalIndent + 1 + cell.coords.x, veticalIndent + 1 + cell.coords.y, String.valueOf(cell.getSt().getSymbol()));
                    }
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    public void drawWarFog(Cell[][] field) throws IOException {
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.BLACK.getCol());
        for (Cell[] cells : field) {
            for (Cell cell : cells) {
                if (!cell.isVisited()) {
                    textGraphics.putString(horizontalIndent + 1 + cell.coords.x, veticalIndent + 1 + cell.coords.y, " ");
                } else if (cell.getSt() instanceof RoomSpace && !cell.isPlayerOn()){
                    textGraphics.setForegroundColor(cell.getSt().getColor().getCol());
                    textGraphics.putString(horizontalIndent + 1 + cell.coords.x, veticalIndent + 1 + cell.coords.y, String.valueOf(cell.getSt().getSymbol()));
                }
            }
        }
    }

    public void printDamageAndItemInfo(String takeItemMessage, String[] damageMessages, String usedItemMessage){
        cleanMessage();
        var textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(Color.GREEN.getCol());
        int indent = 0;
        if (takeItemMessage != null){
            textGraphics.putString(0, 0, takeItemMessage);
            indent++;
        }
        if (damageMessages != null){
            for (String message : damageMessages) {
                if (message != null){
                    textGraphics.setForegroundColor(Color.ORANGE.getCol());
                    textGraphics.putString(0, indent++, message);
                    if (indent == 3){
                        cleanMessage();
                    }
                    indent /= 3;
                }
            }
        }
        if (usedItemMessage != null){
            if (indent == 0) {
                cleanMessage();
            }
            textGraphics.putString(0, indent, usedItemMessage);
        }
    }

    public void cleanMessage(){
        var textGraphics = screen.newTextGraphics();
        String clear = "                                                                                       ";
        for (int i = 0; i < 3; ++i){
            textGraphics.putString(0, i, clear);
        }
    }

    public void printAdditionalInfo(GameSession gameSession){
        clearAdditionalInfo();
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.ORANGE.getCol());
        int vi = veticalIndent + veticalIndentSub + height + 2;
        String name = "None";
        if (gameSession.getPlayer() != null && gameSession.getPlayer().getWeapon() != null && gameSession.getPlayer().getWeapon().getName() != null){
            name = gameSession.getPlayer().getWeapon().getName();
        }
        textGraphics.putString(horizontalIndent + 1, vi + 1, "Level:" + gameSession.getCurrentLevel().getLevelNumber());
        textGraphics.putString(horizontalIndent + 9, vi + 1, "Gold:" + gameSession.getBackpack().getTreasuresCost());
        textGraphics.putString(horizontalIndent + 1, vi + 2, "Heath:" + gameSession.getPlayer().getHealth() + "(" + gameSession.getPlayer().getMaxHealth() + ")");
        textGraphics.putString(horizontalIndent + 1, vi + 3, "Weapon:" + name);
    }

    public void clearAdditionalInfo(){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.BLACK.getCol());
        for (int x = 0; x < width; ++x){
            for (int y = 0; y < heightAddInfo - 2; ++y){
                textGraphics.putString(xAddInfo + x + 1, yAddInfo + y + 1, " ");
            }
        }
    }

    public void printWeapon(Backpack backpack){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.CYAN.getCol());
        int i = 1;
        textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent, "Select weapon ");
        for (Item item: backpack.getItems()){
            String type = item.getType();
            if (ItemType.valueOf(type) == ItemType.WEAPON){
                textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent + i, i + ":" + item.getSubType());
                i++;
            }
        }
    }

    public void printElixir(Backpack backpack){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.CYAN.getCol());
        int i = 1;
        textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent, "Select elixir ");
        for (Item item: backpack.getItems()){
            String type = item.getType();
            if (ItemType.valueOf(type) == ItemType.ELIXIR){
                textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent + i, i + ":" + item.getSubType());
                i++;
            }
        }
    }

    public void printFood(Backpack backpack){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.CYAN.getCol());
        int i = 1;
        textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent, "Select food ");
        for (Item item: backpack.getItems()){
            String type = item.getType();
            if (ItemType.valueOf(type) == ItemType.FOOD){
                textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent + i, i + ":" + item.getSubType());
                i++;
            }
        }
    }

    public void printScroll(Backpack backpack){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.CYAN.getCol());
        int i = 1;
        textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent, "Select scroll ");
        for (Item item: backpack.getItems()){
            String type = item.getType();
            if (ItemType.valueOf(type) == ItemType.SCROLL){
                textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent, veticalIndent + i, i + ":" + item.getSubType());
                i++;
            }
        }
    }

    public void cleanSelectMenu(){
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.BLACK.getCol());
        for (int y = 0; y < height + heightAddInfo + 3; ++y){
            for (int i = 0; i < 150; ++i){
                textGraphics.putString(horizontalIndent + 1 + width + horizontalIndent + i, veticalIndent + y, " ");
            }
        }
    }

    public void drawGameOver() throws IOException, InterruptedException {
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.RED.getCol());
        screen.clear();
        textGraphics.fill(' ');
        textGraphics.putString(4, 4, "  _____            __  __  ______    ____  __      __ ______  _____");
        textGraphics.putString(4, 5, " / ____|    /\\    |  \\/  ||  ____|  / __ \\ \\ \\    / /|  ____||  __ \\");
        textGraphics.putString(4, 6, "| |  __    /  \\   | \\  / || |__    | |  | | \\ \\  / / | |__   | |__) |");
        textGraphics.putString(4, 7, "| | |_ |  / /\\ \\  | |\\/| ||  __|   | |  | |  \\ \\/ /  |  __|  |  _  /");
        textGraphics.putString(4, 8, "| |__| | / ____ \\ | |  | || |____  | |__| |   \\  /   | |____ | | \\ \\");
        textGraphics.putString(4, 9, " \\_____|/_/    \\_\\|_|  |_||______|  \\____/     \\/    |______||_|  \\_\\");
        screen.refresh();
    }

    public void drawStatistic(GameRecords gameRecords) throws IOException {
        List<PlaythroughStatistics> statistics = gameRecords.getTopStatistics();
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.CYAN.getCol());
        screen.clear();
        textGraphics.fill(' ');
        textGraphics.putString(horizontalIndent, veticalIndent, "PlayerName|Gold|Elixirs|Hits|Misses|Levels|Food|Enemies|Scrolls|  Cells");
        int y = 2;
        for (PlaythroughStatistics st : statistics){
            textGraphics.putString(horizontalIndent, veticalIndent + y, st.getPlayerName());
            textGraphics.putString(horizontalIndent + 8, veticalIndent + y, String.format("%6d",st.getTreasuresCost()));
            textGraphics.putString(horizontalIndent + 8 + 6, veticalIndent + y, String.format("%6d",st.getConsumedElixirsNumber()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7, veticalIndent + y, String.format("%6d",st.getHitsNumber()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7, veticalIndent + y, String.format("%6d",st.getMissesNumber()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7 + 6, veticalIndent + y, String.format("%6d",st.getReachedLevelCount()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7 + 6 + 7, veticalIndent + y, String.format("%6d",st.getConsumedFoodAmount()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7 + 6 + 7 + 6, veticalIndent + y, String.format("%6d",st.getDefeatedEnemiesCount()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7 + 6 + 7 + 6 + 7, veticalIndent + y, String.format("%6d",st.getReadScrollsNumber()));
            textGraphics.putString(horizontalIndent + 8 + 6 + 7 + 7 + 6 + 7 + 6 + 7 + 7, veticalIndent + y, String.format("%9d",st.getTraversedCellsNumber()));
            y++;
        }
        screen.refresh();
    }

    public char loadGame() throws IOException {
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.RED.getCol());
        screen.clear();
        textGraphics.fill(' ');
        screen.refresh();
        textGraphics.putString(2, 2, "Найден файл сохранения. Загрузить игру? (y/n)");
        screen.refresh();
        char symb;
        do {
            symb = screen.readInput().getCharacter();
        } while (symb != 'y' && symb != 'Y' && symb != 'n' && symb != 'N');
        return symb;
    }

    public String readName() throws IOException {
        var textGraphics = screen.newTextGraphics();
        textGraphics.setBackgroundColor(Color.BLACK.getCol());
        textGraphics.setForegroundColor(Color.RED.getCol());
        screen.clear();
        textGraphics.fill(' ');
        screen.refresh();
        textGraphics.putString(2, 2, "Введите имя игрока: ");
        screen.refresh();
        StringBuilder result = new StringBuilder();
        char symb = ' ';
        int i = 2;
        do {
            var key = screen.readInput();
            if (key.getKeyType() == KeyType.Enter) {
                break;
            }
            symb = key.getCharacter();
            textGraphics.putString(i, 3, String.valueOf(symb));
            screen.refresh();
            result.append(symb);
            ++i;
        } while (true);
        return result.toString().trim();
    }
}
