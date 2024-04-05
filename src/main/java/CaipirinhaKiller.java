import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class CaipirinhaKiller extends AdvancedRobot {
    private double previousEnergy = 100;
    private int movementDirection = 1;
    private int gunDirection = 1;
    private int radarDirection = 1;

    public void run() {
        setBodyColor(new Color(0, 200, 0));
        setGunColor(new Color(0, 150, 50));
        setRadarColor(new Color(0, 100, 100));
        setScanColor(Color.white);
        setBulletColor(Color.blue);

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            turnRadarRight(360 * radarDirection);

            // Movement
            if (getTime() % 20 == 0) {
                movementDirection *= -1;
                setAhead(150 * movementDirection);
            }
            if (getTime() % 5 == 0) {
                gunDirection *= -1; // Changes the gun direction
                setTurnGunRight(360 * gunDirection);
            }

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        // Radar lock
        double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
        setTurnRadarRightRadians(1.9 * robocode.util.Utils.normalRelativeAngle(radarTurn));

        // Firepower based on distance
        double firePower = Math.min(400 / e.getDistance(), 3);
        
        // If the other robot has a drop in energy, it likely fired, so dodge.
        double changeInEnergy = previousEnergy - e.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            movementDirection *= -1;
            setAhead(150 * movementDirection);
        }
        previousEnergy = e.getEnergy();

        // Fire when the gun is cool and the bot is aligned with the enemy.
        if (Math.abs(bearingFromGun) <= 3) {
            setTurnGunRight(bearingFromGun);
            setFire(firePower);
        } else {
            setTurnGunRight(bearingFromGun);
        }

        // If the enemy is too close, back off
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                setBack(100);
            } else {
                setAhead(100);
            }
        }

        scan(); // Might not be needed, but added for safety to keep scanning.
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        setTurnLeft(90 - e.getBearing());
    }
}
