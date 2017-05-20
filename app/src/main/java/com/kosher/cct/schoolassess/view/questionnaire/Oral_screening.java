package com.kosher.cct.schoolassess.view.questionnaire;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.kosher.cct.schoolassess.R;
import com.kosher.cct.schoolassess.app.Constants;
import com.kosher.cct.schoolassess.customview.Question_close_ended;
import com.kosher.cct.schoolassess.customview.Question_open_ended_alpha;
import com.kosher.cct.schoolassess.customview.Question_open_ended_num;
import com.kosher.cct.schoolassess.customview.Question_sub_title;
import com.kosher.cct.schoolassess.helper.DbHelper;
import com.kosher.cct.schoolassess.model.QuestionSubdivision;
import com.kosher.cct.schoolassess.model.ScreeningQuestion;

import java.util.ArrayList;

public class Oral_screening extends AppCompatActivity {


    public ArrayList<ScreeningQuestion> screeningQuestionsList = new ArrayList<>();
    public ArrayList<QuestionSubdivision> questionSubdivisionList = new ArrayList<>();
    public DbHelper databhelper ;
    LinearLayout questionnairelyt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oral_screening);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        try {
            databhelper= new DbHelper(getApplicationContext());
            databhelper.createDatabase();
        } catch (Exception e) {

            e.printStackTrace();
        }


        questionnairelyt = (LinearLayout) findViewById(R.id.questionnairelyt);


        GetScreeningQuestion(Constants.ORAL_SCRID);

    }


    public void GetScreeningQuestion(int id)
    {


        questionSubdivisionList = (ArrayList<QuestionSubdivision>) databhelper.getQuestionSubData(id);

        for (QuestionSubdivision qu : questionSubdivisionList)
        {

            setQuestionnaireLayout((ArrayList<ScreeningQuestion>) databhelper.getScreeningQuestData(qu.getScCatID(),qu.getQusubID()), qu.getQusubName());
            // screeningQuestionsList.addAll((ArrayList<ScreeningQuestion>) databhelper.getScreeningQuestData(qu.getScCatID(),qu.getQusubID()));

           /* for(ScreeningQuestion sq :(ArrayList<ScreeningQuestion>) databhelper.getScreeningQuestData(qu.getScCatID(),qu.getQusubID()) )
            {
                screeningQuestionsList.add(sq);
            }*/
        }

    }

    /**
     *
     * @param questionList
     * @param title
     */
    public void setQuestionnaireLayout(ArrayList<ScreeningQuestion> questionList, String title)
    {
        Question_sub_title sub_title = new Question_sub_title(getApplicationContext());
        sub_title.setQuesTitle(title);

        questionnairelyt.addView(sub_title);

        for(int i =0; i<questionList.size(); i++){
            Question_open_ended_num questnum = null;
            Question_close_ended questclose = null;
            Question_open_ended_alpha questalpha = null;

            ScreeningQuestion sc = questionList.get(i);

            LinearLayout lSquest = new LinearLayout(getApplicationContext());
            lSquest.setOrientation(LinearLayout.VERTICAL);

            View lineView = new View(getApplicationContext());
            lineView.setBackgroundColor(Color.DKGRAY);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,1);
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            lp.leftMargin = 10;
            lp.rightMargin = 10;

            lineView.setLayoutParams(lp);

            if( sc.getQuType().equals(Constants.QUTYPE_OPEN_NUM))
            {
                questnum =  new Question_open_ended_num(getApplicationContext());
                questnum.setDescText(sc.getScqudesc());
                lSquest.addView(questnum);

            }else if (sc.getQuType().equals(Constants.QUTYPE_OPEN_ALPHA))
            {
                questalpha =  new Question_open_ended_alpha(getApplicationContext());
                questalpha.setDescText(sc.getScqudesc());
                lSquest.addView(questalpha);
            }else if (sc.getQuType().equals(Constants.QUTYPE_CLOSE_ENDED))
            {
                questclose =  new Question_close_ended(getApplicationContext());
                questclose.setDescText(sc.getScqudesc());
                lSquest.addView(questclose);
            }


            lSquest.addView(lineView);

            questionnairelyt.addView(lSquest);

        }



    }




}
