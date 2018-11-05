package za.co.lbnkosi.watchdog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.lbnkosi.watchdog.ui.base.BaseActivity;
import za.co.lbnkosi.watchdog.ui.logind.LoginActivity;
import za.co.lbnkosi.watchdog.utils.MainMenuMap;

public class MainActivityTest extends AppCompatActivity {

    @BindView(R.id.testRecycler)
    RecyclerView friendList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivityTest.this, LoginActivity.class));
            finish();
        });

        ButterKnife.bind(this);

       /* ImageView imageView = findViewById(R.id.infoicon);
        imageView.setOnClickListener(v->{
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.cardviewAccount), "Hi there", "This is just the home screen. If you're finding it difficult to configure the app then just hit the configure button then help")
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(20)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.colorAccent)  // Specify the color of the description text
                            .textColor(R.color.textColorWhite)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.blueGrey)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional

                        }
                    });
        });

        SmoothProgressBar smoothProgressBar = findViewById(R.id.progress_bar);

        CardView cardAccount = findViewById(R.id.cardviewAccount);
        cardAccount.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardConfigure = findViewById(R.id.cardviewConfigure);
        cardConfigure.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, ConfigureActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardSettings = findViewById(R.id.cardviewSettings);
        cardSettings.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardDonate = findViewById(R.id.cardviewDonate);
        cardDonate.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });

        CardView cardAbout = findViewById(R.id.cardviewAbout);
        cardAbout.setOnClickListener(v->{
            smoothProgressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        });*/

       init();
       getFriendList();

    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList(){
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();

        Query query = db.collection("main_menu");

        FirestoreRecyclerOptions<MainMenuMap> response = new FirestoreRecyclerOptions.Builder<MainMenuMap>()
                .setQuery(query, MainMenuMap.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MainMenuMap, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull FriendsHolder holder, int position, MainMenuMap model) {
                holder.textName.setText(model.getTitle());
                holder.textTitle.setText(model.getDescription());

                holder.itemView.setOnClickListener(v -> {
                    //startActivity(new Intent(PurchaseHistoryActivity.this, PurchaseHistoryActivity.class));
                });
            }

            @NonNull
            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        friendList.setAdapter(adapter);
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView textName;
        @BindView(R.id.title)
        TextView textTitle;

        public FriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
