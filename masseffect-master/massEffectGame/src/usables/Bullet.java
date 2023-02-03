package usables;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import playable.*;

import application.ObjectController;
import application.ObjectSpawner;

public class Bullet extends WeaponPane{
	
	public Node bulletNode;
	public Circle bulletShape = null;
	int x, y, angleOfFire;// 0 degrees is vertically upwards, counts clockwise.
	final int pixelsPerSecond = 6, bulletRange = 300;
	int pixelsTravelled = 0;
	private ObjectController objControl;
	public static float uniqueId;
	public Player playerWhoShot;


	public Bullet(int x, int y, int angleOfFire, Scene scene, Pane root, ObjectController objControl, Player playerWhoShot) {
		super(x, y, scene);
		
		this.x = x;
		this.y = y;
		this.angleOfFire = angleOfFire;
		this.objControl = objControl;
		this.playerWhoShot = playerWhoShot;
		
		if (angleOfFire == 90) {//changing the spawn position of the bullet
			x += 76;
			y += 62;
		} else if (angleOfFire == 270) {
			y += 62;
		}
		
		bulletShape = new Circle(x, y, 3, Color.BLUE);
		
		bulletNode = bulletShape;
		 
		root.getChildren().addAll(bulletNode);
		
		this.uniqueId = x*y*ObjectSpawner.currentLevelClock;
	}
	
	public void update() {
		
		pixelsTravelled++;
		if(pixelsTravelled >= bulletRange && !playerWhoShot.getPlayableID().equals(PlayableID.GunSeeker)) {
			bulletShape.setRadius(0);
			objControl.bullets.remove(this);

		} else if(playerWhoShot.getPlayableID().equals(PlayableID.GunSeeker)) {
			if(pixelsTravelled > (bulletRange*(7/4))) {
				bulletShape.setRadius(0);
				objControl.bullets.remove(this);
			}
		}
		
		movement();
		collision();		
				
		if (x > 1920 || x < 0 || y > 1080 || y < 0) {
			bulletShape.setRadius(0);
			objControl.bullets.remove(this);
		}
			
	}
	
	public void movement() {
		
		if(angleOfFire == 90) {
			bulletShape.setCenterX(bulletShape.getCenterX() + pixelsPerSecond);
		} else if (angleOfFire == 270) {
			bulletShape.setCenterX(bulletShape.getCenterX() - pixelsPerSecond);
		}
		x = (int) bulletShape.getCenterX();
		y = (int) bulletShape.getCenterY();
		pixelsTravelled += pixelsPerSecond;
		
	}
	
	public void collision() {
				
		for(Player tempPlayer: objControl.players) {
			if(tempPlayer.getBounds().contains(bulletShape.getCenterX(), bulletShape.getCenterY())) {
				if(Player.isHider(tempPlayer)){
					tempPlayer.setHealth(tempPlayer.getHealth() - (Hider.MAX_HEALTH/4));
					bulletShape.setRadius(0);
					objControl.removeBullet(this);
				}
				if(tempPlayer.getPlayableID().equals(PlayableID.HidingPlayer) || tempPlayer.getPlayableID().equals(PlayableID.AIHidingPlayer)){
					tempPlayer.setHealth(tempPlayer.getHealth() - (HidingPlayer.HEALTH/4));
					bulletShape.setRadius(0);
					objControl.removeBullet(this);
				}
			}
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Node getNode() {
		return bulletNode;
	}

	public void setNode(Node bulletNode) {
		this.bulletNode = bulletNode;
	}

	public int getAngleOfFire() {
		return angleOfFire;
	}

	public void setAngleOfFire(int angleOfFire) {
		this.angleOfFire = angleOfFire;
	}

	public static float getUniqueId() {
		return uniqueId;
	}

	public static void setUniqueId(float uniqueId) {
		Bullet.uniqueId = uniqueId;
	}
}
