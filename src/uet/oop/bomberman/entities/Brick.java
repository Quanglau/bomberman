package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Iterator;

public class Brick extends Entity {
    public boolean aBoolean = true;
    private long time = 1;
    public char aChar;

    private Image[] imagesBrickExploded = new Image[]{
            Sprite.brick_exploded.getFxImage(),
            Sprite.brick_exploded1.getFxImage(),
            Sprite.brick_exploded2.getFxImage()
    };



    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }
    public Brick(int x, int y, Image img, char aChar) {
        super(x, y, img);
        this.aChar = aChar;
    }


    long startTime = BombermanGame.time;
    long lastTime = 0;
    @Override
    public void update() {
        if (time<0) aBoolean = false;
    }

    public void brickExploded(){
        if ( (BombermanGame.time - startTime)/1000 > lastTime) {
            if (time>=0) time--;
            lastTime = (BombermanGame.time - startTime)/1000;
        }
        this.img = imagesBrickExploded[(int) ((BombermanGame.time/100)%imagesBrickExploded.length)];

    }


}
