package com.idtk.smallchart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.data.PointData;
import com.idtk.smallchart.data.XAxisData;
import com.idtk.smallchart.data.YAxisData;

import java.util.ArrayList;

/**
 * Created by Idtk on 2016/6/6.
 * Blog : http://www.idtkm.com
 * GitHub : https://github.com/Idtk
 * 描述 ; 曲线图渲染类
 */
public class CurveChartRender extends ChartRender<CurveData> {

    private CurveData curveData;
    private Path cubicPath = new Path();
    private Path cubicFillPath = new Path();
    private Paint cubicPaint = new Paint();
    private XAxisData xAxisData = new XAxisData();
    private YAxisData yAxisData = new YAxisData();
    private ArrayList<PointF> pointList = new ArrayList<>();
    private PointRender mPointRender = new PointRender();
    private PointData pointData;
    private Paint outpointPaint = new Paint();
    private Paint inPointPaint = new Paint();
    private float intensity;
    private float offset;

    public CurveChartRender(CurveData curveData, XAxisData xAxisData, YAxisData yAxisData, PointData pointData,float offset) {
        super();
        this.curveData = curveData;
        this.xAxisData = xAxisData;
        this.yAxisData = yAxisData;
        this.pointData = pointData;
        this.offset = offset;
        cubicPaint.setStyle(Paint.Style.STROKE);
        cubicPaint.setAntiAlias(true);
        outpointPaint.setStyle(Paint.Style.FILL);
        outpointPaint.setAntiAlias(true);
        inPointPaint.setStyle(Paint.Style.FILL);
        inPointPaint.setAntiAlias(true);
        cubicPaint.setStrokeWidth(curveData.getPaintWidth());
        outpointPaint.setStrokeWidth(cubicPaint.getStrokeWidth());
        inPointPaint.setStrokeWidth(cubicPaint.getStrokeWidth());
        intensity = curveData.getIntensity();
    }

    @Override
    public void drawGraph(Canvas canvas, float animatedValue) {
        float prevDx = 0f;
        float prevDy = 0f;
        float curDx = 0f;
        float curDy = 0f;

        PointF prevPrev = curveData.getValue().get(0);
        PointF prev = prevPrev;
        PointF cur = prev;
        PointF next = curveData.getValue().get(1);

        pointList.clear();
        cubicPath.moveTo((cur.x-xAxisData.getMinimum())*xAxisData.getAxisScale(),
                -(cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale()*animatedValue);
        /**
         * 保存
         */
        pointList.add(new PointF((cur.x-xAxisData.getMinimum())*xAxisData.getAxisScale(),
                -(cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale()*animatedValue));

        for (int j=1; j< curveData.getValue().size(); j++){
            prevPrev = curveData.getValue().get(j == 1 ? 0 : j - 2);
            prev = curveData.getValue().get(j-1);
            cur = curveData.getValue().get(j);
            next = curveData.getValue().size() > j+1 ? curveData.getValue().get(j+1) : cur;

            prevDx = (float) ((cur.x-prevPrev.x)*intensity*xAxisData.getAxisScale());
            prevDy = (float) ((cur.y-prevPrev.y)*intensity*yAxisData.getAxisScale());
            curDx = (float) ((next.x-prev.x)*intensity*xAxisData.getAxisScale());
            curDy = (float) ((next.y-prev.y)*intensity*yAxisData.getAxisScale());

            cubicPath.cubicTo((float) ((prev.x-xAxisData.getMinimum())*xAxisData.getAxisScale()+prevDx),
                    (float) -(((prev.y-yAxisData.getMinimum())*yAxisData.getAxisScale()+prevDy)*animatedValue),
                    (float) ((cur.x-xAxisData.getMinimum())*xAxisData.getAxisScale()-curDx),
                    (float) -(((cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale()-curDy)*animatedValue),
                    (float) ((cur.x-xAxisData.getMinimum())*xAxisData.getAxisScale()),
                    (float) -(((cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale())*animatedValue));
            /**
             * 保存
             */
            pointList.add(new PointF((cur.x-xAxisData.getMinimum())*xAxisData.getAxisScale(),
<<<<<<< HEAD
                    -((cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale())*animatedValue));
=======
                -((cur.y-yAxisData.getMinimum())*yAxisData.getAxisScale())*animatedValue));
>>>>>>> origin/master
        }

        canvas.save();
        canvas.translate(offset,0);
        cubicPaint.setColor(curveData.getColor());
//        cubicPaint.setPathEffect(mPathEffect);
        canvas.drawPath(cubicPath,cubicPaint);
        cubicFillPath.addPath(cubicPath);
        cubicPath.rewind();

        /**
         * 填充曲线图
         */
        if (curveData.getDrawable()!=null){
            //填充颜色
            cubicFillPath.lineTo((float) ((curveData.getValue().get(curveData.getValue().size()-1).x
                    -xAxisData.getMinimum())*xAxisData.getAxisScale()),0);
            cubicFillPath.lineTo((float) ((curveData.getValue().get(0).x-xAxisData.getMinimum())*
                    xAxisData.getAxisScale()),0);
            cubicFillPath.close();

            canvas.save();
            canvas.clipPath(cubicFillPath);
            curveData.getDrawable().setBounds(-canvas.getWidth()+(int) xAxisData.getAxisLength(), -(int) yAxisData.getAxisLength(), (int) xAxisData.getAxisLength(),canvas.getHeight()-(int) yAxisData.getAxisLength());
            curveData.getDrawable().draw(canvas);
            canvas.restore();
            cubicFillPath.rewind();
        }


        /**
         * 点绘制
         */
        outpointPaint.setColor(curveData.getColor());
        inPointPaint.setColor(Color.WHITE);
        pointData.setInPaint(inPointPaint);
        pointData.setOutPaint(outpointPaint);
        if (curveData.isTextSize)
            for (int j=0; j<pointList.size(); j++) {
                mPointRender.drawCirclePoint(canvas, pointList.get(j),pointData,curveData.getTextSize(),curveData.getValue().get(j));
            }
        canvas.restore();
    }
}
