package org.example;

import org.example.view.Controller;
import org.example.view.View;
import org.example.datalayer.DataManager;
import org.example.domain.GameSession;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        GameSession session = null;
        String playerName = "Player";
        try {
            View view = new View(30, 30);
            view.getScreen().startScreen();
            if (DataManager.saveFileExists()) {
                char choice = view.loadGame();
                if ('y' == choice || 'Y' == choice) {
                    session = DataManager.loadGame();
                    if (session != null && session.getPlayer() != null) {
                        playerName = session.getPlayer().getName();
                    }
                } else {
                    playerName = view.readName();
                }
            } else {
                playerName = view.readName();
                if (playerName.trim().isEmpty()) {
                    playerName = "Player";
                }
            }
            Controller gameController = new Controller(view, session, playerName);
            gameController.gameCycle();
            view.getScreen().stopScreen();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}