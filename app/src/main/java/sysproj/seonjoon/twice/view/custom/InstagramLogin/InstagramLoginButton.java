package sysproj.seonjoon.twice.view.custom.InstagramLogin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.google.firebase.database.annotations.NotNull;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.view.SNSLinkingActivity;

import static android.app.Activity.RESULT_OK;

public class InstagramLoginButton extends androidx.appcompat.widget.AppCompatButton {

    private Context context;
    private static final String TAG = "InstagramLogin";
    private InstagramLoginCallBack loginCallBack;

    public InstagramLoginButton(@NonNull Context context) {
        this(context, null);
    }

    public InstagramLoginButton(Context context, AttributeSet set) {
        this(context, set, android.R.attr.buttonStyle);
    }

    public InstagramLoginButton(Context context, AttributeSet set, int style) {
        super(context, set, style);
        setupButton();

        this.context = context;
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
        super.setOnClickListener(new ButtonClickListener());


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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == InstagramActivity.reqCode) {
            if (resultCode == RESULT_OK) {
                String token = data.getStringExtra("result");
                loginCallBack.onSuccess(token);
            } else {
                String message = data.getStringExtra("fail_message");
                loginCallBack.onCancel(message);
            }
        }
    }

    public void registerCallback(@NotNull InstagramLoginCallBack loginCallback) {
        this.loginCallBack = loginCallback;
    }

    private class ButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, InstagramActivity.class);
            ((SNSLinkingActivity) context).startActivityForResult(intent, InstagramActivity.reqCode);
        }
    }
}
