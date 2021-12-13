package uet.oop.bomberman.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    protected double x;   
    protected double y;   

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    protected Image img;

   
    public Entity( double xUnit, double yUnit, Image img) {
        this.x = xUnit ;
        this.y = yUnit ;
        this.img = img;
    }


    public void render(GraphicsContext gc) {
        gc.drawImage(img, x* Sprite.SCALED_SIZE, y* Sprite.SCALED_SIZE);
    }

    public Rectangle2D getBoundary()
    {
        if (img == null) return new Rectangle2D(x* Sprite.SCALED_SIZE,y* Sprite.SCALED_SIZE,Sprite.SCALED_SIZE,Sprite.SCALED_SIZE);
        return new Rectangle2D(x* Sprite.SCALED_SIZE,y* Sprite.SCALED_SIZE,img.getWidth(),img.getHeight());
    }
    public boolean intersects(Entity s) {
        return s.getBoundary().intersects( this.getBoundary() );
    }
    public abstract void update();

}
