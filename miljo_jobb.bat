rem förbereder miljön för android bygge
rem https://spring.io/guides/gs/gradle-android/

rem java
rem set JAVA_HOME=C:\own\ejgit\java\jdk1.7
set JAVA_HOME=C:\own\ejgit\java\javax86\jdk1.7.0_79
set PATH=%PATH%;%JAVA_HOME%\bin

rem gradle
set GRADLE_HOME=C:\own\ejgit\gradle-2.4-bin\gradle-2.4
set PATH=%PATH%;%GRADLE_HOME%\bin

rem android sdk, ANDROID_SDK_HOME används av bla avd manager för att skapa AVD's
set ANDROID_HOME=C:\own\ejgit\sdk
set ANDROID_SDK_HOME=C:\own\ejgit\sdk
set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools

rem om HTTPSinspeciton: https://sc1.checkpoint.com/documents/R77/CP_R77_ThreatPrevention_WebAdmin/101685.htm
rem importera cert i JDK, http://www.thesqlreport.com/?p=576
rem https://jcenter.bintray.com

rem keytool -import -alias "HTTPSinspection" -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts" -file "C:\own\HTTPSinspection.cer"
rem keytool -import -alias "HTTPSinspectionParent" -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts" -file "C:\own\HTTPSinspectionParent.cer"
rem keytool -list -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts"
rem changeit


rem own stuff
set PATH=%PATH%;c:\own

rem https://jcenter.bintray.com/com/android/tools/build/gradle-core/1.1.3/gradle-core-1.1.3.jar
