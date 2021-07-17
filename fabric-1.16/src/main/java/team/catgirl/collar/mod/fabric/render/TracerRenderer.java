package team.catgirl.collar.mod.fabric.render;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import team.catgirl.collar.api.location.Location;
import team.catgirl.collar.client.Collar;
import team.catgirl.collar.mod.common.CollarService;
import team.catgirl.plastic.Plastic;
import team.catgirl.pounce.Preference;
import team.catgirl.pounce.Subscribe;

import java.awt.*;

public final class TracerRenderer {

    private final Plastic plastic;
    private final CollarService service;

    public TracerRenderer(Plastic plastic, CollarService service) {
        this.plastic = plastic;
        this.service = service;
    }

    @Subscribe(Preference.CALLER)
    public void onRender(WorldRenderEvent event) {
        service.getCollar().ifPresent(collar -> {
            if (collar.getState() != Collar.State.CONNECTED || !collar.configuration.debugConfiguration.tracers) {
                return;
            }
            collar.location().playerLocations().forEach((player, location) -> {
                // Don't show where other players are if they are not in the same dimension
                Location playerLocation = plastic.world.currentPlayer().location();
                if (!location.dimension.equals(playerLocation.dimension)) {
                    return;
                }
                ProjectionUtil.INSTANCE.updateBuffers();
                renderLine(from(playerLocation), from(location), Color.MAGENTA, 3f);
            });
        });
    }

    private Vec3d from(Location location) {
        return new Vec3d(location.x, location.y, location.z);
    }

    private static void renderLine(Vec3d from, Vec3d to, Color col, float width) {
        Camera c = BlockEntityRenderDispatcher.INSTANCE.camera;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glRotated(MathHelper.wrapDegrees(c.getPitch()), 1, 0, 0);
        GL11.glRotated(MathHelper.wrapDegrees(c.getYaw() + 180.0), 0, 1, 0);
        GL11.glColor4f(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, col.getAlpha() / 255F);
        GL11.glBegin(GL11.GL_LINES);
        {
            Vec3d f1 = from.subtract(c.getPos());
            Vec3d t1 = to.subtract(c.getPos());
            GL11.glVertex3d(f1.x, f1.y, f1.z);
            GL11.glVertex3d(t1.x, t1.y, t1.z);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    private static Vector3d lerpPos(Location location, final float alpha) {
        Location lastRenderLocation = location; // TODO: figuree out how to handle this
        float x = MathHelper.lerp(alpha, lastRenderLocation.x.floatValue(), location.x.floatValue());
        float y = MathHelper.lerp(alpha, lastRenderLocation.y.floatValue(), location.y.floatValue());
        float z = MathHelper.lerp(alpha, lastRenderLocation.z.floatValue(), location.z.floatValue());
        return new Vector3d(x, y, z);
    }
}
