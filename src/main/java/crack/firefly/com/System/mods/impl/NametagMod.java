package crack.firefly.com.System.mods.impl;

import crack.firefly.com.System.event.EventTarget;
import crack.firefly.com.System.event.impl.EventRenderPlayer;
import crack.firefly.com.System.language.TranslateText;
import crack.firefly.com.System.mods.Mod;
import crack.firefly.com.System.mods.ModCategory;

public class NametagMod extends Mod {

    private static NametagMod instance;

    public NametagMod() {
        super(TranslateText.NAMETAG, TranslateText.NAMETAG_DESCRIPTION, ModCategory.PLAYER);
        instance = this;
    }

    public static NametagMod getInstance() {
        return instance;
    }

    @EventTarget
    public void onRenderPlayer(EventRenderPlayer event) {
        // O código de renderização do ícone foi removido.
        // Isso corrige o bug do jogador ficar "brilhante" (iluminação estourada)
        // e remove o ícone da nametag, voltando ao visual padrão.
    }
}