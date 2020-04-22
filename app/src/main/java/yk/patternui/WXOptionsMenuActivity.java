package yk.patternui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import yk.pattern_ui.wx_options_menu.WXOptionsMenu;

public class WXOptionsMenuActivity extends AppCompatActivity {

    private WXOptionsMenu wxOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_options_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wx_options_menu_activity, menu);
        //建议在这里new MenuWindow
        wxOptionsMenu = new WXOptionsMenu(this);
        //指定menu
        wxOptionsMenu.setMenus(new int[]{
                        R.drawable.ic_chat,
                        R.drawable.ic_person_add,
                        R.drawable.ic_camera,
                        R.drawable.ic_payment,
                        R.drawable.ic_mail,
                },
                getResources().getStringArray(R.array.wx_options_menu_titles));
        wxOptionsMenu.setAnchorMenuItemId(R.id.more);
        wxOptionsMenu.setMenuClickListener((id -> {
            Toast.makeText(this, "点击id:" + id, Toast.LENGTH_SHORT).show();
            wxOptionsMenu.dismiss();
        }));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        wxOptionsMenu.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}
