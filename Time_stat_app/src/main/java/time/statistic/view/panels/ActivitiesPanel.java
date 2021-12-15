package time.statistic.view.panels;

import time.statistic.controller.Controller;
import time.statistic.db.DbService;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;
import time.statistic.utils.Observer;
import time.statistic.view.ViewConstants;

import javax.swing.*;
import java.awt.*;

//public class ActivitiesPanel extends JPanel implements Observer {
//
//    private final Model modelInstance;
//
//    private final DefaultListModel<String> dlm = new DefaultListModel<String>();
//
//    private JScrollPane curActivitiesList = null;
//
//    public ActivitiesPanel(Model model) {
//        super(new BorderLayout());
//
//        this.setPreferredSize(new Dimension(ViewConstants.MAIN_WINDOW_WIDTH / 3, 1));
//
//        modelInstance = model;
//        model.addObserver(this);
//
//        JLabel activitiesListDescription = new JLabel();
//        activitiesListDescription.setText("    Список всех активностей:");
//        activitiesListDescription.setPreferredSize(new Dimension(1, 50));
//        this.add(activitiesListDescription, BorderLayout.NORTH);
//
//
//        JPanel emptyPanelSouth = new JPanel();
//        emptyPanelSouth.setPreferredSize(new Dimension(1, 50));
//        this.add(emptyPanelSouth, BorderLayout.SOUTH);
//
//        JPanel emptyPanelWest = new JPanel();
//        emptyPanelWest.setPreferredSize(new Dimension(10, 1));
//        this.add(emptyPanelWest, BorderLayout.WEST);
//
//        JPanel emptyPanelEast = new JPanel();
//        emptyPanelEast.setPreferredSize(new Dimension(10, 1));
//        this.add(emptyPanelEast, BorderLayout.EAST);
//    }
//
//
//    private JScrollPane createNewActivitiesList() {
//        for (var str : DB.getData()) {
//            dlm.add(0, str);
//        }
//        JList<String> list1 = new JList<String>(dlm);
//        return new JScrollPane(list1);
//    }
//
//    @Override
//    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) {
//        if (curModelState == ModelStates.GET_STATISTIC && prevModelState != ModelStates.GET_STATISTIC) {
//            if (curActivitiesList != null) {
//                this.remove(curActivitiesList);
//            }
//            curActivitiesList = createNewActivitiesList();
//            curActivitiesList.setPreferredSize(
//                    new Dimension(ViewConstants.ACTIVITIES_LIST_WIDTH, ViewConstants.ACTIVITIES_LIST_HEIGHT));
//            this.add(curActivitiesList, BorderLayout.CENTER);
//            this.repaint();
//            this.revalidate();
//        }
//    }
//}

public class ActivitiesPanel extends JPanel implements Observer {

    private final Model modelInstance;
    private final Controller controller;

    private JPanel curRadioPanel = null;
    private ButtonGroup curButtonGroup = null;

    public ActivitiesPanel(Model model, Controller controller) {
        super(new BorderLayout());

        this.setPreferredSize(new Dimension(ViewConstants.MAIN_WINDOW_WIDTH / 3, 1));

        modelInstance = model;
        model.addObserver(this);
        this.controller = controller;

        JPanel emptyPanelSouth = new JPanel();
        emptyPanelSouth.setPreferredSize(new Dimension(1, 50));
        this.add(emptyPanelSouth, BorderLayout.SOUTH);

        JPanel emptyPanelWest = new JPanel();
        emptyPanelWest.setPreferredSize(new Dimension(10, 1));
        this.add(emptyPanelWest, BorderLayout.WEST);

        JPanel emptyPanelEast = new JPanel();
        emptyPanelEast.setPreferredSize(new Dimension(10, 1));
        this.add(emptyPanelEast, BorderLayout.EAST);
    }

    private JPanel createRadioPanel() {
        JPanel newRadioPanel = new JPanel(new FlowLayout());

        newRadioPanel.setBorder(BorderFactory.createTitledBorder("Фиксированные активности:"));
        curButtonGroup = new ButtonGroup();
        for (String nextActivityName : DbService.getActivitiesSet()) {
            JRadioButton radio = new JRadioButton(nextActivityName);
            radio.addItemListener(controller);

            newRadioPanel.add(radio);
            curButtonGroup.add(radio);
        }

        return newRadioPanel;
    }

    @Override
    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) {
        if (curModelState == ModelStates.STATISTIC_WINDOW && prevModelState != ModelStates.STATISTIC_WINDOW) {
            if (curRadioPanel != null) {
                this.remove(curRadioPanel);
            }
            curRadioPanel = createRadioPanel();

            curRadioPanel.setPreferredSize(
                    new Dimension(ViewConstants.ACTIVITIES_LIST_WIDTH, ViewConstants.ACTIVITIES_LIST_HEIGHT));
            this.add(curRadioPanel, BorderLayout.CENTER);
            this.repaint();
            this.revalidate();
        }
    }
}
