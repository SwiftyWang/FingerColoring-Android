package com.swifty.fillcolor.controller.paint;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.controller.BaseActivity;
import com.swifty.fillcolor.factory.AnimateFactory;
import com.swifty.fillcolor.factory.MyDialogFactory;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.model.SaveImageAsyn;
import com.swifty.fillcolor.util.FileUtils;
import com.swifty.fillcolor.util.ImageSaveUtil;
import com.swifty.fillcolor.util.L;
import com.swifty.fillcolor.util.ShareImageUtil;
import com.swifty.fillcolor.util.UmengUtil;
import com.swifty.fillcolor.view.ColorPicker;
import com.swifty.fillcolor.view.ImageButton_define;
import com.swifty.fillcolor.view.ImageButton_define_secondLay;
import com.swifty.fillcolor.view.ImageCheckBox_define;
import com.swifty.fillcolor.view.MyProgressDialog;
import com.swifty.fillcolor.view.OnCheckedChangeListener;

import java.io.File;

import uk.co.senab.photoview.ColourImageView;
import uk.co.senab.photoview.OnDrawLineListener;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PaintActivity extends BaseActivity implements View.OnClickListener {
    ColourImageView colourImageView;
    PhotoViewAttacher mAttacher;
    TableLayout tableLayout;
    ImageView currentColor, cColor1, cColor2, cColor3, cColor4;
    ColorPicker colorPickerSeekBar, largecolorpicker;
    ImageView advanceColor;
    ImageButton_define save, share, open, more, delete;
    ImageButton_define_secondLay undo, redo;
    ImageCheckBox_define pick, drawLine, jianbian_color;
    String URL;
    int PAINTNAME;
    LinearLayout advanceLay, largecolorpickerlay;
    private Bitmap cachedBitmap;
    private int isShowing = 2;
    private boolean fromSDcard = false;
    MyDialogFactory myDialogFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityUtil.hideStatusBar(this);
        setContentView(R.layout.activity_paint);
        initViews();
        addEvents();
        if (getIntent().hasExtra(MyApplication.BIGPIC)) {
            fromSDcard = false;
            URL = getIntent().getExtras().getString(MyApplication.BIGPIC);
            loadLargeImage();
        } else if (getIntent().hasExtra(MyApplication.BIGPICFROMUSER)) {
            fromSDcard = true;
            URL = getIntent().getExtras().getString(MyApplication.BIGPICFROMUSER);
            PAINTNAME = getIntent().getExtras().getInt(MyApplication.BIGPICFROMUSERPAINTNAME);
            loadLargeImageFromSDcard();
        } else {
            finish();
        }
    }

    private void loadLargeImageFromSDcard() {
        MyProgressDialog.show(this, null, getString(R.string.loadpicture));
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                AsynImageLoader.showLagreImageAsynWithNoCacheOpen(colourImageView, URL, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        MyProgressDialog.DismissDialog();
                        Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mAttacher = new PhotoViewAttacher(colourImageView, bitmap);
                        //rotate screen load bitmap saved in the savestates
                        if (cachedBitmap != null) {
                            colourImageView.setImageBT(cachedBitmap);
                        }
                        MyProgressDialog.DismissDialog();
                        //show advancelay
                        advanceLaytoggle();

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        MyProgressDialog.DismissDialog();
                        Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }.execute();
    }

    private void loadLargeImage() {
        MyProgressDialog.show(this, null, getString(R.string.loadpicture));
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                AsynImageLoader.showLagreImageAsynWithAllCacheOpen(colourImageView, URL, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        MyProgressDialog.DismissDialog();
                        Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        mAttacher = new PhotoViewAttacher(colourImageView, bitmap);
                        //rotate screen load bitmap saved in the savestates
                        if (cachedBitmap != null) {
                            colourImageView.setImageBT(cachedBitmap);
                        } else {
                            openSaveImage(ImageSaveUtil.convertImageLageUrl(URL).hashCode());
                            showHintDialog();
                        }
                        MyProgressDialog.DismissDialog();
                        //show advancelay
                        advanceLaytoggle();

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        MyProgressDialog.DismissDialog();
                        Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }.execute();
    }

    private void showHintDialog() {
        myDialogFactory.showPaintHintDialog();
    }

    private void initViews() {
        myDialogFactory = new MyDialogFactory(this);
        advanceLay = (LinearLayout) findViewById(R.id.topfirstlay);
        colourImageView = (ColourImageView) findViewById(R.id.fillImageview);
        cColor1 = (ImageView) findViewById(R.id.current_pen1);
        cColor2 = (ImageView) findViewById(R.id.current_pen2);
        cColor3 = (ImageView) findViewById(R.id.current_pen3);
        cColor4 = (ImageView) findViewById(R.id.current_pen4);
        tableLayout = (TableLayout) findViewById(R.id.colortable);
        colorPickerSeekBar = (ColorPicker) findViewById(R.id.seekcolorpicker);
        largecolorpicker = (ColorPicker) findViewById(R.id.largepicker);
        largecolorpickerlay = (LinearLayout) findViewById(R.id.largepickerlay);
        advanceColor = (ImageView) findViewById(R.id.advance_color);
        undo = (ImageButton_define_secondLay) findViewById(R.id.undo);
        redo = (ImageButton_define_secondLay) findViewById(R.id.redo);
        save = (ImageButton_define) findViewById(R.id.save);
        open = (ImageButton_define) findViewById(R.id.open);
        share = (ImageButton_define) findViewById(R.id.share);
        more = (ImageButton_define) findViewById(R.id.more);
        delete = (ImageButton_define) findViewById(R.id.delete);
        pick = (ImageCheckBox_define) findViewById(R.id.pickcolor);
        drawLine = (ImageCheckBox_define) findViewById(R.id.drawline);
        jianbian_color = (ImageCheckBox_define) findViewById(R.id.jianbian_color);
    }

    private void addEvents() {
        advanceColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advanceLaytoggle();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colourImageView.undo();
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colourImageView.redo();
            }
        });
        currentColor = cColor1;
        getSavedColors(cColor1, cColor2, cColor3, cColor4);
        cColor1.setOnClickListener(checkCurrentColor);
        cColor2.setOnClickListener(checkCurrentColor);
        cColor3.setOnClickListener(checkCurrentColor);
        cColor4.setOnClickListener(checkCurrentColor);
        changeCurrentColor(currentColor);
        colorPickerSeekBar.setOnChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void colorChangedListener(int color) {
                changeCurrentColor(color);
            }
        });
        colorPickerSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        largecolorpickerlay.setVisibility(View.VISIBLE);
                        largecolorpickerlay.startAnimation(AnimateFactory.getInstance().popupAnimation(PaintActivity.this));
                    case MotionEvent.ACTION_MOVE:
                        largecolorpicker.setColor(colorPickerSeekBar.getColor());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        largecolorpickerlay.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        colorPickerSeekBar.setColor(getResources().getColor(R.color.maincolor));
        colourImageView.setOnRedoUndoListener(new ColourImageView.OnRedoUndoListener() {
            @Override
            public void onRedoUndo(int undoSize, int redoSize) {
                if (undoSize != 0) {
                    undo.setEnabled(true);
                    undo.setImageSrc(R.drawable.blueundo);
                } else {
                    undo.setEnabled(false);
                    undo.setImageSrc(R.drawable.greyundo);
                }
                if (redoSize != 0) {
                    redo.setEnabled(true);
                    redo.setImageSrc(R.drawable.blueredo);
                } else {
                    redo.setEnabled(false);
                    redo.setImageSrc(R.drawable.greyredo);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToLocal();
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromSDcard) {
                    openSaveImage(PAINTNAME);
                } else {
                    openSaveImage(ImageSaveUtil.convertImageLageUrl(URL).hashCode());
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            for (int j = 0; j < ((TableRow) tableLayout.getChildAt(i)).getChildCount(); j++) {
                if (((TableRow) tableLayout.getChildAt(i)).getChildAt(j) instanceof Button) {
                    ((TableRow) tableLayout.getChildAt(i)).getChildAt(j).setOnClickListener(this);
                }
            }
        }
        pick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View buttonView, boolean isChecked) {
                if (isChecked) {
                    drawLine.setChecked(false);
                    myDialogFactory.showPickColorHintDialog();
                    colourImageView.setModel(ColourImageView.Model.PICKCOLOR);
                    colourImageView.setOnColorPickListener(new ColourImageView.OnColorPickListener() {
                        @Override
                        public void onColorPick(boolean status, int color) {
                            if (status == true) {
                                changeCurrentColor(color);
                                pick.setChecked(false);
                            } else {
                                Toast.makeText(PaintActivity.this, getString(R.string.pickcolorerror), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    backToColorModel();
                }
            }
        });
        drawLine.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View buttonView, boolean isChecked) {
                if (isChecked) {
                    pick.setChecked(false);
                    myDialogFactory.showBuxianButtonClickDialog();
                    colourImageView.setModel(ColourImageView.Model.DRAW_LINE);
                    colourImageView.setOnDrawLineListener(new OnDrawLineListener() {
                        @Override
                        public void OnDrawFinishedListener(boolean drawed, int startX, int startY, int endX, int endY) {
                            if (!drawed) {
                                Toast.makeText(PaintActivity.this, getString(R.string.drawLineHint_finish), Toast.LENGTH_SHORT).show();
                            } else {
                                myDialogFactory.showBuxianNextPointSetDialog();
                            }
                        }

                        @Override
                        public void OnGivenFirstPointListener(int startX, int startY) {
                            myDialogFactory.showBuxianFirstPointSetDialog();
                        }

                        @Override
                        public void OnGivenNextPointListener(int endX, int endY) {

                        }
                    });
                } else {
                    colourImageView.clearPoints();
                    backToColorModel();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogFactory.dismissDialog();
                        repaint();
                    }
                };
                myDialogFactory.showRepaintDialog(listener);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAdvancePaintActivity();
            }
        });

        jianbian_color.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    myDialogFactory.showGradualHintDialog();
                    colourImageView.setModel(ColourImageView.Model.FILLGRADUALCOLOR);
                    jianbian_color.setText(R.string.jianbian_color);
                } else {
                    colourImageView.setModel(ColourImageView.Model.FILLCOLOR);
                    jianbian_color.setText(R.string.normal_color);
                }
            }
        });
    }

    private void backToColorModel() {
        colourImageView.setModel(ColourImageView.Model.FILLCOLOR);
        jianbian_color.setChecked(false);
    }

    View.OnClickListener checkCurrentColor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.current_pen1:
                    //change background
                    view.setBackgroundResource(R.drawable.main_bg);
                    setWhiteRoundBg(R.id.current_pen2, R.id.current_pen3, R.id.current_pen4);
                    //set currentimageview
                    currentColor = (ImageView) view;
                    //change colourImageview color
                    changeCurrentColor((ImageView) view);
                    break;
                case R.id.current_pen2:
                    //change background
                    view.setBackgroundResource(R.drawable.main_bg);
                    setWhiteRoundBg(R.id.current_pen1, R.id.current_pen3, R.id.current_pen4);
                    //set currentimageview
                    currentColor = (ImageView) view;
                    //change colourImageview color
                    changeCurrentColor((ImageView) view);
                    break;
                case R.id.current_pen3:
                    //change background
                    view.setBackgroundResource(R.drawable.main_bg);
                    setWhiteRoundBg(R.id.current_pen2, R.id.current_pen1, R.id.current_pen4);
                    //set currentimageview
                    currentColor = (ImageView) view;
                    //change colourImageview color
                    changeCurrentColor((ImageView) view);
                    break;
                case R.id.current_pen4:
                    //change background
                    view.setBackgroundResource(R.drawable.main_bg);
                    setWhiteRoundBg(R.id.current_pen2, R.id.current_pen3, R.id.current_pen1);
                    //set currentimageview
                    currentColor = (ImageView) view;
                    //change colourImageview color
                    changeCurrentColor((ImageView) view);
                    break;
            }
        }
    };

    private void setWhiteRoundBg(int view1, int view2, int view3) {
        findViewById(view1).setBackgroundResource(R.drawable.white_bg);
        findViewById(view2).setBackgroundResource(R.drawable.white_bg);
        findViewById(view3).setBackgroundResource(R.drawable.white_bg);
    }


    private void gotoAdvancePaintActivity() {
        L.e("advancepaint");
        SaveImageAsyn.OnSaveFinishListener finishlistener = new SaveImageAsyn.OnSaveFinishListener() {
            @Override
            public void onSaveFinish(String path) {
                MyProgressDialog.DismissDialog();
                Intent intent = new Intent(PaintActivity.this, AdvancePaintActivity.class);
                intent.putExtra("imagepath", path);
                startActivityForResult(intent, MyApplication.PaintActivityRequest);
            }
        };
        saveToLocal(finishlistener);

    }

    private void advanceLaytoggle() {
        if (isShowing == 2) {
            advanceColor.setImageResource(R.drawable.hidebutton);
            AnimateFactory.getInstance().BounceInDownAnimation(advanceLay, findViewById(R.id.advancelay2), findViewById(R.id.colorpicklay));
            isShowing = 1;
        } else if (isShowing == 1) {
            if (advanceLay.getVisibility() == View.VISIBLE) {
                advanceColor.setImageResource(R.drawable.hidebutton);
                AnimateFactory.getInstance().SlideOutUpAnimation(advanceLay);
                isShowing = 0;
            } else {
                advanceColor.setImageResource(R.drawable.hidebutton);
                AnimateFactory.getInstance().BounceInDownAnimation(findViewById(R.id.advancelay2), findViewById(R.id.colorpicklay));
                isShowing = 0;
            }
        } else {
            advanceColor.setImageResource(R.drawable.showbutton);
            AnimateFactory.getInstance().SlideOutUpAnimation(advanceLay, findViewById(R.id.advancelay2), findViewById(R.id.colorpicklay));
            isShowing = 2;
        }
    }

    private void shareImage() {
        UmengUtil.analysitic(PaintActivity.this, UmengUtil.SHAREIMAGE, URL);
        MyProgressDialog.show(PaintActivity.this, null, getString(R.string.savingimage));
        SaveImageAsyn saveImageAsyn = new SaveImageAsyn();
        if (fromSDcard) {
            saveImageAsyn.execute(colourImageView.getmBitmap(), PAINTNAME);
        } else {
            saveImageAsyn.execute(colourImageView.getmBitmap(), ImageSaveUtil.convertImageLageUrl(URL).hashCode());
        }
        saveImageAsyn.setOnSaveSuccessListener(new SaveImageAsyn.OnSaveFinishListener() {
            @Override
            public void onSaveFinish(String path) {
                MyProgressDialog.DismissDialog();
                if (path == null) {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show();
                    ShareImageUtil.getInstance(PaintActivity.this).shareImg(path);
                }
            }
        });
    }

    private void saveToLocal() {
        UmengUtil.analysitic(PaintActivity.this, UmengUtil.SAVEIMAGE, URL);
        MyProgressDialog.show(PaintActivity.this, null, getString(R.string.savingimage));
        SaveImageAsyn saveImageAsyn = new SaveImageAsyn();
        if (fromSDcard) {
            saveImageAsyn.execute(colourImageView.getmBitmap(), PAINTNAME);
        } else {
            saveImageAsyn.execute(colourImageView.getmBitmap(), ImageSaveUtil.convertImageLageUrl(URL).hashCode());
        }
        saveImageAsyn.setOnSaveSuccessListener(new SaveImageAsyn.OnSaveFinishListener() {
            @Override
            public void onSaveFinish(String path) {
                MyProgressDialog.DismissDialog();
                if (path == null) {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveToLocal(SaveImageAsyn.OnSaveFinishListener onSaveFinishListener) {
        UmengUtil.analysitic(PaintActivity.this, UmengUtil.SAVEIMAGE, URL);
        MyProgressDialog.show(PaintActivity.this, null, getString(R.string.savingimage));
        SaveImageAsyn saveImageAsyn = new SaveImageAsyn();
        if (fromSDcard) {
            saveImageAsyn.execute(colourImageView.getmBitmap(), PAINTNAME);
        } else {
            saveImageAsyn.execute(colourImageView.getmBitmap(), ImageSaveUtil.convertImageLageUrl(URL).hashCode());
        }
        saveImageAsyn.setOnSaveSuccessListener(onSaveFinishListener);
    }

    private void openSaveImage(int hashCode) {
        try {
            String root = Environment.getExternalStorageDirectory().getPath() + "/MyFCWorks/";
            String path = root + hashCode + ".png";
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("open image failed");
            }
            Bitmap bMap = BitmapFactory.decodeFile(path);
            colourImageView.setImageBT(bMap);
            Toast.makeText(this, getString(R.string.opensuccess), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            L.e(e.toString());
            Toast.makeText(this, getString(R.string.openfailed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int color;
        if (Build.VERSION.SDK_INT >= 11) {
            color = ((ColorDrawable) view.getBackground()).getColor();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
            view.getBackground().draw(canvas);
            int pix = bitmap.getPixel(0, 0);
            bitmap.recycle();
            color = pix;
        }
        L.e(color + "");
        colorPickerSeekBar.setColor(color);
        changeCurrentColor(color);
    }

    private void changeCurrentColor(int color) {
        setFillColorModel();
        colourImageView.setColor(color);
        currentColor.setImageDrawable(new ColorDrawable(color));
    }

    private void setFillColorModel() {
        pick.setChecked(false);
        drawLine.setChecked(false);
    }


    private void changeCurrentColor(ImageView currentColor) {
        setFillColorModel();
        colorPickerSeekBar.setColor(((ColorDrawable) currentColor.getDrawable()).getColor());
        colourImageView.setColor(((ColorDrawable) currentColor.getDrawable()).getColor());
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener savelistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnDismissListener onCancelListener = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        saveToLocalandFinish();
                    }
                };
                myDialogFactory.dismissDialog(onCancelListener);
            }
        };
        View.OnClickListener quitlistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogFactory.dismissDialog();
                finish();
            }
        };
        myDialogFactory.FinishSaveImageDialog(savelistener, quitlistener);
    }

    private void saveToLocalandFinish() {
        UmengUtil.analysitic(PaintActivity.this, UmengUtil.SAVEIMAGE, URL);
        MyProgressDialog.show(PaintActivity.this, null, getString(R.string.savingimage));
        SaveImageAsyn saveImageAsyn = new SaveImageAsyn();
        if (fromSDcard) {
            saveImageAsyn.execute(colourImageView.getmBitmap(), PAINTNAME);
        } else {
            saveImageAsyn.execute(colourImageView.getmBitmap(), ImageSaveUtil.convertImageLageUrl(URL).hashCode());
        }
        saveImageAsyn.setOnSaveSuccessListener(new SaveImageAsyn.OnSaveFinishListener() {
            @Override
            public void onSaveFinish(String path) {
                MyProgressDialog.DismissDialog();
                if (path == null) {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaintActivity.this, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (colourImageView != null && colourImageView.getmBitmap() != null) {
            outState.putParcelable("bitmap", (colourImageView.getmBitmap().copy(colourImageView.getmBitmap().getConfig(), true)));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getParcelable("bitmap") != null)
            cachedBitmap = savedInstanceState.getParcelable("bitmap");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyApplication.PaintActivityRequest) {
            switch (resultCode) {
                case MyApplication.RepaintResult:
                    repaint();
                    break;
            }
        }

    }

    private void repaint() {
        if (fromSDcard) {
            if (FileUtils.deleteFile(URL)) {
                finish();
            } else {
                Toast.makeText(this, getString(R.string.deletePaintFailed), Toast.LENGTH_SHORT).show();
            }
        } else {
            MyProgressDialog.show(this, null, getString(R.string.loadpicture));
            colourImageView.clearStack();
            AsynImageLoader.showLagreImageAsynWithAllCacheOpen(colourImageView, URL, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    MyProgressDialog.DismissDialog();
                    Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mAttacher = new PhotoViewAttacher(colourImageView, bitmap);
                    MyProgressDialog.DismissDialog();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    MyProgressDialog.DismissDialog();
                    Toast.makeText(PaintActivity.this, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSavedColors(cColor1, cColor2, cColor3, cColor4);
    }

    private void setSavedColors(ImageView cColor1, ImageView cColor2, ImageView cColor3, ImageView cColor4) {
        SharedPreferencesFactory.saveInteger(this, SharedPreferencesFactory.SavedColor1, ((ColorDrawable) cColor1.getDrawable()).getColor());
        SharedPreferencesFactory.saveInteger(this, SharedPreferencesFactory.SavedColor2, ((ColorDrawable) cColor2.getDrawable()).getColor());
        SharedPreferencesFactory.saveInteger(this, SharedPreferencesFactory.SavedColor3, ((ColorDrawable) cColor3.getDrawable()).getColor());
        SharedPreferencesFactory.saveInteger(this, SharedPreferencesFactory.SavedColor4, ((ColorDrawable) cColor4.getDrawable()).getColor());
    }

    private void getSavedColors(ImageView cColor1, ImageView cColor2, ImageView cColor3, ImageView cColor4) {
        cColor1.setImageDrawable(new ColorDrawable(SharedPreferencesFactory.getInteger(this, SharedPreferencesFactory.SavedColor1, getResources().getColor(R.color.red))));
        cColor2.setImageDrawable(new ColorDrawable(SharedPreferencesFactory.getInteger(this, SharedPreferencesFactory.SavedColor2, getResources().getColor(R.color.yellow))));
        cColor3.setImageDrawable(new ColorDrawable(SharedPreferencesFactory.getInteger(this, SharedPreferencesFactory.SavedColor3, getResources().getColor(R.color.skyblue))));
        cColor4.setImageDrawable(new ColorDrawable(SharedPreferencesFactory.getInteger(this, SharedPreferencesFactory.SavedColor4, getResources().getColor(R.color.green))));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        colourImageView.onRecycleBitmaps();
    }
}