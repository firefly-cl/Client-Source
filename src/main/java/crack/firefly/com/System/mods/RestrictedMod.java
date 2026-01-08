package crack.firefly.com.System.mods;

import crack.firefly.com.Firefly;
import crack.firefly.com.System.notification.NotificationType;
import crack.firefly.com.System.remote.blacklists.BlacklistManager;
import crack.firefly.com.System.remote.blacklists.Server;
import crack.firefly.com.Support.ServerUtils;

import java.util.List;

public class RestrictedMod {

    String currentServerIP = "";
    public Boolean shouldCheck = true;

    BlacklistManager blm = Firefly.getInstance().getBlacklistManager();

    public boolean checkAllowed(Mod m) {
        if (shouldCheck) {
            List<Server> servers = blm.getBlacklist();
            for (Server server : servers) {
                if (currentServerIP.contains(server.getServerIp())) {
                    List<String> blacklistedMods = server.getMods();
                    if (blacklistedMods.contains(m.getNameKey())) {
                        m.setAllowed(false);
                        return false;
                    }
                }
            }
        }
        m.setAllowed(true);
        return true;
    }

    public void joinServer(String ip) {
        blm.check();
    }

    public void joinWorld(){
        this.currentServerIP = ServerUtils.getServerIP();
        for(Mod m : Firefly.getInstance().getModManager().getMods()){
            if(!checkAllowed(m) && m.isToggled()){
                m.setToggled(false);
                Firefly.getInstance().getNotificationManager().post(m.getName(),  "Disabled due to serverside blacklist" , NotificationType.INFO);
            }
        }
    }

}
