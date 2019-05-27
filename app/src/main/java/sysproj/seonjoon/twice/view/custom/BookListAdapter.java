package sysproj.seonjoon.twice.view.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.DataLoadCompleteCallback;
import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.BookPostVO;
import sysproj.seonjoon.twice.manager.DBManager;

public class BookListAdapter extends BaseAdapter {
    private ArrayList<BookPostVO> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public BookListAdapter(ArrayList<BookPostVO> items) {
        this.listViewItemList = items;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_post_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView Ficon = (ImageView) convertView.findViewById(R.id.facebook_icon);
        ImageView Iicon = (ImageView) convertView.findViewById(R.id.instargram_icon);
        ImageView Ticon = (ImageView) convertView.findViewById(R.id.twitter_icon);
        TextView dateText = (TextView) convertView.findViewById(R.id.date_text);
        final TextView postText = (TextView) convertView.findViewById(R.id.post_text);
        Button removeBtn = (Button) convertView.findViewById(R.id.post_delete_button);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final BookPostVO listViewItem = listViewItemList.get(position);

        dateText.setText(listViewItem.getDateToString());
        postText.setText(listViewItem.getMessage());

        if (!listViewItem.isCheckFacebook())
            Ficon.setVisibility(View.INVISIBLE);
        if (!listViewItem.isCheckInstargram())
            Iicon.setVisibility(View.INVISIBLE);
        if (!listViewItem.isCheckTwitter())
            Ticon.setVisibility(View.INVISIBLE);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postText.getVisibility() == View.GONE) {
                    postText.setVisibility(View.VISIBLE);
                } else
                    postText.setVisibility(View.GONE);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setMessage("선택한 게시글이 삭제됩니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                DBManager.getInstance().DeleteBookInquiry(listViewItem.getId(), new DataLoadCompleteCallback() {
                                    @Override
                                    public void Complete(boolean isSuccess, JSONObject result) {
                                        dialogInterface.dismiss();
                                        listViewItemList.remove(position);
                                        BookListAdapter.this.notifyDataSetChanged();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

}
