package sysproj.seonjoon.twice.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;

import sysproj.seonjoon.twice.R;

public class InstagramLoginButton extends androidx.appcompat.widget.AppCompatButton {

    private static final String TAG = "InstagramLogin";

    public InstagramLoginButton(@NonNull Context context) {
        this(context, null);
    }

    public InstagramLoginButton(Context context, AttributeSet set){
        this(context, set, android.R.attr.buttonStyle);
    }

    public InstagramLoginButton(Context context, AttributeSet set, int style){
        super(context, set, style);
        setupButton();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupButton() {
        final Resources res = getResources();
        super.setCompoundDrawablesWithIntrinsicBounds(
                res.getDrawable(R.drawable.instagram_white), null, null, null);
        super.setCompoundDrawablePadding(
                res.getDimensionPixelSize(R.dimen.instagram_login_btn_drawable_padding));
        super.setText(R.string.link_sns_instagram_login_text);
        super.setTextColor(res.getColor(R.color.white));
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                res.getDimensionPixelSize(R.dimen.instagram_login_text_size));
        super.setTypeface(Typeface.DEFAULT_BOLD);
        super.setPadding(res.getDimensionPixelSize(R.dimen.instagram_login_btn_left_padding), 0,
                res.getDimensionPixelSize(R.dimen.instagram_login_button_right_padding), 0);
        super.setBackgroundResource(R.drawable.instagram_login_btn_background);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setAllCaps(false);
        }
    }

    protected Activity getActivity() {
        if (getContext() instanceof Activity) {
            return (Activity) getContext();
        } else if (isInEditMode()) {
            return null;
        } else {
            throw new IllegalStateException("No Activity");
        }
    }
}
