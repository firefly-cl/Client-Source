package crack.firefly.com.Support.animation.normal.other;

import crack.firefly.com.Support.animation.normal.Animation;
import crack.firefly.com.Support.animation.normal.Direction;

public class SmoothStepAnimation extends Animation {

    public SmoothStepAnimation(int ms, double endPoint) {
        super(ms, endPoint);
        this.reset();
    }

    public SmoothStepAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
        this.reset();
    }

    protected double getEquation(double x) {
        double x1 = x / (double) duration;
        return -2 * Math.pow(x1, 3) + (3 * Math.pow(x1, 2));
    }
}
