package crack.firefly.com.System.cosmetic;
import java.util.ArrayList;
import java.util.List;

public class HatManager {
    private final List<Hat> hats = new ArrayList<>();
    private Hat currentHat;
    public HatManager() {
        hats.add(new Hat("Fox Hat", "fox.obj", "fox.png"));
    }
    public List<Hat> getHats() { return hats; }
    public Hat getCurrentHat() { return currentHat; }
    public void setCurrentHat(Hat hat) { this.currentHat = hat; }
}