package com.kosher.cct.schoolassess.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.kosher.cct.schoolassess.model.Patient;
import com.kosher.cct.schoolassess.model.QuestionSubdivision;
import com.kosher.cct.schoolassess.model.ScreeningQuestion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loicstephan on 2017/05/20.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static  String DATABASE_NAME = "cha.db";//healthscreen.sqlite
    public static  String DB_PATH= "/data/data/com.kosher.cct.schoolassess/databases/";


    private final Context context;
    private static SQLiteDatabase chadb;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }


    ///check that the database is there
    public boolean checkDatabase()
    {
        SQLiteDatabase db = null;
        try{
            String mypath = DB_PATH +DATABASE_NAME;
            db= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e)
        {
            //too bad no db add a toast here
        }
        if(db!=null)
        {
            db.close();
        }

        return db!=null? true : false;
    }

    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDatabase();

        if(!dbExist)
        {

            //empty db detected create new one and overwrite
            this.getReadableDatabase();
            try{
                copyDatabase();
            }catch(IOException e)
            {
                throw new Error ("Error copying the database");}
        }


    }

    private void copyDatabase()throws IOException
    {
        //open your local db input
        InputStream inputSt = context.getAssets().open(DATABASE_NAME);

        //path to create an empty db
        String outFileName = DB_PATH+DATABASE_NAME;

        OutputStream outputSt = new FileOutputStream(outFileName);

        //tranfers bytes  from inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while((length= inputSt.read(buffer))>0)
        {
            outputSt.write(buffer,0,length);

        }
        //close the stream
        outputSt.flush();
        outputSt.close();
        inputSt.close();
    }


    public static void openDatabase () throws SQLException
    {
        //open db
        String path = DB_PATH+DATABASE_NAME;
        chadb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public SQLiteDatabase getReadDatabase()
    {
        //open db
        String path = DB_PATH+DATABASE_NAME;
        chadb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return chadb;
    }

    @Override
    public synchronized void close()
    {
        if(chadb!=null)
            chadb.close();

        super.close();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*********************** Escape string for single quotes (Insert,Update)************/

    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    /***********************************this part concerned the entities in the database*******************************************/
    /** Table names */

    public static final String answers_Table ="answers";
    public static final String assent_Table ="assent";
    public static final String patient_Table ="patient";
    public static final String questionsubdivision_Table ="questionsubdivision";
    public static final String referral_Table ="referral";
    public static final String school_Table ="school";
    public static final String screenincategory_Table ="screenincategory";
    public static final String screening_Table ="screening";
    public static final String screeningquestion_Table ="screeningquestion";


    /********************Patient Table Fields ************/

    public static final String KEY_PATIENT_ID = "patientID";
    public static final String KEY_PATIENT_NAME = "firstname";
    public static final String KEY_PATIENT_SURNAME = "surname";
    public static final String KEY_PATIENT_GRADE = "grade";
    public static final String KEY_PATIENT_GENDER = "gender";
    public static final String KEY_PATIENT_EMIS = "emisnum";
    public static final String KEY_PATIENT_SCHOOL = "school";
    public static final String KEY_PATIENT_SCHOOLID = "schoolID";
    public static final String KEY_PATIENT_DOB = "DOB";
    public static final String KEY_PATIENT_NOK_NAME = "nokname";
    public static final String KEY_PATIENT_NOK_CONTACT = "nokcontact";
    public static final String KEY_PATIENT_NOK_ADD1 = "nokaddressline1";
    public static final String KEY_PATIENT_NOK_ADD2 = "nokaddressline2";
    public static final String KEY_PATIENT_R2H= "hasroadtohealth";

    //((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0)

    public List<Patient> getAllPatientData() {
        List<Patient> List = new ArrayList<Patient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + patient_Table;

        try {
            if(chadb==null)
            {
                openDatabase();
            }
            Cursor cursor = chadb.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Patient pat = new Patient(
                            ((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0),
                            (cursor.getString(1)),
                            (cursor.getString(2)),
                            (cursor.getString(3)),
                            (cursor.getString(4)) ,
                            (cursor.getString(5)) ,
                            (cursor.getString(6)) ,
                            ((cursor.getString(7))!=null ? Integer.parseInt(cursor.getString(7)):0) ,
                            (cursor.getString(8)),
                            (cursor.getString(9)),
                            (cursor.getString(10)),
                            (cursor.getString(11)),
                            (cursor.getString(12)),
                            (cursor.getInt(13)>0)
                    );



                    // Adding contact to list
                    List.add(pat);
                } while (cursor.moveToNext());
            }
        } catch (SQLException se) {
            // TODO: handle exception
            se.printStackTrace();;
        }
        return List;
    }


    public boolean addPatientData(Patient patient) {

        boolean added = false;
        try {
            if(chadb==null)
            {
                openDatabase();
            }

            ContentValues cVal = new ContentValues();

            cVal.put(KEY_PATIENT_NAME, sqlEscapeString(patient.getFirstname()));
            cVal.put(KEY_PATIENT_SURNAME, sqlEscapeString(patient.getSurname()));
            cVal.put(KEY_PATIENT_GRADE, sqlEscapeString(patient.getGrade()));
            cVal.put(KEY_PATIENT_GENDER, sqlEscapeString(patient.getGender()));
            cVal.put(KEY_PATIENT_SCHOOL, sqlEscapeString(patient.getSchool()));
            cVal.put(KEY_PATIENT_SCHOOLID, sqlEscapeString(String.valueOf(patient.getSchoolID()) ));
            cVal.put(KEY_PATIENT_DOB, sqlEscapeString(patient.getDob()));
            cVal.put(KEY_PATIENT_NOK_NAME, sqlEscapeString(patient.getNextofKinName()));
            cVal.put(KEY_PATIENT_NOK_CONTACT, sqlEscapeString(patient.getNextofKinContact()));
            cVal.put(KEY_PATIENT_NOK_ADD1, sqlEscapeString(patient.getAddline1()));
            cVal.put(KEY_PATIENT_NOK_ADD2, sqlEscapeString(patient.getAddline2()));
            cVal.put(KEY_PATIENT_R2H, patient.HasRoadtoHealth());
            // cVal.put(KEY_PATIENT_R2H, sqlEscapeString(sch.getAddresslineTwo()));

            chadb.insert(patient_Table, null, cVal);
            added = true;
        } catch (Exception e) {
            // TODO: handle exception
            added= false;
        }

        return added;
        // Closing database connection
    }

    public boolean UpdatePatientData(Patient patient) {

        boolean added = false;
        try {
            if(chadb==null)
            {
                openDatabase();
            }

            ContentValues cVal = new ContentValues();

            cVal.put(KEY_PATIENT_NAME, sqlEscapeString(patient.getFirstname()));
            cVal.put(KEY_PATIENT_SURNAME, sqlEscapeString(patient.getSurname()));
            cVal.put(KEY_PATIENT_GRADE, sqlEscapeString(patient.getGrade()));
            cVal.put(KEY_PATIENT_GENDER, sqlEscapeString(patient.getGender()));
            cVal.put(KEY_PATIENT_SCHOOL, sqlEscapeString(patient.getSchool()));
            cVal.put(KEY_PATIENT_SCHOOLID, sqlEscapeString(String.valueOf(patient.getSchoolID()) ));
            cVal.put(KEY_PATIENT_DOB, sqlEscapeString(patient.getDob()));
            cVal.put(KEY_PATIENT_NOK_NAME, sqlEscapeString(patient.getNextofKinName()));
            cVal.put(KEY_PATIENT_NOK_CONTACT, sqlEscapeString(patient.getNextofKinContact()));
            cVal.put(KEY_PATIENT_NOK_ADD1, sqlEscapeString(patient.getAddline1()));
            cVal.put(KEY_PATIENT_NOK_ADD2, sqlEscapeString(patient.getAddline2()));
            cVal.put(KEY_PATIENT_R2H, patient.HasRoadtoHealth());


            chadb.update(patient_Table,   cVal, KEY_PATIENT_ID+" = ? ", new String[]{ String.valueOf(patient.getId()) });
            added = true;
        } catch (Exception e) {
            // TODO: handle exception
            added= false;
        }

        return added;
        // Closing database connection
    }





  //  ********************questionsubdivision Table Fields ************/

//  public QuestionSubdivision(int qusubID, int scCatID, String qusubName, String qusubDesc)

    public ArrayList<QuestionSubdivision> getAllQuestionSubData() {
        ArrayList<QuestionSubdivision> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + questionsubdivision_Table;

        try {
            if(chadb==null)
            {
                openDatabase();
            }
            Cursor cursor = chadb.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    QuestionSubdivision qs = new QuestionSubdivision(
                            Integer.parseInt(cursor.getString(0)),
                            Integer.parseInt(cursor.getString(3)),
                            (cursor.getString(1)),
                            (cursor.getString(2))
                    );

                    // Adding contact to list
                    List.add(qs);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException se) {
            // TODO: handle exception
            se.printStackTrace();;
        }
        return List;
    }




    public ArrayList<QuestionSubdivision> getQuestionSubData(int screenCatID) {
        ArrayList<QuestionSubdivision> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + questionsubdivision_Table+ " WHERE scCatID = "+screenCatID;

        try {
            if(chadb==null)
            {
                openDatabase();
            }
            Cursor cursor = chadb.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    QuestionSubdivision qs = new QuestionSubdivision(
                            Integer.parseInt(cursor.getString(0)),
                            Integer.parseInt(cursor.getString(3)),
                            (cursor.getString(1)),
                            (cursor.getString(2))
                    );

                    // Adding contact to list
                    List.add(qs);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException se) {
            // TODO: handle exception
            se.printStackTrace();;
        }
        return List;
    }



    /********************screeningquestion Table Fields ************/


//  ScreeningQuestion(int scquID, String scqudesc, int scCatID, int qusubID, String quType)

    public ArrayList<ScreeningQuestion> getAllScreeningQuestData() {
        ArrayList<ScreeningQuestion> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + screeningquestion_Table;

        try {
            if(chadb==null)
            {
                openDatabase();
            }
            Cursor cursor = chadb.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ScreeningQuestion sq = new ScreeningQuestion(
                            Integer.parseInt(cursor.getString(0)),
                            (cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            (cursor.getString(4))
                    );

                    // Adding item to list
                    List.add(sq);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException se) {
            // TODO: handle exception
            se.printStackTrace();;
        }
        return List;
    }


    public ArrayList<ScreeningQuestion> getScreeningQuestData(int screenCatID, int questSubID ) {
        ArrayList<ScreeningQuestion> List = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + screeningquestion_Table + " WHERE scCatID = "+screenCatID+" AND qusubID = "+questSubID;

        try {
            if(chadb==null)
            {
                openDatabase();
            }
            Cursor cursor = chadb.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ScreeningQuestion sq = new ScreeningQuestion(
                            Integer.parseInt(cursor.getString(0)),
                            (cursor.getString(1)),
                            Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)),
                            (cursor.getString(4))
                    );

                    // Adding item to list
                    List.add(sq);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (SQLException se) {
            // TODO: handle exception
            se.printStackTrace();;
        }


        return List;
    }













}
