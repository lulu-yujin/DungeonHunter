package shop;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopUI {
    
    private Player player;
    private Shop shop;
    private JFrame frame;
    private JLabel coinsLabel;
    private JLabel currentWeaponLabel;
    private JTextArea outputArea;
    
    // 武器价格
    private static final int IRON_SWORD_PRICE = 100;
    private static final int FIRE_SWORD_PRICE = 250;
    private static final int LEGENDARY_SWORD_PRICE = 500;
    
    public ShopUI() {
        player = new Player("Hero");
        shop = new Shop();
        initializeUI();
    }
    
    private void initializeUI() {
        // 创建主窗口
        frame = new JFrame("武器商店");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout(10, 10));
        
        // 创建顶部信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        coinsLabel = new JLabel("金币: " + player.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        currentWeaponLabel = new JLabel("当前武器: " + player.getCurrentWeapon().getName());
        currentWeaponLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(coinsLabel);
        infoPanel.add(currentWeaponLabel);
        
        // 创建武器按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton ironSwordBtn = new JButton("铁剑 - " + IRON_SWORD_PRICE + "金币");
        JButton fireSwordBtn = new JButton("火剑 - " + FIRE_SWORD_PRICE + "金币");
        JButton legendarySwordBtn = new JButton("传说之剑 - " + LEGENDARY_SWORD_PRICE + "金币");
        JButton exitBtn = new JButton("退出商店");
        
        // 设置按钮样式
        styleButton(ironSwordBtn, new Color(169, 169, 169));
        styleButton(fireSwordBtn, new Color(255, 69, 0));
        styleButton(legendarySwordBtn, new Color(255, 215, 0));
        styleButton(exitBtn, new Color(100, 100, 100));
        
        buttonPanel.add(ironSwordBtn);
        buttonPanel.add(fireSwordBtn);
        buttonPanel.add(legendarySwordBtn);
        buttonPanel.add(exitBtn);
        
        // 创建输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(240, 240, 240));
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("购买记录"));
        scrollPane.setPreferredSize(new Dimension(380, 100));
        
        // 添加事件监听器
        ironSwordBtn.addActionListener(e -> purchaseWeapon("Iron Sword", IRON_SWORD_PRICE, new IronSword()));
        fireSwordBtn.addActionListener(e -> purchaseWeapon("Fire Sword", FIRE_SWORD_PRICE, new FireSword()));
        legendarySwordBtn.addActionListener(e -> purchaseWeapon("Legendary Sword", LEGENDARY_SWORD_PRICE, new LegendarySword()));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "确定要退出商店吗？", "确认退出", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // 组装界面
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);
        
        // 居中显示
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void purchaseWeapon(String weaponName, int price, Weapon weapon) {
        if (shop.buyWeapon(player, weapon)) {
            outputArea.append("✓ " + weaponName + " 购买成功！\n");
            updateInfo();
            showSuccessMessage(weaponName);
        } else {
            outputArea.append("✗ 金币不足，无法购买 " + weaponName + "！\n");
            showErrorMessage();
        }
    }
    
    private void updateInfo() {
        coinsLabel.setText("金币: " + player.getCoins());
        currentWeaponLabel.setText("当前武器: " + player.getCurrentWeapon().getName());
    }
    
    private void showSuccessMessage(String weaponName) {
        JOptionPane.showMessageDialog(frame, 
            weaponName + " 购买成功！\n" + 
            "当前金币: " + player.getCoins(),
            "购买成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorMessage() {
        JOptionPane.showMessageDialog(frame, 
            "金币不足！请赚取更多金币。\n" +
            "当前金币: " + player.getCoins(),
            "购买失败", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    public static void main(String[] args) {
        // 在事件调度线程中创建UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShopUI();
            }
        });
    }
}