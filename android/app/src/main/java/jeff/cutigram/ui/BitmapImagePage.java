package jeff.cutigram.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import jeff.cutigram.R;

public class BitmapImagePage extends Fragment {

    private Bitmap bitmap;

    public BitmapImagePage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_board_media_image, container, false);
        ImageView imageView = linearLayout.findViewById(R.id.image_view);

        imageView.setImageBitmap(bitmap);
        return linearLayout;
    }
}
