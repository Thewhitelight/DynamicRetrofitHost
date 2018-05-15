package cn.libery.switchhost;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;

import cn.libery.switchhost.api.Api;
import cn.libery.switchhost.api.DynamicInterceptor;
import cn.libery.switchhost.model.Response;

public class MainActivity extends AppCompatActivity {

    private String[] items = new String[]{Api.DAILY_HOST, Api.PREVIEW_HOST, Api.RELEASE_HOST};
    private String[] showItems = new String[]{"测试", "预发", "生产"};

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(R.id.content_txt);
        loadData();
        showSwitchHost();
    }

    private void showSwitchHost() {
        DragFloatButton dragFloatButton = findViewById(R.id.switch_host);
        if (AppUtil.canSwitchHost()) {
            dragFloatButton.setVisibility(View.VISIBLE);
        } else {
            dragFloatButton.setVisibility(View.GONE);
        }
        dragFloatButton.setAlpha(0.5f);
        String host = "";
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(Api.getHost())) {
                host = showItems[i];
            }
        }
        dragFloatButton.setTitle("host:" + host);
        dragFloatButton.setOnClickListener(v -> showSwitchDialog(dragFloatButton));
    }

    private void showSwitchDialog(DragFloatButton btn) {
        String[] shows = Arrays.copyOf(showItems, showItems.length);
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(DynamicInterceptor.getHost())) {
                shows[i] = ("正在使用:" + shows[i]);
            }
        }
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("切换HOST")
                .setItems(shows, (dialog, which) -> {
                    DynamicInterceptor.setHost(items[which]);
                    btn.setTitle("host:" + showItems[which]);
                    loadData();
                })
                .show();
    }

    public void loadData() {
        Api.getInstance().getArticle().subscribe(new Subscriber<Response>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Response response) {
                content.setText(response.toString());

            }

            @Override
            public void onError(Throwable t) {
                content.setText(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
