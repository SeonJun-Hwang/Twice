package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import android.widget.Button;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.Post;

public abstract class FacebookViewHolder extends BaseViewHolder {

    protected Button reply;
    protected Button thumbsUp;
    protected Button share;

    public FacebookViewHolder(View itemView) {
        super(itemView);

        reply = (Button) itemView.findViewById(R.id.card_reply);
        thumbsUp = (Button) itemView.findViewById(R.id.card_thumbs_up);
        share = (Button) itemView.findViewById(R.id.card_share);
    }

    @Override
    public void bind(Post item) {
        super.bind(item);

        setRFSField();
    }

    @Override
    protected void setSubTitle(Post item) {
        subTitleText.setText(calTime(item.getCreateDate()));
    }

    private void setRFSField(){
        thumbsUp.setText("Thumbs Up!");
        reply.setText("Comment");
        share.setText("Share");
    }
}
