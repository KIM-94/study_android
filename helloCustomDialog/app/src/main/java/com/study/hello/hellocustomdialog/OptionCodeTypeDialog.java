package com.study.hello.hellocustomdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;

public class OptionCodeTypeDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView tvTitle, tvNegative, tvPositive;

    public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_codetype_dialog);

        tvTitle = findViewById(R.id.option_codetype_dialog_title_tv);
        tvPositive = findViewById(R.id.option_codetype_dialog_positive);
        tvNegative = findViewById(R.id.option_codetype_dialog_negative);

        tvPositive.setOnClickListener(v -> {
            // 저장버튼 클릭
            this.customDialogClickListener.onPositiveClick();
            dismiss();
        });
        tvNegative.setOnClickListener(v -> {
            // 취소버튼 클릭
            this.customDialogClickListener.onNegativeClick();
            dismiss();
        });
    }
}