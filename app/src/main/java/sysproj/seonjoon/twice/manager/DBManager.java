package sysproj.seonjoon.twice.manager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.DBLoadSuccessCallback;
import sysproj.seonjoon.twice.staticdata.SNSTag;

public class DBManager {

    private final static String TAG = "DB_Manager";
    private static DBManager instance = null;
    private static FirebaseFirestore db = null;
    private static FirebaseAuth firebaseAuth = null;
    private static Map mResult;
    private static boolean locking;

    private DBManager() {
    }

    // Sync Processing
    public Map<String, Object> getDB(String collection, String doc) {
        Log.e(TAG, "Start Load Data");

        locking = true;

        if (db == null)
            db = FirebaseFirestore.getInstance();

        mResult = null;
        DocumentReference docRef = db.collection(collection).document(doc);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.e(TAG, "Complete Load Data");
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        mResult = docSnap.getData();
                        Log.e(TAG, "Success Load Data");
                    } else
                        Log.e(TAG, "Not Exist Doc");

                } else {
                    Log.e(TAG, "Fail Load Data");
                }
                locking = false;
            }
        });

        while (locking) ;

        Log.e(TAG, "End Load Data");

        return mResult;
    }

    // Async Processing
    public void getDB(String collection, String doc, @Nullable final DBLoadSuccessCallback callback) {
        Log.e(TAG, "Start Load Data");

        if (db == null)
            db = FirebaseFirestore.getInstance();

        mResult = null;
        DocumentReference docRef = db.collection(collection).document(doc);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.e(TAG, "Complete Load Data");
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        callback.LoadDataCallback(true, docSnap.getData());
                        Log.e(TAG, "Success Load Data");
                    } else {
                        callback.LoadDataCallback(false, null);
                        Log.e(TAG, "Not Exist Doc");
                    }

                } else {
                    Log.e(TAG, "Fail Load Data");
                }
            }
        });

        Log.e(TAG, "End Load Data");
    }

    public void addDB(final String collection, final String doc, Map<String, Object> data, @Nullable final DBAccessResultCallback callback) {
        if (db == null)
            db = FirebaseFirestore.getInstance();

        Log.e(TAG, "Insert Add DB");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        db.collection(collection).document(doc)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Success Upload" + collection + " - " + doc);
                        callback.AccessCallback(true);
                        locking = false;
                        countDownLatch.countDown();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Fail Upload" + '\n' + e);
                        callback.AccessCallback(false);
                        countDownLatch.countDown();
                    }
                });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createUser(Activity activity, String id, String password, @Nullable final DBAccessResultCallback callback) {
        Log.e(TAG, "Start Create User");

        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "Create User : " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.e(TAG, task.getResult().getUser().getUid());
                            callback.AccessCallback(true);
                        } else {
                            //Log.e(TAG, task.getException().toString());
                            callback.AccessCallback(false);
                        }
                    }
                });

        Log.e(TAG, "End Create User");
    }

    public void loginUser(Activity activity, String id, String password, @Nullable final DBAccessResultCallback callback) {
        locking = true;

        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        String authID = id + SNSTag.TWICE_EMAIL_TAIL;

        Log.e(TAG, authID);

        firebaseAuth.signInWithEmailAndPassword(authID, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "Login Success");
                            callback.AccessCallback(true);
                        } else {
                            Log.e(TAG, "Login Fail");
                            Log.e(TAG, task.getException().toString());
                            callback.AccessCallback(false);
                        }
                        locking = false;
                    }
                });

        while (locking);
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();

        return instance;
    }
}