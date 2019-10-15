package com.niceguysoft.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int TIP_PERCENTAGE_DEFAULT_VALUE = 12;

    private EditText billAmountEditText;
    private EditText tipPrecEditText;
    private EditText tipAmountEditText;
    private EditText totalBillAmountEditText;

    private SeekBar tipPrecSeekBar;

    private Switch roundUpSwitch;

    private Button clearButton;

    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        clearAllViews();

        billAmountEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


    }

    // Just init the views
    protected void initViews () {
        billAmountEditText = findViewById(R.id.editTextBillAmount);
        tipPrecEditText = findViewById(R.id.editTextTipPrec);
        tipAmountEditText = findViewById(R.id.editTextTipAmount);
        totalBillAmountEditText = findViewById(R.id.editTextTotalAmount);

        tipPrecSeekBar = findViewById(R.id.seekBarTipPrec);

        roundUpSwitch = findViewById(R.id.switchRoundUp);

        clearButton = findViewById(R.id.buttonClear);

        versionTextView = findViewById(R.id.textViewVersion);
        versionTextView.setText("ver "+BuildConfig.VERSION_NAME);

        setViewsListener();
    }

    protected void setViewsListener () {

        billAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalBill();
            }
        });

        tipPrecEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditTextEmpty(tipPrecEditText)) {
                    tipPrecEditText.setText("0");
                }
                int value = Integer.parseInt(tipPrecEditText.getText().toString());
                tipPrecSeekBar.setProgress(value);
                calculateTotalBill();

            }
        });

        tipPrecSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tipPrecEditText.setText("" + progress);
                    calculateTotalBill();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        roundUpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotalBill();
            }
        });


        tipAmountEditText.setKeyListener(null);
        totalBillAmountEditText.setKeyListener(null);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllViews();
            }
        });
    }

    protected void calculateTotalBill () {

        double billAmount = 0;
        if (!isEditTextEmpty(billAmountEditText)) {
            billAmount = Double.parseDouble(billAmountEditText.getText().toString());
        }

        double tipPrec = 0;
        if (!isEditTextEmpty(billAmountEditText)) {
            tipPrec = Double.parseDouble(tipPrecEditText.getText().toString());
        }

        if (tipPrec == 0) {
            tipAmountEditText.setText(""+String.format("%.2f", 0.0));
            totalBillAmountEditText.setText(""+String.format("%.2f", billAmount));
            return;
        }

        double tipMultiply = (tipPrec + 100)/100;

        double totalAmount = billAmount * tipMultiply;

        if (roundUpSwitch.isChecked()) {
            totalAmount = Math.round(totalAmount);
        }

        double tipAmount = totalAmount - billAmount;

        tipAmountEditText.setText(""+String.format("%.2f", tipAmount));
        totalBillAmountEditText.setText(""+String.format("%.2f", totalAmount));
    }

    protected boolean isEditTextEmpty (EditText et) {
        return et.getText().toString().matches("");
    }

    protected void clearAllViews () {
        billAmountEditText.setText("");
        tipPrecEditText.setText(""+TIP_PERCENTAGE_DEFAULT_VALUE);
        tipPrecSeekBar.setProgress(TIP_PERCENTAGE_DEFAULT_VALUE);
        tipAmountEditText.setText("");
        totalBillAmountEditText.setText("");



    }

}
