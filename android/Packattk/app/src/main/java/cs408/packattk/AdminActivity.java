package cs408.packattk;


import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


public class AdminActivity extends ActionBarActivity {

    private ListView listView;
    private Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // fix this to read username + any other needed information
        //Intent intent = getIntent();

        Button refreshButton= (Button) findViewById(R.id.refresh_button);
        Button scanButton = (Button) findViewById(R.id.scan_button);
        listView = (ListView) findViewById(R.id.listView2);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRefresh();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptScanPackage();
            }
        });
        List<String> packageList = client.getPackages(Data.username);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,packageList);
        listView.setAdapter(adapter);

    }

    public void attemptRefresh() {
        List<String> packageList = client.getPackages(Data.username);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,packageList);
        listView.setAdapter(adapter);
    }

    public void attemptScanPackage() {
        String username = "";
        String packageid = "";
        String location = "";

        //Create a dialog for the user to enter the package information into the system
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_package_entry);

        EditText usernameBox = (EditText)findViewById(R.id.user_email);
        EditText packageIdBox = (EditText)findViewById(R.id.packageId);
        EditText locationBox = (EditText)findViewById(R.id.location);

        client.addPackage(username, packageid, location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
