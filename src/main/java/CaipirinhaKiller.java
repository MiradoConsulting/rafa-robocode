import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import java.awt.Color;

public class CaipirinhaKiller extends AdvancedRobot {
    private static final double CLOSE_DISTANCE = 150;
    private int corner = 0;

    public void run() {
        setColors(Color.white, Color.green, Color.white);
        while(true) {
            goToCorner();
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getDistance() < CLOSE_DISTANCE) {
            double bulletPower = Math.min(3.0, getEnergy());
            double myX = getX();
            double myY = getY();
            double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
            double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
            double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
            double enemyHeading = e.getHeadingRadians();
            double enemyVelocity = e.getVelocity();

            double deltaTime = 0;
            double battleFieldHeight = getBattleFieldHeight(),
                   battleFieldWidth = getBattleFieldWidth();
            double predictedX = enemyX, predictedY = enemyY;
            while((++deltaTime) * (20.0 - 3.0 * bulletPower) <
                  Math.sqrt((predictedX - myX) * (predictedX - myX) + (predictedY - myY) * (predictedY - myY))) {
                predictedX += Math.sin(enemyHeading) * enemyVelocity;
                predictedY += Math.cos(enemyHeading) * enemyVelocity;
                if( predictedX < 18.0 
                    || predictedY < 18.0
                    || predictedX > battleFieldWidth - 18.0
                    || predictedY > battleFieldHeight - 18.0){

                    predictedX = Math.min(Math.max(18.0, predictedX), 
                                          battleFieldWidth - 18.0); 
                    predictedY = Math.min(Math.max(18.0, predictedY), 
                                          battleFieldHeight - 18.0);
                    break;
                }
            }
            double theta = Utils.normalAbsoluteAngle(Math.atan2(
                predictedX - getX(), predictedY - getY()));

            setTurnRadarRightRadians(Utils.normalRelativeAngle(
                absoluteBearing - getRadarHeadingRadians()));
            setTurnGunRightRadians(Utils.normalRelativeAngle(
                theta - getGunHeadingRadians()));
            fire(bulletPower);
        }
    }

    private void goToCorner() {
        switch (corner) {
            case 0:
                setTurnRight(0);
                setAhead(100);
                break;
            case 1:
                setTurnRight(90);
                setAhead(100);
                break;
            case 2:
                setTurnRight(90);
                setAhead(100);
                break;
            case 3:
                setTurnRight(90);
                setAhead(100);
                break;
        }
        corner = ++corner % 4;
    }
}
