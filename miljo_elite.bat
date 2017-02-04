rem se incheckad fil miljo_jobb i roten av android-repo
rem java
set JAVA_HOME="C:\Program Files\Android\Android Studio\jre"
set PATH=%PATH%;%JAVA_HOME%\bin

rem gradle
set GRADLE_HOME=C:\android\gradle-3.2.1
set PATH=%PATH%;%GRADLE_HOME%\bin

rem android sdk, ANDROID_SDK_HOME används av bla avd manager för att skapa AVD's
set ANDROID_HOME=c:\users\lars\appdata\local\android\sdk\
set ANDROID_SDK_HOME=c:\users\lars\appdata\local\android\sdk\
set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools

