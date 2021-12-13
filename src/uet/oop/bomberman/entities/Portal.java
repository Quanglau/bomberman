package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

public class Portal extends Entity {

    public Portal(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        if (this.intersects(BombermanGame.bomberman)){
            if (BombermanGame.entities.isEmpty()) BombermanGame.nextLevel = true;

        }
    }
}
