package crack.firefly.com.System.emote;

import crack.firefly.com.Loader.Abstractions.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class EmoteManager {

    private static final EmoteManager instance = new EmoteManager();
    public static EmoteManager getInstance() { return instance; }

    private String currentEmote = "None";

    public final String[] emotes = {"WAVE", "DANCE", "REBOLADO", "T-POSE", "STOP"};

    public void playEmote(String name) { this.currentEmote = name; }
    public void stopEmote() { this.currentEmote = "None"; }
    public boolean isPlaying() { return !currentEmote.equals("None"); }

    public float getRotation(String part, String axis) {
        if (!isPlaying() || Minecraft.getMinecraft().thePlayer == null) return 0;

        // USA O IMixinMinecraft PARA ACESSAR O TIMER SEM ERRO
        float partialTicks = ((IMixinMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks;
        float ticks = Minecraft.getMinecraft().thePlayer.ticksExisted + partialTicks;
        float speed = ticks * 0.35f; 

        switch (currentEmote) {
            case "REBOLADO":
                // Corpo inclina pra frente
                if (part.equals("body") && axis.equals("x")) return 0.5f;
                // Rebolado (Balanço lateral e circular)
                if (part.equals("body") && axis.equals("y")) return MathHelper.sin(speed * 2.0f) * 0.45f;
                if (part.equals("body") && axis.equals("z")) return MathHelper.cos(speed * 2.0f) * 0.15f;
                
                // Pernas dobradas (Valores negativos dobram para frente, positivos para trás)
                // Se a perna estava subindo, é porque o valor estava muito alto ou no eixo errado.
                if (part.contains("Leg") && axis.equals("x")) return -0.3f; 
                
                // Braços
                if (part.contains("Arm") && axis.equals("x")) return -0.4f;
                break;

            case "WAVE":
                if (part.equals("rightArm") && axis.equals("z")) return -MathHelper.cos(speed) * 0.5f - 0.5f;
                if (part.equals("rightArm") && axis.equals("x")) return -2.0f;
                break;
                
            case "DANCE":
                if (axis.equals("y")) return MathHelper.sin(speed) * 0.5f;
                if (part.contains("Arm") && axis.equals("x")) return -MathHelper.cos(speed) * 0.6f - 0.8f;
                break;
        }
        return 0;
    }
}