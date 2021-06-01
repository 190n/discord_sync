package org._190n.discord_sync;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import javax.security.auth.login.LoginException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;

public class App extends JavaPlugin implements Listener, EventListener {
    FileConfiguration config = getConfig();
    JDA jda = null;

    @Override
    public void onEnable() {
        config.addDefault("discordToken", "");
        config.addDefault("roleId", "");
        config.addDefault("guildId", "");
        config.addDefault("players", new HashMap<String, String>());
        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);

        JDABuilder builder = JDABuilder.createDefault(config.getString("discordToken"))
            .addEventListeners(this);
        try {
            jda = builder.build();
            getLogger().info("connected to discord");
        } catch (LoginException e) {
            getLogger().severe("failed to connect to discord");
        }
    }

    @Override public void onDisable() {
        for (String playerId : config.getConfigurationSection("players").getKeys(false)) {
            setRole(playerId, false);
        }
    }

    private void setRole(String uuid, boolean assign) {
        if (jda == null) {
            getLogger().warning("ignoring player event as we are not connected to discord or guild is invalid");
        } else {
            Guild guild = jda.getGuildById(config.getString("guildId"));
            Role role = jda.getRoleById(config.getString("roleId"));

            if (guild == null) {
                getLogger().severe("guild not found");
            } else if (role == null) {
                getLogger().severe("role not found");
            } else {
                getLogger().info("player join: " + uuid);
                ConfigurationSection players = config.getConfigurationSection("players");
                if (players.contains(uuid)) {
                    String discordId = players.getString(uuid);
                    getLogger().info("discord id: " + discordId);
                    if (assign) {
                        guild.addRoleToMember(discordId, role).queue();
                    } else {
                        guild.removeRoleFromMember(discordId, role).queue();
                    }
                } else {
                    getLogger().info("no discord account for this player");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setRole(event.getPlayer().getUniqueId().toString(), true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        setRole(event.getPlayer().getUniqueId().toString(), false);
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof ReadyEvent) {
            getLogger().info("discord ready!");
            for (String playerId : config.getConfigurationSection("players").getKeys(false)) {
                Player player = getServer().getPlayer(UUID.fromString(playerId));
                setRole(playerId, player != null);
            }
        }
    }
}
