package crack.firefly.com.Support;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    public static void render(ResourceLocation loc) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
            
            List<float[]> v = new ArrayList<>();
            List<float[]> vt = new ArrayList<>();
            WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
            wr.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll("  +", " ");
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] s = line.split(" ");
                if (s[0].equals("v")) {
                    v.add(new float[]{Float.parseFloat(s[1]), Float.parseFloat(s[2]), Float.parseFloat(s[3])});
                } else if (s[0].equals("vt")) {
                    vt.add(new float[]{Float.parseFloat(s[1]), 1.0F - Float.parseFloat(s[2])});
                } else if (s[0].equals("f")) {
                    for (int i = 1; i <= 3; i++) {
                        String[] idx = s[i].split("/");
                        float[] vert = v.get(Integer.parseInt(idx[0]) - 1);
                        float[] tex = (vt.size() > 0 && idx.length > 1 && !idx[1].isEmpty()) 
                                      ? vt.get(Integer.parseInt(idx[1]) - 1) : new float[]{0, 0};
                        wr.pos(vert[0], vert[1], vert[2]).tex(tex[0], tex[1]).endVertex();
                    }
                }
            }
            Tessellator.getInstance().draw();
            reader.close();
        } catch (Exception e) {
            System.err.println("ERRO FIREFLY: Nao foi possivel carregar " + loc.getResourcePath());
        }
    }
}