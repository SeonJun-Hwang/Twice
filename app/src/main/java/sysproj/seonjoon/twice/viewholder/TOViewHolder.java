package sysproj.seonjoon.twice.viewholder;

import android.view.View;
import sysproj.seonjoon.twice.entity.Post;

public class TOViewHolder extends TwitterViewHolder {

    private static final String TAG = "TOHolder";

    public TOViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    protected void setImageContent(Post post) {
        // Not Contain Image Content
    }

    @Override
    protected void setExtendField(Post extPost) {

    }

}
