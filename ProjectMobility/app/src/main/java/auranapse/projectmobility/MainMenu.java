package auranapse.projectmobility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener
{
    private Button btn_start;
    private Button btn_help;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_main_menu);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_help = (Button)findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        Intent intent = new Intent();
        intent.setClass(this, gameScene.class);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if(v == btn_start)
        {
            intent.setClass(this, gameScene.class);
        }
        else if(v == btn_help)
        {
            //intent.setClass(this, Helppage.class);
        }

        startActivity(intent);
    }

    protected void onPause()
    {
        super.onPause();
    }
}
