package com.example.andreas.walk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.andreas.walk_eshop.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkStateReceiver.NetworkStateReceiverListener {

    private WebView eshopwebview;
    boolean doubleBackToExitPressedOnce = false;
    boolean clearHistory = false;
    private NetworkStateReceiver networkStateReceiver;
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListView expListView;
    private ExpandListAdapter listAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;


    @Override
    protected void onStart() {
        super.onStart();
        mySwipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (eshopwebview.getScrollY() == 0)
                            mySwipeRefreshLayout.setEnabled(true);
                        else
                            mySwipeRefreshLayout.setEnabled(false);

                    }
                });

    }


    @Override
    protected void onStop() {
        super.onStop();
        mySwipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
    }

    private void refreshEnd() {
        // TODO implement a refresh
        mySwipeRefreshLayout.setRefreshing(false); // Disables the refresh icon
    }


    public void refreshWebview(){

        eshopwebview.clearCache(true);
        eshopwebview.clearFormData();
        eshopwebview.reload();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableExpandableList();
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swiperefresh);


        FirebaseMessaging.getInstance().subscribeToTopic("all");



        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshWebview();
                        refreshEnd();
                    }
                }


        );





        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener( this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        ImageView menu = (ImageView) findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);

            }
        });

        ImageView cart = (ImageView) findViewById(R.id.cart_button);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eshopwebview.loadUrl("https://www.walk.gr/basket.asp");
                clearHistory = false;
            }
        });

        ImageView user = (ImageView) findViewById(R.id.user_button);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eshopwebview.loadUrl("https://www.walk.gr/signin.asp");
                clearHistory = false;
            }
        });

        ImageView home = (ImageView) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eshopwebview.loadUrl("https://www.walk.gr");
                clearHistory = true;
            }
        });

        //from main start

        eshopwebview = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = eshopwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        eshopwebview.loadUrl("https://www.walk.gr");
        clearHistory = false;
        eshopwebview.setWebViewClient(new WebViewClient(){




            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                visible1();
            }



            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                eshopwebview.loadUrl("javascript:(function() { " +
                        "var head = document.getElementById('header').style.display='none'; " + "var footer = document.getElementById('footer').style.display='none'; "  + "var menu = document.getElementById('navigation').style.display='none'; "  +
                        "})()" );

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Fun Part will be here :)
//                eshopwebview.loadUrl("javascript:(function() { " +
//                        "var head = document.getElementById('header').style.display='none'; " + "var footer = document.getElementById('footer').style.display='none'; " +
//                        "})()" );
                  //  eshopwebview.clearCache(true);


                    unvisible();
                    if (clearHistory)
                    {
                        clearHistory = false;
                        eshopwebview.clearHistory();
                    }
                    super.onPageFinished(view, url);

            }


        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //travaei to token toy FCM kai to emfanizei sto logcat
        Log.d("token from service", "token =" +  SharedPrefManager.getmInstance(getApplicationContext()).getToken());

  /*      String key = getIntent().getStringExtra("key");
        Log.d("didthekeycame", "onCreate: "+key);*/


            checkForOffers();


    }


    @Override
    protected void onResume()
    {
        super.onResume();
        checkForOffers();
    }

    private void checkForOffers() {



        //gets data if app was on bg during FCM
        if (getIntent().getExtras() != null) {

            String key = getIntent().getStringExtra("key");
            String value = getIntent().getStringExtra("value");

            Log.d("getdatafromdrawer", "Key: " + key + " Value: " + value);


            if(key != null) {
                if (key.equals("offer")) {
                    key = "https://www.walk.gr" + value;
                    eshopwebview.loadUrl(key);
                } else {
                    eshopwebview.loadUrl("https://www.walk.gr");
                }
            }else {
                eshopwebview.loadUrl("https://www.walk.gr");
            }
        }else {
            eshopwebview.loadUrl("https://www.walk.gr");


        }



    }


    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (eshopwebview.canGoBack()) {
                eshopwebview.goBack();
            } else {

                if (doubleBackToExitPressedOnce) {
                    finish();
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

        }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


 /*       if (id == R.id.nav_andras_kal) {
            // Handle the andras action
            eshopwebview.loadUrl("https://www.walk.gr/categories/47/andras-/?flag2=&filter1=&filter1=%CE%9A%CE%91%CE%9B%CE%A4%CE%A3%CE%95%CE%A3&filter2=&filter3=&filter4=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_andras_es) {
            eshopwebview.loadUrl("https://www.walk.gr/categories/47/andras-/?flag2=&filter1=&filter1=%CE%95%CE%A3%CE%A9%CE%A1%CE%9F%CE%A5%CE%A7%CE%91&filter2=&filter3=&filter4=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_andras_ou) {
            eshopwebview.loadUrl("https://www.walk.gr/categories/47/andras-/?flag2=&filter1=OUTWEAR&filter2=&filter3=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_gynaika_kal) {
            // Handle the andras action
            eshopwebview.loadUrl("https://www.walk.gr/categories/48/gynaika-/?flag2=&filter1=&filter1=%CE%9A%CE%91%CE%9B%CE%A4%CE%A3%CE%95%CE%A3&filter2=&filter3=&filter4=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_gynaika_es) {
            eshopwebview.loadUrl("https://www.walk.gr/categories/48/gynaika-/?flag2=&filter1=%CE%95%CE%A3%CE%A9%CE%A1%CE%9F%CE%A5%CE%A7%CE%91&filter2=&filter3=&filter4=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_gynaika_ou) {
            eshopwebview.loadUrl("https://www.walk.gr/categories/48/gynaika-/?flag2=&filter1=OUTWEAR&filter2=&filter3=&filter5=");
            clearHistory = false;
        } else if (id == R.id.nav_paidi_kal) {
            eshopwebview.loadUrl("https://www.walk.gr/categories/49/paidi-/?flag2=&filter1=&filter1=%CE%9A%CE%91%CE%9B%CE%A4%CE%A3%CE%95%CE%A3&filter2=&filter3=&filter4=&filter5=");
            clearHistory = false;
        } else*/ /*if (id == R.id.nav_bamboo) {
            eshopwebview.loadUrl("https://www.walk.gr/search.asp?filter5=BAMBOO");
            clearHistory = false;
        } else if (id == R.id.nav_logariasmos) {
            eshopwebview.loadUrl("https://www.walk.gr/signin.asp");
            clearHistory = false;
        } else if (id == R.id.nav_kalathi) {
            eshopwebview.loadUrl("https://www.walk.gr/basket.asp");
            clearHistory = false;
        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("WrongConstant")
    public void visible1(){

        WebView webview = (WebView) findViewById(R.id.webView);


        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);

     //   webview.setVisibility(10);

        bar.setVisibility(0);
    }

    @SuppressLint("WrongConstant")
    public void unvisible(){

        WebView webview = (WebView) findViewById(R.id.webView);

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);

   //     webview.setVisibility(0);

        bar.setVisibility(10);

    }


    @Override
    public void networkAvailable() {

        /* TODO: Your connection-oriented stuff here */
            }

    @Override
    public void networkUnavailable() {

        launchNoConnectionActivity();




        /* TODO: Your disconnection-oriented stuff here */
        Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();

    }

    private void launchNoConnectionActivity() {

        Intent intent = new Intent(this, NoConnectionActivity.class);
        startActivity(intent);
        finish();
    }







    //expand list adapter

    private void enableExpandableList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        prepareListData(listDataHeader, listDataChild);
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();

                if(((listDataHeader.get(groupPosition).toString()).equals("BAMBOO"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/search.asp?filter5=BAMBOO");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                    return true;
                }else if(((listDataHeader.get(groupPosition).toString()).equals("Ο λογαριασμός μου"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/signin.asp");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                    return true;
                }else if(((listDataHeader.get(groupPosition).toString()).equals("Καλάθι"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/basket.asp");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                    return true;
                }
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
/*                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/



            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
 /*               Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
*/
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:

                // till here
 /*               Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/

                Log.d("switch", listDataHeader.get(groupPosition).toString()
                        + " : "
                        + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition).toString());

                if((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΑΝΔΡΑΣ : Κάλτσες")) {
                         eshopwebview.loadUrl("https://www.walk.gr/categories/388/andraskaltses-/");
                         clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                    return false;
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΑΝΔΡΑΣ : Εσώρουχα"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/404/andrasesoroyxa-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΑΝΔΡΑΣ : Outerwear"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/406/andrasoutwear-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΓΥΝΑΙΚΑ : Κάλτσες"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/387/gynaikakaltses-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΓΥΝΑΙΚΑ : Εσώρουχα"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/407/gynaikaesoroyxa-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΓΥΝΑΙΚΑ : Outerwear"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/414/gynaikaoutwear-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("ΠΑΙΔΙ : Κάλτσες"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/categories/405/paidikaltses-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
/*                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("BAMBOO : "))) {
                eshopwebview.loadUrl("https://www.walk.gr/search.asp?filter5=BAMBOO");
                clearHistory = false;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Ο λογαριασμός μου : "))) {
                    eshopwebview.loadUrl("https://www.walk.gr/signin.asp");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Καλάθι : "))) {
                    eshopwebview.loadUrl("https://www.walk.gr/basket.asp");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);*/
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Πληροφορίες : Επικοινωνία"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/content/25/epikoinonia-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Πληροφορίες : Πληρωμή/Αποστολή"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/content/27/pliromiapostoli-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Πληροφορίες : Επιστροφές/Αλλαγές"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/content/29/epistrofesallages-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Πληροφορίες : Όροι Χρήσης"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/content/60/oroi-xrisis-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
                }else if(((listDataHeader.get(groupPosition).toString()+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString()).equals("Πληροφορίες : Ασφάλεια Πληρωμών"))) {
                    eshopwebview.loadUrl("https://www.walk.gr/content/62/asfaleia-pliromon-/");
                    clearHistory = false;
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(Gravity.LEFT);
            }



                return false;
            }
        });}



    private void prepareListData(List<String> listDataHeader, Map<String,
                List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("ΑΝΔΡΑΣ");
        listDataHeader.add("ΓΥΝΑΙΚΑ");
        listDataHeader.add("ΠΑΙΔΙ");
        listDataHeader.add("BAMBOO");
        listDataHeader.add("Ο λογαριασμός μου");
        listDataHeader.add("Καλάθι");
        listDataHeader.add("Πληροφορίες");

        // Adding child data
        List<String> andras = new ArrayList<String>();
        andras.add("Κάλτσες");
        andras.add("Εσώρουχα");
        andras.add("Outerwear");


        List<String> gynaika = new ArrayList<String>();
        gynaika.add("Κάλτσες");
        gynaika.add("Εσώρουχα");
        gynaika.add("Outerwear");

        List<String> paidi = new ArrayList<String>();
        paidi.add("Κάλτσες");

        List<String> bamboo = new ArrayList<String>();

        List<String> user = new ArrayList<String>();

        List<String> basket = new ArrayList<String>();

        List<String> about = new ArrayList<String>();
        about.add("Επικοινωνία");
        about.add("Πληρωμή/Αποστολή");
        about.add("Επιστροφές/Αλλαγές");
        about.add("Όροι Χρήσης");
        about.add("Ασφάλεια Πληρωμών");



        listDataChild.put(listDataHeader.get(0), andras); // Header, Child data
        listDataChild.put(listDataHeader.get(1), gynaika);
        listDataChild.put(listDataHeader.get(2), paidi);
        listDataChild.put(listDataHeader.get(3), bamboo);
        listDataChild.put(listDataHeader.get(4), user);
        listDataChild.put(listDataHeader.get(5), basket);
        listDataChild.put(listDataHeader.get(6), about);
    }

}
