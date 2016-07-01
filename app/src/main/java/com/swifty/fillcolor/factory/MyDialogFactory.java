package com.swifty.fillcolor.factory;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.listener.OnAddWordsSuccessListener;
import com.swifty.fillcolor.listener.OnChangeBorderListener;
import com.swifty.fillcolor.listener.OnLoginSuccessListener;
import com.swifty.fillcolor.listener.OnUnLockImageSuccessListener;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.model.AsynImageLoader;
import com.swifty.fillcolor.util.CommentUtil;
import com.swifty.fillcolor.util.DensityUtil;
import com.swifty.fillcolor.util.FileUtils;
import com.swifty.fillcolor.util.SNSUtil;
import com.swifty.fillcolor.util.UmengLoginUtil;
import com.swifty.fillcolor.util.UmengUtil;
import com.swifty.fillcolor.view.ColorPickerSeekBar;
import com.swifty.fillcolor.view.MyDialogStyle;
import com.swifty.fillcolor.view.MyProgressDialog;

/**
 * Created by Swifty.Wang on 2015/6/12.
 */
public class MyDialogFactory extends MyDialogStyle {

    //just for add border
    int drawableid;

    public MyDialogFactory(Context context) {
        super(context);
    }

    public void FinishSaveImageDialog(View.OnClickListener savelistener, View.OnClickListener quitlistener) {
        showTwoButtonDialog(context.getString(R.string.quitorsave), context.getString(R.string.save), context.getString(R.string.quit), savelistener, quitlistener, true);
    }

    public void showAboutDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.version) + ":" + MyApplication.getVersion(context) + "\n");
        buffer.append(UmengUtil.getCurrentVersionDetail(context) + "\n");
        buffer.append(context.getString(R.string.aboutImage) + "\n");
        buffer.append(context.getString(R.string.email) + ":" + context.getString(R.string.authoremail) + "\n");
        buffer.append(context.getString(R.string.qqgroup) + ":" + context.getString(R.string.qqgroupnumber));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        showOneButtonDialog(context.getString(R.string.app_name), buffer, context.getString(R.string.ok), listener, true);
    }

    public void showSettingDialog() {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_setting, null);
        TextView textView = (TextView) layout.findViewById(R.id.current_stacksize);
        textView.setText(context.getText(R.string.undostacksize) + " : " + SharedPreferencesFactory.getInteger(context, SharedPreferencesFactory.StackSize));
        SeekBar seekBar = (SeekBar) layout.findViewById(R.id.stacksize);
        seekBar = SeekBarFactory.getInstance().getStackSeekBar(context, seekBar, textView);
        Button button = (Button) layout.findViewById(R.id.clearcache);
        button.setText(context.getString(R.string.clearcache));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.show(context, null, context.getString(R.string.clearcacheing));
                AsynImageLoader.setOnClearCacheFinishListener(new AsynImageLoader.OnClearCacheFinishListener() {
                    @Override
                    public void clearCacheFinish() {
                        MyProgressDialog.DismissDialog();
                        Toast.makeText(context, context.getString(R.string.clearcachesuccess), Toast.LENGTH_SHORT).show();
                    }
                });
                AsynImageLoader.clearCache();
            }
        });
        Button deletePaint = (Button) layout.findViewById(R.id.deletePaint);
        deletePaint.setText(context.getString(R.string.deleteAllPaints));
        deletePaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogFactory myDialogFactory = new MyDialogFactory(context);
                myDialogFactory.showDeletePaintsDialog();
            }
        });
        Button checkupdate = (Button) layout.findViewById(R.id.checkupdate);
        checkupdate.setText(context.getString(R.string.checkupdate));
        checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengUtil.checkUpdate(context);
            }
        });
        Button button2 = (Button) layout.findViewById(R.id.joinQQGroup);
        button2.setText(context.getString(R.string.joinGroup));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SNSUtil.joinQQGroup(context);
            }
        });
        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);
        initLanguageRadioGroup(radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.CN:
                        MyApplication.setLanguageCode(context, 1);
                        dismissDialog();
                        MyApplication.restart(context);
                        break;
                    case R.id.TW:
                        MyApplication.setLanguageCode(context, 2);
                        dismissDialog();
                        MyApplication.restart(context);
                        break;
                    case R.id.EN:
                        MyApplication.setLanguageCode(context, 3);
                        dismissDialog();
                        MyApplication.restart(context);
                        break;
                }
            }
        });
        showBlankDialog(context.getString(R.string.action_settings), layout);
    }

    private void initLanguageRadioGroup(RadioGroup radioGroup) {
        int lancode = MyApplication.getCurrentLanguageCode(context);
        if (lancode == 1) {
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        } else if (lancode == 2) {
            ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
        } else if (lancode == 3) {
            ((RadioButton) radioGroup.getChildAt(2)).setChecked(true);
        }
    }

    private void showDeletePaintsDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.confirmDeleteAllPaints) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                FileUtils.deleteAllPaints();
                Toast.makeText(context, context.getString(R.string.deleteAllPaintsSuccess), Toast.LENGTH_SHORT).show();
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.delete), context.getString(R.string.cancel), listener1, listener2, true);
    }

    public void showCommentDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.commentDialogContent) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                CommentUtil.commentApp(context);
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.gocomment), context.getString(R.string.nexttime), listener1, listener2, true);
    }

    public void showUnLockdialog(int status, final OnUnLockImageSuccessListener commentSuccessListener) {
        switch (status) {
            //status == 1 comment unlock
            case 1:
                StringBuffer buffer = new StringBuffer();
                buffer.append(context.getString(R.string.giveme5stars) + "\n");
                View.OnClickListener listener2 = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismissDialog();
                    }
                };
                View.OnClickListener listener1 = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismissDialog();
                        CommentUtil.commentApp(context, commentSuccessListener);
                    }
                };
                showTwoButtonDialog(buffer, context.getString(R.string.gocomment), context.getString(R.string.cancel), listener1, listener2, true);
                break;
            case 2:
                //status == 2 share unlock
                StringBuffer buffer2 = new StringBuffer();
                buffer2.append(context.getString(R.string.sharemeunlock) + "\n");
                View.OnClickListener listener4 = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismissDialog();
                    }
                };
                View.OnClickListener listener3 = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismissDialog();
                        SNSUtil.shareApp(context, commentSuccessListener);
                    }
                };
                showTwoButtonDialog(buffer2, context.getString(R.string.share), context.getString(R.string.cancel), listener3, listener4, true);
                break;
        }
    }


    public void showPaintFirstOpenDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.paintHint));
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SharedPreferencesFactory.PaintHint);
    }

    public void showPaintFirstOpenSaveDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.paintHint2));
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SharedPreferencesFactory.PaintHint2);
    }

    public void showAddWordsDialog(final OnAddWordsSuccessListener onAddWordsSuccessListener) {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_addwords, null);
        final EditText editText = (EditText) layout.findViewById(R.id.addeditwords);
        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.small:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().SmallTextSize);
                        break;
                    case R.id.middle:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().MiddleTextSize);
                        break;
                    case R.id.large:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().BigTextSize);
                        break;
                    case R.id.huge:
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().HugeSize);
                        break;
                }
            }
        });
        ColorPickerSeekBar colorPicker = (ColorPickerSeekBar) layout.findViewById(R.id.seekcolorpicker);
        colorPicker.setOnColorSeekbarChangeListener(new ColorPickerSeekBar.OnColorSeekBarChangeListener() {
            @Override
            public void onColorChanged(SeekBar seekBar, int color, boolean b) {
                editText.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().trim().isEmpty()) {
                    dismissDialog();
                    onAddWordsSuccessListener.addWordsSuccess(DragTextViewFactory.getInstance().createUserWordTextView(context, editText.getText().toString(), editText.getCurrentTextColor(), (int) editText.getTextSize()));
                } else {
                    Toast.makeText(context, context.getString(R.string.nowords), Toast.LENGTH_SHORT).show();
                }
            }
        };
        showBlankDialog(context.getString(R.string.addtext), layout, listener);
    }

    public void showRepaintDialog(View.OnClickListener confirm) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.confirmRepaint) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.repaint), context.getString(R.string.cancel), confirm, listener2, true);
    }

    public void showPleaseUpdateVersionDialog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.currentVersionIsntSupport) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                UmengUtil.checkUpdate(context);
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.updateVersion), context.getString(R.string.cancel), listener1, listener2, true);
    }

    public void showAddBorderDialog(final OnChangeBorderListener onChangeBorderListener) {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_addborder, null);
        final ImageView border1 = (ImageView) layout.findViewById(R.id.xiangkuang1);
        final ImageView border2 = (ImageView) layout.findViewById(R.id.xiangkuang2);
        drawableid = 1;
        final View.OnClickListener changeBorderOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == border1.getId()) {
                    border1.setBackgroundResource(R.drawable.maincolor_border_bg);
                    drawableid = 1;
                    border2.setBackgroundResource(0);
                } else {
                    border2.setBackgroundResource(R.drawable.maincolor_border_bg);
                    drawableid = 2;
                    border1.setBackgroundResource(0);
                }
            }
        };
        border1.setOnClickListener(changeBorderOnclickListener);
        border2.setOnClickListener(changeBorderOnclickListener);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                if (drawableid == 1) {
                    onChangeBorderListener.changeBorder(R.drawable.xiangkuang, DensityUtil.dip2px(context, 36), DensityUtil.dip2px(context, 36), DensityUtil.dip2px(context, 21), DensityUtil.dip2px(context, 21));
                } else {
                    onChangeBorderListener.changeBorder(R.drawable.xiangkuang2, DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16), DensityUtil.dip2px(context, 16));
                }
            }
        };
        showBlankDialog(context.getString(R.string.addborder), layout, listener);
    }

    public void showAddQQgroup() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.addQQgroupContent) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                SNSUtil.joinQQGroup(context);
            }
        };
        showTwoButtonDialog(buffer, context.getString(R.string.addQQgroup), context.getString(R.string.nexttime), listener1, listener2, true);
    }

    public void showBuxianButtonClickDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfunctionhint), SharedPreferencesFactory.BuXianButtonClickDialogEnable);
    }

    public void showBuxianFirstPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfirstpointsethint), SharedPreferencesFactory.BuXianFirstPointDialogEnable);
    }

    public void showBuxianNextPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxiannextpointsethint), SharedPreferencesFactory.BuXianNextPointDialogEnable);
    }

    public void showPickColorHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.pickcolor), context.getString(R.string.pickcolorhint), SharedPreferencesFactory.PickColorDialogEnable);
    }

    private void showOnceTimesContentDialog(String title, CharSequence contentstr, final String whichDialog) {
        if (SharedPreferencesFactory.getBoolean(context, whichDialog, true)) {
            View layout = LayoutInflater.from(context).inflate(R.layout.view_dialog_with_checkbox, null);
            TextView content = (TextView) layout.findViewById(R.id.content);
            final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkbox_dontshow);
            content.setText(contentstr);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        SharedPreferencesFactory.saveBoolean(context, whichDialog, false);
                    }
                    dismissDialog();
                }
            };
            showBlankDialog(title, layout, listener);
        }
    }

    public void showPaintHintDialog() {
        showPaintFirstOpenDialog();
        if (!dialog.isShowing()) {
            showPaintFirstOpenSaveDialog();
        }
    }

    public void showLoginDialog(final OnLoginSuccessListener onLoginSuccessListener) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.logincontent) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengLoginUtil.getInstance().faceBookLogin(context, onLoginSuccessListener);
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengLoginUtil.getInstance().qqLogin((Activity) context, onLoginSuccessListener);
            }
        };
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            showTwoImageDialog(buffer, context.getResources().getDrawable(R.drawable.umeng_socialize_qq_on), context.getString(R.string.qq), context.getResources().getDrawable(R.drawable.umeng_socialize_facebook), context.getString(R.string.facebooklogin), listener1, listener2, true);
        } else {
            showTwoImageDialog(buffer, context.getResources().getDrawable(R.drawable.umeng_socialize_qq_on, context.getTheme()), context.getString(R.string.qq), context.getResources().getDrawable(R.drawable.umeng_socialize_facebook, context.getTheme()), context.getString(R.string.facebooklogin), listener1, listener2, true);
        }
    }

    public void showFirstTimeLoginDialog(final OnLoginSuccessListener onLoginSuccessListener) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.logincontentfirst) + "\n");
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengLoginUtil.getInstance().faceBookLogin(context, onLoginSuccessListener);
            }
        };
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UmengLoginUtil.getInstance().qqLogin((Activity) context, onLoginSuccessListener);
            }
        };
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            showTwoImageDialog(buffer, context.getResources().getDrawable(R.drawable.umeng_socialize_qq_on), context.getString(R.string.qq), context.getResources().getDrawable(R.drawable.umeng_socialize_facebook), context.getString(R.string.facebooklogin), listener1, listener2, true);
        } else {
            showTwoImageDialog(buffer, context.getResources().getDrawable(R.drawable.umeng_socialize_qq_on, context.getTheme()), context.getString(R.string.qq), context.getResources().getDrawable(R.drawable.umeng_socialize_facebook, context.getTheme()), context.getString(R.string.facebooklogin), listener1, listener2, true);
        }
    }

    public void showGradualHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.gradualModel), context.getString(R.string.gradualModelHint), SharedPreferencesFactory.GradualModel);
    }
}
