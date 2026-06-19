package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	
	final int tileSize = 32; // 32*32 tile
	final int screenCol = 32;
	final int screenRow = 24;
	final int screenWidth = tileSize * screenCol; // 640 pixels
    final int screenHeight =tileSize * screenRow; // 480 pixels

    Thread gameThread;
    
    public GamePanel(){
    	
    	    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    	    this.setBackground(Color.white);
    	    this.setDoubleBuffered(true);
    }
    
    public void startGameThread() {
    	gameThread = new Thread(this);
    	gameThread.start();
    }
    
    
	public void run() {
			while(gameThread != null) {
				
				
				update();
				
				
				repaint();
				
			}
	}
    public void update() {
		
	}
    public void paintComponent(Graphics g) {
    	
    	   super.paintComponent(g);
    	   
    	   Graphics2D g2 = (Graphics2D)g;
    	   
    	   g2.setColor(Color.black);
    	   
    	   g2.fillRect(100, 100, tileSize, tileSize);
    	   
    	   g2.dispose();
    
    }
	
}
