package crack.firefly.com.System.mods;

import crack.firefly.com.Firefly;
import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.notification.NotificationType;
import crack.firefly.com.Support.animation.simple.SimpleAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Mod {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;

    private TranslateText nameTranslate, descriptionTranslate;
    private boolean toggled, hide;
    private SimpleAnimation animation = new SimpleAnimation();
    private ModCategory category;
    private String alias = "\u200B"; // zerowidth space
    private Boolean restricted = false, allowed = true;

    public Mod(TranslateText nameTranslate, TranslateText descriptionTranslate, ModCategory category) {

        this.nameTranslate = nameTranslate;
        this.descriptionTranslate = descriptionTranslate;
        this.toggled = false;
        this.category = category;

        this.setup();
    }

    public Mod(TranslateText nameTranslate, TranslateText descriptionTranslate, ModCategory category, String alias) {

        this.nameTranslate = nameTranslate;
        this.descriptionTranslate = descriptionTranslate;
        this.toggled = false;
        this.category = category;
        this.alias = alias;

        this.setup();
    }

    public Mod(TranslateText nameTranslate, TranslateText descriptionTranslate, ModCategory category, String alias, boolean restricted) {

        this.nameTranslate = nameTranslate;
        this.descriptionTranslate = descriptionTranslate;
        this.toggled = false;
        this.category = category;
        this.alias = alias;
        this.restricted = restricted;

        this.setup();
    }

    public void setup() {
    }

    public void onEnable() {
        if (Firefly.getInstance().getRestrictedMod().checkAllowed(this)) {
            Firefly.getInstance().getEventManager().register(this);
            FireflyConsole.info("[MODULE] " + getName() + " was enabled");
        } else {
            this.setToggled(false);
            Firefly.getInstance().getNotificationManager().post(this.nameTranslate.getText(), "Disabled due to serverside blacklist", NotificationType.INFO);
        }
    }

    public void onDisable() {
        Firefly.getInstance().getEventManager().unregister(this);
        FireflyConsole.info("[MODULE] " + getName() + " was disabled");
    }

    public void toggle() {
        setToggled(!toggled, true);
    }

    public void setToggled(boolean toggled) {
        setToggled(toggled, false);
    }

    public void setToggled(boolean toggled, boolean sound) {

        this.toggled = toggled;

        if (toggled) {
            onEnable();
            if (sound) Firefly.getInstance().getModManager().playToggleSound(true);
        } else {
            onDisable();
            if (sound) Firefly.getInstance().getModManager().playToggleSound(false);
        }
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public String getDescription() {
        return descriptionTranslate.getText();
    }

    public String getNameKey() {
        return nameTranslate.getKey();
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isToggled() {
        return toggled;
    }

    public ModCategory getCategory() {
        return category;
    }

    public void setCategory(ModCategory category) {
        this.category = category;
    }

    public SimpleAnimation getAnimation() {
        return animation;
    }

    public String getAlias() {
        return this.alias;
    }

    public Boolean isRestricted() {
        return this.restricted;
    }

    public Boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(boolean modAllowed) {
        this.allowed = modAllowed;
    }

}
