package uet.oop.bomberman.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;


public class Bomb extends Entity {
    public long timeBomb = 2;
    private boolean aBoolean = true;
    public boolean canMove = true;
    public int bombRadius;

    public Flame[] flame = new Flame[5];
    public int[] distance = new int[5];

    private Image[] imagesBomb = new Image[]{
            Sprite.bomb.getFxImage(),
            Sprite.bomb_1.getFxImage(),
            Sprite.bomb_2.getFxImage()
    };

    public Bomb(int x, int y, Image img) {
        super(x, y, img);

    }
    public Bomb(int x, int y, Image img,int bombRadius) {
        super(x, y, img);
        this.bombRadius = bombRadius;
    }


    long startTime = BombermanGame.time;
    long lastTime = 0;
    long longTime;

    @Override
    public void update() {
        longTime = (BombermanGame.time - startTime);
        if (longTime/1000 > lastTime) {
            if (timeBomb>=0) timeBomb--;
            lastTime = longTime/1000;
        }

        if(timeBomb>=1) bombAnimation();
        else {
            if (aBoolean){
                bombsExplode();
            }
            updateFlame();
        }

        if (!BombermanGame.bomberman.intersects(this)) canMove = false;

    }

    private void bombsExplode(){
        aBoolean = false;
        Sound.play("bombum");
        this.img = Sprite.bomb_exploded.getFxImage();
        DistanceFlame();
        for ( int i=0 ;i<5; i++) flame[i] = new Flame(x,y,null,i,distance[i]);
    }

    private void bombAnimation(){
        this.img = imagesBomb[(int) ((longTime/100)%imagesBomb.length)];

    }


    private  void updateFlame(){
        for ( int i=0 ;i<5; i++) flame[i].update();
    }

    private void DistanceFlame() {
        distance[0] = 1;
        for (int i = 1; i < 5; i++) distance[i] = bombRadius;

        Rectangle2D temp = new Rectangle2D(x*Sprite.SCALED_SIZE, (y-bombRadius)*Sprite.SCALED_SIZE
                , Sprite.explosion_vertical.getFxImage().getWidth(),Sprite.explosion_vertical.getFxImage().getHeight()*bombRadius);
        for (Entity entity:BombermanGame.stillObjects)  if (entity.getBoundary().intersects(temp)){
            if (distance[1] > (int) (y - entity.y)) {
                distance[1] = (int) (y - entity.y);
            }

        }

        temp = new Rectangle2D(x*Sprite.SCALED_SIZE, (y+1)*Sprite.SCALED_SIZE
                , Sprite.explosion_vertical.getFxImage().getWidth(),Sprite.explosion_vertical.getFxImage().getHeight()*bombRadius);
        for (Entity entity:BombermanGame.stillObjects)  if (entity.getBoundary().intersects(temp)){

            if (distance[2] > (int) (entity.y-y)) {
                distance[2] = (int) (entity.y-y);
            }
        }


        temp = new Rectangle2D((x-bombRadius)*Sprite.SCALED_SIZE, y*Sprite.SCALED_SIZE
                , Sprite.explosion_vertical.getFxImage().getWidth()*bombRadius,Sprite.explosion_vertical.getFxImage().getHeight());
        for (Entity entity:BombermanGame.stillObjects)  if (entity.getBoundary().intersects(temp)){

            if (distance[3] > (int) (x - entity.x)){
                distance[3] = (int) (x - entity.x);
            }

        }


        temp = new Rectangle2D((x+1)*Sprite.SCALED_SIZE, y*Sprite.SCALED_SIZE
                , Sprite.explosion_vertical.getFxImage().getWidth()*bombRadius,Sprite.explosion_vertical.getFxImage().getHeight());
        for (Entity entity:BombermanGame.stillObjects)  if (entity.getBoundary().intersects(temp)){
            if (distance[4] > (int) (entity.x-x)){
                distance[4] = (int) (entity.x-x);
            }
        }

    }


    public void render(GraphicsContext gc) {
        super.render(gc);
        if (!aBoolean)for (int i = 0; i < 5; i++) flame[i].renderFlameSegments(gc);
    }
}