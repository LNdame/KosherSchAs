package com.kosher.cct.schoolassess.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kosher.cct.schoolassess.R;
import com.kosher.cct.schoolassess.model.ScreeningQuestion;

/**
 * Created by loicstephan on 2017/05/20.
 */

public class Question_close_ended extends LinearLayout   {

    private TextView questiondesc;

    LinearLayout containerLyt;// quest_open_endedlyt

    private int questionStyle, headerStye;
    private ScreeningQuestion screeningQuestion;
    private RadioButton radYes, radNo;
    private String descText;

    private RadioGroup radChoice;
    String answer="";




    Context context;

    public Question_close_ended(Context context) {
        super(context);
        this.context = context;
        initViews(context);
    }

    public Question_close_ended(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews(context, attrs);
    }

    public Question_close_ended(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }



    public void initViews(final Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.question_close_ended, this);
        containerLyt = (LinearLayout) findViewById(R.id.quest_open_closelyt) ;

        questiondesc = (TextView) findViewById(R.id.txtquestiondesc);
        radChoice = (RadioGroup) findViewById(R.id.radgranswer) ;
        radYes =(RadioButton) findViewById(R.id.radYes);
        radNo =(RadioButton) findViewById(R.id.radNo);
        if(!TextUtils.isEmpty(getDescText()) )
            questiondesc.setText(getDescText());




        radYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radYes.isChecked()){
                    answer= "yes";
                    Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
                    Log.d("Question", answer);
                }

            }
        });

        radNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radNo.isChecked()){
                    answer= "no";
                    Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initViews(final Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.questionLayoutStyle,0,0);

        try{
            questionStyle = a.getResourceId(R.styleable.questionLayoutStyle_questionStyle, R.style.questionnaireItemStyle);
            headerStye = a.getResourceId(R.styleable.questionLayoutStyle_headerStyle,android.R.style.TextAppearance_DeviceDefault  );
        }finally {
            a.recycle();
        }


        LayoutInflater.from(context).inflate(R.layout.question_close_ended, this);
        containerLyt = (LinearLayout) findViewById(R.id.quest_open_closelyt) ;

        questiondesc = (TextView) findViewById(R.id.txtquestiondesc);
        radYes =(RadioButton) findViewById(R.id.radYes);
        radNo =(RadioButton) findViewById(R.id.radNo);


        radYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radYes.isChecked()){
                    answer= "yes";
                    Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
                    Log.d("Question", answer);
                }
            }
        });

        radYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radYes.isChecked()){
                    answer= "yes";
                    Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
                    Log.d("Question", answer);
                }

            }
        });

        radNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(radNo.isChecked()){
                    answer= "no";
                    Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(!TextUtils.isEmpty(getDescText()) )
            questiondesc.setText(getDescText());

        questiondesc.setTextAppearance(context,headerStye);

    }


    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
        if(questiondesc!=null)
            questiondesc.setText(descText);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
