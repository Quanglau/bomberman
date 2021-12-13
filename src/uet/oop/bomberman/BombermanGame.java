package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BombermanGame extends Application {
	public static int level = 1;
	public static int POINT = 0;
	public int row;
	public int column;
	public static long time = 0;
	public static long timeSpace = 0;
	public static boolean nextLevel = false;
	private static Text txt;
	private static GraphicsContext gc;
	private static Canvas canvas;
	private static Group root = new Group();
	private static Stage primaryStage;
	private static Scene scene;
	public static List<Entity> entities = new ArrayList<>();
	public static List<Entity> stillObjects = new ArrayList<>();
	public static List<Entity> grass = new ArrayList<>();
	public static ArrayList<Bomb> bombs = new ArrayList<>();
	public static ArrayList<Portal> portals = new ArrayList<>();
	public static ArrayList<Item> items = new ArrayList<>();
	private String[] maps;
	public static ArrayList<String> input = new ArrayList<String>();
	public static Bomber bomberman;
	public static void main(String[] args) {
		Application.launch(BombermanGame.class);
	}

	@Override
	public void start(Stage stage) {
		Sound.play("soundtrack");
		primaryStage = stage;

		readFromFile(1);
		canvas = new Canvas(Sprite.SCALED_SIZE * column, Sprite.SCALED_SIZE * row);
		
		Text text = new Text(200, 22, "Player: 1");
		text.setFill(Color.RED);
		text.setFont(Font.font("Verdana", 20));
		Text Level = new Text(450, 22, "Level: " + level);
		Level.setFill(Color.RED);
		Level.setFont(Font.font("Verdana", 20));
		gc = canvas.getGraphicsContext2D();
		root.getChildren().addAll(canvas, text, Level);
		scene = new Scene(root);

		// Them scene vao stage
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("Bomberman Game"+ "  | POINT: "+ POINT);
		createMap();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (!input.contains(event.getCode().toString()))
					input.add(event.getCode().toString());
				timeSpace = System.currentTimeMillis() / 100;
			}

		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				input.remove(event.getCode().toString());
			}
		});

		long longTime = System.nanoTime();
		final long[] lastTime = { 0 };
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				update();
				render();
				time = (l - longTime) / 1000000;

				if (nextLevel) {
					nextLevel = false;
					level++;
					Sound.play("CRYST_UP");
					nextLevel();
				}

				if (bomberman.died) {
//					bomberman = new Bomber(1, 2, Sprite.player_right.getFxImage());
//					bomberman.bombNumber = 1;
					endGame();
				}

			}
		};

		timer.start();
	}

	void readFromFile(int level) {
		try {
			File f = new File("res/levels/Level" + level + ".txt");
			Scanner scanner = new Scanner(f);

			level = scanner.nextInt();
			row = scanner.nextInt();
			column = scanner.nextInt();
			maps = new String[column];

			scanner.nextLine();
			for (int i = 0; i < row; i++)
				maps[i] = scanner.nextLine();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createMap() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				Entity object;
				grass.add(new Grass(j, i, Sprite.grass.getFxImage()));
				if (maps[i].charAt(j) == 'p') {
					bomberman = new Bomber(j, i, Sprite.player_right.getFxImage());
				} else if (maps[i].charAt(j) == '1')
					entities.add(new Balloon(j, i, Sprite.balloom_left1.getFxImage()));

				else if (maps[i].charAt(j) == '2')
					entities.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage()));

				else if (maps[i].charAt(j) == '3')
					entities.add(new doll(j, i, Sprite.oneal_left1.getFxImage()));

				if (maps[i].charAt(j) == '#') {
					object = new Wall(j, i, Sprite.wall.getFxImage());
				} else if (maps[i].charAt(j) == '*') {
					object = new Brick(j, i, Sprite.brick.getFxImage());
				} else if (maps[i].charAt(j) == 'x') {
					object = new Brick(j, i, Sprite.brick.getFxImage(), 'x');
				} else if (maps[i].charAt(j) == 'b') {
					object = new Brick(j, i, Sprite.brick.getFxImage(), 'b');
				} else if (maps[i].charAt(j) == 'f') {
					object = new Brick(j, i, Sprite.brick.getFxImage(), 'f');
				} else if (maps[i].charAt(j) == 's') {
					object = new Brick(j, i, Sprite.brick.getFxImage(), 's');
				} else if (maps[i].charAt(j) == 'v') {
					object = new Brick(j, i, Sprite.dell.getFxImage(), 'v');
				} else
					continue;
				stillObjects.add(object);
			}
		}
	}

	public void update() {
		entities.forEach(Entity::update);
		bomberman.update();
		stillObjects.forEach(Entity::update);
		bombs.forEach(Bomb::update);
		portals.forEach(Portal::update);
		brickRemove();
		entityRemove();
	}

	public void render() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		grass.forEach(g -> g.render(gc));

		portals.forEach(g -> g.render(gc));
		items.forEach(g -> g.render(gc));
		bombs.forEach(g -> g.render(gc));

		stillObjects.forEach(g -> g.render(gc));
		entities.forEach(g -> g.render(gc));
		bomberman.render(gc);

	}

	public void brickRemove() {
		Iterator<Entity> brickBag = BombermanGame.stillObjects.iterator();
		while (brickBag.hasNext()) {
			Entity brick = brickBag.next();
			if (brick instanceof Brick) {
				if (!((Brick) brick).aBoolean) {
					if (((Brick) brick).aChar == 'x') {
						portals.add(new Portal((int) brick.getX(), (int) brick.getY(), Sprite.portal.getFxImage()));
						brickBag.remove();
					} else if (((Brick) brick).aChar == 'b') {
						items.add(new Item((int) brick.getX(), (int) brick.getY(), Sprite.powerup_bombs.getFxImage(),
								'b'));
						brickBag.remove();
					} else if (((Brick) brick).aChar == 'f') {
						items.add(new Item((int) brick.getX(), (int) brick.getY(), Sprite.powerup_flames.getFxImage(),
								'f'));
						brickBag.remove();
					} else if (((Brick) brick).aChar == 's') {
						items.add(new Item((int) brick.getX(), (int) brick.getY(), Sprite.powerup_speed.getFxImage(),
								's'));
						brickBag.remove();
					}

					else
						brickBag.remove();
				}
			}
		}
	}

	public void entityRemove() {
		Iterator<Entity> entitiesBag = BombermanGame.entities.iterator();
		while (entitiesBag.hasNext()) {
			Entity entity = entitiesBag.next();
			if (entity instanceof Balloon) {
				if (((Balloon) entity).timeDied < 0) {
					entitiesBag.remove();
					POINT += 100;
				}
			}
			if (entity instanceof Oneal) {
				if (((Oneal) entity).died) {
					entitiesBag.remove();
					POINT += 200;
				}
			}

			if (entity instanceof doll) {
				if (((doll) entity).died) {
					entitiesBag.remove();
					POINT += 300;
				}
			}
			primaryStage.setTitle("Bomberman Game"+ "  | POINT: "+ POINT);
		}
	}

	private void nextLevel() {
		primaryStage.close();
		readFromFile(level);
		entities.clear();
		stillObjects.clear();
		bombs.clear();
		canvas = new Canvas(Sprite.SCALED_SIZE * column, Sprite.SCALED_SIZE * row);
		gc = canvas.getGraphicsContext2D();
		root.getChildren().clear();
		Text text = new Text(200, 22, "Player: 1");
		text.setFill(Color.RED);
		text.setFont(Font.font("Verdana", 20));
		Text Level = new Text(450, 22, "Level: " + level);
		Level.setFill(Color.RED);
		Level.setFont(Font.font("Verdana", 20));
		root.getChildren().addAll(canvas, text, Level);
		primaryStage.getScene().setRoot(root);
		primaryStage.isFullScreen();

		primaryStage.show();
		createMap();

	}

	private void endGame() {
		bomberman = new Bomber(2000, 2000, Sprite.red.getFxImage());
		entities.clear();
		stillObjects.clear();
		bombs.clear();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				Entity object;
				object = new Brick(j, i, Sprite.red.getFxImage(), 'v');
				stillObjects.add(object);
			}
		}

		Text text = new Text((Sprite.SCALED_SIZE * column) / 2 - 150, ((Sprite.SCALED_SIZE * row) / 2)-50, "GAME OVER");
		text.setFill(Color.YELLOW);
		text.setFont(Font.font("Verdana", 50));
		System.out.println(Sprite.SCALED_SIZE * column);
		Text point = new Text(346, ((Sprite.SCALED_SIZE * row) / 2)+50, "POINT: "+ POINT);
		point.setFill(Color.YELLOW);
		point.setFont(Font.font("Verdana", 40));
		
		root.getChildren().addAll(text,point);
		primaryStage.getScene().setRoot(root);
		primaryStage.isFullScreen();
		primaryStage.setTitle("GAME OVER");
		primaryStage.show();
	}
}
