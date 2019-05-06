package sysproj.seonjoon.twice.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sysproj.seonjoon.twice.R;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FragmentTwiceForm extends Fragment {

    private Context mContext;
    private TextView headText;
    private AutoCompleteTextView idEdit;
    private EditText passwordEdit;
    private AutoCompleteTextView emailEdit;
    private Button duplicateButton;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_twice_regist, container, false);

        headText = (TextView) root.findViewById(R.id.twice_form_head_text);
        idEdit = (AutoCompleteTextView) root.findViewById(R.id.twice_id);
        passwordEdit = (EditText) root.findViewById(R.id.twice_password);
        emailEdit = (AutoCompleteTextView) root.findViewById(R.id.twice_email);
        duplicateButton = (Button) root.findViewById(R.id.twice_duplicate);
        nextButton = (Button) root.findViewById(R.id.twice_next);
        mContext = getContext();

        headText.setText(getString(R.string.twice_form_head));
        nextButton.setText(getString(R.string.share_next_tag));

        setListener();

        return root;
    }

    private boolean checkEnableId() {
        String id = idEdit.getText().toString();
        return id.matches("^[a-z0-9_]*$");
    }

    private boolean checkPassword() {
        String password = passwordEdit.getText().toString();

        return password.matches("^[a-z0-9_]*$") && (password.length() >= UserSession.MIN_PASSWORD_LENGTH);
    }

    private void setListener() {
        duplicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEnableId()) {
                    CheckDuplicateAsnc cda = new CheckDuplicateAsnc();
                    cda.execute(idEdit.getText().toString());
                } else
                    Toast.makeText(mContext, "ID는 영어 소문자 및 숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPassword()) {
                    setData();
                    ((RegisterActivity) getActivity()).changeFragment(2);
                } else
                    Toast.makeText(mContext, "Password 칸이 비어 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        String id = idEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String email = emailEdit.getText().toString();
        ((RegisterActivity) getActivity()).setData(id, password, email);
    }

    private class CheckDuplicateAsnc extends AsyncTask<String, Void, Void> implements DialogInterface.OnClickListener {
        private String input;
        private Boolean result;

        @Override
        protected Void doInBackground(String... strings) {
            result = true;
            input = strings[0];

            //TODO : Check ID Duplicate Code

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            if (result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(input + "은 사용 가능한 아이디 입니다.\n 사용하시겠습니까?")
                        .setPositiveButton(getString(R.string.register_dialog_able_message_positive), this)
                        .setNegativeButton(getString(R.string.register_dialog_able_message_negative), null);
                AlertDialog resultDialog = builder.create();
                resultDialog.show();
            } else {
                Toast.makeText(mContext, "누락된 정보가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            idEdit.setEnabled(!result);
            passwordEdit.setEnabled(result);
            emailEdit.setEnabled(result);
            nextButton.setEnabled(result);
        }
    }
}
