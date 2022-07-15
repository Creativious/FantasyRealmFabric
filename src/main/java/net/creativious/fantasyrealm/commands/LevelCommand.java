package net.creativious.fantasyrealm.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.jmx.Server;

import javax.swing.text.html.parser.Entity;
import java.awt.event.TextEvent;
import java.util.Collection;
import java.util.Iterator;

public class LevelCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> getCommand() {

        return (CommandManager.literal("level").requires(serverCommandSource -> {
            return serverCommandSource.hasPermissionLevel(3);
        })).then(CommandManager.literal("set").then(CommandManager.literal("level").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("level", IntegerArgumentType.integer()).executes((commandContext) -> {
            return executeLevelCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "level"), "set");
        })))).then(CommandManager.literal("progress").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("levelProgress", FloatArgumentType.floatArg()).executes((commandContext) -> {
            return executeProgressCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), FloatArgumentType.getFloat(commandContext, "levelProgress"), "set");
                }))
        )));
    }

    private static int executeLevelCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, int level, String type) {
        Iterator<ServerPlayerEntity> serverPlayerEntityIterator = targets.iterator();

        while (serverPlayerEntityIterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) serverPlayerEntityIterator.next();
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) serverPlayerEntity).getPlayerStatsManager(serverPlayerEntity);
            if (type.equals("set")) {
                playerStatsManager.setLevel(level);
                PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, serverPlayerEntity);

                source.sendFeedback(Text.translatable("commands.level.changed", serverPlayerEntity.getDisplayName(), Integer.toString(level)), true);


            }

        }
        return 0;
    }

    private static int executeProgressCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Float progress, String type) {
        Iterator<ServerPlayerEntity> serverPlayerEntityIterator = targets.iterator();

        while (serverPlayerEntityIterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) serverPlayerEntityIterator.next();
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) serverPlayerEntity).getPlayerStatsManager(serverPlayerEntity);
            if (type.equals("set")) {
                playerStatsManager.setLevelProgress(progress);
                PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, serverPlayerEntity);
                source.sendFeedback(Text.translatable("commands.level.progress.changed", serverPlayerEntity.getDisplayName(), progress), true);


            }

        }
        return 0;
    }
}
