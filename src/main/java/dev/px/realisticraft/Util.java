package dev.px.realisticraft;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class Util { // unused class may use in future

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void ClearonJoin(int lines) {
        for (int i = 0; i < lines; i++) {
            mc.player.sendMessage(new TextComponentString(""));
        }
    }
    @SubscribeEvent
    public static void PlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        
    }

    public static void sendClientMessage(String text) {
        if(mc.player != null && mc.world != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(text));
        }
    }
}
