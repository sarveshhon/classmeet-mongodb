package com.sarveshhon.classmeet;

import static com.sarveshhon.classmeet.Helper.DEFAULT_BATCH;
import static com.sarveshhon.classmeet.Helper.getDay;
import static com.sarveshhon.classmeet.Helper.getSlot;
import static com.sarveshhon.classmeet.IntroActivity.prefs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sarveshhon.classmeet.databinding.ActivityMainBinding;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.mongo.MongoCollection;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    // MongoDb Database Parameters
    public static String APP_ID = "<your-app-id>";
    public static String CLIENT_NAME = "mongodb-atlas";
    public static String DATABASE_NAME = "MongoDB";
    public static String COLLECTION_NAME = "timetable";

    App app;
    MongoCollection mongoCollection;

    private static int fetchCount  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefresh.setRefreshing(true);

        // Initialise MongoDB Realm
        Realm.init(this);

        // Build app Object Connected to Server
        app = new App(new AppConfiguration.Builder(APP_ID).build());

        // Anonymous Login Implemented here
        app.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
//                Toast.makeText(this, "Login Successful ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

            try {
                // Path to MongoDB Collection with respect to Current User Authorisations
                mongoCollection = app.currentUser().getMongoClient(CLIENT_NAME).getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong.\nRestart App", Toast.LENGTH_SHORT).show();
            }

        });

        new Handler().postDelayed(() -> fetch(), 2000);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            fetch();
        });

        checkChips();

        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (binding.chipGroup.getCheckedChipId()) {
                case R.id.chipB1:
                    prefs.edit().putString("batch", "1").apply();
                    checkChips();
                    fetch();
                    break;
                case R.id.chipB2:
                    prefs.edit().putString("batch", "2").apply();
                    checkChips();
                    fetch();
                    break;
                case R.id.chipB3:
                    prefs.edit().putString("batch", "3").apply();
                    checkChips();
                    fetch();
                    break;
                case R.id.chipB4:
                    prefs.edit().putString("batch", "4").apply();
                    checkChips();
                    fetch();
                    break;
            }
        });
    }

    void checkChips() {
        switch (prefs.getString("batch", DEFAULT_BATCH)) {
            case "1":
                binding.chipB1.setChecked(true);
                break;
            case "2":
                binding.chipB2.setChecked(true);
                break;
            case "3":
                binding.chipB3.setChecked(true);
                break;
            default:
                binding.chipB4.setChecked(true);
                break;
        }
    }

    //Finding Document from Collection using Roll No
    void fetch() {
        binding.swipeRefresh.setRefreshing(true);
        try {
            // Document Object with Parameters
            Document filter = new Document().append("batch", prefs.getString("batch", DEFAULT_BATCH));

            // MongoDB Method for Deleting Single Document
            mongoCollection.findOne(filter).getAsync(result -> {
                if (result.isSuccess()) {
                    try {
                        // Document Object with result Parameters
                        Document data = (Document) result.get();
                        Document data2 = (Document) data.get(getDay());
                        Document data3 = (Document) data2.get(getSlot());
                        binding.tvDay.setText(getDay());
                        binding.tvSubject.setText(data3.getString("name"));
                        binding.tvDuration.setText(data3.getString("duration"));

                        binding.btnJoinMeet.setOnClickListener(v -> {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
                            if (launchIntent != null) {
                                if (getSlot().equals("0")) {
                                    Toast.makeText(this, "No Meeting Today!\nEnjoy your Free Time", Toast.LENGTH_SHORT).show();
                                } else {
                                    launchIntent.setAction(Intent.ACTION_VIEW);
                                    launchIntent.setData(Uri.parse(data3.getString("link")));
                                    startActivity(launchIntent);
                                }
                            } else {
                                Toast.makeText(this, "There is no Chrome Browser App\navailable in android", Toast.LENGTH_LONG).show();
                            }

                        });

                        binding.swipeRefresh.setRefreshing(false);

                        binding.btnJoinMeet.setOnLongClickListener(v -> {

                            if (!data3.getString("link").isEmpty()) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(data3.getString("name") + " Lecture Link", data3.getString("link"));
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(this, "Link Copied", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(this, "Try after sometime", Toast.LENGTH_LONG).show();
                            }

                            return true;
                        });

                    } catch (Exception e) {
                        binding.swipeRefresh.setRefreshing(false);
//                        Toast.makeText(this, "Not Found ", Toast.LENGTH_SHORT).show();
                        binding.tvDay.setText("Not Found");
                        binding.tvSubject.setText("Not Found");
                        binding.tvDuration.setText("Not Found");
                    }

//                    Toast.makeText(this, "Fetched Successful ", Toast.LENGTH_SHORT).show();

                } else {
//                    Toast.makeText(this, "Fetching Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("MyError", e.getMessage());
            fetchCount++;
            if(fetchCount<3) {
                new Handler().postDelayed(() -> fetch(), 2500);
            } else{

                Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
            }
        }

    }

}