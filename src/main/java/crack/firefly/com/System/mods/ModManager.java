package crack.firefly.com.System.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import crack.firefly.com.System.mods.impl.*;
import crack.firefly.com.System.mods.settings.Setting;

public class ModManager {

    private ArrayList<Mod> mods = new ArrayList<Mod>();
    private ArrayList<Setting> settings = new ArrayList<Setting>();
    
    public void init() {
        mods.add(new AnimationsMod());
        addHidden(new AuraCosmeticMod());
        addHidden(new AppleSkinMod()); 
        mods.add(new ArmorStatusMod());
        addHidden(new ArrayListMod()); 
        addHidden(new AsyncScreenshotMod()); 
        mods.add(new AutoTextMod());
        addHidden(new BlockInfoMod()); 
        mods.add(new BlockOverlayMod());
        addHidden(new BloodParticlesMod()); 
        mods.add(new BorderlessFullscreenMod());
        mods.add(new BossHealthMod());
        addHidden(new BowZoomMod()); 
        addHidden(new BreadcrumbsMod()); 
        addHidden(new CalendarMod()); 
        mods.add(new ChatMod());
        addHidden(new ChatTranslateMod()); 
        addHidden(new ChunkAnimatorMod()); 
        mods.add(new ChunkBordersMod());
        mods.add(new ClearGlassMod());
        mods.add(new ClearWaterMod());
        addHidden(new ClientSpooferMod()); 
        mods.add(new ClockMod());
        mods.add(new ColorSaturationMod());
        mods.add(new ComboCounterMod());
        addHidden(new CompassMod()); 
        mods.add(new CoordsMod());
        mods.add(new CPSDisplayMod());
        mods.add(new CrosshairMod());
        mods.add(new CustomHeldItemsMod()); 
        mods.add(new EmoteMod());
        mods.add(new DamageTiltMod()); 
        mods.add(new DamageTintMod());
        mods.add(new DayCounterMod());
        mods.add(new DiscordRPCMod());
        addHidden(new EarsMod()); 
        mods.add(new EntityCullingMod());
        addHidden(new FarCameraMod()); 
        addHidden(new FemaleGenderMod()); 
        mods.add(new FovModifierMod());
        mods.add(new FPSBoostMod());
        mods.add(new FPSDisplayMod());
        mods.add(new FPSLimiterMod());
        addHidden(new FPSSpooferMod()); 
        mods.add(new FreelookMod());
        mods.add(new FullbrightMod());
        mods.add(new GlintColorMod());
        addHidden(new InternalSettingsMod()); 
        addHidden(new GodbridgeAssistMod()); 
        addHidden(new HealthDisplayMod()); 
        mods.add(new HitBoxMod());
        mods.add(new HitColorMod());
        mods.add(new HitDelayFixMod());
        addHidden(new HorseStatsMod()); 
        addHidden(new HypixelMod()); 
        addHidden(new HypixelQuickPlayMod()); 
        addHidden(new ImageDisplayMod()); 
        addHidden(new InventoryDisplayMod()); 
        mods.add(new InventoryMod());
        addHidden(new ItemInfoMod()); 
        mods.add(new ItemPhysicsMod());
        mods.add(new Items2DMod());
        addHidden(new JumpCircleMod()); 
        mods.add(new KeystrokesMod());
        addHidden(new KillEffectsMod()); 
        addHidden(new KillSoundsMod()); 
        addHidden(new MechvibesMod()); 
        mods.add(new MemoryUsageMod());
        addHidden(new MinemenMod()); 
        mods.add(new NoHurtCam());
        addHidden(new MinimapMod()); 
        addHidden(new MoBendsMod()); 
        addHidden(new ModernHotbarMod()); 
        mods.add(new MotionBlurMod());
        mods.add(new MouseStrokesMod());
        addHidden(new NameDisplayMod()); 
        mods.add(new NameProtectMod());
        mods.add(new NametagMod());
        addHidden(new OverlayEditorMod()); 
        mods.add(new PackDisplayMod());
        mods.add(new ParticleCustomizerMod());
        mods.add(new PingDisplayMod());
        addHidden(new PlayerCounterMod()); 
        addHidden(new PlayerDisplayMod()); 
        addHidden(new PlayerListMod()); 
        addHidden(new PlayerPredicatorMod()); 
        mods.add(new PlayTimeDisplayMod());
        addHidden(new PotionCounterMod()); 
        mods.add(new PotionStatusMod());
        addHidden(new ProjectileTrailMod()); 
        addHidden(new QuickSwitchMod()); 
        mods.add(new RawInputMod());
        addHidden(new ReachCirclesMod()); 
        mods.add(new ReachDisplayMod());
        addHidden(new RearviewMod()); 
        mods.add(new ScoreboardMod());
        mods.add(new ServerIPDisplayMod());
        addHidden(new SessionInfoMod()); 
        mods.add(new ShinyPotsMod());
        mods.add(new Skin3DMod());
        addHidden(new SkinProtectMod()); 
        mods.add(new SlowSwingMod());
        addHidden(new SoundModifierMod()); 
        addHidden(new SoundSubtitlesMod()); 
        mods.add(new SpeedometerMod());
        addHidden(new StopwatchMod()); 
        mods.add(new TabEditorMod());
        addHidden(new TaplookMod()); 
        addHidden(new TargetIndicatorMod()); 
        addHidden(new TargetInfoMod()); 
        mods.add(new TimeChangerMod());
        mods.add(new TNTTimerMod());
        addHidden(new ToggleSneakMod()); 
        mods.add(new ToggleSprintMod());
        addHidden(new UHCOverlayMod()); 
        addHidden(new WaveyCapesMod()); 
        mods.add(new WaypointMod());
        mods.add(new WeatherChangerMod());
        addHidden(new WeatherDisplayMod()); 
        mods.add(new ZoomMod());
    }
    
    private void addHidden(Mod mod) {
        mod.setHide(true);
        mods.add(mod);
    }
    
    public ArrayList<Mod> getMods() {
        return mods;
    }
    
    public Mod getModByTranslateKey(String key) {
        for(Mod m : mods) {
            if(m.getNameKey().equals(key)) {
                return m;
            }
        }
        return null;
    }
    
    public ArrayList<HUDMod> getHudMods(){
        ArrayList<HUDMod> result = new ArrayList<HUDMod>();
        for(Mod m : mods) {
            if(m instanceof HUDMod && ((HUDMod) m).isDraggable()) {
                result.add((HUDMod) m);
            }
        }
        return result;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }
    
    public ArrayList<Setting> getSettingsByMod(Mod m){
        ArrayList<Setting> result = new ArrayList<Setting>();
        for(Setting s : settings) {
            if(s.getParent().equals(m)) {
                result.add(s);
            }
        }
        if(result.isEmpty()) return null;
        return result;
    }
    
    public String getWords(Mod mod) {
        String result = "";
        for(Mod m : mods) {
            if(m.equals(mod)) result = result + m.getName() + " ";
        }
        for(Setting s : settings) {
            if(s.getParent().equals(mod)) result = result + s.getName() + " ";
        }
        for(Mod m : mods) {
            if(m.equals(mod) && !Objects.equals(m.getAlias(), "\u200B")) {
                result = result + m.getAlias() + " ";
            }
        }
        return result;
    }
    
    public void addSettings(Setting... settingsList) {
        settings.addAll(Arrays.asList(settingsList));
    }
    
    public void disableAll() {
        for(Mod m : mods) {
            m.setToggled(false);
        }
        InternalSettingsMod.getInstance().setToggled(true);
    }

    public void playToggleSound(boolean toggled){
        // Sons removidos
    }
}