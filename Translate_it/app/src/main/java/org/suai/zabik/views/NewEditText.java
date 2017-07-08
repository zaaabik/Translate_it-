package org.suai.zabik.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by zabik on 23.04.17.
 */

@SuppressLint("AppCompatCustomView")
public class NewEditText extends EditText{
    public NewEditText(Context context) {
        super(context);
    }

    public NewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dispatchKeyEvent(event);
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public NewEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
