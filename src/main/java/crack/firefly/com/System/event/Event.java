package crack.firefly.com.System.event;

import crack.firefly.com.Firefly;

public abstract class Event {

    private boolean cancelled;

    public void call() {
        this.cancelled = false;
        // Envia para o manager processar o loop
        Firefly.getInstance().getEventManager().call(this);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}