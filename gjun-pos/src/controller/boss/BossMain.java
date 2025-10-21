package controller.boss;

import model.Member;

import javax.swing.*;
import java.awt.*;

public class BossMain extends JFrame {
    private Member currentUser;
    public BossMain(Member m) {
        this.currentUser = m;
        setTitle("Boss Main - " + m.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Members", new MemberPanel());
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Stock", new StockPanel());
        tabs.addTab("Sales", new SalesPanel());
        tabs.addTab("History", new HistoryPanel());

        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
