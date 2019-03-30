package sysproj.seonjoon.twice.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.view.RegisterActivity;

public class FragmentConsentForm extends Fragment{

    private TextView headText;
    private TextView personalText;
    private TextView facebookText;
    private TextView instagramText;
    private TextView twitterText;
    private CheckBox personalCheck;
    private CheckBox facebookCheck;
    private CheckBox instagramCheck;
    private CheckBox twitterCheck;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_consent_form, container, false);

        headText = (TextView) root.findViewById(R.id.consent_form_head_text);
        personalText = (TextView) root.findViewById(R.id.consent_personal_text);
        facebookText = (TextView) root.findViewById(R.id.consent_facebook_text);
        instagramText = (TextView) root.findViewById(R.id.consent_instagram_text);
        twitterText = (TextView) root.findViewById(R.id.consent_twitter_text);
        personalCheck = (CheckBox) root.findViewById(R.id.consent_personal_check);
        facebookCheck = (CheckBox) root.findViewById(R.id.consent_facebook_check);
        instagramCheck = (CheckBox) root.findViewById(R.id.consent_instagram_check);
        twitterCheck = (CheckBox) root.findViewById(R.id.consent_twitter_check);
        nextButton = (Button) root.findViewById(R.id.consent_form_next);

        headText.setText(getString(R.string.consent_form_head));
        personalText.setText(getString(R.string.consent_form_personal));
        facebookText.setText(getString(R.string.consent_form_facebook));
        instagramText.setText(getString(R.string.consent_form_instagram));
        twitterText.setText(getString(R.string.consent_form_twitter));
        nextButton.setText(getString(R.string.share_next_tag));

        setListener();

        return root;
    }

    private void setListener()
    {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegisterActivity)getActivity()).changeFragment(1);
            }
        });

        personalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Show Dialog about Using Personal Information Doc.
            }
        });

        facebookText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Show Dialog about Using Facebook Token Doc.
            }
        });

        instagramText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Show Dialog about Using Instagram Token Doc.
            }
        });

        twitterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Show Dialog about Using Twitter Token Doc.
            }
        });
    }
}
