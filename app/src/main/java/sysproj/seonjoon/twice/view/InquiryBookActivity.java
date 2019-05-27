package sysproj.seonjoon.twice.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import javax.annotation.Nullable;
import javax.xml.transform.Templates;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.entity.BookPostVO;
import sysproj.seonjoon.twice.view.custom.BookListAdapter;

public class InquiryBookActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_post);

        mContext = this;

        ListView listview;
        final BookListAdapter adapter;

        // Adapter 생성
        adapter = new BookListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.book_list);
        listview.setAdapter(adapter);

        adapter.addItem("2019년05월28일", "하하하");

    }
}
