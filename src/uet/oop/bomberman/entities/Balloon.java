package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

import java.util.Random;

public class Balloon extends Entity {
    Random rd = new Random();
    private int direction = rd.nextInt(4);
    public boolean died = false;
    public long timeDied = 1;

    public Balloon(int x, int y, Image img) {
        super(x, y, img);
    }

    long startTime = BombermanGame.time;
    long lastTime = 0;
    long time = 0;
    @Override
    public void update() {
        if (timeDied<1) died = true;
        if ( (BombermanGame.time - startTime)/100> lastTime) {
            if (!died)move();
            lastTime = (BombermanGame.time - startTime)/100;
        }
        if (this.intersects(BombermanGame.bomberman)) BombermanGame.bomberman.bomberDied();

    }

    public void move() {

        if (direction == 0) {
            y = y - 0.1;
            if (canMove()) y = Math.round(y);
            this.img = Sprite.movingSprite(Sprite.balloom_left1,Sprite.balloom_left2,Sprite.balloom_left3,(int) lastTime,10).getFxImage();

        }
        if (direction == 1) {
            y = y + 0.1;
            if (canMove()) y = Math.round(y);
            this.img = Sprite.movingSprite(Sprite.balloom_right1,Sprite.balloom_right2,Sprite.balloom_right3,(int) lastTime,10).getFxImage();

        }

        if (direction == 2) {
            x = x - 0.1;
            if (canMove()) x = Math.round(x);
            this.img = Sprite.movingSprite(Sprite.balloom_left1,Sprite.balloom_left2,Sprite.balloom_left3,(int) lastTime,10).getFxImage();

        }

        if (direction == 3) {
            x = x + 0.1;
            if (canMove()) x = Math.round(x);
            this.img = Sprite.movingSprite(Sprite.balloom_right1,Sprite.balloom_right2,Sprite.balloom_right3,(int) lastTime,10).getFxImage();
        }

    }

    private boolean canMove(){
        for (Entity entity:BombermanGame.stillObjects ) {
            if (this.intersects(entity)) {
                direction = rd.nextInt(4);
                return true;
            }
        }
        for (Bomb bomb:BombermanGame.bombs) if(this.intersects(bomb)) {
            x = Math.round(x);
            y = Math.round(y);
            direction = rd.nextInt(4);
            return true;
        }

        return false;
    }

    public void balloonDied(){
        if ( (BombermanGame.time - startTime)/1000 > time) {
        	Sound.play("aa");
            if (timeDied>=0) timeDied--;
            time = (BombermanGame.time - startTime)/1000;
        }
        this.img = Sprite.balloom_dead.getFxImage();
    }

}
