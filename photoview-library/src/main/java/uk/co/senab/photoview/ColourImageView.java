package uk.co.senab.photoview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LogPrinter;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;

import java.util.Stack;


public class ColourImageView extends ImageView {

    public void clearPoints() {
        undopoints.clear();
        redopoints.clear();
    }

    public enum Model {
        FILLCOLOR,
        FILLGRADUALCOLOR,
        PICKCOLOR,
        DRAW_LINE,
    }

    private Bitmap mBitmap;
    private int mBorderColor = -1;

    private Stack<Point> mStacks = new Stack<Point>();
    private Stack<PointBean> undoBeans = new Stack<>();
    private Stack<PointBean> redoBeans = new Stack<>();
    private int mColor = 0xFF00BCD4;
    private int stacksize = 10;
    private Stack<Bitmap> bmstackundo;
    private Stack<Bitmap> bmstackredo;
    private Stack<Point> undopoints;
    private Stack<Point> redopoints;
    private OnRedoUndoListener onRedoUndoListener;
    private AsyncTask loaderTask;
    private AsyncTask undoTask;
    private AsyncTask redoTask;

    private Model model = Model.FILLCOLOR;
    private OnColorPickListener onColorPickListener;

    private OnDrawLineListener onDrawLineListener;

    public ColourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStack();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void createBitMap(Bitmap bt) {
        mBitmap = bt.copy(bt.getConfig(), true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
        }
        return super.onTouchEvent(event);
    }

    public void pickColor(int x, int y) {
        int color = 0;
        boolean status;
        try {
            if (!isBorderColor(mBitmap.getPixel(x, y)) && mBitmap.getPixel(x, y) != Color.TRANSPARENT) {
                color = mBitmap.getPixel(x, y);
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            status = false;
        }
        if (onColorPickListener != null) {
            onColorPickListener.onColorPick(status, color);
        }
    }


    /**
     *填充颜色，并将其添加到stack中
     * @param x
     * @param y
     */
    public void fillColorToSameArea(int x, int y) {
        //there x,y may be many problems such x<0 x>getwidth catch all the exceptions and this touch do nothing!
        try {
            if (mBitmap.getPixel(x, y) != mColor && !isBorderColor(mBitmap.getPixel(x, y)) && mBitmap.getPixel(x, y) != Color.TRANSPARENT) {
                ProgressLoading.show(getContext(), true);
                ProgressLoading.setOndismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (loaderTask != null) {
                            //destroy asyntask
                            loaderTask.cancel(true);
                        }
                    }
                });
                loaderTask = new AsyncTask() {
                    Bitmap bm = mBitmap;

                    @Override
                    protected Object doInBackground(Object[] objects) {

                        //save bm to undo stack
                        try {
//                            pushUndoStack(bm.copy(bm.getConfig(), true)); //添加bitmap到返回栈中
                            int pixel = bm.getPixel((int) objects[0], (int) objects[1]);
                            undoBeans.push(new PointBean(new Point((int) objects[0],(int) objects[1]),pixel,mColor)); //保存到栈列中 点 + 当前点颜色+ 填充点颜色
                            redoBeans.clear();
//                            填充时，添加到返回栈清空重复栈
                            int w = bm.getWidth();
                            int h = bm.getHeight();
                            int[] pixels = new int[w * h];
                            bm.getPixels(pixels, 0, w, 0, 0, w, h);
                            fillColor(pixels, w, h, pixel, mColor, (int) objects[0], (int) objects[1],0);
                            bm.setPixels(pixels, 0, w, 0, 0, w, h);
                            return true;
                        } catch (Exception e) {
                           undoBeans.pop();
                            return false;
                        }
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        ProgressLoading.DismissDialog();
                        setImageDrawable(new BitmapDrawable(getResources(), bm)); //将绘制完的bitmap显示出来
                        if(onRedoUndoListener != null){
                            onRedoUndoListener.onRedoUndo(undoBeans.size(),redoBeans.size());
                        }
                    }
                }.execute(x, y);
            }
        } catch (Exception e) {
            //do nothing.
        }
    }

    private boolean isBorderColor(int color) {
        if (Color.red(color) < 0x10 && Color.green(color) < 0x10 && Color.blue(color) < 0x10) {
            return true;
        } else {
            return false;
        }
    }

    private void pushUndoStack(Bitmap bm) {
        bmstackundo.push(bm);
        bmstackredo.clear();
    }

    /**
     * @param pixels   像素数组
     * @param w        宽度
     * @param h        高度
     * @param pixel    当前点的颜色
     * @param newColor 填充色
     * @param i        横坐标
     * @param j        纵坐标
     */
    private void fillColor(int[] pixels, int w, int h, int pixel, int newColor, int i, int j,int taskId) {
        LogUtils.e("w:"+w+",h:"+h+",pixel:"+pixel+",newColor:"+newColor+",i:"+i+",j:"+j);
        int orginalX = i;
        int orginalY = j;
        mStacks.clear(); //清空原有的点栈
        mStacks.push(new Point(i, j));
        while (!mStacks.isEmpty()) {
            if(taskId == 0){
                if (loaderTask.isCancelled()) {
                    break;
                }
            }
            if(taskId == 1){
                if (undoTask.isCancelled()) {
                    break;
                }
            }
            if(taskId == 2){
                if (redoTask.isCancelled()) {
                    break;
                }
            }
            Point seed = mStacks.pop();  //获取这个栈的point值
            int count = fillLineLeft(pixels, pixel, w, h, newColor, seed.x, seed.y, orginalX, orginalY); //填充左边点亚瑟
            int left = seed.x - count + 1;
            count = fillLineRight(pixels, pixel, w, h, newColor, seed.x + 1, seed.y, orginalX, orginalY);//填充右线颜色
            int right = seed.x + count;

            if (seed.y - 1 >= 0)
                findSeedInNewLine(pixels, pixel, w, h, seed.y - 1, left, right);
            if (seed.y + 1 < h)
                findSeedInNewLine(pixels, pixel, w, h, seed.y + 1, left, right);
        }


    }

    /**
     * @param pixels
     * @param pixel
     * @param w
     * @param h
     * @param i
     * @param left
     * @param right
     */
    private void findSeedInNewLine(int[] pixels, int pixel, int w, int h, int i, int left, int right) {
        int begin = i * w + left;
        int end = i * w + right;
        boolean hasSeed = false;
        int rx = -1, ry = -1;
        ry = i;
        while (end >= begin) {

            if (needFillPixel(pixels, pixel, end)) {
                if (!hasSeed) {
                    rx = end % w;
                    mStacks.push(new Point(rx, ry));
                    hasSeed = true;
                }
            } else {
                hasSeed = false;
            }
            end--;
        }
    }


    /**
     * 填充线左边
     * @param pixels  像素数组
     * @param pixel  当前填充颜色
     * @param w 宽度
     * @param h  高度
     * @param newColor 新填充色
     * @param x  点的x坐标
     * @param y  点的y坐标
     * @param orginalX  原点的x坐标
     * @param orginalY 原点的y坐标
     * @return
     */
    private int fillLineLeft(int[] pixels, int pixel, int w, int h, int newColor, int x, int y, int orginalX, int orginalY) {
        int count = 0;
        while (x >= 0) {
            int index = y * w + x;
            if (needFillPixel(pixels, pixel, index)) {
                if (model == Model.FILLCOLOR) {
                    pixels[index] = newColor;
                } else if (model == Model.FILLGRADUALCOLOR) {
                    float[] colorHSV = new float[]{0f, 0f, 1f};
                    Color.colorToHSV(newColor, colorHSV); //color转化为 HSV
                    LogUtils.e(colorHSV);
                    float dis = (float) Math.sqrt((x - orginalX) * (x - orginalX) + (y - orginalY) * (y - orginalY));
                    colorHSV[1] = (colorHSV[1] - dis * 0.006) < 0.2 ? 0.2f : (colorHSV[1] - dis * 0.006f);
                    pixels[index] = Color.HSVToColor(colorHSV);//HSV转化为 color
                }
                count++;
                x--;
            } else {
                break;
            }

        }
        return count;
    }

    private int fillLineRight(int[] pixels, int pixel, int w, int h, int newColor, int x, int y, int orginalX, int orginalY) {
        int count = 0;

        while (x < w) {
            int index = y * w + x;
            if (needFillPixel(pixels, pixel, index)) {
                if (model == Model.FILLCOLOR) {
                    pixels[index] = newColor;
                } else if (model == Model.FILLGRADUALCOLOR) {
                    float[] colorHSV = new float[]{0f, 0f, 1f};
                    Color.colorToHSV(newColor, colorHSV);
                    float dis = (float) Math.sqrt((x - orginalX) * (x - orginalX) + (y - orginalY) * (y - orginalY));
                    colorHSV[1] = (colorHSV[1] - dis * 0.006) < 0.2 ? 0.2f : (colorHSV[1] - dis * 0.006f);
                    pixels[index] = Color.HSVToColor(colorHSV);
                }
                count++;
                x++;
            } else {
                break;
            }

        }
        return count;
    }

    /**
     * 是否需要填充颜色
     * @param pixels
     * @param pixel
     * @param index
     * @return
     */
    private boolean needFillPixel(int[] pixels, int pixel, int index) {
        if (model == Model.FILLGRADUALCOLOR) {
            return pixels[index] == pixel;
        } else
            return pixels[index] == pixel;
    }

    public void setImageBT(Bitmap bm) {
        pushUndoStack(mBitmap.copy(mBitmap.getConfig(), true));
        mBitmap = bm.copy(bm.getConfig(), true);
        setImageDrawable(new BitmapDrawable(getResources(), mBitmap));
        if (onRedoUndoListener != null) {
            onRedoUndoListener.onRedoUndo(bmstackundo.size(), bmstackredo.size());
        }
    }

    public void update() {
        setMeasuredDimension(getMeasuredWidth(),
                getDrawable().getIntrinsicHeight() * getMeasuredWidth() / getDrawable().getIntrinsicWidth());
    }

    /**
     * 设置颜色
     * @param color
     */
    public void setColor(int color) {
        mColor = color;
    }

    /**撤销功能
     * @return true: has element can undo;
     */
    public boolean undo() {
        try {
            if(undoBeans.peek() != null){
                PointBean bean =  undoBeans.pop();
                redoBeans.push(bean); //将undoBeans的第一个栈添加到重复栈中
                try {
                        ProgressLoading.show(getContext(), true);
                        ProgressLoading.setOndismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (undoTask != null) {
                                    //destroy asyntask
                                    undoTask.cancel(true);
                                }
                            }
                        });
                    undoTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {

                                //save bm to undo stack
                                try {
//                            pushUndoStack(bm.copy(bm.getConfig(), true)); //添加bitmap到返回栈中
                                    PointBean bean1 =  (PointBean) objects[0];
//                            填充时，添加到返回栈清空重复栈
                                    int w = mBitmap.getWidth();
                                    int h = mBitmap.getHeight();
                                    int[] pixels = new int[w * h];
                                    mBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
                                    fillColor(pixels, w, h, bean1.getNewColor(), bean1.getOldColor(), bean1.getPoint().x, bean1.getPoint().y,1);
                                    mBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
                                    return true;
                                } catch (Exception e) {
                                    return false;
                                }
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                ProgressLoading.DismissDialog();
                                setImageDrawable(new BitmapDrawable(getResources(), mBitmap)); //将绘制完的bitmap显示出来
                            }
                        }.execute(bean);
                } catch (Exception e) {
                    //do nothing.
                }
                if (onRedoUndoListener != null) {  //获得撤销的大小，这样查看是否还能撤销
                    onRedoUndoListener.onRedoUndo(undoBeans.size(), redoBeans.size());
                }
                return !undoBeans.empty();
            }

//            if (bmstackundo.peek() != null) {  //返回栈顶对象
//                bmstackredo.push(mBitmap.copy(mBitmap.getConfig(), true)); //添加到重复栈中
//                mBitmap = bmstackundo.pop();  //撤销栈顶，返回这个对象
//                setImageDrawable(new BitmapDrawable(getResources(), mBitmap)); //设置图片资源
//                if (onRedoUndoListener != null) {  //获得撤销的大小，这样查看是否还能撤销
//                    onRedoUndoListener.onRedoUndo(bmstackundo.size(), bmstackredo.size());
//                }
//                if (undopoints != null && !undopoints.empty()) { //如果撤销点不是空
//                    redopoints.push(undopoints.pop()); //添加到重复点中
//                }
//                return !bmstackundo.empty();
//            }
        } catch (Exception e) {

        }
        return false;
    }

    /**重复功能
     * @return true: has element ,can redo;
     */
    public boolean redo() {
        try {
            if(redoBeans.peek() != null){
                PointBean redo = redoBeans.pop();
                undoBeans.push(redo);
                try {
                    ProgressLoading.show(getContext(), true);
                    ProgressLoading.setOndismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (redoTask != null) {
                                //destroy asyntask
                                redoTask.cancel(true);
                            }
                        }
                    });
                    redoTask = new AsyncTask() {
                        Bitmap bm = mBitmap;
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            //save bm to undo stack
                            try {
//                            pushUndoStack(bm.copy(bm.getConfig(), true)); //添加bitmap到返回栈中
                                PointBean bean1 =  (PointBean) objects[0];
//                            填充时，添加到返回栈清空重复栈
                                int w = bm.getWidth();
                                int h = bm.getHeight();
                                int[] pixels = new int[w * h];
                                bm.getPixels(pixels, 0, w, 0, 0, w, h);
                                fillColor(pixels, w, h, bean1.getOldColor(), bean1.getNewColor(), bean1.getPoint().x, bean1.getPoint().y,2);
                                bm.setPixels(pixels, 0, w, 0, 0, w, h);
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            ProgressLoading.DismissDialog();
                            setImageDrawable(new BitmapDrawable(getResources(), bm)); //将绘制完的bitmap显示出来
                        }
                    }.execute(redo);
                } catch (Exception e) {
                    //do nothing.
                    LogUtils.e("捕获异常");
                }
                if (onRedoUndoListener != null) {
                    onRedoUndoListener.onRedoUndo(undoBeans.size(), redoBeans.size()); //数量变换
                }
                return !redoBeans.empty();
            }
//            if (bmstackredo.peek() != null) {   //返回栈顶对象
//                bmstackundo.push(mBitmap.copy(mBitmap.getConfig(), true)); //增加一个撤销对象到栈顶
//                mBitmap = bmstackredo.pop();  //获取重复栈顶数据
//                setImageDrawable(new BitmapDrawable(getResources(), mBitmap));
//                if (onRedoUndoListener != null) {
//                    onRedoUndoListener.onRedoUndo(bmstackundo.size(), bmstackredo.size()); //数量变换
//                }
//                if (redopoints != null && !redopoints.empty()) {
//                    undopoints.push(redopoints.pop()); //将重复点添加到撤销点中
//                }
//                return !bmstackredo.empty();
//            }
        } catch (Exception e) {

        }
        return false;
    }

    public OnRedoUndoListener getOnRedoUndoListener() {
        return onRedoUndoListener;
    }

    public void setOnRedoUndoListener(OnRedoUndoListener onRedoUndoListener) {
        this.onRedoUndoListener = onRedoUndoListener;
    }

    //clear stack and the current image
    public void clearStack() {
        bmstackredo.clear();
        bmstackundo.clear();
        onRedoUndoListener.onRedoUndo(bmstackundo.size(), bmstackredo.size());
        mBitmap = null;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setOnColorPickListener(OnColorPickListener onColorPickListener) {
        this.onColorPickListener = onColorPickListener;
    }

    public interface OnRedoUndoListener {
        void onRedoUndo(int undoSize, int redoSize);
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    private void initStack() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Cache", Context.MODE_PRIVATE);
        stacksize = sharedPreferences.getInt("stack_max_size", 10);
        bmstackundo = new SizedStack<>(stacksize);
        bmstackredo = new SizedStack<>(stacksize);
        undopoints = new Stack<>();
        redopoints = new Stack<>();
    }

    public interface OnColorPickListener {
        void onColorPick(boolean status, int color);
    }

    /**
     * call only for activity destroyed
     */
    public void onRecycleBitmaps() {
        while (bmstackundo != null && !bmstackundo.empty()) {
            bmstackundo.pop().recycle();
            bmstackundo.clear();
        }

        while (bmstackredo != null && !bmstackredo.empty()) {
            bmstackredo.pop().recycle();
            bmstackredo.clear();
        }
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    public void drawLine(int x, int y) {
        if (undopoints != null && !undopoints.empty()) {
            drawBlackLine(undopoints.peek().x, undopoints.peek().y, x, y);
            undopoints.push(new Point(x, y));
            if (onDrawLineListener != null)
                onDrawLineListener.OnGivenNextPointListener(x, y);
        } else {
            undopoints.push(new Point(x, y));
            if (onDrawLineListener != null)
                onDrawLineListener.OnGivenFirstPointListener(x, y);
        }
    }

    private void drawBlackLine(int startX, int startY, int endX, int endY) {
        try {
            Log.e("draw", startX + "," + startY + "," + endX + "," + endY);
            Bitmap bm = mBitmap;
            //format points
            startX = startX >= bm.getWidth() ? bm.getWidth() - 1 : startX;
            startX = startX < 0 ? 0 : startX;
            startY = startY >= bm.getHeight() ? bm.getHeight() - 1 : startY;
            startY = startY < 0 ? 0 : startY;
            endX = endX >= bm.getWidth() ? bm.getWidth() - 1 : endX;
            endX = endX < 0 ? 0 : endX;
            endY = endY >= bm.getHeight() ? bm.getHeight() - 1 : endY;
            endY = endY < 0 ? 0 : endY;
            //test points
            bm.getPixel(endX, endY);
            bm.getPixel(startX, startY);
            pushUndoStack(bm.copy(bm.getConfig(), true));
            doingDrawLine(bm, startX, startY, endX, endY);
            setImageDrawable(new BitmapDrawable(getResources(), bm));
            if (onRedoUndoListener != null) {
                onRedoUndoListener.onRedoUndo(bmstackundo.size(), bmstackredo.size());
            }
            if (onDrawLineListener != null)
                onDrawLineListener.OnDrawFinishedListener(true, startX, startY, endX, endY);
        } catch (Exception e) {
            Log.e("drawline", e.toString());
            bmstackundo.pop();
            if (onDrawLineListener != null)
                onDrawLineListener.OnDrawFinishedListener(false, startX, startY, endX, endY);
        }

    }

    private void doingDrawLine(Bitmap bm, int startX, int startY, int endX, int endY) {
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setColor(0xFF000000);
        paint.setStrokeWidth(2);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
    public void setOnDrawLineListener(OnDrawLineListener onDrawLineListener) {
        this.onDrawLineListener = onDrawLineListener;
    }
}