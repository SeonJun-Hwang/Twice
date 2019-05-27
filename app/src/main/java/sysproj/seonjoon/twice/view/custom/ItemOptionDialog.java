package sysproj.seonjoon.twice.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class ItemOptionDialog extends Dialog {

    private TwitterPost post;
    private Context mContext;

    public ItemOptionDialog(Context context, TwitterPost post) {
        super(context, true, null);

        this.mContext = context;
        this.post = post;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다이얼로그 밖 화면 어둡게 만들기
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        if (post.getUserProfile().getId() == UserSession.TwitterProfile.getId() ||
                post.getRetweetUser() != null && post.getRetweetUser().getId() == UserSession.TwitterProfile.getId())
            initOwnModeDialog();
        else
            initOtherModeDialog();

    }

    private void initOwnModeDialog() {
        setContentView(R.layout.dialog_custom_item_option_own);
        TextView updateButton = findViewById(R.id.item_option_update);
        TextView removeButton = findViewById(R.id.item_option_remove);
    }

    private void initOtherModeDialog() {
        setContentView(R.layout.dialog_custom_item_option_other);
        TextView followText = findViewById(R.id.item_option_follow);
        TextView muteText = findViewById(R.id.item_option_mute);
        TextView ignoreText = findViewById(R.id.item_option_ignore);

        if (post.getRetweetUser() != null) {
            String userName = "@" + post.getRetweetUser().getName() + "님 ";
            followText.setText(userName + followText.getText());
            muteText.setText(userName + muteText.getText());
            ignoreText.setText(userName + ignoreText.getText());
        } else {
            String userName = "@" + post.getUserProfile().getName() + "님 ";
            followText.setText(userName + followText.getText());
            muteText.setText(userName + muteText.getText());
            ignoreText.setText(userName + ignoreText.getText());
        }
    }
}
