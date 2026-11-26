package application;

import model.Server;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationPanel extends JPanel {
    private List<Task> waitingTasks = new ArrayList<>();
    private List<Server> servers = new ArrayList<>();

    public SimulationPanel() {
        setBackground(new Color(245, 245, 250));
    }

    public void updateState(List<Task> waiting, List<Server> servers) {
        this.waitingTasks = new ArrayList<>(waiting);
        this.servers = new ArrayList<>(servers);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();

        g2d.setColor(new Color(220, 220, 230));
        g2d.fillRect(0, 0, width, 80);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Waiting Area (Clients to arrive)", 10, 20);

        int startX = 10;
        int startY = 40;
        for (int i = 0; i < waitingTasks.size(); i++) {
            g2d.setColor(new Color(220, 80, 80)); // Red circles
            int xPos = startX + (i * 35) % Math.max(1, width - 40);
            int yPos = startY + (i * 35 / Math.max(1, width)) * 35;
            g2d.fillOval(xPos, yPos, 26, 26);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.valueOf(waitingTasks.get(i).getId()), xPos + 8, yPos + 18);
        }

        if (servers.isEmpty()) return;
        int serverLaneWidth = width / servers.size();

        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);
            int laneCenter = (i * serverLaneWidth) + (serverLaneWidth / 2);
            int serverY = 110;

            g2d.setColor(new Color(80, 180, 120)); // Green checkout lane
            g2d.fillRect(laneCenter - 30, serverY, 60, 40);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Q" + s.getId(), laneCenter - 10, serverY + 25);

            Task[] queuedTasks = s.getTasks();
            for (int j = 0; j < queuedTasks.length; j++) {
                int clientY = serverY + 50 + (j * 35);
                g2d.setColor(new Color(80, 130, 220)); // Blue circles
                g2d.fillOval(laneCenter - 15, clientY, 30, 30);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.valueOf(queuedTasks[j].getId()), laneCenter - 5, clientY + 20);
            }
        }
    }
}