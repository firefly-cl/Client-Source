package crack.firefly.com;

import java.io.File;
import java.util.Arrays;

import crack.firefly.com.menus.main.FireflyUI;
import crack.firefly.com.menus.mod.GuiModMenu;
import crack.firefly.com.System.mods.RestrictedMod;
import crack.firefly.com.System.remote.blacklists.BlacklistManager;
import crack.firefly.com.System.remote.discord.DiscordStats;
import crack.firefly.com.System.remote.news.NewsManager;
import crack.firefly.com.System.remote.update.Update;
import crack.firefly.com.ui.ClickEffects;
import crack.firefly.com.Support.Sound;

import org.apache.commons.lang3.ArrayUtils;

import crack.firefly.com.Loader.mixin.FireflyTweaker;
import crack.firefly.com.Console.FireflyConsole;
import crack.firefly.com.System.cape.CapeManager;
import crack.firefly.com.System.cape.CloudCapeManager;
import crack.firefly.com.System.web.UserTracker;
import crack.firefly.com.System.web.websocket.WebSocketManager;
import crack.firefly.com.System.remote.changelog.ChangelogManager;
import crack.firefly.com.System.color.ColorManager;
import crack.firefly.com.System.command.CommandManager;
import crack.firefly.com.System.event.EventManager;
import crack.firefly.com.System.file.FileManager;
import crack.firefly.com.System.language.LanguageManager;
import crack.firefly.com.System.mods.ModManager;
import crack.firefly.com.System.mods.impl.InternalSettingsMod;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.notification.NotificationManager;
import crack.firefly.com.System.profile.ProfileManager;
import crack.firefly.com.System.quickplay.QuickPlayManager;
import crack.firefly.com.System.screenshot.ScreenshotManager;
import crack.firefly.com.System.security.SecurityFeatureManager;
import crack.firefly.com.System.waypoint.WaypointManager;
import crack.firefly.com.Support.OptifineUtils;
import crack.firefly.com.System.cosmetic.HatManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Firefly {

    public static float customScale = 2.0f;
    private static Firefly instance = new Firefly();
    private Minecraft mc = Minecraft.getMinecraft();

    private boolean updateNeeded, soar8Released;
    private String name, version;
    private int verIdentifier;

    private NanoVGManager nanoVGManager;
    private FileManager fileManager;
    private LanguageManager languageManager;
    private EventManager eventManager;
    private ModManager modManager;
    private CapeManager capeManager;
    private HatManager hatManager;
    private ColorManager colorManager;
    private ProfileManager profileManager;
    private CommandManager commandManager;
    private ScreenshotManager screenshotManager;
    private NotificationManager notificationManager;
    private SecurityFeatureManager securityFeatureManager;
    private QuickPlayManager quickPlayManager;
    private ChangelogManager changelogManager;
    private NewsManager newsManager;
    private DiscordStats discordStats;
    private WaypointManager waypointManager;
    private GuiModMenu modMenu;
    private FireflyUI mainMenu;
    private long launchTime;
    private File firstLoginFile;
    private Update update;
    private ClickEffects clickEffects;
    private BlacklistManager blacklistManager;
    private RestrictedMod restrictedMod;

    // 🔌 WEBSOCKET
    private WebSocketManager webSocketManager;

    public Firefly() {
        name = "Firefly";
        version = "";
        verIdentifier = 0;
    }

    public void start() {
        try {
            OptifineUtils.disableFastRender();
            this.removeOptifineZoom();
        } catch (Exception ignored) {}

        blacklistManager = new BlacklistManager();
        restrictedMod = new RestrictedMod();

        try {
            restrictedMod.shouldCheck = !System
                    .getProperty("me.eldodebug.soar.glideclient.blacklistchecks", "true")
                    .equalsIgnoreCase("false");
        } catch (Exception ignored) {}

        fileManager = new FileManager();
        firstLoginFile = new File(fileManager.getCacheDir(), "first.tmp");
        languageManager = new LanguageManager();
        eventManager = new EventManager();

        modManager = new ModManager();
        modManager.init();

        capeManager = new CapeManager();
        hatManager = new HatManager();

        CloudCapeManager.reload();
        UserTracker.track();

        colorManager = new ColorManager();
        profileManager = new ProfileManager();
        modMenu = new GuiModMenu();
        mainMenu = new FireflyUI();
        launchTime = System.currentTimeMillis();
        commandManager = new CommandManager();
        screenshotManager = new ScreenshotManager();
        notificationManager = new NotificationManager();
        securityFeatureManager = new SecurityFeatureManager();
        quickPlayManager = new QuickPlayManager();
        changelogManager = new ChangelogManager();
        newsManager = new NewsManager();

        discordStats = new DiscordStats();
        discordStats.check();

        update = new Update();
        update.check();

        waypointManager = new WaypointManager();
        eventManager.register(new FireflyManipulador());
        InternalSettingsMod.getInstance().setToggled(true);

        clickEffects = new ClickEffects();
        mc.updateDisplay();

        // 🔌 INICIA WEBSOCKET
        webSocketManager = new WebSocketManager();
        webSocketManager.init();

        FireflyConsole.info("Firefly Client iniciado com sucesso");
    }

    public void stop() {
        profileManager.save();

        if (webSocketManager != null) {
            webSocketManager.shutdown();
        }
    }

    private void removeOptifineZoom() {
        if (FireflyTweaker.hasOptifine) {
            try {
                this.unregisterKeybind(
                        (KeyBinding) GameSettings.class
                                .getField("ofKeyBindZoom")
                                .get(mc.gameSettings)
                );
            } catch (Exception e) {
                FireflyConsole.error("Failed to unregister zoom key", e);
            }
        }
    }

    private void unregisterKeybind(KeyBinding key) {
        if (Arrays.asList(mc.gameSettings.keyBindings).contains(key)) {
            mc.gameSettings.keyBindings =
                    ArrayUtils.remove(
                            mc.gameSettings.keyBindings,
                            Arrays.asList(mc.gameSettings.keyBindings).indexOf(key)
                    );
            key.setKeyCode(0);
        }
    }

    public static Firefly getInstance() { return instance; }

    public HatManager getHatManager() { return hatManager; }
    public String getName() { return name; }
    public String getVersion() { return version; }
    public int getVersionIdentifier() { return verIdentifier; }
    public FileManager getFileManager() { return fileManager; }
    public ModManager getModManager() { return modManager; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public EventManager getEventManager() { return eventManager; }
    public NanoVGManager getNanoVGManager() { return nanoVGManager; }
    public ColorManager getColorManager() { return colorManager; }
    public ProfileManager getProfileManager() { return profileManager; }
    public CapeManager getCapeManager() { return capeManager; }
    public CommandManager getCommandManager() { return commandManager; }
    public ScreenshotManager getScreenshotManager() { return screenshotManager; }
    public void setNanoVGManager(NanoVGManager nanoVGManager) { this.nanoVGManager = nanoVGManager; }
    public NotificationManager getNotificationManager() { return notificationManager; }
    public SecurityFeatureManager getSecurityFeatureManager() { return securityFeatureManager; }
    public QuickPlayManager getQuickPlayManager() { return quickPlayManager; }
    public ChangelogManager getChangelogManager() { return changelogManager; }
    public NewsManager getNewsManager() { return newsManager; }
    public DiscordStats getDiscordStats() { return discordStats; }
    public WaypointManager getWaypointManager() { return waypointManager; }
    public GuiModMenu getModMenu() { return modMenu; }
    public FireflyUI getMainMenu() { return mainMenu; }
    public long getLaunchTime() { return launchTime; }
    public void createFirstLoginFile() { getFileManager().createFile(firstLoginFile); }
    public boolean isFirstLogin() { return !firstLoginFile.exists(); }
    public Update getUpdateInstance() { return update; }
    public void setUpdateNeeded(boolean in) { updateNeeded = in; }
    public boolean getUpdateNeeded() { return updateNeeded; }
    public void setSoar8Released(boolean in) { soar8Released = in; }
    public boolean getSoar8Released() { return soar8Released; }
    public ClickEffects getClickEffects() { return clickEffects; }
    public BlacklistManager getBlacklistManager() { return blacklistManager; }
    public RestrictedMod getRestrictedMod() { return restrictedMod; }
}
