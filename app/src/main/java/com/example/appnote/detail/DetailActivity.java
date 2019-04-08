package com.example.appnote.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.appnote.R;
import com.example.appnote.adapter.ImageAdapter;
import com.example.appnote.data.local.NoteDataSource;
import com.example.appnote.data.model.Constants;
import com.example.appnote.data.model.Note;
import com.example.appnote.data.model.NoteAction;
import com.example.appnote.receiver.AlarmReceiver;
import com.example.appnote.utils.Utility;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener,
        DetailContract.View, AdapterView.OnItemSelectedListener, ImageAdapter.ItemImageClickListener {
    public static int REQUEST_CAMERA = 101;
    public static int SELECT_FILE = 102;
    private DetailPresenter mDetailPresenter;
    private TextView mTvTitleBar;
    private Dialog mColorPicker;
    private LinearLayout mViewParent;
    private RecyclerView mRecyclerImage;
    private EditText mEdtTitle;
    private EditText mEdtContent;
    private TextView mTvCurrentDate;
    private TextView mButtonAlarm;
    private Spinner mSpinnerDays;
    private Spinner mSpinnerHours;
    private ImageView mButtonClose;
    private ImageView mButtonOption;
    private ImageView mButtonCamera;
    private ImageView mButtonDone;
    private ImageView mButtonChangeColor;
    private ImageView mButtonBack;
    private LinearLayout mViewPicker;
    private FrameLayout mBottomBar;
    private ImageView mButtonPrevious;
    private ImageView mButtonNext;
    private ImageView mButtonShare;
    private ImageView mButtonDelete;
    private Uri mUri;
    private Note mNote = new Note();
    private List<Note> mNotes;
    private ImageAdapter mImageAdapter;
    private Intent mIntent;
    private int mPosition, _id;
    private String _title, _content, _hour, _day;
    private Date _createDate;
    private boolean _isAlarm;
    private ArrayAdapter<CharSequence> mDayAdapter;
    private ArrayAdapter<CharSequence> mHourAdapter;
    private String mAction;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDetailPresenter = new DetailPresenter(this, NoteDataSource.getInstance());
        initViews();
    }

    private void initViews() {
        getControls();
        mImageAdapter = new ImageAdapter(this, this);
        mIntent = getIntent();
        mPosition = mIntent.getIntExtra(Constants.Extra.EXTRA_POSITION, 0);
        mAction = mIntent.getStringExtra(Constants.Extra.EXTRA_ACTION);
        if (mAction.equals(NoteAction.NEW_NOTE)) {
            mDetailPresenter.setupUi(NoteAction.NEW_NOTE);
        } else if (mAction.equals(NoteAction.EDIT_NOTE)) {
            mDetailPresenter.getAllNote();
            mDetailPresenter.setupUi(NoteAction.EDIT_NOTE);
        }
        mImageAdapter.setData(mNote.getUriStrings());
        mRecyclerImage.setAdapter(mImageAdapter);
    }

    @Override
    public void setupUi(@NoteAction String action) {

        mTvCurrentDate.setText(Utility.getCurrentDate());

        // TODO set default day spinner
        mDayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, Constants.days);
        mDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDays.setAdapter(mDayAdapter);

        // TODO set default hour spinner
        mHourAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, Constants.hours);
        mHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerHours.setAdapter(mHourAdapter);

        if (action.equals(NoteAction.NEW_NOTE)) {
            setupForNewNote();
        } else if (action.equals(NoteAction.EDIT_NOTE)) {
            setupForEditNote();
        }
    }

    public void takeNewNote() {
        if (mEdtTitle.getText().toString().isEmpty()) {
            _title = getString(R.string.text_untitle);
        } else {
            _title = mEdtTitle.getText().toString();
        }
        _content = mEdtContent.getText().toString();
        _createDate = Calendar.getInstance().getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                finish();
                break;

            case R.id.buttonCamera:
                selectImage();
                break;

            case R.id.buttonChangeColor:
                showDialogColorPicker();
                break;

            case R.id.buttonDone:
                if (_isAlarm && !isNullTime()) {
                    Toast.makeText(this, getString(R.string.text_null_time), Toast.LENGTH_LONG).show();
                    return;
                }
                takeNewNote();
                if (mAction.equals(NoteAction.NEW_NOTE)) {
                    mNote.setTitle(_title);
                    mNote.setContent(_content);
                    mNote.setAlarm(_isAlarm);
                    mNote.setCreateDate(_createDate);
                    mNote.setDay(_day);
                    mNote.setHour(_hour);
                    _id = (int) UUID.randomUUID().getMostSignificantBits();
                    mNote.setId(_id);
                    mDetailPresenter.saveNewNote(mNote);
                } else {
                    mDetailPresenter.updateNote(mNote, _title, _content, _day, _hour, _createDate, _isAlarm);
                }
                if (_isAlarm) mDetailPresenter.scheduleNotification(false);
                break;

            case R.id.buttonOption:
                mDetailPresenter.showDialogConfirm(NoteAction.NEW_NOTE);
                break;

            case R.id.buttonClose:
                mViewPicker.setVisibility(View.GONE);
                mButtonAlarm.setVisibility(View.VISIBLE);
                _isAlarm = false;
                break;

            case R.id.buttonAlarm:
                setupArlarm();
                break;

            case R.id.buttonWhite:
                mDetailPresenter.changeBackgroundColor(mNote, Color.WHITE);
                break;

            case R.id.buttonYellow:
                mDetailPresenter.changeBackgroundColor(mNote, Color.YELLOW);
                break;

            case R.id.buttonGreeen:
                mDetailPresenter.changeBackgroundColor(mNote, Color.GREEN);
                break;

            case R.id.buttonBlue:
                mDetailPresenter.changeBackgroundColor(mNote, getColor(R.color.colorBlue));
                break;

            case R.id.buttonShare:
                mDetailPresenter.share();
                break;

            case R.id.buttonDelete:
                mDetailPresenter.showDialogConfirm(NoteAction.DELETE);
                break;

            case R.id.buttonNext:
                updateActionBottom(NoteAction.NEXT);
                break;

            case R.id.buttonPrevious:
                updateActionBottom(NoteAction.PREVIOUS);
                break;

            default:
                break;
        }
    }

    @Override
    public void setupForNewNote() {
        Constants.days[0] = getString(R.string.text_day);
        Constants.hours[0] = getString(R.string.text_time);
        mButtonOption.setVisibility(View.GONE);
        mViewPicker.setVisibility(View.GONE);
        mBottomBar.setVisibility(View.GONE);
        mButtonAlarm.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogConfirm(@NoteAction final String action) {
        String message = null;
        if (action.equals(NoteAction.NEW_NOTE)) {
            message = getString(R.string.text_dialog_new_note);
        } else message = getString(R.string.text_dialog_delete);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (action.equals(NoteAction.NEW_NOTE)) {
                            Intent intent = DetailActivity.getIntent(DetailActivity.this);
                            intent.putExtra(Constants.Extra.EXTRA_ACTION, NoteAction.NEW_NOTE);
                            startActivity(intent);
                            finish();
                        } else {
                            if (_isAlarm) mDetailPresenter.scheduleNotification(true);
                            mDetailPresenter.deleteNote(mPosition);
                        }
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @Override
    public void setupForEditNote() {
        mTvTitleBar.setText(mNote.getTitle());
        mButtonOption.setVisibility(View.VISIBLE);
        mBottomBar.setVisibility(View.VISIBLE);
        mEdtTitle.setText(mNote.getTitle());
        mEdtContent.setText(mNote.getContent());
        mViewParent.setBackgroundColor(mNote.getColor());
        _isAlarm = mNote.isAlarm();
        if (_isAlarm) {
            mViewPicker.setVisibility(View.VISIBLE);
            mButtonAlarm.setVisibility(View.GONE);
            _day = mNote.getDay();
            _hour = mNote.getHour();
            Constants.days[0] = mNote.getDay();
            mDayAdapter.notifyDataSetChanged();
            Constants.hours[0] = mNote.getHour();
            mHourAdapter.notifyDataSetChanged();
        } else {
            Constants.hours[0] = getString(R.string.text_time);
            Constants.days[0] = getString(R.string.text_day);
            _day = null;
            _hour = null;
            mViewPicker.setVisibility(View.GONE);
            mButtonAlarm.setVisibility(View.VISIBLE);
        }
        if (mNotes.size() == 1) {
            mButtonNext.setAlpha(60);
            mButtonPrevious.setAlpha(60);
        } else if (mPosition == mNotes.size() - 1) {
            mButtonPrevious.setAlpha(255);
            mButtonNext.setAlpha(60);
        } else if (mPosition == 0) {
            mButtonNext.setAlpha(255);
            mButtonPrevious.setAlpha(60);
        } else {
            mButtonNext.setAlpha(255);
            mButtonPrevious.setAlpha(255);
        }
    }

    public boolean isNullTime() {
        if (_day == null || _hour == null) {
            return false;
        }
        return true;
    }

    private void updateActionBottom(@NoteAction String action) {
        if (action.equals(NoteAction.NEXT) && mPosition < mNotes.size() - 1) {
            ++mPosition;
        } else if (action.equals(NoteAction.PREVIOUS) && mPosition > 0) {
            --mPosition;
        }
        mNote = mNotes.get(mPosition);
        mImageAdapter.setData(mNote.getUriStrings());
        setupForEditNote();
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void scheduleNotification(boolean isCancel) {
        long when = Utility.parseDateToMilisecond(_day + " " + _hour);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(Constants.Extra.EXTRA_NOTIFICATION_ID, mNote.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mNote.getId(), intent, 0);
        if (isCancel) am.cancel(pendingIntent);
        else am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinnerDays) {
            switch (position) {
                case 0:
                    _day = null;
                    break;

                case 1:
                    _day = Utility.addDate(0);
                    break;

                case 2:
                    _day = Utility.addDate(1);
                    break;

                case 3:
                    _day = Utility.addDate(2);
                    break;

                case 4:
                    showDialogDatePicker();
                    break;

                default:
                    break;
            }
        } else if (parent.getId() == R.id.spinnerHours) {
            switch (position) {
                case 0:
                    _hour = null;
                    break;

                case 1:
                    _hour = getString(R.string.text_hour8);
                    break;

                case 2:
                    _hour = getString(R.string.text_hour12);
                    break;

                case 3:
                    _hour = getString(R.string.text_hour16);
                    break;

                case 4:
                    _hour = getString(R.string.text_hour20);
                    break;

                case 5:
                    showDialogTimePicker();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void showDialogColorPicker() {
        mColorPicker = new Dialog(this);
        mColorPicker.setCancelable(true);
        mColorPicker.setContentView(R.layout.dialog_color_picker);
        View buttonWhite = mColorPicker.findViewById(R.id.buttonWhite);
        View buttonYellow = mColorPicker.findViewById(R.id.buttonYellow);
        View buttonGreen = mColorPicker.findViewById(R.id.buttonGreeen);
        View buttonBlue = mColorPicker.findViewById(R.id.buttonBlue);

        buttonWhite.setOnClickListener(this);
        buttonYellow.setOnClickListener(this);
        buttonGreen.setOnClickListener(this);
        buttonBlue.setOnClickListener(this);
        mColorPicker.show();

    }

    @Override
    public void doneAndUpdate() {
        finish();
    }

    @Override
    public void setNotes(List<Note> notes) {
        mNotes = notes;
        _id = mIntent.getIntExtra(Constants.Extra.EXTRA_NOTIFICATION_ID, -1);
        if (_id != -1) {
            for (Note note : mNotes) {
                if (note.getId() == _id) {
                    mNote = note;
                    break;
                }
            }
        } else mNote = notes.get(mPosition);
    }

    @Override
    public void onRemove(int position) {
        mDetailPresenter.removeImageNote(mNote, position);
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String url) {
        previewImage(url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                mDetailPresenter.addImageNote(mNote, mUri.toString());
            } else if (requestCode == SELECT_FILE) {
                mDetailPresenter.addImageNote(mNote, data.getData().toString());
            }
        }
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateBackgroundColor(@ColorInt int color) {
        mViewParent.setBackgroundColor(color);
        mColorPicker.dismiss();
    }

    @Override
    public void previewImage(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), Constants.Type.TYPE_IMAGE);
        startActivity(intent);
    }

    private void getControls() {
        mViewParent = findViewById(R.id.viewParent);
        mViewPicker = findViewById(R.id.viewPicker);
        mBottomBar = findViewById(R.id.bottomBar);

        mTvTitleBar = findViewById(R.id.tvTitlerBar);
        mRecyclerImage = findViewById(R.id.recyclerImage);
        mButtonCamera = findViewById(R.id.buttonCamera);
        mButtonChangeColor = findViewById(R.id.buttonChangeColor);
        mButtonDone = findViewById(R.id.buttonDone);
        mButtonOption = findViewById(R.id.buttonOption);

        mButtonBack = findViewById(R.id.buttonBack);
        mButtonAlarm = findViewById(R.id.buttonAlarm);
        mSpinnerDays = findViewById(R.id.spinnerDays);
        mSpinnerHours = findViewById(R.id.spinnerHours);
        mButtonClose = findViewById(R.id.buttonClose);
        mEdtTitle = findViewById(R.id.edtTitle);
        mEdtContent = findViewById(R.id.edtContent);
        mTvCurrentDate = findViewById(R.id.textCurrentDate);

        mButtonPrevious = findViewById(R.id.buttonPrevious);
        mButtonShare = findViewById(R.id.buttonShare);
        mButtonDelete = findViewById(R.id.buttonDelete);
        mButtonNext = findViewById(R.id.buttonNext);

        mButtonCamera.setOnClickListener(this);
        mButtonChangeColor.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);
        mButtonOption.setOnClickListener(this);
        mButtonBack.setOnClickListener(this);
        mButtonAlarm.setOnClickListener(this);
        mButtonClose.setOnClickListener(this);

        mButtonShare.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mSpinnerDays.setOnItemSelectedListener(this);
        mSpinnerHours.setOnItemSelectedListener(this);
    }

    @Override
    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mEdtContent.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TITLE, mEdtTitle.getText().toString());
        sendIntent.setType(Constants.Type.TYPE_SHARE);
        startActivity(Intent.createChooser(sendIntent, getString(R.string.text_share)));
    }

    @Override
    public void selectImage() {
        final CharSequence[] items = {getString(R.string.text_camera), getString(R.string.text_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_insert_picture));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals(getString(R.string.text_camera))) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "pic_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[i].equals(R.string.text_gallery)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType(Constants.Type.TYPE_IMAGE);
                    startActivityForResult(intent, SELECT_FILE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void setupArlarm() {
        mButtonAlarm.setVisibility(View.GONE);
        mViewPicker.setVisibility(View.VISIBLE);
        _isAlarm = true;
    }

    @Override
    public void showDialogDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        _day = year + "-" + monthOfYear + "-" + dayOfMonth;
                        Constants.days[0] = _day;
                        mDayAdapter.notifyDataSetChanged();
                        mSpinnerDays.setSelection(0);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showDialogTimePicker() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                _hour = selectedHour + ":" + selectedMinute;
                Constants.hours[0] = _hour;
                mHourAdapter.notifyDataSetChanged();
                mSpinnerHours.setSelection(0);
            }
        }, hour, minute, true);
        mTimePicker.show();
    }

}
