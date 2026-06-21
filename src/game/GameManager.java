package game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import player.Player;
import game.CollisionManager;

public class GameManager {

    private Player player;
    private CollisionManager collisionManager;

    public GameManager(Player player, CollisionManager collisionManager) {
        this.player = player;
        this.collisionManager = collisionManager;
    }

    public void handleKeyPressed(KeyEvent event) {

        if (event.getCode() == KeyCode.W) {
            Player.Direction dir = Player.Direction.UP;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(player,newRow, newCol)) {
                player.move(dir);
                checkPortalAfterMove();
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.S) {
            Player.Direction dir = Player.Direction.DOWN;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(player,newRow, newCol)) {
                player.move(dir);
                checkPortalAfterMove();
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.A) {
            Player.Direction dir = Player.Direction.LEFT;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(player,newRow, newCol)) {
                player.move(dir);
                checkPortalAfterMove();
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.D) {
            Player.Direction dir = Player.Direction.RIGHT;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(player,newRow, newCol)) {
                player.move(dir);
                checkPortalAfterMove();
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.J) {
            int attackRow = player.getAttackRow();
            int attackCol = player.getAttackCol();

            System.out.println("Player attacks tile: " + attackRow + ", " + attackCol);

            // 后面和 Enemy 系统连接：
            // enemy.takeDamage(player.getDamage());
        }

        else if (event.getCode() == KeyCode.E) {
            System.out.println("Open shop");
        }

        else if (event.getCode() == KeyCode.ESCAPE) {
            System.out.println("Pause game");
        }
    }
    
    private void checkPortalAfterMove() {
        // Later connect with map system.
        // If current tile is portal 'T', check whether player has key.
        // Example:
        //
        // if (mazeMap.isPortal(player.getRow(), player.getCol())) {
        //     if (player.hasKey()) {
        //         mazeMap.nextLevel();
        //         int[] spawn = mazeMap.getSpawnPoint();
        //         player.resetToSpawn(spawn[0], spawn[1]);
        //     } else {
        //         System.out.println("You need a key to enter the next map.");
        //     }
        // }
    }

    public Player getPlayer() {
        return player;
    }
}