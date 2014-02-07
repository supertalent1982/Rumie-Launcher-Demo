package com.jsdev.ruime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jsdev.ruime.adapters.CodeAdapter;
import com.jsdev.ruime.structures.Code;
import com.jsdev.rumie.utility.CodeHelper;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator extends Activity {

    private Button generateButton;
    private Button clearButton;

    private ListView codeList;

    private EditText numberEdit;
    private EditText validEdit;

    private CodeAdapter codeAdapter;

    private List<Code> codes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        generateButton = (Button) findViewById(R.id.codeGenerateButton);
        clearButton = (Button) findViewById(R.id.codeClearButton);

        codeList = (ListView) findViewById(R.id.codeList);

        numberEdit = (EditText) findViewById(R.id.codeEditText);
        validEdit = (EditText) findViewById(R.id.codeValidEdit);

        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeHelper.clearList(CodeGenerator.this);
                codes = CodeHelper.getCodes(CodeGenerator.this);
                codeAdapter = new CodeAdapter(CodeGenerator.this, codes);
                codeList.setAdapter(codeAdapter);
                codeList.invalidateViews();
            }
        });

        generateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberEdit.getText().toString();
                int val = 0;

                String valid = validEdit.getText().toString();
                int time = 0;

                if (number == null || number.length() < 1) {
                    val = 10;
                } else {
                    val = Integer.parseInt(number);
                }

                if (valid == null || valid.length() < 1) {
                    time = 30;
                } else {
                    time = Integer.parseInt(valid);
                }

                List<Code> newCodes = new ArrayList<Code>();

                for (int i = 0; i < val; i++) {
                    Code code = new Code(generateCode(), time);
                    newCodes.add(code);
                }

                CodeHelper.addCodes(CodeGenerator.this, newCodes);
                codes = CodeHelper.getCodes(CodeGenerator.this);
                codeAdapter = new CodeAdapter(CodeGenerator.this, codes);
                codeList.setAdapter(codeAdapter);
                codeList.invalidateViews();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        codes = CodeHelper.getCodes(this);

        if (codeAdapter == null) {
            codeAdapter = new CodeAdapter(this, codes);
        } else {
            codeAdapter.notifyDataSetChanged();
        }

        codeList.setAdapter(codeAdapter);
    }

    private String generateCode() {
        String code = "";

        for (int i = 0; i < 8; i++) {
            code += String.valueOf((int) (Math.random() * 10));
        }

        return code;
    }

}
