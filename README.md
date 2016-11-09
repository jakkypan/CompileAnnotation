# CompileAnnotation

编译时注解的一个简单实现，在AndroidStudio上。

##使用方式
```java
package baidu.myapp;

import com.example.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import baidu.myandroidprocessor.HyViewInjector;

@InjectView(R.layout.activity_main)
public class MainActivity extends Activity {
    @InjectView(R.id.id_tv_name)
    public TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyViewInjector.inject(this);
        mTvName.setText("Hello Annotation");
    }

}```
