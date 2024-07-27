package plugin.micra_twentythree.command;

import java.net.http.WebSocket.Listener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.micra_twentythree.Main;


public class EnemyDownCommand implements CommandExecutor, Listener, org.bukkit.event.Listener {

  public Player player;

  private int score;

  private Map<Player, Integer> scores;

  private Main main;

  public EnemyDownCommand() {
    this.main = main;
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s,
      String[] strings) {
    if (commandSender instanceof Player player) {
      this.player = player;
      World world = player.getWorld();

      initPlayerStatus(player);

      world.spawnEntity(getEnemySpawnLocation(player, world), getEnemy());


    }
    return false;
  }

  /**
   * @param e 倒した敵の点数、 倒した敵1体につき、10点もらえる。
   */
  @EventHandler
  public void onEnemyDeath(EntityDeathEvent e) {
    Player player = e.getEntity().getKiller();
//    イベント実行前と実行後で、プレイヤーがNullであれば、スキップする(Null対策)
    if (Objects.isNull(player)) {
      return;
    }
    if (Objects.isNull(this.player)) {
      return;
    }
//  敵を倒した時の点数を表示する
    if (this.player.getName().equals(player.getName())) {
      score += 10;
      player.sendMessage("敵を倒した！ 現在のスコアは、" + score + "点です。");
    }
  }


  /**
   * ゲームを始める前にプレイヤーの状態を設定する コマンドを使って、体力とスタミナ回復、飛行、アイテム装備を上書きする(set)
   *
   * @param player コマンドを実行したプレイヤー
   */
  private void initPlayerStatus(Player player) {
    //      体力回復
    player.setHealth(20);
//      スタミナ回復
    player.setFoodLevel(20);
//      飛行許可
    player.setAllowFlight(true);
//      飛行可能
    player.setFlying(true);
//      飛行スピード
    player.setFlySpeed(1f);

//      アイテム装備を取得
    PlayerInventory inventory = player.getInventory();
//      アイテム装備の一覧
    inventory.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    inventory.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    inventory.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
  }


  /**
   * 敵の出現場所を取得する
   * <p>
   * 出現エリアのX軸とZ軸は自分の位置からプラス、ランダムで-10~9の値が設定 Y軸は、プレイヤーと同じ位置
   *
   * @param player コマンドを実行したプレイヤー
   * @param world  　コマンドを実行したプレイヤーが所属するワールド
   * @return 敵の出現場所
   */
  private Location getEnemySpawnLocation(Player player, World world) {
    //   出現する敵のエリアを判定する
    Location playerlocation = player.getLocation();
    int randomX = new SplittableRandom().nextInt(20) - 10;
    int randomZ = new SplittableRandom().nextInt(20) - 10;

    double x = playerlocation.getX();
    double y = playerlocation.getY();
    double z = playerlocation.getZ();

    return new Location(world, x, y, z);

  }

  /**
   * ランダムで敵を抽選して、その結果の敵を取得
   *
   * @return 敵
   */

  private EntityType getEnemy() {
    List<EntityType> enemyList = List.of(EntityType.ZOMBIE, EntityType.WITHER_SKELETON,
        EntityType.SKELETON);
    int random = new SplittableRandom().nextInt(enemyList.size());
    return enemyList.get(random);
  }
}