package org.example.domain.characters;

public class DamageResult {
    private boolean isDamageDealt;
    private String message;

    public DamageResult(boolean isDamageDealt, String message) {
        this.isDamageDealt = isDamageDealt;
        this.message = message;
    }

    public boolean isDamageDealt() {
        return isDamageDealt;
    }

    public void setDamageDealt(boolean damageDealt) {
        isDamageDealt = damageDealt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


