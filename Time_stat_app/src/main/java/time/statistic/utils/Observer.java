package time.statistic.utils;

import time.statistic.model.ModelStates;

public interface Observer {

    void handleEvent(ModelStates prevModelState, ModelStates curModelState);
}
