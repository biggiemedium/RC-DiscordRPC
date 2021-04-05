package dev.px.realisticraft;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class Util {

    private Minecraft mc = Minecraft.getMinecraft();

    public static void ClearonJoin(int lines) {
        for (int i = 0; i < lines; i++) {
           // Minecraft.getMinecraft().player.sendMessage(new TextComponentString(""));
        }
    }
    @SubscribeEvent
    public static void PlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        
    }

    public static void sendClientMessage(String text) {
        if (Minecraft.getMinecraft().ingameGUI != null || Minecraft.getMinecraft().player == null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(text));
        }

    }
}
