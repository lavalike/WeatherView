package com.wangzhen.weatherview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wangzhen.weatherview.R;
import com.wangzhen.weatherview.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 天气折线图
 * Created by wangzhen on 2018/4/18.
 */
public class WeatherView extends View {
    private static final int COLOR_WHITE_ALPHA = Color.parseColor("#4dffffff");
    private static final int COLOR_WHITE = Color.parseColor("#ffffff");
    private int minHeight = dip2px(150);
    private int minWidth = dip2px(150);
    private int lineDiagramHeight = dip2px(150);

    //折线图宽
    private int mWidth;
    //折线图高
    private int mHeight;
    //竖分割线
    private Paint mPaintVerticalLine;
    //各天天气宽度
    private int perWidth;
    private Paint mPaintWeek;
    //新一行Y坐标值
    private int currentAxisY;

    private List<String> listWeeks = new ArrayList<>();
    private List<String> listDayWeathers = new ArrayList<>();
    private List<Bitmap> listDayIcons = new ArrayList<>();
    private List<Integer> listDayTemperature = new ArrayList<>();
    private List<Integer> listNightTemperature = new ArrayList<>();
    private List<Bitmap> listNightIcons = new ArrayList<>();
    private List<String> listNightWeathers = new ArrayList<>();
    private List<String> listDate = new ArrayList<>();
    private List<String> listWind = new ArrayList<>();
    private List<String> listWindLevel = new ArrayList<>();

    private int maxDayTemperature;//白间最高温度
    private int minNightTemperature;//夜间最低湿度
    private float dotRadius = dip2px(2);//温度圆点直径
    private Paint mPaintPath;


    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initData();
    }

    /**
     * 临时假数据，需要跟真实数据对接
     */
    private void initData() {
        //添加星期几数据 星期六
        Date currentDate = new Date(System.currentTimeMillis());
        listWeeks.add("今天");
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));

        //添加日期数据 04/20
        currentDate = new Date(System.currentTimeMillis());
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));

        //添加天气
        listDayWeathers.add("多云");
        listDayWeathers.add("多云");
        listDayWeathers.add("多云");
        listDayWeathers.add("小雨");
        listDayWeathers.add("多云");

        //添加白天天气图标
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_small));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));

        //添加夜间天气图标
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_small));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));

        //添加夜晚天气
        listNightWeathers.add("多云");
        listNightWeathers.add("多云");
        listNightWeathers.add("小雨");
        listNightWeathers.add("阴");
        listNightWeathers.add("多云");

        //添加风向
        listWind.add("南风");
        listWind.add("东南风");
        listWind.add("南风");
        listWind.add("北风");
        listWind.add("北风");

        //添加风力等级
        listWindLevel.add("3~4级");
        listWindLevel.add("4~5级");
        listWindLevel.add("4~5级");
        listWindLevel.add("微风");
        listWindLevel.add("微风");

        //添加白天最高温
        listDayTemperature.add(27);
        listDayTemperature.add(26);
        listDayTemperature.add(27);
        listDayTemperature.add(20);
        listDayTemperature.add(21);

        //添加夜间最低温
        listNightTemperature.add(14);
        listNightTemperature.add(12);
        listNightTemperature.add(13);
        listNightTemperature.add(13);
        listNightTemperature.add(13);

        //查询近几天白天最高温度
        maxDayTemperature = listDayTemperature.get(0);
        for (int i = 0; i < listDayTemperature.size(); i++) {
            int max = listDayTemperature.get(i);
            if (max > maxDayTemperature) {
                maxDayTemperature = max;
            }
        }

        //查询近几天夜间最低温度
        minNightTemperature = listNightTemperature.get(0);
        for (int i = 0; i < listNightTemperature.size(); i++) {
            int min = listNightTemperature.get(i);
            if (min < minNightTemperature) {
                minNightTemperature = min;
            }
        }
    }

    private void initPaint() {
        mPaintVerticalLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintVerticalLine.setStyle(Paint.Style.FILL);
        mPaintVerticalLine.setStrokeWidth(dip2px(0.5f));
        mPaintVerticalLine.setColor(COLOR_WHITE_ALPHA);

        mPaintWeek = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWeek.setTextSize(sp2px(16f));
        mPaintWeek.setColor(COLOR_WHITE);

        mPaintPath = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPath.setStrokeWidth(dip2px(1));
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setColor(COLOR_WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawVerticalLines(canvas);
        drawWeeks(canvas);
        drawDayWeather(canvas);
        drawDayIcons(canvas);
        drawLineDiagram(canvas);
        drawNightIcons(canvas);
        drawNightWeather(canvas);
        drawDate(canvas);
        drawWind(canvas);
        drawWindLevel(canvas);
    }

    private void drawLineDiagram(Canvas canvas) {
        int totalDiff = maxDayTemperature - minNightTemperature;
        if (totalDiff == 0) return;
        Path path = new Path();
        Paint.FontMetrics fontMetrics = mPaintWeek.getFontMetrics();
        //绘制高温折线
        for (int i = 0; i < listDayTemperature.size(); i++) {
            int centerX = perWidth * i + perWidth / 2;
            int centerY = currentAxisY;
            int temperature = listDayTemperature.get(i);
            //计算当前温度与最高温差值占最高温与最低温差值的比例
            float ratio = (Math.abs(maxDayTemperature - temperature)) * 1f / totalDiff;
            centerY = (int) (centerY + (ratio * lineDiagramHeight));
            float textWidth = mPaintWeek.measureText(String.valueOf(temperature));
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            canvas.drawText(String.valueOf(temperature), centerX - textWidth / 2, centerY + textHeight / 2, mPaintWeek);
            canvas.drawCircle(centerX, centerY + textHeight, dotRadius, mPaintWeek);
            centerY += textHeight;
            if (i == 0) {
                path.moveTo(perWidth / 2, centerY);
            } else {
                path.lineTo(centerX, centerY);
            }
            canvas.drawPath(path, mPaintPath);
        }
        //绘制低温折线
        path.reset();
        for (int i = 0; i < listNightTemperature.size(); i++) {
            int centerX = perWidth * i + perWidth / 2;
            int centerY = currentAxisY + lineDiagramHeight;
            int temperature = listNightTemperature.get(i);
            //计算当前温度与最低温差值占最高温与最低温差值的比例
            float ratio = (Math.abs(temperature - minNightTemperature)) * 1f / totalDiff;
            centerY = (int) (centerY - (ratio * lineDiagramHeight));
            float textWidth = mPaintWeek.measureText(String.valueOf(temperature));
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            canvas.drawText(String.valueOf(temperature), centerX - textWidth / 2, centerY - textHeight / 2, mPaintWeek);
            canvas.drawCircle(centerX, centerY, dotRadius, mPaintWeek);
            if (i == 0) {
                path.moveTo(perWidth / 2, centerY);
            } else {
                path.lineTo(centerX, centerY);
            }
            canvas.drawPath(path, mPaintPath);
        }
        currentAxisY += lineDiagramHeight;
    }

    private void drawDate(Canvas canvas) {
        if (listDate == null || listDate.isEmpty()) return;
        String date;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listDate.size(); i++) {
            date = listDate.get(i);
            textWidth = mPaintWeek.measureText(date);
            canvas.drawText(date, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawWind(Canvas canvas) {
        if (listWind == null || listWind.isEmpty()) return;
        String wind;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listWind.size(); i++) {
            wind = listWind.get(i);
            textWidth = mPaintWeek.measureText(wind);
            canvas.drawText(wind, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawWindLevel(Canvas canvas) {
        if (listWindLevel == null || listWindLevel.isEmpty()) return;
        String wind;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listWindLevel.size(); i++) {
            wind = listWindLevel.get(i);
            textWidth = mPaintWeek.measureText(wind);
            canvas.drawText(wind, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawDayIcons(Canvas canvas) {
        if (listDayIcons == null || listDayIcons.isEmpty())
            return;
        int width;
        int height = 0;
        Bitmap bitmap;
        for (int i = 0; i < listDayIcons.size(); i++) {
            bitmap = listDayIcons.get(i);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            RectF rectF = new RectF(
                    perWidth * i + perWidth / 2 - width / 2,
                    currentAxisY,
                    perWidth * i + perWidth / 2 + width / 2,
                    currentAxisY + height
            );
            canvas.drawBitmap(bitmap, null, rectF, mPaintWeek);
            bitmap.recycle();
        }
        currentAxisY += height;
    }

    private void drawNightIcons(Canvas canvas) {
        if (listNightIcons == null || listNightIcons.isEmpty())
            return;
        int width;
        int height = 0;
        Bitmap bitmap;
        for (int i = 0; i < listNightIcons.size(); i++) {
            bitmap = listNightIcons.get(i);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            RectF rectF = new RectF(
                    perWidth * i + perWidth / 2 - width / 2,
                    currentAxisY,
                    perWidth * i + perWidth / 2 + width / 2,
                    currentAxisY + height
            );
            canvas.drawBitmap(bitmap, null, rectF, mPaintWeek);
            bitmap.recycle();
        }
        currentAxisY += height;
    }

    private void drawDayWeather(Canvas canvas) {
        if (listDayWeathers == null || listDayWeathers.isEmpty())
            return;
        String weather;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listDayWeathers.size(); i++) {
            weather = listDayWeathers.get(i);
            textWidth = mPaintWeek.measureText(weather);
            canvas.drawText(weather, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawNightWeather(Canvas canvas) {
        if (listNightWeathers == null || listNightWeathers.isEmpty())
            return;
        String weather;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listNightWeathers.size(); i++) {
            weather = listNightWeathers.get(i);
            textWidth = mPaintWeek.measureText(weather);
            canvas.drawText(weather, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawWeeks(Canvas canvas) {
        if (listWeeks == null || listWeeks.isEmpty())
            return;
        String week;
        float textWidth;
        Paint.FontMetricsInt fontMetrics = mPaintWeek.getFontMetricsInt();
        currentAxisY += fontMetrics.bottom - fontMetrics.top;
        for (int i = 0; i < listWeeks.size(); i++) {
            week = listWeeks.get(i);
            textWidth = mPaintWeek.measureText(week);
            canvas.drawText(week, perWidth * i + perWidth / 2 - textWidth / 2, currentAxisY, mPaintWeek);
        }
    }

    private void drawVerticalLines(Canvas canvas) {
        perWidth = mWidth / 5;
        //画4根竖线
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(perWidth * (i + 1), 0, perWidth * (i + 1), mHeight, mPaintVerticalLine);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(minHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = minHeight;
                break;
        }
        return MeasureSpec.makeMeasureSpec(height, heightMode);
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(minWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = minWidth;
                break;
        }
        return MeasureSpec.makeMeasureSpec(width, widthMode);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int sp2px(float spVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics()) + 0.5f);
    }
}