package Beings;

import java.util.Random;
import java.util.Set;

public class Herbivorous extends Animals {
    private int count = 0;

    public Herbivorous(boolean male, int maxHp, int x, int y, double speed) {
        super(male, maxHp, x, y, speed);
    }

    @Override
    void reproduction(Set<Beings> unit) { //размножение травоядных
        Random rand = new Random();
        final Herbivorous mam = (Herbivorous) searchPartner(unit, Herbivorous.class);
        if (enemy(unit) == null && mam != null) {
            int distance = (int) Math.sqrt(Math.pow(x - mam.x, 2) + Math.pow(y - mam.y, 2));
            final double sin = (mam.y - y) / distance;
            final double cos = (mam.x - x) / distance;
            x += speed * cos;
            y += speed * sin;
            if (Math.abs(x - mam.x) <= 3 && Math.abs(y - mam.y) <= 3) {
                count++;
                currentHp -= currentHp * 0.0002;
                unit.add(new Herbivorous(rand.nextBoolean(),(int)(Math.min(maxHp, mam.maxHp) * 0.9 +
                        rand.nextDouble() * (Math.abs(maxHp - mam.maxHp) * 1.1)), (int)(x + 15), (int)(y + 15),
                        Math.min(speed, mam.speed) * 0.9 + rand.nextDouble() * (Math.abs(speed - mam.speed) * 1.1)));
            }
        }
        else if (enemy(unit) != null) {
            escape(unit);
        }
        else {
            movement();
        }
    }


    @Override
    Beings search(Set<Beings> unit, Class searchingClass) {
        int delta = Integer.MAX_VALUE;
        Beings searching = null;
        for(Beings temp: unit){
            if(temp.getClass().equals(searchingClass) && (int)(Math.sqrt(Math.pow(x - temp.x, 2)
                    + Math.pow(y - temp.y, 2))) < delta && ((searchingClass.equals(Plants.class)
                    || searchingClass.equals(Carnivorous.class) && temp.currentHp * 1.5 > currentHp))) {
                delta = (int)Math.sqrt(Math.pow(x - temp.x, 2) + Math.pow(y - temp.y, 2));
                searching = temp;
            }
        }
        return searching;
    }

    private Carnivorous enemy(Set<Beings> unit) { //проверка есть ли опасность
        final Carnivorous temp = (Carnivorous) search(unit, Carnivorous.class);
        if (temp != null && (int) Math.sqrt(Math.pow(x - temp.x, 2) + Math.pow(y - temp.y, 2)) <= 200) {
            return temp;
        }
        else return null;
    }

    private void escape(Set<Beings> unit) { //побег
        final Carnivorous enemy = enemy(unit);
        final int distance = (int) Math.sqrt(Math.pow(x - enemy.x, 2) + Math.pow(y - enemy.y, 2));
        final double sin = (enemy.y - y) / distance;
        final double cos = (enemy.x - x)/ distance;
        x -= 1.4 * speed * cos;
        y -= 1.4 * speed * sin;
        currentHp -= currentHp * 0.001;
        if(Math.abs(x - enemy.x) <= 1 && Math.abs(y - enemy.y) <= 1){
            live = false;
        }
    }

    private void hunger(Set<Beings> unit){ //поиск еды
        Plants temp = (Plants) search(unit, Plants.class);
        if(temp != null) {
            final int distance = (int) Math.sqrt(Math.pow(x - temp.x, 2) + Math.pow(y - temp.y, 2));
            final double sin = (temp.y - y) / distance;
            final double cos = (temp.x - x) / distance;
            x += 1.2 * speed * cos;
            y += 1.2 * speed * sin;
            currentHp -= currentHp * 0.002;
            if (Math.abs(x - temp.x) <= currentHp && Math.abs(y - temp.y) <= currentHp) {
                if(currentHp + temp.currentHp > maxHp){
                    currentHp = maxHp;
                }
                else {
                    currentHp += temp.currentHp;
                }
                temp.currentHp = 0;
            }
        }
        else {
            movement();
        }
    }

    @Override
    public boolean live(Set<Beings> unit){
        Random rand = new Random();
        check();

        currentHp -= maxHp * 0.0002;
        age += 0.002;
        if(currentHp <= 0.00001){
            return false;
        }
        else if(rand.nextInt((int)age * 100 + 1) == 450){
            return false;
        }
        else if(enemy(unit) != null){
            escape(unit);
            return live;
        }
        else if(currentHp < maxHp * 0.6){
            hunger(unit);
        }
        else if(currentHp >= maxHp * 0.6 && age >= 1 && age <= 1.1 && count < 2){
            reproduction(unit);
        }
        else {
            movement();
        }
        return true;
    }
}