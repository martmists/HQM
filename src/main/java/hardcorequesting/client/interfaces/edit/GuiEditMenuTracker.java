package hardcorequesting.client.interfaces.edit;

import hardcorequesting.client.interfaces.GuiBase;
import hardcorequesting.tileentity.TrackerBlockEntity;
import hardcorequesting.tileentity.TrackerType;
import hardcorequesting.util.Translator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringRenderable;

public class GuiEditMenuTracker extends GuiEditMenuExtended {
    
    private TrackerBlockEntity tracker;
    
    public GuiEditMenuTracker(GuiBase gui, PlayerEntity player, final TrackerBlockEntity tracker) {
        super(gui, player, true, 20, 30, 20, 130);
        
        this.tracker = tracker;
        
        textBoxes.add(new TextBoxNumber(gui, 0, "hqm.menuTracker.radius.title") {
            @Override
            protected void draw(MatrixStack matrices, GuiBase gui, boolean selected) {
                super.draw(matrices, gui, selected);
                
                gui.drawString(matrices, gui.getLinesFromText(Translator.translated("hqm.menuTracker.radius.desc"), 0.7F, 130), BOX_X, BOX_Y + BOX_OFFSET + TEXT_OFFSET, 0.7F, 0x404040);
            }
            
            @Override
            protected int getValue() {
                return tracker.getRadius();
            }
            
            @Override
            protected void setValue(int number) {
                tracker.setRadius(number);
            }
        });
    }
    
    @Override
    public void draw(MatrixStack matrices, GuiBase gui, int mX, int mY) {
        super.draw(matrices, gui, mX, mY);
        
        gui.drawCenteredString(matrices, tracker.getCurrentQuest() != null ? Translator.plain(tracker.getCurrentQuest().getName()) : Translator.translated("hqm.menuTracker.noQuest"), 0, 5, 1F, 170, 20, 0x404040);
    }
    
    @Override
    protected void onArrowClick(boolean left) {
        if (left) {
            tracker.setTrackerType(TrackerType.values()[(tracker.getTrackerType().ordinal() + TrackerType.values().length - 1) % TrackerType.values().length]);
        } else {
            tracker.setTrackerType(TrackerType.values()[(tracker.getTrackerType().ordinal() + 1) % TrackerType.values().length]);
        }
    }
    
    @Override
    protected String getArrowText() {
        return tracker.getTrackerType().getName();
    }
    
    @Override
    protected String getArrowDescription() {
        return tracker.getTrackerType().getDescription();
    }
    
    @Override
    public void save(GuiBase gui) {
        tracker.sendToServer();
    }
    
    public boolean doesRequiredDoublePage() {
        return false;
    }
}
