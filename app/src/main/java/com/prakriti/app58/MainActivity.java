package com.prakriti.app58;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtComputerName, edtComputerType;
    Button btnAdd, btnDelete;
    ListView listView;

    List<Computer> allComputers;
    ArrayList<String> computerNames;
    MySQLiteHandler databaseHandler;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtComputerName = findViewById(R.id.edtComputerName);
        edtComputerType = findViewById(R.id.edtComputerType);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        listView = findViewById(R.id.listView);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        databaseHandler = new MySQLiteHandler(this);
        allComputers = databaseHandler.getAllComputers();
        computerNames = new ArrayList<>();

        if(allComputers.size()>0) {
            for (int i=0; i<allComputers.size(); i++) {
                Computer computer = allComputers.get(i);
                computerNames.add(computer.getComputerName() + " - " + computer.getComputerType());
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, computerNames);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdd:
                if(edtComputerName.getText().toString().matches("") || edtComputerType.getText().toString().matches(""))
                    return;
                Computer computer = new Computer(edtComputerName.getText().toString(), edtComputerType.getText().toString());
                databaseHandler.addComputer(computer);
                allComputers.add(computer);
                computerNames.add(computer.getComputerName() + " - " + computer.getComputerType());
                edtComputerName.setText("");
                edtComputerType.setText("");
                break;

            case R.id.btnDelete:
                if(allComputers.size()>0) {
                    databaseHandler.deleteComputer(allComputers.get(0));
                    allComputers.remove(0);
                    computerNames.remove(0);
                } else
                    return;
                break;
        }
        adapter.notifyDataSetChanged(); // to update listview after adding or deleting
    }

}