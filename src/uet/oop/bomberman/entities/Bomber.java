package uet.oop.bomberman.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

import java.util.ArrayList;
import java.util.Iterator;

public class Bomber extends Entity {
	public long count = 0;
	public double speed = 1.5;
	public int bombNumber = 1;
	private double x_ = x, y_ = y;
	public int bombRadius = 1;
	public boolean died = false;
	public static int tx,ty;
	
	
	private Image[] imagesRIGHT = new Image[] { Sprite.player_right.getFxImage(), Sprite.player_right_1.getFxImage(),
			Sprite.player_right_2.getFxImage() };
	private Image[] imagesLEFT = new Image[] { Sprite.player_left.getFxImage(), Sprite.player_left_1.getFxImage(),
			Sprite.player_left_2.getFxImage() };

	private Image[] imagesDOWN = new Image[] { Sprite.player_down.getFxImage(), Sprite.player_down_1.getFxImage(),
			Sprite.player_down_2.getFxImage() };
	private Image[] imagesUP = new Image[] { Sprite.player_up.getFxImage(), Sprite.player_up_1.getFxImage(),
			Sprite.player_up_2.getFxImage() };

	public Bomber(double x, double y, Image img) {
		super(x, y, img);
	}

	@Override
	public void update() {
		count = BombermanGame.time / 100;
		move();
		if (BombermanGame.input.contains("SPACE") && bombNumber > 0
				&& BombermanGame.timeSpace == System.currentTimeMillis() / 100)
			putBomb();
		bombRemove();
		itemsCollide();

	}

	public void move() {
		tx= (int) Math.round(x);
		ty= (int) Math.round(y);
		
		if (BombermanGame.input.contains("UP")) {
			y = y - 0.035 * speed;
			if (canMove())
				y = Math.round(y);
			int i = (int) ((count % (imagesUP.length * Math.pow(10, -speed)) / Math.pow(10, -speed)));
			this.img = imagesUP[i];
		}
		if (BombermanGame.input.contains("DOWN")) {
			y = y + 0.035 * speed;
			if (canMove())
				y = Math.round(y);
			int i = (int) ((count % (imagesDOWN.length * Math.pow(10, -speed)) / Math.pow(10, -speed)));
			this.img = imagesDOWN[i];
		}

		if (BombermanGame.input.contains("LEFT")) {
			x = x - 0.035 * speed;
			if (canMove())
				x = Math.round(x);
			int i = (int) ((count % (imagesLEFT.length * Math.pow(10, -speed)) / Math.pow(10, -speed)));
			this.img = imagesLEFT[i];
		}

		if (BombermanGame.input.contains("RIGHT")) {
			x = x + 0.035 * speed;
			if (canMove())
				x = Math.round(x);

			int i = (int) ((count % (imagesRIGHT.length * Math.pow(10, -speed)) / Math.pow(10, -speed)));
			this.img = imagesRIGHT[i];
		}

	}

	private boolean canMove() {
		for (Entity entity : BombermanGame.stillObjects) {

			if (this.intersects(entity)) {
				if (BombermanGame.input.contains("RIGHT")) {
					if (Math.abs(y - Math.round(y)) < 0.3)
						y = Math.round(y);
					return true;
				}
				if (BombermanGame.input.contains("LEFT")) {
					if (Math.abs(y - Math.round(y)) < 0.3)
						y = Math.round(y);
					return true;
				}
				if (BombermanGame.input.contains("UP")) {
					if (Math.abs(x - Math.round(x)) < 0.3)
						x = Math.round(x);
					return true;
				}
				if (BombermanGame.input.contains("DOWN")) {
					if (Math.abs(x - Math.round(x)) < 0.3)
						x = Math.round(x);
					return true;
				}
				return true;
			}
		}

		for (Bomb bomb : BombermanGame.bombs) {
			if (this.intersects(bomb))
				if (!bomb.canMove)
					return true;
		}

		return false;
	}

	private void putBomb() {
		bombNumber--;
		BombermanGame.timeSpace = 0;
		BombermanGame.bombs.add(new Bomb((int) Math.round(x), (int) Math.round(y), null, bombRadius));
		Sound.play("BOM_SET");

	}

	public void bombRemove() {
		Iterator<Bomb> bombsBag = BombermanGame.bombs.iterator();
		while (bombsBag.hasNext()) {
			Bomb bomb = bombsBag.next();
			if (bomb.timeBomb < 0) {
				bombsBag.remove();
				BombermanGame.bomberman.bombNumber++;
			}
		}
	}

	public void itemsCollide() {
		Iterator<Item> itemsBag = BombermanGame.items.iterator();
		while (itemsBag.hasNext()) {
			Item item = itemsBag.next();
			if (item.intersects(this)) {
				if (item.aChar == 'b')
					bombNumber++;
				else if (item.aChar == 'f')
					bombRadius++;
				else if (item.aChar == 's')
					speed++;
				Sound.play("Item");
				itemsBag.remove();
			}
		}
	}

	public void bomberDied() {
		died = true;
		bombNumber -= 1;
		Sound.play("AA123");
		this.img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, (int) count, 10)
				.getFxImage();
		
		
	}

}
