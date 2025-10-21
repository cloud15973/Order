package controller.sale;

import model.Member;

import javax.swing.*;
import java.awt.*;

public class OrderMain extends JFrame {
    private Member currentUser;
    public OrderMain(Member m) {
        this.currentUser = m;
        setTitle("Order Main - " + m.getName());
        setSize(800,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cart", new CartPanel());
        tabs.addTab("Review", new ReviewPanel());
        tabs.addTab("History", new HistoryPanel());
        tabs.addTab("Profile", new MemberPanel());
        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
