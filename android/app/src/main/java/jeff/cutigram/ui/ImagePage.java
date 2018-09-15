package jeff.cutigram.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import jeff.cutigram.R;

public class ImagePage extends Fragment {

    private String photoSrc;

    public ImagePage(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_board_media_image, container, false);
        ImageView imageView = linearLayout.findViewById(R.id.image_view);
        Glide.with(this).load(photoSrc).into(imageView);
        return linearLayout;
    }
}
