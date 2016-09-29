# launchRocket
仿腾讯发射小火箭
最主要的是要在清单文件中给Activity设置透明的主题。
为了防止矛盾，MainActivity最好直接继承Activity。
该主题如下：

 <activity android:name=".MainActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        该module存在一些bug，比如每次火箭发射后，在前一次发射的基础上，都会上移一小段距离，直至移出屏幕外。

