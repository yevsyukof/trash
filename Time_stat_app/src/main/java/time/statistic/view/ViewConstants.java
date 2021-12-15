package time.statistic.view;

import java.awt.*;

public final class ViewConstants {

    public static final int CELLS_FIELD_WIDTH = 10;  // cnt of cells panels
    public static final int CELLS_FIELD_HEIGHT = 20;

    public static final Color EMPTY_CELL_COLOR = Color.white;


    public static final int CELL_SIZE = 30; // cnt of pixels in cell
    public static final int VIEW_FIELD_WIDTH = CELLS_FIELD_WIDTH * CELL_SIZE + CELLS_FIELD_WIDTH - 1;
    public static final int VIEW_FIELD_HEIGHT = CELLS_FIELD_HEIGHT * CELL_SIZE + CELLS_FIELD_HEIGHT - 1;

    public static final int MENU_BAR_WIDTH = VIEW_FIELD_WIDTH; // не имеет смысла, т.к. растягивается
    public static final int MENU_BAR_HEIGHT = 40;

    public static final int MAIN_WINDOW_WIDTH = VIEW_FIELD_WIDTH * 2; // cnt of pixels
    public static final int MAIN_WINDOW_HEIGHT = VIEW_FIELD_HEIGHT + MENU_BAR_HEIGHT;

    public static final int ACTIVITIES_LIST_WIDTH = 200;
    public static final int ACTIVITIES_LIST_HEIGHT = 350;
}
