package crack.firefly.com.Loader.mixin.mixins.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crack.firefly.com.System.event.impl.EventFireOverlay;
import crack.firefly.com.System.event.impl.EventRenderItemInFirstPerson;
import crack.firefly.com.System.event.impl.EventWaterOverlay;
import crack.firefly.com.System.mods.impl.AnimationsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
	
    @Shadow @Final private Minecraft mc;
    @Shadow private ItemStack itemToRender;
    @Shadow private float prevEquippedProgress;
    @Shadow private float equippedProgress;
    @Shadow private int equippedItemSlot;

    // Variável única para salvar o tempo parcial entre os frames
    @Unique private float firefly$partialTicks;
    
    /**
     * Captura o partialTicks diretamente da assinatura do método original do Minecraft 1.8.9
     * renderItemInFirstPerson(float partialTicks)
     */
    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"))
    public void captureTicks(float partialTicks, CallbackInfo ci) {
        this.firefly$partialTicks = partialTicks;
        new EventRenderItemInFirstPerson().call();
    }
    
    @Inject(method = "renderWaterOverlayTexture", at = @At("HEAD"), cancellable = true)
    private void preRenderWaterOverlayTexture(CallbackInfo ci) {
        EventWaterOverlay event = new EventWaterOverlay();
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
    
    @Inject(at = @At("HEAD"), method = "renderFireInFirstPerson", cancellable = true)
    private void renderFireInFirstPerson(CallbackInfo ci) {
        EventFireOverlay event = new EventFireOverlay();
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
    
    /**
     * LÓGICA DE ANIMAÇÃO 1.7 + FIX DE ANIMAÇÃO 1.8
     * Substitui a constante 0.0f pela animação real do braço.
     */
    @ModifyConstant(method = "renderItemInFirstPerson", constant = @Constant(floatValue = 0.0f))
    public float modifySwingProgress(float original) {
        AnimationsMod mod = AnimationsMod.getInstance();
        AbstractClientPlayer player = mc.thePlayer;
        
        if (player == null) return original;

        // swingProgress é o que faz o braço balançar (valor de 0.0 a 1.0)
        float swingProgress = player.getSwingProgress(this.firefly$partialTicks);

        // Se o mod de Animações estiver ligado e o BlockHit (clique direito) ativo
        if (mod.isToggled() && mod.getBlockHitSetting().isToggled()) {
            // Se o player está defendendo OU segurando o botão de defesa enquanto bate
            if (player.getItemInUseCount() > 0 || (mc.gameSettings.keyBindUseItem.isKeyDown() && swingProgress > 0)) {
                
                // Aplica o Snappy / Always Swing se estiver ligado
                if (mod.getAlwaysSwingSetting().isToggled()) {
                    return swingProgress > 0.8F ? 1.0F : swingProgress * 1.5F;
                }
                return swingProgress;
            }
        }
        
        // --- FIX PARA O 1.8 PADRÃO ---
        // Se o braço está balançando (swingProgress > 0), retornamos esse valor.
        // Se retornarmos o 'original' (que é 0.0f), o braço fica duro/congelado.
        if (swingProgress != 0) {
            return swingProgress;
        }

        return original;
    }
    
    /**
     * ANIMAÇÃO DA VARA DE PESCA (OLD ROD) 1.7
     */
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"))
    public void oldRod(ItemRenderer instance, EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        AnimationsMod mod = AnimationsMod.getInstance();
    	
        if (mod.isToggled() && mod.getRodSetting().isToggled() && !mc.getRenderItem().shouldRenderItemIn3D(heldStack)) {
            GlStateManager.pushMatrix();
            if (heldStack.getItem().shouldRotateAroundWhenRendering()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }
            
            GlStateManager.translate(0.58800083f, 0.06999986f, -0.77000016f);
            GlStateManager.scale(1.5f, 1.5f, 1.5f);
            GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-0.9375F, -0.0625F, 0.0F);
            GlStateManager.scale(-2, 2, -2);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            
            instance.renderItem(entityIn, heldStack, ItemCameraTransforms.TransformType.NONE);
            GlStateManager.popMatrix();
        } else {
            instance.renderItem(entityIn, heldStack, transform);
        }
    }
    
    /**
     * TROCA DE ITEM RÁPIDA (FAST SWITCH)
     */
    @Inject(method = "updateEquippedItem", at = @At("HEAD"), cancellable = true)
    private void preUpdateEquippedItem(CallbackInfo ci) {
        AnimationsMod mod = AnimationsMod.getInstance();
    	
        if (mod.isToggled() && mod.getItemSwitchSetting().isToggled()) {
            ci.cancel();
            prevEquippedProgress = equippedProgress;
            EntityPlayerSP player = mc.thePlayer;
            ItemStack itemstack = player.inventory.getCurrentItem();
            boolean flag = equippedItemSlot == player.inventory.currentItem && itemstack == itemToRender;
            
            if (itemToRender == null && itemstack == null) {
                flag = true;
            }
            
            if (itemstack != null && itemToRender != null && itemstack != itemToRender && itemstack.getItem() == itemToRender.getItem() && itemstack.getItemDamage() == itemToRender.getItemDamage()) {
                itemToRender = itemstack;
                flag = true;
            }
            
            float speed = 0.4F; 
            float target = flag ? 1.0F : 0.0F;
            float delta = MathHelper.clamp_float(target - equippedProgress, -speed, speed);
            
            equippedProgress += delta;
            
            if (equippedProgress < 0.1F) {
                itemToRender = itemstack;
                equippedItemSlot = player.inventory.currentItem;
            }
        }
    }
}