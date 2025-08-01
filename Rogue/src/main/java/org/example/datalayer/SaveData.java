package org.example.datalayer;

import org.example.domain.GameSession;

public class SaveData {
    private GameSession session;
    private long timestamp;

    public SaveData() {}

    public SaveData(GameSession session) {
        this.session = session;
        this.timestamp = System.currentTimeMillis();
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GameSession toGameSession() {
        return session;
    }
}
