package com.idtk.smallchart.interfaces.IChart;

/**
 * Created by Administrator on 2016/6/7.
 */
public interface ICurveChart extends IBarLineCurveChart {

    void setIntensity(float intensity);
    void setPointInRadius(float pointInRadius);
    void setPointOutRadius(float pointOutRadius);
}
