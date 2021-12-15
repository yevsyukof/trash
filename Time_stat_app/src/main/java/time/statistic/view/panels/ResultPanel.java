package time.statistic.view.panels;

import time.statistic.controller.Controller;
import time.statistic.db.DbService;
import time.statistic.model.Model;
import time.statistic.model.ModelStates;
import time.statistic.utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel implements Observer {

    private final String offset = "     ";
    private final String str1 = offset + "Название активности: ";
    private final String str2 = offset + "Продолжительность за промежуток: ";

    private JLabel activityName;
    private JLabel activityLongitude;

    private JLabel stat;

    private final Model model;

    public ResultPanel(Model model) {
        super(new GridLayout(3, 1));

        this.model = model;
        model.addObserver(this);

        activityLongitude = new JLabel();
        activityLongitude.setFont(new Font("Calibri", Font.BOLD, 14));
        activityLongitude.setText(str2);

        activityName = new JLabel();
        activityName.setFont(new Font("Calibri", Font.BOLD, 14));
        activityName.setText(str1);

        stat = new JLabel();
        stat.setFont(new Font("Calibri", Font.BOLD, 14));

        this.add(activityName);
        this.add(activityLongitude);

        this.add(stat);
    }

    @Override
    public void handleEvent(ModelStates prevModelState, ModelStates curModelState) {
//        if (curModelState == ModelStates.STATISTIC_WINDOW && prevModelState != ModelStates.START_WINDOW) {
//            this.add(stat);
//            this.repaint();
//            this.revalidate();
//        }

        if (curModelState == ModelStates.LOOK_AT_STATISTIC) {
//            if (prevModelState != ModelStates.STATISTIC_WINDOW) {
//                activityName.setText(str1);
//                this.remove(stat);
//                return;
//            }

            String [] interestingInterval = model.getInterestingInterval();
            model.setInterestingInterval(null, null);

            String interestingActivity = model.getCurInterestingActivity();
            model.setCurInterestingActivity(null);
            activityName.setText(str1 + interestingActivity);

            List<Long> statSet = DbService.getData(interestingActivity,
                    interestingInterval[0], interestingInterval[1]);
            long result = 0;
            for (var a : statSet) {
                result += a;
            }
            long days = result / 60 / 60 / 24;
            result -= days * 24 * 60 * 60;
            long hours = result / 60 / 60;
            long minutes = result / 60 % 60;
            long seconds = result % 60;

            stat.setText(offset + "days: " + days + ", hrs: " + hours + ", min: " + minutes + ", sec: " + seconds);
        }
    }
}
