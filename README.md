# CompileAnnotation

编译时注解的一个简单实现，运行于AndroidStudio上。

##项目结构
* myandroidprocessor

    android library，需要依赖android库的d代码就在这里实现。将依赖myprocessor。
* myprocessor

    java library，由于android中没有javax，所以需要建立一个java库，实现依赖于javax的部分。
* myapp

    android app，它将depends myandroidprocessor来完成注解编程。

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

}
```
