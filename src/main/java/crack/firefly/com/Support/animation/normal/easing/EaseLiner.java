package crack.firefly.com.Support.animation.normal.easing;

import crack.firefly.com.Support.animation.normal.Animation;

public class EaseLiner extends Animation {

	public EaseLiner(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
		return x / duration;
	}
}