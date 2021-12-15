package time.statistic.view.panels;

import time.statistic.controller.Controller;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;
import time.statistic.utils.Observer;
import time.statistic.view.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class TimeStatisticPanel extends JPanel implements Observer {

    private final Model modelInstance;

    private final ResultPanel resultPanel;

    public TimeStatisticPanel(Model model, Controller controller) {
        super(new GridLayout(4, 1));
//        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(ViewConstants.MAIN_WINDOW_WIDTH / 3 * 2, 1));

        this.add(new DatePickerPanel(model, controller));

        this.modelInstance = model;
        modelInstance.addObserver(this);

        resultPanel = new ResultPanel(model);

        this.add(resultPanel);
    }

    @Override
    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) {

    }
}
