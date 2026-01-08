package crack.firefly.com.System.event.impl;

import crack.firefly.com.System.event.Event;
import net.minecraft.entity.Entity;

public class EventAttackEntity extends Event {
    private Entity entity;

    public EventAttackEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    // ADICIONADO PARA CORRIGIR O ERRO
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}