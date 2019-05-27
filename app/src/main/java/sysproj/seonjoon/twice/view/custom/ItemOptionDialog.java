package sysproj.seonjoon.twice.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.TwitterPost;
import sysproj.seonjoon.twice.loader.TwitterLoader;
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

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterLoader loader = new TwitterLoader(mContext);
                loader.DestoryTweet(post.getId(), new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess)
                            Toast.makeText(mContext, "게시글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "작업을 완료 할 수 없었습니다.", Toast.LENGTH_SHORT).show();
                        ItemOptionDialog.this.dismiss();;
                    }
                });
            }
        });
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

        followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterLoader loader = new TwitterLoader(mContext);
                loader.CreateFollowship(post.getUserProfile().getId(), new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess)
                            Toast.makeText(mContext, "팔로우 되었습니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "작업을 완료 할 수 없었습니다.", Toast.LENGTH_SHORT).show();
                        ItemOptionDialog.this.dismiss();
                    }
                });
            }
        });

        muteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterLoader loader = new TwitterLoader(mContext);
                loader.CreateMute(post.getUserProfile().getId(), new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess)
                            Toast.makeText(mContext, "뮤트 되었습니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "작업을 완료 할 수 없었습니다.", Toast.LENGTH_SHORT).show();
                        ItemOptionDialog.this.dismiss();
                    }
                });
            }
        });

        ignoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterLoader loader = new TwitterLoader(mContext);
                loader.CreateBlock(post.getUserProfile().getId(), new DataLoadCompleteCallback() {
                    @Override
                    public void Complete(boolean isSuccess, JSONObject result) {
                        if (isSuccess)
                            Toast.makeText(mContext, "블록 되었습니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, "작업을 완료 할 수 없었습니다.", Toast.LENGTH_SHORT).show();
                        ItemOptionDialog.this.dismiss();
                    }
                });
            }
        });
    }
}
