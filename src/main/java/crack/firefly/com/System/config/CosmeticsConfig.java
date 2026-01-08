package crack.firefly.com.System.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CosmeticsConfig {

    private static final File DIR =
            new File(Minecraft.getMinecraft().mcDataDir, "Firefly-Profile");

    private static final File FILE = new File(DIR, "cosmetics.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // ===== COSMETICS =====
    public String cape = "";
    public boolean shield = false;

    // ===== SKIN =====
    public String skinFile = "";
    public boolean slimModel = false;

    // ===== AURA =====
    public String aura = "";

    public static CosmeticsConfig load() {
        try {
            if (!FILE.exists()) return new CosmeticsConfig();
            return GSON.fromJson(new FileReader(FILE), CosmeticsConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new CosmeticsConfig();
        }
    }

    public void save() {
        try {
            DIR.mkdirs();
            FileWriter writer = new FileWriter(FILE);
            GSON.toJson(this, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
