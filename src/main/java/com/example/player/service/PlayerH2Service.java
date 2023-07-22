/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;

import com.example.player.repository.PlayerRepository;

@Service 
public class PlayerH2Service implements PlayerRepository{
    @Autowired
    private JdbcTemplate db;

    @Override 
    public ArrayList<Player> getPlayers(){
        List<Player> playerList = db.query("select * from TEAM",new PlayerRowMapper());
        ArrayList<Player> player = new ArrayList<>(playerList);
        return player;
    }

    @Override
    public Player getPlayerById(int playerId){
        try{
            Player player = db.queryForObject("select * from TEAM where PLAYERID = ?", new PlayerRowMapper(), playerId);
            return player;
        }
        catch (Exception e){ 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    }

    @Override 
    public Player addPlayer(Player player){
        db.update("insert into TEAM(PLAYERNAME,JERSEYNUMBER,ROLE) values(?,?,?)",player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        Player savedplayer = db.queryForObject("select * from TEAM where PLAYERNAME=? and JERSEYNUMBER=? and ROLE=?", 
                              new PlayerRowMapper(),player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        return savedplayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player){
        if(player.getPlayerName() != null){
            db.update("update TEAM set PLAYERNAME = ? where PLAYERID = ?", player.getPlayerName(), playerId);
        }
        if(player.getJerseyNumber() != 0){
            db.update("update TEAM set JERSEYNUMBER = ? where PLAYERID = ?", player.getJerseyNumber(), playerId);
        }
        if(player.getRole() != null){
            db.update("update TEAM set ROLE = ? where PLAYERID = ?",player.getRole(), playerId);
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId){
        db.update("delete from TEAM where PLAYERID = ?", playerId);
    }
}