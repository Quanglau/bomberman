package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

import java.util.Random;

public class Oneal extends Entity {
	Random rd = new Random();
	private int direction = rd.nextInt(4);
	public boolean died = false;
	public long timeDied = 1;
	Bomber bm;

	public Oneal(int x, int y, Image img) {
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

		if (Math.abs(bm.tx - x) <= 3 &&Math.abs(bm.ty - y) <= 3){
			if (bm.ty < y) {
				y = y - 0.2;
				if (canMove())
					y = Math.round(y);
				this.img = Sprite
						.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, (int) lastTime, 10)
						.getFxImage();

			}
			if (bm.ty > y) {
				y = y + 0.2;
				if (canMove())
					y = Math.round(y);
				this.img = Sprite
						.movingSprite(Sprite.oneal_right1, Sprite.oneal_right1, Sprite.oneal_right1, (int) lastTime, 10)
						.getFxImage();

			}
			if (bm.tx > x)

			{
				x = x + 0.2;
				if (canMove())
					x = Math.round(x);
				this.img = Sprite
						.movingSprite(Sprite.oneal_right1, Sprite.oneal_right1, Sprite.oneal_right1, (int) lastTime, 10)
						.getFxImage();

			}
			if (bm.tx < x) {
				x = x - 0.2;
				if (canMove())
					x = Math.round(x);
				this.img = Sprite
						.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, (int) lastTime, 10)
						.getFxImage();

			}

		} else {

			if (direction == 0) {
				y = y - 0.3;
				if (canMove())
					y = Math.round(y);
				this.img = Sprite
						.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, (int) lastTime, 10)
						.getFxImage();

			}
			if (direction == 1) {
				y = y + 0.3;
				if (canMove())
					y = Math.round(y);
				this.img = Sprite
						.movingSprite(Sprite.oneal_right1, Sprite.oneal_right1, Sprite.oneal_right1, (int) lastTime, 10)
						.getFxImage();

			}

			if (direction == 2) {
				x = x - 0.3;
				if (canMove())
					x = Math.round(x);
				this.img = Sprite
						.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, (int) lastTime, 10)
						.getFxImage();

			}

			if (direction == 3) {
				x = x + 0.3;
				if (canMove())
					x = Math.round(x);
				this.img = Sprite
						.movingSprite(Sprite.oneal_right1, Sprite.oneal_right1, Sprite.oneal_right1, (int) lastTime, 10)
						.getFxImage();
			}

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

	public void onealDied() {
		if ((BombermanGame.time - startTime) / 1000 > time) {
			Sound.play("aa");
			if (timeDied >= 0)
				timeDied--;
			time = (BombermanGame.time - startTime) / 1000;
		}
		this.img = Sprite.balloom_dead.getFxImage();
	}

}
