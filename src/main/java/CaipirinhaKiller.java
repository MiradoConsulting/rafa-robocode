import robocode.*;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Rafa - a robot by (your name here)
 */
public class CaipirinhaKiller extends Robot{
    public void run() {
        // Robot main loop: Do nothing (robot stays still) until an event occurs
        while(true) {
            turnGunRight(360); // Turn the gun to scan for enemies
            // Doing nothing else here means the robot stays still when not scanning
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Aim by rotating the gun to the enemy's bearing
        double enemyBearing = this.getHeading() + e.getBearing();
        double gunTurnAmount = enemyBearing - this.getGunHeading();
        turnGunRight(normalizeBearing(gunTurnAmount));
        
        fire(1);
        turnRight(20);
        ahead(10);
    }
    
    // Method to normalize a bearing to between +180 and -180
    private double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
