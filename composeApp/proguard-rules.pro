# Keep metadata for classes with @Serializable
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Keep the generated serializers
-keep class **$$serializer { *; }
-keepclassmembers class ** {
    static **$$serializer SERIALIZER;
}

# Crashlytics
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Keep custom exceptions.