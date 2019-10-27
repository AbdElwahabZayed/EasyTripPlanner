package com.iti.mansoura.tot.easytripplanner.home.history.map;

import java.util.List;



public interface DirectionFinderListener {
    void onDirectionFinderStart();

    void onDirectionFinderSuccess(List<Route> route);
}
