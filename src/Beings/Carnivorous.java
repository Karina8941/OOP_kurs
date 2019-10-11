package Beings;

import java.util.Random;
import java.util.Set;

public class Carnivorous extends Animals{

    public Carnivorous(boolean male, int maxHp, int x, int y, double speed){
        super(male, maxHp, x, y, speed);
    }

    void reproduction(Set<Beings> unit){ //размножение хищников
        double sin, cos;
        Carnivorous mam = (Carnivorous) search(unit, Carnivorous.class);
        if(mam != null) {
            int distance = (int) Math.sqrt(Math.pow(x - mam.x, 2) + Math.pow(y - mam.y, 2));
            sin = Math.abs(y - mam.y) / distance;
            cos = Math.abs(x - mam.x) / distance;
            x += speed * cos;
            y += speed * sin;
            if (Math.abs(x - mam.x) <= 3 && Math.abs(y - mam.y) <= 3) {
                currentHp -= maxHp * 0.15;
                goal = true;
                unit.add(new Carnivorous(new Random().nextBoolean(), (int) (maxHp * 0.8 + new Random().nextInt((int) Math.abs(maxHp
                        - mam.maxHp))), x + 15, y + 15, (int) (speed * 0.8 + new Random().nextInt((int) (Math.abs(speed
                        - mam.speed))))));
            }
        }
    }

    private Herbivorous hunger(Set<Beings> unit){ //поиск еды
        Herbivorous temp = (Herbivorous) search(unit, Herbivorous.class);
        if(temp != null) {
            int distance = (int) Math.sqrt(Math.pow(x - temp.x, 2) + Math.pow(y - temp.y, 2));
            if (distance <= 300) {
                return temp;
            }
        }
        return null;
    }

    private void pursuit(Set<Beings> unit){ //преследование добычи
        Herbivorous hunger = hunger(unit);
        if(hunger != null) {
            int distance;
            double sin, cos;
            distance = (int) Math.sqrt(Math.pow(x - hunger.x, 2) + Math.pow(y - hunger.y, 2));
            sin = Math.abs(y - hunger.y) / distance;
            cos = Math.abs(x - hunger.x) / distance;
            x += 2 * speed * cos;
            y += 2 * speed * sin;
            currentHp -= 0.002;
            if(Math.abs(this.x - hunger.x) <= 3 && Math.abs(this.y - hunger.y) <= 3){
                currentHp += hunger.currentHp * 0.8;
                hunger.currentHp = 0;
            }
        }
    }

    public boolean live(Set<Beings> unit){
        check();
        currentHp -= 0.01;
        age += 0.002;
        if(currentHp <= 0){
            return false;
        }
        else if(currentHp < maxHp * 0.7){
            pursuit(unit);
        }
        else if(currentHp >= maxHp * 0.8 && age >= 2 && age <= 2.1 && !goal){
            reproduction(unit);
        }
        else {
            movement();
        }
        return true;
    }
}
