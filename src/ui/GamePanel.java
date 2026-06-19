package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class GamePanel extends JPenal{
	
	final int tileSize = 32; // 32*32 tile
	final int screenCol = 20;
	final int screenRow = 15;
	final int screenWidth = tileSize * screenCol; // 640 pixels
    final int screenHeight =tileSize * screenRow; // 480 pixels

    public GamePanel() {
    	
    	    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    	    this.setBackground(Color.black);
    	    this.setDoubleBuffered(true);
    }
}
