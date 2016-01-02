package com.rdc.imageloader.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.rdc.imageloader.R;
import com.rdc.imageloader.net.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private ImageView imageview;
    private ImageAdapter adapter;

    private List<String> mUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mUrlList = new ArrayList<>();
        mUrlList.add("http://d.hiphotos.baidu.com/image/h%3D360/sign=13505bc7af18972bbc3a06ccd6cc7b9d/267f9e2f07082838dc76bbc7bc99a9014d08f1ee.jpg");
        mUrlList.add("http://c.hiphotos.baidu.com/image/h%3D360/sign=d4bd2930a96eddc439e7b2fd09dab6a2/377adab44aed2e73519d81f98301a18b86d6faeb.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=2b51ed55cf134954611eee62664f92dd/ac6eddc451da81cb4c55d6195666d016082431b6.jpg");
        mUrlList.add("http://b.hiphotos.baidu.com/image/h%3D360/sign=52b74a248244ebf872716239e9f8d736/37d12f2eb9389b50761593758035e5dde7116e2a.jpg");
        mUrlList.add("http://e.hiphotos.baidu.com/image/h%3D360/sign=c9341858b6b7d0a264c9029bfbee760d/b2de9c82d158ccbf0881c1d01dd8bc3eb135411e.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=897a01ed37fa828bce239be5cd1e41cd/0eb30f2442a7d933fdd1619ba94bd11372f001d8.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=f7030d1d1a178a82d13c79a6c602737f/6c224f4a20a446237cd252b39c22720e0df3d7c3.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=d9e0c234f91f4134ff370378151d95c1/c995d143ad4bd1130c0ee8e55eafa40f4afb0521.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=d932a108b08f8c54fcd3c3290a282dee/c9fcc3cec3fdfc03e426845ed03f8794a5c226fd.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=a08270a8212dd42a400907ad33395b2f/e4dde71190ef76c6d047deea9916fdfaae51675d.jpg");
        mUrlList.add("http://a.hiphotos.baidu.com/image/h%3D360/sign=cb645751a964034f10cdc4009fc27980/622762d0f703918f2073a446533d269758eec498.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=7c0a220aa544ad3431bf8181e0a30c08/574e9258d109b3de71ab648fc8bf6c81810a4cc5.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=0c819fc1efc4b7452b94b110fffe1e78/58ee3d6d55fbb2fb45c241fa4b4a20a44723dc68.jpg");
        mUrlList.add("http://a.hiphotos.baidu.com/image/h%3D360/sign=15c5c324ab51f3dedcb2bf62a4ecf0ec/4610b912c8fcc3ce4b2ec2dd9645d688d53f2075.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=a08270a8212dd42a400907ad33395b2f/e4dde71190ef76c6d047deea9916fdfaae51675d.jpg");
        mUrlList.add("http://a.hiphotos.baidu.com/image/h%3D360/sign=cb645751a964034f10cdc4009fc27980/622762d0f703918f2073a446533d269758eec498.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=7c0a220aa544ad3431bf8181e0a30c08/574e9258d109b3de71ab648fc8bf6c81810a4cc5.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=0c819fc1efc4b7452b94b110fffe1e78/58ee3d6d55fbb2fb45c241fa4b4a20a44723dc68.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=decbbe69770e0cf3bff748fd3a47f23d/adaf2edda3cc7cd9c38927a23c01213fb80e9120.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=caa2d267cfef7609230b9f991edca301/6d81800a19d8bc3e7763d030868ba61ea9d345e5.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=decbbe69770e0cf3bff748fd3a47f23d/adaf2edda3cc7cd9c38927a23c01213fb80e9120.jpg");
        mUrlList.add("http://b.hiphotos.baidu.com/image/h%3D360/sign=3fbffe3dce134954611eee62664f92dd/ac6eddc451da81cb58bbc5715766d01609243128.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=9af7917fb7fb4316051f7c7c10a54642/ac345982b2b7d0a2ab6ef529ceef76094a369adb.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=289907f50846f21fd6345855c6256b31/8c1001e93901213f475d07d256e736d12f2e9556.jpg");
        mUrlList.add("http://d.hiphotos.baidu.com/image/h%3D360/sign=13505bc7af18972bbc3a06ccd6cc7b9d/267f9e2f07082838dc76bbc7bc99a9014d08f1ee.jpg");
        mUrlList.add("http://c.hiphotos.baidu.com/image/h%3D360/sign=d4bd2930a96eddc439e7b2fd09dab6a2/377adab44aed2e73519d81f98301a18b86d6faeb.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=2b51ed55cf134954611eee62664f92dd/ac6eddc451da81cb4c55d6195666d016082431b6.jpg");
        mUrlList.add("http://b.hiphotos.baidu.com/image/h%3D360/sign=52b74a248244ebf872716239e9f8d736/37d12f2eb9389b50761593758035e5dde7116e2a.jpg");
        mUrlList.add("http://e.hiphotos.baidu.com/image/h%3D360/sign=c9341858b6b7d0a264c9029bfbee760d/b2de9c82d158ccbf0881c1d01dd8bc3eb135411e.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=897a01ed37fa828bce239be5cd1e41cd/0eb30f2442a7d933fdd1619ba94bd11372f001d8.jpg");
        mUrlList.add("http://f.hiphotos.baidu.com/image/h%3D360/sign=f7030d1d1a178a82d13c79a6c602737f/6c224f4a20a446237cd252b39c22720e0df3d7c3.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=d9e0c234f91f4134ff370378151d95c1/c995d143ad4bd1130c0ee8e55eafa40f4afb0521.jpg");
        mUrlList.add("http://a.hiphotos.baidu.com/image/h%3D360/sign=15c5c324ab51f3dedcb2bf62a4ecf0ec/4610b912c8fcc3ce4b2ec2dd9645d688d53f2075.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=caa2d267cfef7609230b9f991edca301/6d81800a19d8bc3e7763d030868ba61ea9d345e5.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=decbbe69770e0cf3bff748fd3a47f23d/adaf2edda3cc7cd9c38927a23c01213fb80e9120.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=caa2d267cfef7609230b9f991edca301/6d81800a19d8bc3e7763d030868ba61ea9d345e5.jpg");
        mUrlList.add("http://g.hiphotos.baidu.com/image/h%3D360/sign=decbbe69770e0cf3bff748fd3a47f23d/adaf2edda3cc7cd9c38927a23c01213fb80e9120.jpg");
        mUrlList.add("http://b.hiphotos.baidu.com/image/h%3D360/sign=3fbffe3dce134954611eee62664f92dd/ac6eddc451da81cb58bbc5715766d01609243128.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=9af7917fb7fb4316051f7c7c10a54642/ac345982b2b7d0a2ab6ef529ceef76094a369adb.jpg");
        mUrlList.add("http://h.hiphotos.baidu.com/image/h%3D360/sign=289907f50846f21fd6345855c6256b31/8c1001e93901213f475d07d256e736d12f2e9556.jpg");

        mGridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageAdapter(this,mUrlList);
        adapter.setGridViewScrollState(true);
        mGridView.setAdapter(adapter);

      /*  mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    adapter.setGridViewScrollState(true);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setGridViewScrollState(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

    }

}
