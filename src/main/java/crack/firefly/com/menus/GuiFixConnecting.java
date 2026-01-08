package crack.firefly.com.menus;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.event.impl.EventJoinServer;
import crack.firefly.com.System.nanovg.NanoVGManager;
import crack.firefly.com.System.nanovg.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

public class GuiFixConnecting extends GuiScreen {
	
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;

    private static final ResourceLocation LOGO = new ResourceLocation("Firefly/gui/main/menu/Horizontal_branca.png");

    public GuiFixConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1181_1_;
        mcIn.loadWorld(null);
        mcIn.setServerData(p_i1181_3_);
        this.connectServerData(p_i1181_3_);
    }

    public GuiFixConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1182_1_;
        mcIn.loadWorld(null);
        this.connect(hostName, port);
    }

    private void connectServerData(final ServerData serverData) {
    	new EventJoinServer(serverData.serverIP).call();
        (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
            public void run() {
                InetAddress inetaddress = null;
                ServerAddress serveraddress = ServerAddress.fromString(serverData.serverIP);
                String ip = serveraddress.getIP();
                int port = serveraddress.getPort();
                try {
                    if (cancel) return;
                    inetaddress = InetAddress.getByName(ip);
                    networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, mc.gameSettings.isUsingNativeTransport());
                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, previousGuiScreen));
                    networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
                    networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
                } catch (Exception exception) {
                    if (cancel) return;
                    mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {exception.getMessage()})));
                }
            }
        }).start();
    }
    
    private void connect(final String ip, final int port) {
    	new EventJoinServer(ip).call();
        (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
            public void run() {
                try {
                    if (cancel) return;
                    InetAddress inetaddress = InetAddress.getByName(ip);
                    networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, mc.gameSettings.isUsingNativeTransport());
                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, previousGuiScreen));
                    networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
                    networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
                } catch (Exception exception) {
                    if (cancel) return;
                    mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {exception.getMessage()})));
                }
            }
        }).start();
    }

    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                this.networkManager.processReceivedPackets();
            } else {
                this.networkManager.checkDisconnected();
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {}

    public void initGui() {
        this.buttonList.clear();
        // Coloca o botão Cancelar bem lá embaixo (height - 40)
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 40, I18n.format("gui.cancel")));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.cancel = true;
            if (this.networkManager != null) this.networkManager.closeChannel(new ChatComponentText("Aborted"));
            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 1. Fundo Escuro Badlion Style
        this.drawRect(0, 0, this.width, this.height, new Color(12, 12, 12).getRGB());

        // 2. Renderizar Logo Centralizada
        renderLogo();

        // 3. Textos e Barra com NanoVG
        NanoVGManager nvg = Firefly.getInstance().getNanoVGManager();
        float w = (float) this.width;
        float h = (float) this.height;
        
        String status = (this.networkManager == null) ? I18n.format("connect.connecting") : I18n.format("connect.authorizing");

        nvg.setupAndDraw(() -> {
            // Título Principal
            nvg.drawCenteredText("ESTABELECENDO CONEXÃO", w / 2f, h / 2f + 25f, Color.WHITE, 10f, Fonts.SEMIBOLD);
            
            // Subtítulo
            nvg.drawCenteredText(status.toUpperCase() + "...", w / 2f, h / 2f + 38f, new Color(160, 160, 160), 8f, Fonts.REGULAR);

            // Barra de progresso deslizante
            float barW = 120f;
            float barX = (w - barW) / 2f;
            float barY = h / 2f + 55f;
            nvg.drawRoundedRect(barX, barY, barW, 2f, 1f, new Color(255, 255, 255, 15));
            
            float progress = (float) Math.abs(Math.sin(System.currentTimeMillis() / 600.0));
            nvg.drawRoundedRect(barX + (progress * (barW - 35f)), barY, 35f, 2f, 1f, new Color(0, 255, 180));
        });

        // 4. Desenha o botão (que agora está na posição height - 40)
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderLogo() {
        int logoW = 160;
        int logoH = 40;
        float x = (this.width / 2f) - (logoW / 2f);
        float y = (this.height / 2f) - (logoH / 2f) - 30f;

        mc.getTextureManager().bindTexture(LOGO);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture((int)x, (int)y, 0f, 0f, logoW, logoH, (float)logoW, (float)logoH);
        GlStateManager.disableBlend();
    }
}