package com.kosher.cct.schoolassess.view.pupil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.kosher.cct.schoolassess.R;
import com.kosher.cct.schoolassess.helper.DbHelper;
import com.kosher.cct.schoolassess.model.Patient;
import com.kosher.cct.schoolassess.model.School;

import java.util.ArrayList;

public class AddEditPupil extends AppCompatActivity {


    EditText edtName,edtSurname,edtGrade,edtEmisNum, edtNokName,edtNokContact,edtNokAddrLine1,edtNokAddrLine2;

    TextView txtDOB;
    RadioButton radYES,radNO;
    Spinner spinGender, spinSchool;
    ImageButton imgCalendar;

    Button btnAddPatient;
    public DbHelper databhelper ;

    ArrayList<School> schoolList = new ArrayList<School>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pupil);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            databhelper= new DbHelper(getApplicationContext());
            databhelper.createDatabase();
        } catch (Exception e) {

            e.printStackTrace();
        }


        setupLayout();

    }




    void setupLayout()
    {
        edtName = (EditText) findViewById(R.id.edtfirstname);
        edtSurname = (EditText) findViewById(R.id.edtsurname);
        edtGrade = (EditText) findViewById(R.id.edtgrade);
        edtEmisNum = (EditText) findViewById(R.id.edtEmis);
        edtNokName = (EditText) findViewById(R.id.edtnokfirstname);
        edtNokContact = (EditText) findViewById(R.id.edtnoksurname);
        edtNokAddrLine1 = (EditText) findViewById(R.id.edtnokadd1);
        edtNokAddrLine2 = (EditText) findViewById(R.id.edtnokadd2);

        txtDOB= (TextView) findViewById(R.id.txtdob);;
        radYES = (RadioButton) findViewById(R.id.radYes);
        radNO = (RadioButton) findViewById(R.id.radNo);
        spinGender = (Spinner) findViewById(R.id.spingender);
        spinSchool= (Spinner) findViewById(R.id.spinschool);
        imgCalendar= (ImageButton) findViewById(R.id.imgcalendar);

      //  btnAddPatient = (Button) findViewById(R.id.btnaddPatient);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGender.setAdapter(adapter);

       // schoolList =(ArrayList<School>)databhelper.getAllSchoolData();



        String[] schoollist = new String[schoolList.size()];
        int i=0;
        for(School sch:schoolList) {
            schoollist[i] = sch.getSchoolName();
            i++;

        }

        ArrayAdapter<String> adapt =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, schoollist );
        adapt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); //appear white fix it
        spinSchool.setAdapter(adapt);

    }



    Patient getFieldData()
    {

        String name, surname, grade,  emisnum, nokname, nokcontact , nokadddr1, nokaddr2, dob, gender, school;
        int schoolID;


        name =edtName.getText().toString();
        surname=edtSurname.getText().toString();
        grade=edtGrade.getText().toString();
        emisnum=edtEmisNum.getText().toString();
        nokname=edtNokName.getText().toString();
        nokcontact =edtNokContact.getText().toString();
        nokadddr1=edtNokAddrLine1.getText().toString();
        nokaddr2=edtNokAddrLine2.getText().toString();

        dob=txtDOB.getText().toString();
        gender=spinGender.getSelectedItem().toString();
        school= "school";//  spinSchool.getSelectedItem().toString();
        //schoolID =  schoolList.get(spinSchool.getSelectedItemPosition()).getSchoolID();

        schoolID=1;

        boolean hasRoadtoHealth = false;
        if(radYES.isChecked())
            hasRoadtoHealth =true;

        Patient patient = new Patient(name, surname,grade,gender,emisnum,school,schoolID,dob,nokname,nokcontact,nokadddr1,nokaddr2,hasRoadtoHealth);

        return patient;
    }

    public void registerPatient(View view)
    {

        if (databhelper.addPatientData(getFieldData()))
        {
            Snackbar.make(view, "Patient has been successfully added to the records", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }


}
