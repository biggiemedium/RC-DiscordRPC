
package dev.px.realisticraft;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.util.math.MathHelper;

public class RPC {

    private  DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private  Minecraft mc = Minecraft.getMinecraft();
    private  String user = mc.getSession().getUsername();

    private DiscordRichPresence presence = new DiscordRichPresence();
    private Thread thread = null;
    private DiscordRPC lib = DiscordRPC.INSTANCE;

    public void Start() {
        String applicationId = "803057502786551808";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        lib.Discord_UpdatePresence(presence);
        presence.largeImageKey = "icon";
        this.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                presence.details = setDetails();
                presence.state = setState();
                lib.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler");

        thread.start();
    }

    public float getSpeedInKM() {
        double deltaX = this.mc.player.posX - this.mc.player.prevPosX;
        double deltaZ = this.mc.player.posZ - this.mc.player.prevPosZ;

        float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        double floor = Math.floor(( distance/1000.0f ) / ( 0.05f/3600.0f ));

        String formatter = String.valueOf(floor);

        if (!formatter.contains("."))
            formatter += ".0";

        return Float.valueOf(formatter);
    }

    public void Stop() {
        if(!(this.thread == null) && !this.thread.isInterrupted()) {
            this.thread.interrupt();
        }
        this.lib.Discord_ClearPresence();
        this.lib.Discord_Shutdown();
    }

    private String setState() { // mc.player != null will run first
        String back = discordRichPresence.state;


        if(mc.player == null)
            return "Loading...";

        if(mc.player.isInLava()) {
            return "Roasting";
        }

        if(mc.player.isInWater()) {
            return "Cooling off";
        }

        if(mc.player != null && mc.player.onGround && mc.player.moveStrafing == 0 && mc.player.moveForward == 0) {
            return "Standing";
        }

        if(mc.player != null && mc.player.onGround) {
            return "Moving " + getSpeedInKM() + " KMH";
        }

        if(!mc.player.onGround) {
            return "Chilling in " + mc.world.getBiome(mc.player.getPosition()).getBiomeName();
        }

        if(mc.player.isOnLadder()) {
            return "Climbing";
        }

        if(mc.player.isDead) {
            return "Died";
        }

        if(mc.currentScreen instanceof GuiGameOver) {
            return "Dead";
        }

        if(mc.player.isSpectator()) {
            return "In spectator";
        }

        return back;
    }

    private String setDetails() {
        String detail = discordRichPresence.details;

        if(mc.player == null && mc.currentScreen instanceof GuiMainMenu) {
            return user + " | " + "In menu";
        }

        if(mc.player != null || mc.isIntegratedServerRunning()) {
            return user + " | " + "Singleplayer" + " | " + "Gaming";
        }

        if(mc.player != null && !mc.isIntegratedServerRunning() && mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.equals("")) {
            return user + " | " + "Multiplayer" + " | " + "Gaming";
        }

        return detail;
    }
}

