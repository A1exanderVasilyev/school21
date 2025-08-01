package org.example.datalayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AttemptHistory {
    private List<SaveData> attempts = new ArrayList<>();

    public List<SaveData> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<SaveData> attempts) {
        this.attempts = attempts;
    }

    public List<SaveData> getSortedAttempts() {
        attempts.sort(Comparator.comparingLong(SaveData::getTimestamp).reversed());
        return attempts;
    }
}