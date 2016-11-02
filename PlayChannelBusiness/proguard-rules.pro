# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\development\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.ninethree.palychannelbusiness.activity.WebViewActivity {
   public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*


# 实体类不混淆
-keep class com.ninethree.palychannelbusiness.bean.** { *; }

# Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#微信
-keep class com.tencent.mm.sdk.** { *; }
