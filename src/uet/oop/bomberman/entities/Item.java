package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

public class Item extends Entity {
    public char aChar;
    public Item(int x, int y, Image img) {
        super(x, y, img);
    }
    public Item(int x, int y, Image img, char aChar) {
        super(x, y, img);
        this.aChar = aChar;
    }

    @Override
    public void update() {

    }
}
