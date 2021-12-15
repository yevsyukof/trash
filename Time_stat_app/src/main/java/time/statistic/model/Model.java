package time.statistic.model;

import time.statistic.utils.Observable;
import time.statistic.utils.Observer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Model implements Observable {

    private final List<Observer> observers;

    private ModelStates curModelState;
    private ModelStates prevModelState = null;

    private String curInterestingActivity = null;
    private String interestingStart = null;
    private String interestingEnd = null;

    public void setCurInterestingActivity(String newActivity) {
        curInterestingActivity = newActivity;
    }

    public String getCurInterestingActivity() {
        return curInterestingActivity;
    }

    public void setInterestingInterval(String start, String end) {
        interestingStart = start;
        interestingEnd = end;
    }

    public String[] getInterestingInterval() {
        return new String[]{interestingStart, interestingEnd};
    }

    private boolean isRecording = false;
    private String curRecordingActivity = null;
    private Date startTime = null;


    public Model() {
        observers = new ArrayList<>();
    }

    public void initNewModel() {
        isRecording = false;
        curModelState = ModelStates.START_WINDOW;
        prevModelState = null;
        notifyObservers();
    }

    public boolean isRecordingStat(){
        return isRecording;
    }

    public void setCurRecordingActivity(String activity) {
        curRecordingActivity = activity;
        isRecording = true;
        startTime = new Date();

        setModelState(ModelStates.START_WINDOW);
    }

    public String getCurRecordingActivity() { // будем использовать в конце фиксирования
        isRecording = false;
        String tmp = curRecordingActivity;
        curRecordingActivity = null;

        setModelState(ModelStates.START_WINDOW);
        return tmp;
    }

    public long getTimeLongitude() {
        return ((new Date().getTime()) - startTime.getTime()) / 1000;
    }

    public void setModelState(ModelStates newModelState) {
        prevModelState = curModelState;
        curModelState = newModelState;
        notifyObservers();
    }

    public ModelStates getCurModelState() {
        return curModelState;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.handleEvent(prevModelState, curModelState);
        }
    }
}
