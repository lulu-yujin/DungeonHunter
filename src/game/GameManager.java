package game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import player.Player;

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

            if (!collisionManager.checkPlayerCollision(newRow, newCol)) {
                player.move(dir);
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.S) {
            Player.Direction dir = Player.Direction.DOWN;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(newRow, newCol)) {
                player.move(dir);
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.A) {
            Player.Direction dir = Player.Direction.LEFT;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(newRow, newCol)) {
                player.move(dir);
            } else {
                player.face(dir);
            }
        }

        else if (event.getCode() == KeyCode.D) {
            Player.Direction dir = Player.Direction.RIGHT;

            int newRow = player.getNextRow(dir);
            int newCol = player.getNextCol(dir);

            if (!collisionManager.checkPlayerCollision(newRow, newCol)) {
                player.move(dir);
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

    public Player getPlayer() {
        return player;
    }
}