#Offical Link
Facebook Login
https://developers.facebook.com/docs/facebook-login/

#Facebook 登录 
##编辑您的资源和清单
为您的 Facebook 应用编号以及启用 Chrome 自定义选项卡所需的编号创建字符串。另外，请将 FacebookActivity 添加到您的 Android 清单文件中。
打开您的 /app/res/values/strings.xml 文件。
添加如下所示的代码：
```
<string name="facebook_app_id">533148873902099</string> 
<string name="fb_login_protocol_scheme">fb533148873902099</string>
```
打开 /app/manifest/AndroidManifest.xml 文件。
在 application 元素后添加以下 uses-permission 元素：
```
  <uses-permission android:name="android.permission.INTERNET"/>
```
在 application 元素中添加以下 meta-data 元素、一个针对 Facebook 的 activity 元素以及一个针对 Chrome 自定义选项卡的 activity 元素和意向筛选条件：
```
<meta-data 
    android:name="com.facebook.sdk.ApplicationId" 
    android:value="@string/facebook_app_id"/> 
<activity 
    android:name="com.facebook.FacebookActivity" 
    android:configChanges= "keyboard|keyboardHidden|screenLayout|screenSize|orientation" 
    android:label="@string/app_name" />
<activity 
    android:name="com.facebook.CustomTabActivity"
    android:exported="true"> 
    <intent-filter>
        <action android:name="android.intent.action.VIEW" /> 
        <category android:name="android.intent.category.DEFAULT" /> 
        <category android:name="android.intent.category.BROWSABLE" /> 
        <data android:scheme="@string/fb_login_protocol_scheme" /> 
    </intent-filter> 
</activity>
```
##为应用提供开发和发布密钥散列
启用Google Play App Signing后的大麻烦和解决办法(不用重新创建应用)
https://blog.csdn.net/weixin_40707679/article/details/80827889