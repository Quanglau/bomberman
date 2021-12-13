package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
// load khung hình các nhân vật
public class Flame extends Entity {
	private int radiusFlame, direction;

	public FlameSegment[] flameSegments;

	private Image[] imagesFlameCenter = new Image[] { Sprite.bomb_exploded.getFxImage(),
			Sprite.bomb_exploded1.getFxImage(), Sprite.bomb_exploded2.getFxImage() };
	private Image[] imagesFlameTopLast = new Image[] { Sprite.explosion_vertical_top_last.getFxImage(),
			Sprite.explosion_vertical_top_last1.getFxImage(), Sprite.explosion_vertical_top_last2.getFxImage() };
	private Image[] imagesFlameDownLast = new Image[] { Sprite.explosion_vertical_down_last.getFxImage(),
			Sprite.explosion_vertical_down_last1.getFxImage(), Sprite.explosion_vertical_down_last2.getFxImage() };
	private Image[] imagesFlameLeftLast = new Image[] { Sprite.explosion_horizontal_left_last.getFxImage(),
			Sprite.explosion_horizontal_left_last1.getFxImage(), Sprite.explosion_horizontal_left_last2.getFxImage() };
	private Image[] imagesFlameRightLast = new Image[] { Sprite.explosion_horizontal_right_last.getFxImage(),
			Sprite.explosion_horizontal_right_last1.getFxImage(),
			Sprite.explosion_horizontal_right_last2.getFxImage() };
	private Image[] imagesFlameHorizontal = new Image[] { Sprite.explosion_horizontal.getFxImage(),
			Sprite.explosion_horizontal1.getFxImage(), Sprite.explosion_horizontal2.getFxImage() };
	private Image[] imagesFlameVertical = new Image[] { Sprite.explosion_vertical.getFxImage(),
			Sprite.explosion_vertical1.getFxImage(), Sprite.explosion_vertical2.getFxImage() };

	public Flame(double x, double y, Image img, int direction, int radiusFlame) {

		super(x, y, img);
		this.direction = direction;
		this.radiusFlame = radiusFlame;

		switch (direction) {
		case 0:
			flameSegments = new FlameSegment[1];
			flameSegments[0] = new FlameSegment(x, y, Sprite.bomb_exploded.getFxImage());
			break;
		case 1:
			flameSegments = new FlameSegment[radiusFlame];
			for (int i = 0; i < radiusFlame; i++)
				flameSegments[i] = new FlameSegment(x, y - i - 1, null);
			break;
		case 2:
			flameSegments = new FlameSegment[radiusFlame];
			for (int i = 0; i < radiusFlame; i++)
				flameSegments[i] = new FlameSegment(x, y + i + 1, null);
			break;
		case 3:
			flameSegments = new FlameSegment[radiusFlame];
			for (int i = 0; i < radiusFlame; i++)
				flameSegments[i] = new FlameSegment(x - i - 1, y, null);
			break;
		case 4:
			flameSegments = new FlameSegment[radiusFlame];
			for (int i = 0; i < radiusFlame; i++)
				flameSegments[i] = new FlameSegment(x + i + 1, y, null);
			break;
		}

	}

	@Override
	public void update() {
		FlameAnimation();
		flamesCollide();
	}

	public void FlameAnimation() {
		switch (direction) {
		case 0:
			flameSegments[0].img = imagesFlameCenter[(int) ((BombermanGame.time / 100) % imagesFlameCenter.length)];
			break;
		case 1:
			for (int i = 0; i < radiusFlame - 1; i++)
				flameSegments[i].img = imagesFlameVertical[(int) ((BombermanGame.time / 100)
						% imagesFlameCenter.length)];
			flameSegments[radiusFlame
					- 1].img = imagesFlameTopLast[(int) ((BombermanGame.time / 100) % imagesFlameCenter.length)];
			break;
		case 2:
			for (int i = 0; i < radiusFlame - 1; i++)
				flameSegments[i].img = imagesFlameVertical[(int) ((BombermanGame.time / 100)
						% imagesFlameCenter.length)];
			flameSegments[radiusFlame
					- 1].img = imagesFlameDownLast[(int) ((BombermanGame.time / 100) % imagesFlameCenter.length)];
			break;
		case 3:
			for (int i = 0; i < radiusFlame - 1; i++)
				flameSegments[i].img = imagesFlameHorizontal[(int) ((BombermanGame.time / 100)
						% imagesFlameCenter.length)];
			flameSegments[radiusFlame
					- 1].img = imagesFlameLeftLast[(int) ((BombermanGame.time / 100) % imagesFlameCenter.length)];
			break;
		case 4:
			for (int i = 0; i < radiusFlame - 1; i++)
				flameSegments[i].img = imagesFlameHorizontal[(int) ((BombermanGame.time / 100)
						% imagesFlameCenter.length)];
			flameSegments[radiusFlame
					- 1].img = imagesFlameRightLast[(int) ((BombermanGame.time / 100) % imagesFlameCenter.length)];
			break;
		}
	}

	public void flamesCollide() {
		for (int i = 0; i < radiusFlame; i++) {
			for (Entity entity : BombermanGame.entities)
				if (flameSegments[i].intersects(entity)) {
					if (entity instanceof Oneal)
						((Oneal) entity).onealDied();
					if (entity instanceof Balloon)
						((Balloon) entity).balloonDied();
					if (entity instanceof doll)
						((doll) entity).dollDied();
				}

			for (Entity entity : BombermanGame.stillObjects)
				if (flameSegments[i].intersects(entity) && (entity instanceof Brick))
					((Brick) entity).brickExploded();

			if (flameSegments[i].intersects(BombermanGame.bomberman))
				BombermanGame.bomberman.bomberDied();
		}
	}

	public void renderFlameSegments(GraphicsContext gc) {
		for (int i = 0; i < radiusFlame; i++)
			flameSegments[i].render(gc);
	}
}