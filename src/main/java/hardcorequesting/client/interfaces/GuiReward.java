package hardcorequesting.client.interfaces;


import com.mojang.blaze3d.systems.RenderSystem;
import hardcorequesting.bag.Group;
import hardcorequesting.config.HQMConfig;
import hardcorequesting.items.BagItem;
import hardcorequesting.util.Translator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiReward extends GuiBase {
    
    public static final Identifier TEXTURE = ResourceHelper.getResource("reward");
    public static final Identifier C_TEXTURE = ResourceHelper.getResource("c_reward");
    private static final int ITEMS_PER_LINE = 7;
    private static final int ITEM_SIZE = 16;
    private static final int ITEM_MARGIN = 5;
    private static final int TOP_HEIGHT = 52;
    private static final int MIDDLE_HEIGHT = 22;
    private static final int BOTTOM_HEIGHT = 24;
    private static final int TITLE_HEIGHT = 25;
    private static final int TOP_SRC_Y = 0;
    private static final int MIDDLE_SRC_Y = 67;
    private static final int BOTTOM_SRC_Y = 107;
    private static final int TEXTURE_WIDTH = 170;
    private Group group;
    private int lines;
    private List<Reward> rewards;
    private String statisticsText;
    
    public GuiReward(Group group, int bagTier, PlayerEntity player) {
        super(NarratorManager.EMPTY);
        this.group = group;
        this.rewards = new ArrayList<>();
        
        
        int totalWeight = 0;
        for (Group other : Group.getGroups().values()) {
            if (other.isValid(player)) {
                totalWeight += other.getTier().getWeights()[bagTier];
            }
        }
        
        int myWeight = group.getTier().getWeights()[bagTier];
        float chance = ((float) myWeight / totalWeight);
        
        statisticsText = I18n.translate("hqm.rewardGui.chance", ((int) (chance * 10000)) / 100F);
        
        
        lines = (int) Math.ceil((float) group.getItems().size() / ITEMS_PER_LINE);
        for (int i = 0; i < lines; i++) {
            int y = TOP_HEIGHT + MIDDLE_HEIGHT * i + (MIDDLE_HEIGHT - ITEM_SIZE) / 2;
            int itemsInLine = Math.min(group.getItems().size() - i * ITEMS_PER_LINE, ITEMS_PER_LINE);
            for (int j = 0; j < itemsInLine; j++) {
                int x = (TEXTURE_WIDTH - (itemsInLine * ITEM_SIZE + (itemsInLine - 1) * ITEM_MARGIN)) / 2 + j * (ITEM_SIZE + ITEM_MARGIN);
                ItemStack stack = group.getItems().get(i * ITEMS_PER_LINE + j);
                if (!stack.isEmpty()) {
                    rewards.add(new Reward(stack, x, y));
                }
            }
        }
    }
    
    public static void open(PlayerEntity player, UUID groupId, int bag, int[] limits) {
        Group rewardGroup = Group.getGroups().get(groupId);
        int i = 0;
        for (Group group : Group.getGroups().values())
            if (group.getLimit() != 0)
                group.setRetrievalCount(player, limits[i++]);
        
        if (BagItem.displayGui) {
            MinecraftClient.getInstance().openScreen(new GuiReward(rewardGroup, bag, player));
        }
    }
    
    @Override
    public void render(MatrixStack matrices, int mX0, int mY0, float f) {
        applyColor(0xFFFFFFFF);
        ResourceHelper.bindResource(TEXTURE);
        
        
        int height = TOP_HEIGHT + MIDDLE_HEIGHT * lines + BOTTOM_HEIGHT;
        this.left = (this.width - TEXTURE_WIDTH) / 2;
        this.top = (this.height - height) / 2;
        
        drawRect(0, 0, 0, TOP_SRC_Y, TEXTURE_WIDTH, TOP_HEIGHT);
        for (int i = 0; i < lines; i++) {
            drawRect(0, TOP_HEIGHT + i * MIDDLE_HEIGHT, 0, MIDDLE_SRC_Y, TEXTURE_WIDTH, MIDDLE_HEIGHT);
        }
        drawRect(0, TOP_HEIGHT + lines * MIDDLE_HEIGHT, 0, BOTTOM_SRC_Y, TEXTURE_WIDTH, BOTTOM_HEIGHT);
        
        
        int mX = mX0 - left;
        int mY = mY0 - top;
        
        String title = group.getDisplayName();
        
        // fall back to the tier's name if this particular bag has no title,
        // or if the user explicitly asked us to do so.
        if (HQMConfig.getInstance().Loot.ALWAYS_USE_TIER || title == null || title.isEmpty()) {
            title = I18n.translate("hqm.rewardGui.tierReward", group.getTier().getName());
        }
        
        drawCenteredString(matrices, StringRenderable.styled(title, Style.EMPTY.withColor(TextColor.fromRgb(group.getTier().getColor().getHexColor() & 0xFFFFFF))), 0, 0, 1F, TEXTURE_WIDTH, TITLE_HEIGHT, 0x404040);
        drawCenteredString(matrices, Translator.plain(statisticsText), 0, TITLE_HEIGHT, 0.7F, TEXTURE_WIDTH, TOP_HEIGHT - TITLE_HEIGHT, 0x707070);
        drawCenteredString(matrices, Translator.translated("hqm.rewardGui.close"), 0, TOP_HEIGHT + lines * MIDDLE_HEIGHT, 0.7F, TEXTURE_WIDTH, BOTTOM_HEIGHT, 0x707070);
        
        for (Reward reward : rewards) {
            try {
                drawItemStack(reward.stack, reward.x, reward.y, true);
                //itemRenderer.renderItemOverlayIntoGUI(fontRendererObj, MinecraftClient.getInstance().getTextureManager(), reward.stack, reward.x + left + 1, reward.y + top + 1);
                itemRenderer.renderGuiItemOverlay(textRenderer, reward.stack, (reward.x + left + 1), (reward.y + top + 1), "");
            } catch (Throwable ignored) {
            }
        }
        RenderSystem.disableLighting();
        for (Reward reward : rewards) {
            if (inBounds(reward.x, reward.y, ITEM_SIZE, ITEM_SIZE, mX, mY)) {
                try {
                    if (Screen.hasShiftDown()) {
                        renderTooltip(matrices, reward.stack, mX0, mY0);
                    } else {
                        List<StringRenderable> str = new ArrayList<>();
                        try {
                            List<Text> info = reward.stack.getTooltip(MinecraftClient.getInstance().player, client.options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL);
                            if (info.size() > 0) {
                                str.add(info.get(0));
                                if (info.size() > 1) {
                                    str.add(Translator.translated("hqm.rewardGui.shiftInfo", GuiColor.GRAY));
                                }
                            }
                            renderTooltip(matrices, str, mX0, mY0);
                        } catch (Throwable ignored) {
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double mX0, double mY0, int b) {
        MinecraftClient.getInstance().openScreen(null);
        return true;
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    private class Reward {
        
        private ItemStack stack;
        private int x;
        private int y;
        
        private Reward(ItemStack stack, int x, int y) {
            this.stack = stack;
            this.x = x;
            this.y = y;
        }
    }
    
}
