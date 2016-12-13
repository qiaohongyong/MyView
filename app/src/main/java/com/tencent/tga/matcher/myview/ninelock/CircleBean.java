package com.tencent.tga.matcher.myview.ninelock;

/**
 * Created by hyqiao on 2016/12/9.
 */

public class CircleBean {
    private int position;
    private boolean isSelected;

    private int x_center;
    private int y_center;

    private int r_circle;

    public CircleBean(int position, boolean isSelected, int x_center, int y_center, int r_circle) {
        this.position = position;
        this.isSelected = isSelected;
        this.x_center = x_center;
        this.y_center = y_center;
        this.r_circle = r_circle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getR_circle() {
        return r_circle;
    }

    public void setR_circle(int r_circle) {
        this.r_circle = r_circle;
    }

    public int getX_center() {
        return x_center;
    }

    public void setX_center(int x_center) {
        this.x_center = x_center;
    }

    public int getY_center() {
        return y_center;
    }

    public void setY_center(int y_center) {
        this.y_center = y_center;
    }

    public boolean isInCircle(float x,float y){
        if(Math.sqrt((x-x_center)*(x-x_center)+(y-y_center)*(y-y_center))<r_circle){
            return true;
        }else{
            return false;
        }
    }
}
