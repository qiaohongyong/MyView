package com.tencent.tga.matcher.myview.ninelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by hyqiao on 2016/12/8.
 */

public class NineLockView extends View {

    private String TAG = "NineLockView";

    /*****************可以合理配置*****************/
    private int r_split_count = 7;//圆的直径是standardSize的1/7
    private int divider_split_count = 4;//圆与圆之间的距离是standardSize的1/4
    private int divider;//圆与圆之间的距离
    private float strokeSize = 5.0f;//画笔的线宽
    private int show_delay = 500;//滑动完毕后再显示500ms
    private String bg_color = "#415C93";//背景颜色
    private String normal_color = "#AEBBEF";//正常圆环颜色
    private String selected_color = "#6C85BB";//圆环选中的颜色
    private String error_color = "#DC9A82";//密码错误的颜色
    /**********************************************/

    private int width,height;//控件的高度和宽度
    private int half_width,half_height;//控件的半高度和半宽度
    private int standardSize;//取控件中高度和宽度小的值为基准
    private int outside_circle_r;//外圆的半径
    private int inside_circle_r;//内圆的半径(outside_circle_r*4/9)

    private float currentPositionX = 0.0f;//滑动的当前位置
    private float currentPositionY = 0.0f;//滑动的当前位置

    private ArrayList<CircleBean> mListCircleBean;//所有的圆圈信息列表
    private ArrayList<CircleBean> mListPosition;//选中的圆圈列表

    private boolean isReadyToSelect = false;//当第一个点落在圆圈的才为true，可以进行选中绘制
    private boolean isShowErrorUI = false;//错误密码UI效果开关

    private onResultListener resultListener;
    public NineLockView(Context context) {
        super(context);
        Log.e(TAG,"NineLockView1");
        init();
    }

    public NineLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG,"NineLockView2");
        init();
    }

    public NineLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG,"NineLockView3");
        init();
    }
    /**
    * 初始化两个列表
    * @author hyqiao
    * @time 2016/12/12 13:13
    */
    private void init(){
        mListCircleBean = new ArrayList<>();
        mListPosition = new ArrayList<>();
    }

    /**
    * 数据为空的时候初始化
    * @author hyqiao
    * @time 2016/12/9 19:38
    */
    private void initData(){
        if(mListCircleBean != null && mListCircleBean.size() == 0){
            mListCircleBean.add(new CircleBean(0,false,half_width-divider,half_height-divider, outside_circle_r));
            mListCircleBean.add(new CircleBean(1,false,half_width,half_height-divider, outside_circle_r));
            mListCircleBean.add(new CircleBean(2,false,half_width+divider,half_height-divider, outside_circle_r));
            mListCircleBean.add(new CircleBean(3,false,half_width-divider,half_height, outside_circle_r));
            mListCircleBean.add(new CircleBean(4,false,half_width,half_height, outside_circle_r));
            mListCircleBean.add(new CircleBean(5,false,half_width+divider,half_height, outside_circle_r));
            mListCircleBean.add(new CircleBean(6,false,half_width-divider,half_height+divider, outside_circle_r));
            mListCircleBean.add(new CircleBean(7,false,half_width,half_height+divider, outside_circle_r));
            mListCircleBean.add(new CircleBean(8,false,half_width+divider,half_height+divider, outside_circle_r));
        }
    }
    /**
    * 滑动操作完成之后数据还原
    * @author hyqiao
    * @time 2016/12/12 13:14
    */
    private void resetData(){
        mListCircleBean.clear();
        initData();
    }

    /**
    * 密码校验后的UI操作,延时消失
    * @author hyqiao
    * @time 2016/12/12 19:29
    */
    public void setFinishUI(boolean isRight){
        if(isRight){
            isShowErrorUI = false;
        }else {
            isShowErrorUI = true;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                isReadyToSelect = false;
                resetData();
                invalidate();
            }
        },show_delay);
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /************基本数据的初始化，可更具具体的UI效果更改***************/
        width = canvas.getWidth();//宽
        height = canvas.getHeight();//高
        half_width = width/2;//半宽
        half_height = height/2;//半高
        standardSize = width<height?width:height;//选择小的作为标准，以免超出范围
        outside_circle_r = standardSize/(r_split_count *2);//大圆环半径
        inside_circle_r = outside_circle_r*4/9;//小圆环半径
        divider = standardSize/divider_split_count;//俩圆心的间隔
        /********************************************************************/

        initData();

        Log.e(TAG,"onDraw  "+width+"  "+height);

        canvas.drawColor(Color.parseColor(bg_color));//绘制背景颜色
        Paint paint_normal = new Paint();
        paint_normal.setColor(Color.parseColor(normal_color));
        paint_normal.setStyle(Paint.Style.STROKE);
        paint_normal.setStrokeWidth(strokeSize);

        //绘制9个圆环
        for(int i = 0; mListCircleBean !=null&&i< mListCircleBean.size(); i++){
            canvas.drawCircle(mListCircleBean.get(i).getX_center(), mListCircleBean.get(i).getY_center(), outside_circle_r,paint_normal);
        }

        //手指滑动中的绘制
        if(isReadyToSelect){
            Paint paint_select = new Paint();
            if(isShowErrorUI){
                paint_select.setColor(Color.parseColor(error_color));//设置密码错误的颜色
            }else {
                paint_select.setColor(Color.parseColor(selected_color));//设置选中的的颜色
            }
            paint_select.setStyle(Paint.Style.STROKE);
            paint_select.setStrokeWidth(strokeSize);

            Paint paint_solid_circle = new Paint();
            if(isShowErrorUI){
                paint_solid_circle.setColor(Color.parseColor(error_color));
            }else {
                paint_solid_circle.setColor(Color.parseColor(selected_color));
            }

            //绘制选中的大小圆环
            for(int i = 0; i<mListPosition.size();i++){
                canvas.drawCircle(mListPosition.get(i).getX_center(), mListPosition.get(i).getY_center(), outside_circle_r,paint_select);
                canvas.drawCircle(mListPosition.get(i).getX_center(), mListPosition.get(i).getY_center(), inside_circle_r,paint_solid_circle);
                if(i>0){
                    canvas.drawLine(mListPosition.get(i-1).getX_center(),mListPosition.get(i-1).getY_center(),mListPosition.get(i).getX_center(),mListPosition.get(i).getY_center(),paint_select);
                }
            }
            //绘制连接的直线
            if(mListPosition.size()>0 && currentPositionX != 0 && currentPositionY != 0){
                int length = mListPosition.size();
                canvas.drawLine(mListPosition.get(length-1).getX_center(),mListPosition.get(length-1).getY_center(),currentPositionX,currentPositionY,paint_select);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.e(TAG,"MotionEvent.ACTION_DOWN");
                isReadyToSelect = false;
                isShowErrorUI = false;
                //判断手指是否放在圆环内
                for(int i = 0; mListCircleBean !=null&&i< mListCircleBean.size(); i++){
                    if(mListCircleBean.get(i).isInCircle(event.getX(),event.getY())){
                        mListPosition.clear();
                        isReadyToSelect = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG,"MotionEvent.ACTION_MOVE");
                //滑动中执行选中的操作
                for(int i = 0;isReadyToSelect && mListCircleBean !=null && i< mListCircleBean.size(); i++){
                    if(mListCircleBean.get(i).isInCircle(event.getX(),event.getY())){
                        if(!mListCircleBean.get(i).isSelected()){
                            Log.e(TAG,"选中第"+i+"个圆圈");
                            mListCircleBean.get(i).setSelected(true);
                            mListPosition.add(mListCircleBean.get(i));
                            invalidate();
                        }else {
                            currentPositionX = event.getX();
                            currentPositionY = event.getY();
                            invalidate();
                        }
                    }else{
                        currentPositionX = event.getX();
                        currentPositionY = event.getY();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //Log.e(TAG,"MotionEvent.ACTION_UP");
                //滑动完成判断密码正确与否
                if(isReadyToSelect){
                    currentPositionX = 0;
                    currentPositionY = 0;

                    invalidate();

                    String result = "";
                    for(int i = 0;i<mListPosition.size();i++){
                        result = result + mListPosition.get(i).getPosition();
                    }
                    if(resultListener!=null){
                        resultListener.setResult(result);
                    }

//                    String result = "";
//                    for(int i = 0;i<mListPosition.size();i++){
//                        result = result + mListPosition.get(i).getPosition();
//                    }
//                    if(result.equals(mSerect)){
//                        Log.e(TAG,"密码正确 : "+result);
//                        isShowErrorUI = false;
//                    }else {
//                        Log.e(TAG,"密码不正确 : "+result);
//                        isShowErrorUI = true;
//                    }
//                    if(resultListener!=null){
//                        resultListener.setResult(result,!isShowErrorUI);
//                    }
//
//                    postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isReadyToSelect = false;
//                            resetData();
//                            invalidate();
//                        }
//                    },show_delay);
//
//                    invalidate();
                }
                break;
        }
        return true;
    }

    public void setOnResultListener(onResultListener l){
        this.resultListener = l;
    }
    public interface onResultListener{
        void setResult(String result);
    }
}
