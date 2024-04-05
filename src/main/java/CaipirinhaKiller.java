import robocode.Robot;
import robocode.ScannedRobotEvent;
import java.awt.Color;
import java.util.Random;

public class MyRobot extends Robot {
    Random rand = new Random();

    public void run() {
        // Set the robot's colors
        setColors(Color.white, // Body
                  Color.green, // Gun
                  Color.white, // Radar
                  Color.white, // Bullet
                  Color.white); // Scan arc
        
        // Robot main loop
        while(true) {
            turnGunRight(360); // Continuously turn gun to scan
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Aim and shoot
        double enemyBearing = this.getHeading() + e.getBearing();
        double gunTurnAmount = enemyBearing - this.getGunHeading();
        turnGunRight(normalizeBearing(gunTurnAmount));
        fire(1);
        
        // Turn right by a random amount between 20 and 60 degrees
        turnRight(20 + rand.nextInt(41));
        
        // Move ahead by a random distance between 20 and 100 units
        ahead(20 + rand.nextInt(81));
    }
    
    private double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
