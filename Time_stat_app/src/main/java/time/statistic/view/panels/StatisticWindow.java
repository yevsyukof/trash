package time.statistic.view.panels;

import time.statistic.controller.Controller;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;
import time.statistic.utils.Observer;

import javax.swing.*;
import java.awt.*;
import time.statistic.view.ViewConstants;

public class StatisticWindow extends JPanel implements Observer {

    private final Model model;

    private final ActivitiesPanel activitiesPanel;
    private final TimeStatisticPanel timeStatisticPanel;

    public StatisticWindow(Model modelInstance, Controller controller) {
        super(new BorderLayout());

        this.model = modelInstance;

        activitiesPanel = new ActivitiesPanel(modelInstance, controller);

        timeStatisticPanel = new TimeStatisticPanel(modelInstance, controller);

        this.add(activitiesPanel, BorderLayout.WEST);
        this.add(timeStatisticPanel, BorderLayout.EAST);

    }

//    public ActivitiesPanel getActivitiesPanel() {
//        return activitiesPanel;
//    }

    @Override
    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) {
        // TODO
    }
}
