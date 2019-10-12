package Beings;

import java.util.Random;
import java.util.Set;

public class Carnivorous extends Animals{
    private int count = 0;

    public Carnivorous(boolean male, int maxHp, int x, int y, double speed){
        super(male, maxHp, x, y, speed);
    }

    void reproduction(Set<Beings> unit){ //размножение хищников
        Random rand = new Random();
        final Carnivorous mam = (Carnivorous) searchPartner(unit, Carnivorous.class);
        if(mam != null) {
            int distance = (int) Math.sqrt(Math.pow(x - mam.x, 2) + Math.pow(y - mam.y, 2));
            final double sin = (mam.y - y) / distance;
            final double cos = (mam.x - x) / distance;
            x += speed * cos;
            y += speed * sin;
            if (Math.abs(x - mam.x) <= 3 && Math.abs(y - mam.y) <= 3) {
                count++;
                currentHp -= currentHp * 0.15;
                unit.add(new Carnivorous(rand.nextBoolean(),(int)(maxHp * 0.8 + rand.nextInt((int) Math.abs(maxHp
                        - mam.maxHp) + 1)), (int)(x + 15), (int)(y + 15), speed * 0.8 + rand.nextInt((int)(Math.abs(speed
                        - mam.speed)) + 1)));
            }
        }
        else {
            movement();
        }
    }

    private Herbivorous hunger(Set<Beings> unit){ //поиск еды
        Herbivorous temp = (Herbivorous) search(unit, Herbivorous.class);
        if(temp != null) {
            int distance = (int) Math.sqrt(Math.pow(x - temp.x, 2) + Math.pow(y - temp.y, 2));
            if (distance <= 500 && temp.currentHp <= currentHp) {
                return temp;
            }
        }
        else {
            movement();
        }
        return null;
    }

    private void pursuit(Set<Beings> unit){ //преследование добычи
        final Herbivorous hunger = hunger(unit);
        if(hunger != null) {
            final int distance = (int) Math.sqrt(Math.pow(x - hunger.x, 2) + Math.pow(y - hunger.y, 2));
            final double sin = (hunger.y - y) / distance;
            final double cos = (hunger.x - x) / distance;
            x += 1.3 * speed * cos;
            y += 1.3 * speed * sin;
            currentHp -= 0.002;
            if(Math.abs(x - hunger.x) <= 3 && Math.abs(y - hunger.y) <= 3){
                currentHp += hunger.currentHp;
                hunger.currentHp = 0;
            }
        }
        else {
            movement();
        }
    }

    public boolean live(Set<Beings> unit){
        Random rand = new Random();
        check();

        currentHp -= 0.001;
        age += 0.002;
        if(currentHp <= 0){
            return false;
        }
        else if(rand.nextInt((int)age + 1) == 3){
            return false;
        }
        else if(currentHp < maxHp * 0.7){
            pursuit(unit);
        }
        else if(currentHp >= maxHp * 0.5 && age >= 0.5 && age <= 0.6 && count < 2){
            reproduction(unit);
        }
        else {
            movement();
        }
        return true;
    }
}
