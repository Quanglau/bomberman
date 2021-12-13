package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

import java.util.Random;

public class doll extends Entity {
	Random rd = new Random();
	private int direction = rd.nextInt(4);
	public boolean died = false;
	public long timeDied = 1;
	Bomber bm;

	public doll(int x, int y, Image img) {
		super(x, y, img);
	}

	long startTime = BombermanGame.time;
	long lastTime = 0;
	long time = 0;

	@Override
	public void update() {
		if (timeDied < 1)
			died = true;
		if ((BombermanGame.time - startTime) / 100 > lastTime) {
			if (!died)
				move();
			lastTime = (BombermanGame.time - startTime) / 100;
		}
		if (this.intersects(BombermanGame.bomberman))
			BombermanGame.bomberman.bomberDied();

	}

	public void move() {

		if (bm.ty < y) {
			y = y - 0.2;
			if (canMove())
				y = Math.round(y);
			this.img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, (int) lastTime, 10)
					.getFxImage();

		}
		if (bm.ty > y) {
			y = y + 0.2;
			if (canMove())
				y = Math.round(y);
			this.img = Sprite
					.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, (int) lastTime, 10)
					.getFxImage();

		}
		if (bm.tx > x)

		{
			x = x + 0.2;
			if (canMove())
				x = Math.round(x);
			this.img = Sprite
					.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, (int) lastTime, 10)
					.getFxImage();

		}
		if (bm.tx < x) {
			x = x - 0.2;
			if (canMove())
				x = Math.round(x);
			this.img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, (int) lastTime, 10)
					.getFxImage();
		}
	}

	private boolean canMove() {
		for (Entity entity : BombermanGame.stillObjects) {
			if (this.intersects(entity)) {
				direction = rd.nextInt(4);
				return true;
			}
		}
		for (Bomb bomb : BombermanGame.bombs)
			if (this.intersects(bomb)) {
				x = Math.round(x);
				y = Math.round(y);
				direction = rd.nextInt(4);
				return true;
			}

		return false;
	}

	public void dollDied() {
		if ((BombermanGame.time - startTime) / 1000 > time) {
			Sound.play("aa");
			if (timeDied >= 0)
				timeDied--;
			time = (BombermanGame.time - startTime) / 1000;
		}
		this.img = Sprite.doll_dead.getFxImage();
	}

}
