<<<<<<< HEAD:1.16/fabric/src/main/java/com/collarmc/plastic/fabric/FabricPlastic.java
package com.collarmc.plastic.fabric;
=======
package com.collarmc.collar.plastic;
>>>>>>> topic/merge:1.16/glue/src/main/java/com/collarmc/collar/plastic/GluePlastic.java

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import com.collarmc.plastic.Plastic;
import com.collarmc.plastic.ui.TextureProvider;
import com.collarmc.pounce.EventBus;

import java.io.File;

public final class GluePlastic extends Plastic {

    public GluePlastic(TextureProvider textureProvider, EventBus eventBus) {
        super(new GlueDisplay(), new GlueWorld(textureProvider, new GlueChatService(new GlueDisplay()), eventBus), eventBus);
    }

    @Override
    public File home() {
        return MinecraftClient.getInstance().runDirectory;
    }

    @Override
    public String serverAddress() {
        ServerInfo currentServerEntry = MinecraftClient.getInstance().getCurrentServerEntry();
        if (currentServerEntry == null) {
            throw new IllegalStateException("not connected to a server");
        }
        return currentServerEntry.address;
    }

    @Override
    public String sessionId() {
        return MinecraftClient.getInstance().getSession().getSessionId();
    }

    @Override
    public String accessToken() {
        return MinecraftClient.getInstance().getSession().getAccessToken();
    }
}
