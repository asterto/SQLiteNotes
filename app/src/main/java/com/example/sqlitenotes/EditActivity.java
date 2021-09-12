package com.example.sqlitenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlitenotes.adapter.ListItem;
import com.example.sqlitenotes.db.MyConstants;
import com.example.sqlitenotes.db.MyDbManager;

public class EditActivity extends AppCompatActivity {

    private EditText edTitle, edDesc;
    private MyDbManager myDbManager;
    private boolean isEditState = true;
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    private void init() {
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        myDbManager = new MyDbManager(this);
    }

    private void getMyIntents() {
        Intent i = getIntent();
        if (i != null) {
            item = (ListItem) i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = i.getBooleanExtra(MyConstants.EDIT_STATE, true);
            if (!isEditState) {
                edTitle.setText(item.getTitle());
                edDesc.setText(item.getDesc());
            }
        }

    }

    public void onClickSave(View view) {
        final String title = edTitle.getText().toString();
        final String desc = edDesc.getText().toString();
        if (title.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            if (isEditState) {
                AppExecuter.getInstance().getSubIO().execute(() -> myDbManager.insertToDb(title, desc));
            } else {
                myDbManager.updateItem(title, desc, item.getId());
            }
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            myDbManager.closeDb();
            finish();

        }
    }


}
