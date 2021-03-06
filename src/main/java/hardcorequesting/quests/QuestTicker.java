package hardcorequesting.quests;

import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

public class QuestTicker {
    
    private int hours;
    private int ticks;
    
    public QuestTicker(boolean isClient) {
        if (isClient) {
            ClientTickCallback.EVENT.register(minecraftClient -> tick(true));
        }
        ServerTickCallback.EVENT.register(minecraftClient -> tick(false));
    }
    
    public void tick(boolean isClient) {
        if (++ticks == 1000) {
            ticks = 0;
            hours++;
            if (!isClient) {
                for (Quest quest : Quest.getQuests().values()) {
                    int total = quest.getRepeatInfo().getDays() * 24 + quest.getRepeatInfo().getHours();
                    if (quest.getRepeatInfo().getType() == RepeatType.INTERVAL) {
                        if (total != 0 && hours % total == 0) {
                            quest.resetAll();
                        }
                    } else if (quest.getRepeatInfo().getType() == RepeatType.TIME) {
                        quest.resetOnTime(hours - total);
                    }
                }
            }
        }
    }
    
    
    public int getHours() {
        return hours;
    }
}
