package org.example.domain.characters;

import org.example.domain.Coords;
import org.example.domain.characters.enemies.Mimic;
import org.example.domain.interfaces.Drawable;
import org.example.domain.characters.enemies.Vampire;
import org.example.domain.enums.EnemyType;
import org.example.domain.items.Elixir;
import org.example.domain.items.Item;
import org.example.domain.items.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player extends Character implements Drawable {
    private static final char PLAYER_SYMBOL = '@';
    private Weapon weapon;
    private boolean isSleep = false;
    private List<Elixir> activeElixirs = new ArrayList<>();
    private int hitsNumber;
    private int missesNumber;
    private int consumedFoodAmount;
    private int consumedElixirsNumber;
    private int readScrollsNumber;

    public Player() {
        super(null, '@', "", 0, 0, 0, 0);
    }

    @JsonCreator
    public Player(@JsonProperty("position") Coords position,
                  @JsonProperty("name") String name,
                  @JsonProperty("maxHealth") int maxHealth,
                  @JsonProperty("health") int health,
                  @JsonProperty("dexterity") int dexterity,
                  @JsonProperty("strength") int strength,
                  @JsonProperty("weapon") Weapon weapon) {
        super(position, PLAYER_SYMBOL, name, maxHealth, health, dexterity, strength);
        this.weapon = weapon;
        this.hitsNumber = 0;
        this.missesNumber = 0;
        this.consumedFoodAmount = 0;
        this.consumedElixirsNumber = 0;
        this.readScrollsNumber = 0;
    }

    public char toDraw(){
        return '@';
    }
    public String getColor(){return "YELLOW";}

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public void setSleep(boolean sleep) {
        isSleep = sleep;
    }

    public int getHitsNumber() {
        return hitsNumber;
    }

    public int getMissesNumber() {
        return missesNumber;
    }

    public int getConsumedFoodAmount() {
        return consumedFoodAmount;
    }

    public void incrementConsumedFoodAmount() {
        consumedFoodAmount++;
    }

    public int getConsumedElixirsNumber() {
        return consumedElixirsNumber;
    }

    public void incrementConsumedElixirsNumber() {
        consumedElixirsNumber++;
    }

    public int getReadScrollsNumber() {
        return readScrollsNumber;
    }

    public void incrementReadScrollsNumber() {
        readScrollsNumber++;
    }

    @Override
    public DamageResult attack(Character target) {
        DamageResult damageResult = new DamageResult(false, null);
        if (isSleep) {
            damageResult.setDamageDealt(false);
            damageResult.setMessage("Player sleep");
            return damageResult;
        }

        Enemy enemy = (Enemy) target;
        if (enemy.getEnemyType().equals(EnemyType.VAMPIRE)) {
            Vampire vampire = (Vampire) target;
            if (!vampire.isAttackMissable()) {
                damageResult = dealDamage(target, weapon);
            } else {
                vampire.setAttackMissable(false);
                damageResult.setMessage("First attack is missed while attack " + vampire.getName());
                missesNumber++;
            }
        } else if (enemy.getEnemyType().equals(EnemyType.MIMIC)) {
            Mimic mimic = (Mimic) target;
            if (mimic.isImitate()) {
                damageResult.setMessage("This item turned out to be a mimic");
                mimic.setImitate(false);
                missesNumber--;
            } else {
                damageResult = dealDamage(target, weapon);
            }
        } else {
            damageResult = dealDamage(target, weapon);
        }
        if (damageResult.isDamageDealt()) {
            hitsNumber++;
        } else {
            missesNumber++;
        }
        return damageResult;
    }

    public void updateMaxHealth(int amount) {
        final int MIN_HEALTH = 1;
        setMaxHealth(getMaxHealth() + amount);
        if (getHealth() + amount > 0) {
            setHealth(getHealth() + amount);
        } else {
            setHealth(MIN_HEALTH);
        }
    }

    public void updateDexterity(int amount) {
        setDexterity(getDexterity() + amount);
    }

    public void updateStrength(int amount) {
        setStrength(getStrength() + amount);
    }

    public void elixirConsumingHandler(Elixir elixir, boolean isExpired) {
        int buffAmount;
        if (!isExpired) {
            activeElixirs.add(elixir);
        }
        switch (elixir.getSubType()) {
            case "MAX_HEALTH":
                buffAmount = elixir.getMaxHealth();
                updateMaxHealth((isExpired) ? -buffAmount : buffAmount);
                break;
            case "DEXTERITY":
                buffAmount = elixir.getDexterity();
                updateDexterity((isExpired) ? -buffAmount : buffAmount);
                break;
            case "STRENGTH":
                buffAmount = elixir.getStrength();
                updateStrength((isExpired) ? -buffAmount : buffAmount);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + elixir.getSubType());
        }
    }

    public void updateElixirs() {
        Iterator<Elixir> iterator = activeElixirs.iterator();
        while (iterator.hasNext()) {
            Elixir elixir = iterator.next();
            elixir.setBoostDuration(elixir.getBoostDuration() - 1);
            if (elixir.getBoostDuration() <= 0) {
                elixirConsumingHandler(elixir, true);
                iterator.remove();
            }
        }
    }

    public void restoreHealth(Item item) {
        setHealth(Math.min(this.getHealth() + item.getHealth(), this.getMaxHealth()));
    }

    @Override
    public String toString() {
        return super.toString() +
                " weapon=" + weapon;
    }
}
