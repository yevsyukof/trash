package time.statistic;

import time.statistic.controller.Controller;
import time.statistic.model.Model;
import time.statistic.view.View;

public class TimeStatApp implements Runnable {

    private static TimeStatApp instance;

    private static Controller controller;
    private static View gui;
    private static Model model;

//    private static Timer timer;

    private TimeStatApp() {
        model = new Model();
        controller = new Controller(model);
        gui = new View(model, controller);
    }

    public static TimeStatApp getInstance() {
        if (instance == null) {
            instance = new TimeStatApp();
        }
        return instance;
    }

    @Override
    public void run() {
        model.addObserver(gui);

//        model.addObserver(gui.getCellsFieldPanel());
//        model.addObserver(gui.getStatisticPanel());
        javax.swing.SwingUtilities.invokeLater(gui);

        model.initNewModel();

//        timer = new Timer(GameConstants.TIMER_DELAY, e -> controller.handleTimerRequest());
//        timer.start();
    }
}
