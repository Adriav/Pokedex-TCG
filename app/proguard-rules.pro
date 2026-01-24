# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Prevent Gson from obfuscating your specific data models
# Replace 'com.adriav.tcgpokemon' with your actual package name for models
-keep class com.adriav.tcgpokemon.database.entity.CardEntity { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep class kotlin.Metadata { *; }

# TCGdex SDK
-keep class net.tcgdex.** { *; }


# General Gson rules
-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class com.google.gson.** { *; }

# Missing Classes
-dontwarn java.awt.image.BufferedImage
-dontwarn javax.imageio.ImageIO