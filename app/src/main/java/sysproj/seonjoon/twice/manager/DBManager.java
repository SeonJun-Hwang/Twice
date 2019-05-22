package sysproj.seonjoon.twice.manager;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import sysproj.seonjoon.twice.BuildConfig;
import sysproj.seonjoon.twice.DBAccessResultCallback;
import sysproj.seonjoon.twice.DBLoadSuccessCallback;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class DBManager {

    private final static String TAG = "DB_Manager";
    private static DBManager instance = null;
    private static FirebaseUser user;
    private static Map mResult;
    private static boolean locking;

    private DBManager() {
    }

    // Sync Processing
    public Map<String, Object> getDB(String collection, String doc) {
        Log.e(TAG, "Start Load Data");

        locking = true;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    void getDB(String collection, String doc, final DBLoadSuccessCallback callback) {
        Log.e(TAG, "Start Load Data");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    @Deprecated
    public void addDB(final String collection, final String doc, Map<String, Object> data, @Nullable final DBAccessResultCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String authID = id + SNSTag.TWICE_EMAIL_TAIL;

        Log.e(TAG, authID);

        firebaseAuth.signInWithEmailAndPassword(authID, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().getUser();
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

        while (locking) ;
    }

    public void checkDuplicateUser(String id, final DBAccessResultCallback callback) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.fetchProvidersForEmail(id + SNSTag.TWICE_EMAIL_TAIL)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        List providers = task.getResult().getProviders();
                        boolean result = true;

                        if (providers != null)
                            result = providers.isEmpty();

                        callback.AccessCallback(result);
                    }
                });
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();

        return instance;
    }

    public void saveFacebookToken(final String collection, final DBAccessResultCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (UserSession.FacebookToken != null) {
            Map<String, Object> data = new HashMap<>();

            data.put(SNSTag.FacebookUIDTag, UserSession.FacebookToken.getUserId());
            data.put(SNSTag.FacebookTokenTag, UserSession.FacebookToken.getToken());
            data.put(SNSTag.FacebookAIDTag, UserSession.FacebookToken.getApplicationId());
            data.put(SNSTag.FacebookPermissionTag, Set2String(UserSession.FacebookToken.getPermissions()));
            data.put(SNSTag.FacebookDPermissionTag, Set2String(UserSession.FacebookToken.getDeclinedPermissions()));
            data.put(SNSTag.FacebookTokenSourceTag, UserSession.FacebookToken.getSource().toString());
            data.put(SNSTag.FacebookExpTimeTag, UserSession.FacebookToken.getExpires().toString());
            data.put(SNSTag.FacebookLastTimeTag, UserSession.FacebookToken.getLastRefresh().toString());
            data.put(SNSTag.FacebookDataAccExpTimeTag, UserSession.FacebookToken.getDataAccessExpirationTime().toString());

            db.collection(collection).document(BuildConfig.FacebookDocTag)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "Facebook Success : " + "Success");

                            callback.AccessCallback(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                            callback.AccessCallback(false);
                        }
                    });

        } else
            Log.e(TAG, "Facebook Token is Null");
    }

    public void saveTwitterToken(final String collection, final DBAccessResultCallback callback) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (UserSession.TwitterToken != null) {
            Map<String, Object> data = new HashMap<>();

            data.put(SNSTag.TwitterTokenTag, UserSession.TwitterToken.getAuthToken().token);
            data.put(SNSTag.TwitterTokenSecretTag, UserSession.TwitterToken.getAuthToken().secret);
            data.put(SNSTag.TwitterUNameTag, UserSession.TwitterToken.getUserName());
            data.put(SNSTag.TwitterUIDTag, UserSession.TwitterToken.getUserId());

            Log.e(TAG, "Facebook Save");

            db.collection(collection).document(BuildConfig.TwitterDocTag)
                    .set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e(TAG, "Facebook Success : " + task.isSuccessful());
                            callback.AccessCallback(task.isSuccessful());
                        }
                    });
        }

    }

    public void removeFacebookToken(final String collection, final DBAccessResultCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(BuildConfig.FacebookDocTag)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.AccessCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.AccessCallback(false);
                    }
                });
    }

    public void removeTwitterToken(final String collection, final DBAccessResultCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection).document(BuildConfig.TwitterDocTag)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.AccessCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.AccessCallback(false);
                    }
                });
    }

    public FirebaseUser getUser() {
        return user;
    }

    private String Set2String(Set<String> input) {
        String result = new String();

        for (String item : input)
            result += item + '\t';
        return result;
    }
}