package application;

import logic.SelectionPolicy;
import logic.SimulationManager;
import logic.TaskGenerator;
import model.Server;
import model.SimulationConfig;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {

    private JTextField tfNumClients, tfNumQueues, tfMaxSimTime;
    private JTextField tfMinArrival, tfMaxArrival, tfMinService, tfMaxService;
    private JComboBox<String> cbStrategy;
    private JTextArea taLog;
    private JButton btnValidate, btnStart;
    private SimulationPanel visualPanel;

    private SimulationConfig validatedConfig = null;

    public SimulationFrame() {
        setTitle("Queues Management Simulation");
        setSize(900, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Clients (N):"));
        tfNumClients = new JTextField("50");
        inputPanel.add(tfNumClients);

        inputPanel.add(new JLabel("Servers/Queues (Q):"));
        tfNumQueues = new JTextField("5");
        inputPanel.add(tfNumQueues);

        inputPanel.add(new JLabel("Max Simulation Time:"));
        tfMaxSimTime = new JTextField("60");
        inputPanel.add(tfMaxSimTime);

        inputPanel.add(new JLabel("Min Arrival Time:"));
        tfMinArrival = new JTextField("2");
        inputPanel.add(tfMinArrival);

        inputPanel.add(new JLabel("Max Arrival Time:"));
        tfMaxArrival = new JTextField("40");
        inputPanel.add(tfMaxArrival);

        inputPanel.add(new JLabel("Min Service Time:"));
        tfMinService = new JTextField("1");
        inputPanel.add(tfMinService);

        inputPanel.add(new JLabel("Max Service Time:"));
        tfMaxService = new JTextField("7");
        inputPanel.add(tfMaxService);

        inputPanel.add(new JLabel("Selection Strategy:"));
        cbStrategy = new JComboBox<>(new String[]{"Shortest Time", "Shortest Queue"});
        inputPanel.add(cbStrategy);

        btnValidate = new JButton("VALIDATE INPUT");
        inputPanel.add(btnValidate);
        inputPanel.add(new JLabel(""));

        btnStart = new JButton("START SIMULATION");
        btnStart.setEnabled(false);
        inputPanel.add(btnStart);
        inputPanel.add(new JLabel(""));

        add(inputPanel, BorderLayout.NORTH);

        visualPanel = new SimulationPanel();
        taLog = new JTextArea();
        taLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taLog);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, visualPanel, scrollPane);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        btnValidate.addActionListener(e -> validateInput());
        btnStart.addActionListener(e -> startSimulation());
    }

    private void validateInput() {
        int n, q, tMax, minArr, maxArr, minSrv, maxSrv;

        try {
            n      = Integer.parseInt(tfNumClients.getText().trim());
            q      = Integer.parseInt(tfNumQueues.getText().trim());
            tMax   = Integer.parseInt(tfMaxSimTime.getText().trim());
            minArr = Integer.parseInt(tfMinArrival.getText().trim());
            maxArr = Integer.parseInt(tfMaxArrival.getText().trim());
            minSrv = Integer.parseInt(tfMinService.getText().trim());
            maxSrv = Integer.parseInt(tfMaxService.getText().trim());
        } catch (NumberFormatException ex) {
            showError("All fields must contain whole numbers (integers).");
            disableStart(); return;
        }

        if (n <= 0)      { showError("Number of clients (N) must be greater than 0."); disableStart(); return; }
        if (q <= 0)      { showError("Number of queues (Q) must be greater than 0."); disableStart(); return; }
        if (tMax <= 0)   { showError("Max simulation time must be greater than 0."); disableStart(); return; }
        if (minArr < 0)  { showError("Min arrival time cannot be negative."); disableStart(); return; }
        if (minArr > maxArr) {
            showError("Min arrival time (" + minArr + ") cannot be greater than max arrival time (" + maxArr + ").");
            disableStart(); return;
        }
        if (maxArr >= tMax) {
            showError("Max arrival time (" + maxArr + ") must be less than simulation time (" + tMax + ").");
            disableStart(); return;
        }
        if (minSrv <= 0) { showError("Min service time must be greater than 0."); disableStart(); return; }
        if (minSrv > maxSrv) {
            showError("Min service time (" + minSrv + ") cannot be greater than max service time (" + maxSrv + ").");
            disableStart(); return;
        }
        if (n < q) {
            showError("Warning: clients (" + n + ") < queues (" + q + "). Some queues will be empty.");
        }

        validatedConfig = new SimulationConfig(n, q, tMax, minArr, maxArr, minSrv, maxSrv);
        btnStart.setEnabled(true);
        JOptionPane.showMessageDialog(this,
                "Input is valid!\n\nClients: " + n + "  |  Queues: " + q
                        + "\nSim time: " + tMax
                        + "\nArrival: [" + minArr + ", " + maxArr + "]"
                        + "\nService: [" + minSrv + ", " + maxSrv + "]"
                        + "\nStrategy: " + cbStrategy.getSelectedItem()
                        + "\n\nYou can now press START SIMULATION.",
                "Validation Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void disableStart() {
        validatedConfig = null;
        btnStart.setEnabled(false);
    }

    private void startSimulation() {
        if (validatedConfig == null) { showError("Please validate the input first."); return; }

        SelectionPolicy policy = (cbStrategy.getSelectedIndex() == 0)
                ? SelectionPolicy.SHORTEST_TIME
                : SelectionPolicy.SHORTEST_QUEUE;

        taLog.setText("");
        btnStart.setEnabled(false);
        btnValidate.setEnabled(false);

        List<Task> tasks = TaskGenerator.generateTasks(validatedConfig);
        SimulationManager manager = new SimulationManager(validatedConfig, tasks, this, policy);
        Thread simThread = new Thread(manager);
        simThread.start();

        new Thread(() -> {
            try { simThread.join(); }
            catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            SwingUtilities.invokeLater(() -> {
                btnStart.setEnabled(true);
                btnValidate.setEnabled(true);
                validatedConfig = null;
            });
        }).start();
    }

    public void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            taLog.append(text + "\n\n");
            taLog.setCaretPosition(taLog.getDocument().getLength());
        });
    }

    public void updateVisuals(List<Task> waiting, List<Server> servers) {
        SwingUtilities.invokeLater(() -> visualPanel.updateState(waiting, servers));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationFrame().setVisible(true));
    }
}