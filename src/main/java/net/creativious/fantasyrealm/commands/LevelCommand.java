package net.creativious.fantasyrealm.commands;

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

import java.util.Collection;
import java.util.Iterator;

public class LevelCommand {

    public static LiteralArgumentBuilder<ServerCommandSource> getCommand() {

        return (CommandManager.literal("level").requires(serverCommandSource -> {
            return serverCommandSource.hasPermissionLevel(3);
        })).then(CommandManager.literal("set").then(CommandManager.literal("level").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("level", IntegerArgumentType.integer()).executes((commandContext) -> {
            return executeLevelCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "level"), "set");
        })))).then(CommandManager.literal("totalExperience").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("totalExperience", IntegerArgumentType.integer()).executes((commandContext) -> {
            return executeTotalExperienceCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "totalExperience"), "set");
                }))
        ))).then(CommandManager.literal("get").then(CommandManager.literal("neededXP").then(CommandManager.argument("targets", EntityArgumentType.players()).executes((commandContext -> {
            return executeNeededXPCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"));
        })))));
    }

    private static int executeLevelCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, int level, String type) {
        Iterator<ServerPlayerEntity> serverPlayerEntityIterator = targets.iterator();

        while (serverPlayerEntityIterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) serverPlayerEntityIterator.next();
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) serverPlayerEntity).getPlayerStatsManager(serverPlayerEntity);
            if (type.equals("set")) {
                playerStatsManager.setLevel(level);
                playerStatsManager.setTotalLevelExperience(playerStatsManager.calcTotalNeedExperienceForLevel(level));
                PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, serverPlayerEntity);

                source.sendFeedback(Text.translatable("commands.level.changed", serverPlayerEntity.getDisplayName(), Integer.toString(level)), true);


            }

        }
        return 0;
    }

    private static int executeTotalExperienceCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, int totalXP, String type) {
        Iterator<ServerPlayerEntity> serverPlayerEntityIterator = targets.iterator();

        while (serverPlayerEntityIterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) serverPlayerEntityIterator.next();
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) serverPlayerEntity).getPlayerStatsManager(serverPlayerEntity);
            if (type.equals("set")) {
                playerStatsManager.setTotalLevelExperience(totalXP);
                playerStatsManager.autoFixLevel();
                PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, serverPlayerEntity);
                source.sendFeedback(Text.translatable("commands.level.totalXP.changed", serverPlayerEntity.getDisplayName(), totalXP), true);


            }

        }
        return 0;
    }

    private static int executeNeededXPCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        Iterator<ServerPlayerEntity> serverPlayerEntityIterator = targets.iterator();

        while (serverPlayerEntityIterator.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) serverPlayerEntityIterator.next();
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) serverPlayerEntity).getPlayerStatsManager(serverPlayerEntity);
            source.sendFeedback(Text.translatable("commands.level.neededXP", serverPlayerEntity.getDisplayName(), Integer.toString(playerStatsManager.neededTotalExperienceForLevelUp())), true);
        }
        return 0;
    }
}
