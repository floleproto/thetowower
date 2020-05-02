package com.floleproto.thetower.game;

import com.floleproto.thetower.Main;
import com.floleproto.thetower.game.save.InventorySave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    private String name;
    private String tag;
    private int score;
    private ChatColor color;
    private ArrayList<UUID> players = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public void setPlayers(ArrayList<UUID> players) {
        this.players = players;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Area getPool() {
        return pool;
    }

    public void setPool(Area pool) {
        this.pool = pool;
    }

    public Area getPoolProtected() {
        return poolProtected;
    }

    public void setPoolProtected(Area poolProtected) {
        this.poolProtected = poolProtected;
    }

    public Area getSpawnProtected() {
        return spawnProtected;
    }

    public void setSpawnProtected(Area spawnProtected) {
        this.spawnProtected = spawnProtected;
    }

    private Location spawn;
    private Area pool;
    private Area poolProtected;
    private Area spawnProtected;

    public Team(String name, String tag, ChatColor color, Location spawn, Area pool, Area poolProtected, Area spawnProtected) {
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.spawn = spawn;
        this.pool = pool;
        this.poolProtected = poolProtected;
        this.spawnProtected = spawnProtected;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> playersParsed = new ArrayList<>();
        for (UUID id: players) {
            playersParsed.add(Bukkit.getPlayer(id));
        }

        return playersParsed;
    }

    public int getSize(){
        return players.size();
    }

    public void setupPlayers(){
        for(Player p : getPlayers()){
            ItemStack[][] itemStacks = InventorySave.loadInventory(tag);
            p.getInventory().setArmorContents(itemStacks[0]);
            p.getInventory().setContents(itemStacks[1]);
            p.teleport(spawn);
            p.setFoodLevel(30);
            Main.instance.scoreboardManager.setScoreboardTemplate(p, GameStates.ONGAME);
        }
    }

    public void setTeamInventory(Player p){
        ItemStack[][] itemStacks = InventorySave.loadInventory(tag);
        p.getInventory().setArmorContents(itemStacks[0]);
        p.getInventory().setContents(itemStacks[1]);
    }

    public void teleportToTeamPoint(Player p){
        p.teleport(spawn);
    }

    public void teleportAllTeam(){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.teleport(spawn);
        }
    }

    public void scorePoint(Player p){
        addScore(1);
        teleportToTeamPoint(p);

        Bukkit.broadcastMessage("§b§lGoal §4§l>§1§l> " + p.getDisplayName() +" has marked one point for the " + name + " team !");

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if(isInTeam(pl)){
                pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1, 2);
            } else {
                pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1, 0.5f);
            }
        }
    }

    public void addPlayer(Player p){
        players.add(p.getUniqueId());
        p.setPlayerListName(color + p.getName() + "§r");
        p.setDisplayName(color + p.getName() + "§r");
    }

    public void removePlayer(Player p){
        players.remove(p.getUniqueId());
        p.setPlayerListName(p.getName() + "§r");
        p.setDisplayName(p.getName() + "§r");
    }

    public boolean isInTeam(Player p){
        return players.contains(p.getUniqueId());
    }

    public void addScore(int i) {
        score += i;
    }

    public boolean checkWin(){
        return score >= Main.instance.getConfig().getInt("scoretowin");
    }
}