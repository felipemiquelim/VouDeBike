package br.com.puc.sqlite;

/**
 * Created by Felipe on 19/10/2015.
 * http://hmkcode.com/android-simple-sqlite-database-tutorial/
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.puc.sqlite.br.com.puc.sqlite.crud.TB_CICLISTA;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "VouDeBikeDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        createTables();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Creation of tables
        //database = db;
        //createTables();

    }

    private void createTables() {
        //create_TB_CICLISTA();
    }

    private void create_TB_CICLISTA() {
        // SQL statement to create TB_CICLISTA table
        String TB_CICLISTA = "CREATE TABLE TB_CICLISTA ( " +
                "NCICLI INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CNOME VARCHAR, "+
                "CEMAIL VARCHAR, "+
                "CCELULAR INTEGER, "+
                "CSTATUS BOOLEAN )";

        // create TB_CICLISTA table
        SQLiteDatabase db = getdatabase();
        //db.execSQL("DROP TABLE IF EXISTS TB_CICLISTA");
        db.execSQL(TB_CICLISTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UpgradeTables(db, oldVersion, newVersion);
    }

    private void UpgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgrade_TB_CICLISTA(db, oldVersion, newVersion);
    }

    private void upgrade_TB_CICLISTA(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older TB_CICLISTA table if existed
        db.execSQL("DROP TABLE IF EXISTS TB_CICLISTA");

        // create fresh TB_CICLISTA table
        this.onCreate(db);
    }

    public void teste (TB_CICLISTA tb_ciclista) {
        SQLiteDatabase db;
        /**
         * CRUD Operations
         * */
        // add ciclista
        /*db = getdatabase();
        tb_ciclista.add_ciclista(new ciclista("Felipe Sarti Miquelim", "felipemiquelim@gmail.com", 966093231, true), db);
        db = getdatabase();
        tb_ciclista.add_ciclista(new ciclista("Barack Obama", "obama.barack@gmail.com", 554567989, true), db);
        db = getdatabase();
        tb_ciclista.add_ciclista(new ciclista("Michael Jackson", "mjay@gmail.com", 556578901, false), db);*/

        // get all ciclista
        db = getdatabase();
        List<ciclista> list = tb_ciclista.getAllciclistas(db);

        // delete one ciclista
        db = getdatabase();
        tb_ciclista.delete_ciclista(list.get(1), db);

        // get all ciclista
        db = getdatabase();
        tb_ciclista.getAllciclistas(db);
    }

    private SQLiteDatabase getdatabase() {
        SQLiteDatabase database = this.getWritableDatabase();
        return  database;
    }
}
