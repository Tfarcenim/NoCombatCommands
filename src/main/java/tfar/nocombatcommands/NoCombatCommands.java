package tfar.nocombatcommands;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NoCombatCommands.MODID)
public class NoCombatCommands
{
    // Directly reference a log4j logger.

    public static final String MODID = "nocombatcommands";

    public NoCombatCommands() {
        MinecraftForge.EVENT_BUS.addListener(this::onCommand);
    }

    public static int TIME  = 200;

    private void onCommand(CommandEvent event) {
        ParseResults<CommandSource> parseResults = event.getParseResults();
        CommandContextBuilder<CommandSource> context = parseResults.getContext();
        CommandSource source = context.getSource();
        if (source.hasPermission(1)) return;
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            LivingEntityDuck livingEntityDuck = (LivingEntityDuck) player;

            int lastAttacked = player.tickCount - livingEntityDuck.getLastAttackedByPlayer();
            int lastAttack = player.tickCount - livingEntityDuck.getLastAttackedPlayer();

            if ((lastAttacked > 0 && lastAttack > 0) && (lastAttacked < TIME || lastAttack < TIME))  {
                event.setCanceled(true);
                event.setException(new CommandException(new StringTextComponent("Cannot use commands in battle!")));
            }
        }
    }
}
