package plugin.micra_twentythree;

import static org.bukkit.Bukkit.getPluginManager;

import java.net.http.WebSocket.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.micra_twentythree.command.EnemyDownCommand;


public final class Main extends JavaPlugin implements Listener, org.bukkit.event.Listener {

  private Player player;


  @Override
  public void onEnable() {
    EnemyDownCommand enemyDownCommand = new EnemyDownCommand();
    Bukkit.getPluginManager().registerEvents(enemyDownCommand, this);
    Bukkit.getPluginManager().registerEvents(this, this);

    getCommand("enemyDown").setExecutor(enemyDownCommand);
  }


  //  プレイヤーがマイクラサーバーに参加した時のイベント
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    player.sendTitle("敵を倒して、点数獲得", "", 10, 70, 20);
  }


}