# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/opt/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontnote
-dontwarn
-ignorewarnings
-dontobfuscate
-keep class akka.actor.LightArrayRevolverScheduler { *; }
-keep class akka.actor.LocalActorRefProvider { *; }
-keep class akka.actor.CreatorFunctionConsumer { *; }
-keep class akka.actor.TypedCreatorFunctionConsumer { *; }
-keep class akka.dispatch.BoundedDequeBasedMessageQueueSemantics { *; }
-keep class akka.dispatch.UnboundedMessageQueueSemantics { *; }
-keep class akka.dispatch.UnboundedDequeBasedMessageQueueSemantics { *; }
-keep class akka.dispatch.DequeBasedMessageQueueSemantics { *; }
-keep class akka.dispatch.MultipleConsumerSemantics { *; }
-keep class akka.actor.LocalActorRefProvider$Guardian { *; }
-keep class akka.actor.LocalActorRefProvider$SystemGuardian { *; }
-keep class akka.dispatch.UnboundedMailbox { *; }
-keep class akka.actor.DefaultSupervisorStrategy { *; }
-keep class macroid.akka.AkkaAndroidLogger { *; }
-keep class akka.event.Logging$LogExt { *; }
-keep class scala.collection.SeqLike {
    public protected *;
}
