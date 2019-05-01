package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;

public class ETOTOViewHolder extends BaseViewHolder {

    private ImageView extProfileImage;
    private ImageView extSNSLogo;
    private TextView extTitleText;
    private TextView extContentText;
    private TextView extContentTime;

    protected Button extReply;
    protected Button extThumbsUp;
    protected Button extShare;

    public ETOTOViewHolder(View itemView) {
        super(itemView);

        extSNSLogo = (ImageView) itemView.findViewById(R.id.card_title_ext_logo);
        extProfileImage = (ImageView) itemView.findViewById(R.id.card_profile_ext_image);
        extTitleText = (TextView) itemView.findViewById(R.id.card_title_ext_text);
        extContentTime = (TextView) itemView.findViewById(R.id.card_title_ext_time);
        extContentText = (TextView) itemView.findViewById(R.id.card_content_ext_text);

        extReply = (Button) itemView.findViewById(R.id.card_ext_reply);
        extThumbsUp = (Button) itemView.findViewById(R.id.card_ext_thumbs_up);
        extShare = (Button) itemView.findViewById(R.id.card_ext_share);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);
    }

    @Override
    protected void setImageContent(Post pos) {

    }

    @Override
    protected void setExtendField(Post extPost) {

    }
}
