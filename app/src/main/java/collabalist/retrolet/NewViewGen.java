package collabalist.retrolet;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by deepak on 27/3/18.
 */

public class NewViewGen {
    public static EditText addEditText(Context context, float heightDP, float widthDP, int textGravity
            , String lbl) {
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                (widthDP > 0 ? getPixFromDP(context, widthDP) : (int) widthDP),
                (heightDP > 0 ? getPixFromDP(context, heightDP) : (int) heightDP));
        EditText editText = new EditText(context);
        editText.setGravity(textGravity);
        editText.setHint(lbl);
        editText.setLayoutParams(lparams);
        return editText;
    }

    public static Button addButton(Context context, float heightDP, float widthDP, int textGravity
            , String lbl) {
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(
                (widthDP > 0 ? getPixFromDP(context, widthDP) : (int) widthDP),
                (heightDP > 0 ? getPixFromDP(context, heightDP) : (int) heightDP));
        Button button = new Button(context);
        button.setGravity(textGravity);
        button.setText(lbl);
        button.setLayoutParams(lparams);
        return button;
    }


    public static int getPixFromDP(Context c, float value) {
        Resources r = c.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return (int) px;
    }
}