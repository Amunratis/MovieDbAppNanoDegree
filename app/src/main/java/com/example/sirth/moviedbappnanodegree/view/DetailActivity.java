package com.example.sirth.moviedbappnanodegree.view;

import android.os.Bundle;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sirth.moviedbappnanodegree.R;
import com.example.sirth.moviedbappnanodegree.dataModel.MovieDetails;
import com.example.sirth.moviedbappnanodegree.dataModel.MoviePages;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  /* Programmatical way of handling CollapsingActionBar, I've decided to go with XML
       initCollapsingToolbar();
*/

        MovieDetails movieDetails=getIntent().getParcelableExtra("parcel");

        ImageView imageView=(ImageView)findViewById(R.id.image);

        Glide.with(getApplicationContext())
                .load(movieDetails.getPoster_path())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

        ImageView backdrop=findViewById(R.id.imagev);
        Glide.with(getApplicationContext())
                .load(movieDetails.getBackdrop_path())
                .placeholder(R.drawable.ic_launcher_background)
                .into(backdrop);

        TextView title=findViewById(R.id.title2);
        title.setText(movieDetails.getTitle());

        TextView released=findViewById(R.id.released);
        released.setText(movieDetails.getRelease_date());

        TextView description=findViewById(R.id.description);
        description.setText(movieDetails.getOverview());

        TextView rating=findViewById(R.id.rating);
        rating.setText(Float.toString(movieDetails.getVote_average()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_collapsing);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset){
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                }else if (isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

}
