package edu.css.cis3334.vogellasampleinclass;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource datasource;      // our link to the datasource for SQLite access
    private int listPostion = 0;                // currently selected item in the list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        datasource = new CommentsDataSource(this);
        datasource.open();

        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        // need to grap the list item click so we remember what item is selected
        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                listPostion= position;
                //Toast.makeText(SuggestionActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> myListAdapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;
        switch (view.getId()) {
            case R.id.add:
                //String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
                //int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                EditText txtComment = (EditText) findViewById(R.id.etComment);
                comment = datasource.createComment(  txtComment.getText().toString()  );
                myListAdapter.add(comment);
                break;
            case R.id.delete:
                // The selected item postion is stored in the variable listPostion by the onItemClick listener
                if (myListAdapter.getCount() > listPostion) {
                    comment = (Comment) getListAdapter().getItem(listPostion);
                    datasource.deleteComment(comment);
                    myListAdapter.remove(comment);
                }
                break;
        }
        myListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
