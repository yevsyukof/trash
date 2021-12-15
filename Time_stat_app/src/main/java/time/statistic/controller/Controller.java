package time.statistic.controller;

import time.statistic.db.DateService;
import time.statistic.db.DbService;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;

public class Controller implements ActionListener, ItemListener {
    // должен следить за пользовательским "вводом в интерфейс"

    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Зафиксировать активность" -> {
                model.setModelState(ModelStates.START_WINDOW);
            }

            case "Посмотреть статистику" -> model.setModelState(ModelStates.STATISTIC_WINDOW);

            case "Start" -> {
                String input = JOptionPane.showInputDialog("Введите название активности");
                System.out.println("Активность = " + input);

                if (input != null && !input.isEmpty()) {
                    model.setCurRecordingActivity(input);
                } else if (input != null){
                    JOptionPane.showMessageDialog(null, "Вы не ввели название активности!");
                }
            }

            case "Stop" -> {
                DbService.insertEntry(model.getCurRecordingActivity(),
                        model.getTimeLongitude(), DateService.formatDate(new Date()));
            }

            case "<html><span style=\"font-weight:normal\">Выбор" -> {
                if (model.getCurInterestingActivity() != null &&
                        model.getInterestingInterval()[0] != null && model.getInterestingInterval()[1] != null) {
                    model.setModelState(ModelStates.LOOK_AT_STATISTIC);
                } else {
                    JOptionPane.showMessageDialog(null, "Вы не выбрали активность",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
            // TODO сделать выборку
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JRadioButton changedButton = (JRadioButton)e.getItem();

        if (changedButton.isSelected()) {
            model.setCurInterestingActivity(changedButton.getText());
        }
    }

}
