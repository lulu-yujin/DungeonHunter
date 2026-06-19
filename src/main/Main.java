package main;

import javax.swing.JFrame;

import ui.GamePanel;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Dungeon Hunter");
		
		GamePanel gamePanel = new GamePanel();
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(True);
		
	}

}
