package crack.firefly.com.Support.animation.normal.easing;

import crack.firefly.com.Support.animation.normal.Animation;

public class EaseOutBack extends Animation {

    private final float easeAmount;

    // Construtor principal
    public EaseOutBack(int ms, double endPoint, float easeAmount) {
        super(ms, endPoint);
        this.easeAmount = easeAmount;
    }
    
    // Construtor padrão (sem definir a força da mola)
    public EaseOutBack(int ms, double endPoint) {
        this(ms, endPoint, 1.70158f); 
    }

    @Override
    protected double getEquation(double x) {
        // Fórmula matemática de EaseOutBack adaptada para double
        double v = (x = x - 1) * x * ((easeAmount + 1) * x + easeAmount) + 1;
        return v;
    }
}