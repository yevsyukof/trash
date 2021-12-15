package time.statistic.view;

import time.statistic.controller.Controller;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;
import time.statistic.utils.Observer;
import time.statistic.view.panels.StatisticWindow;
//import nsu.fit.yevsyukof.view.panels.CellsFieldPanel;
//import nsu.fit.yevsyukof.view.panels.StatisticPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.EventListener;

public class View implements Observer, Runnable { // все манипуляции с отрисовкой будем производить через этот класс

    private final JFrame mainWindow;
    private JMenuBar menuBar;
    private JButton startButton;

    private JPanel startWindow;
    private StatisticWindow statisticWindow;

    private final EventListener controllerInstance;

    private final Model model;

    private static final Font font = new Font("Times new roman", Font.ITALIC, 96);

    public View(Model modelInstance, Controller controller) {
        this.controllerInstance = controller;

        mainWindow = new JFrame("TIME STATISTIC");
        mainWindow.setSize(ViewConstants.MAIN_WINDOW_WIDTH, ViewConstants.MAIN_WINDOW_HEIGHT);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null); // размещает главное окно в центре

        createMenuBar(controllerInstance);

        createMainPanel();
        statisticWindow = new StatisticWindow(modelInstance, controller);

        this.model = modelInstance;
    }

    public StatisticWindow getStatisticPanel() {
        return statisticWindow;
    }

    private void createMainPanel() {
        startWindow = new JPanel(); // TODO нужно указать layout manager
//        mainPanel.setPreferredSize(new Dimension(400, 400));

        createStartButton();

        startWindow.add(startButton);
    }

//    private void createStatisticPanel() {
//
//    }


    private void createMenuBar(EventListener eventListener) {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(ViewConstants.MENU_BAR_WIDTH, ViewConstants.MENU_BAR_HEIGHT));

        JMenuItem recordActionWindowButton = new JMenuItem("Зафиксировать активность");
        JMenuItem getTimeStatisticWindowButton = new JMenuItem("Посмотреть статистику");

        recordActionWindowButton.addActionListener((ActionListener) eventListener);
        getTimeStatisticWindowButton.addActionListener((ActionListener) eventListener);

        recordActionWindowButton.setBackground(Color.WHITE);
        getTimeStatisticWindowButton.setBackground(Color.WHITE);

        menuBar.add(recordActionWindowButton);
        menuBar.add(getTimeStatisticWindowButton);
    }

    @Override
    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) { // TODO
        if (curModelState == ModelStates.START_WINDOW && prevModelState != curModelState) {
            mainWindow.remove(statisticWindow);
            mainWindow.add(startWindow);
            mainWindow.repaint();
        }

        switch (curModelState) {
            case START_WINDOW -> {
                if (model.isRecordingStat()) {
                    startButton.setBackground(Color.GREEN);
                    startButton.setText("Stop");
                } else {
                    startButton.setBackground(Color.PINK);
                    startButton.setText("Start");
                }

            }
            case STATISTIC_WINDOW -> {
                if (prevModelState == ModelStates.STATISTIC_WINDOW
                        || prevModelState == ModelStates.LOOK_AT_STATISTIC) {
                    break;
                }

                mainWindow.remove(startWindow);
                mainWindow.add(statisticWindow);
                statisticWindow.setBackground(Color.PINK);

                statisticWindow.repaint();
                statisticWindow.revalidate();
                mainWindow.repaint();
            }
            case LOOK_AT_STATISTIC -> { }
        }
    }

    private void createStartButton() {
        startButton = new JButton("Start");
        startButton.setBackground(Color.PINK);
        startButton.addActionListener((ActionListener) controllerInstance);
        startButton.setFont(font);
    }

    private JPanel createEmptyPanel(Dimension dimension) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(dimension);
        return emptyPanel;
    }
//
//    @Override
//    public void run() {
//        mainWindow.setJMenuBar(menuBar);
//
//        mainWindow.add(startButton, BorderLayout.CENTER);
//        mainWindow.add(createEmptyPanel(new Dimension(1, 40)), BorderLayout.SOUTH);
//        mainWindow.add(createEmptyPanel(new Dimension(40, 1)), BorderLayout.WEST);
//        mainWindow.add(createEmptyPanel(new Dimension(40, 1)), BorderLayout.EAST);
//
//        mainWindow.setResizable(false);
//        mainWindow.setFocusable(true);
//        mainWindow.setVisible(true);
//    }

    @Override
    public void run() {
        mainWindow.setJMenuBar(menuBar);

        mainWindow.add(startWindow, BorderLayout.CENTER);

        mainWindow.setResizable(false);
        mainWindow.setFocusable(true);
        mainWindow.setVisible(true);
    }
}

