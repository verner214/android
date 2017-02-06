rem java ----------------------
set JAVA_HOME=C:\a\jdk1.8.0_121
set PATH=%PATH%;%JAVA_HOME%\bin

rem gradle ----------------------
set GRADLE_HOME=C:\a\gradle\gradle-3.3
set PATH=%PATH%;%GRADLE_HOME%\bin

rem android sdk, ANDROID_SDK_HOME används av bla avd manager för att skapa AVD's
set ANDROID_HOME=C:\a\tools_r25.2.3
rem set ANDROID_SDK_HOME=C:\own\ejgit\sdk
rem set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools

rem own stuff
set PATH=%PATH%;c:\a




rem -------------------------------------- arkiv ----------------------
rem om HTTPSinspeciton: https://sc1.checkpoint.com/documents/R77/CP_R77_ThreatPrevention_WebAdmin/101685.htm
rem importera cert i JDK, http://www.thesqlreport.com/?p=576
rem https://jcenter.bintray.com

rem keytool -import -alias "HTTPSinspection" -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts" -file "C:\own\HTTPSinspection.cer"
rem keytool -import -alias "HTTPSinspectionParent" -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts" -file "C:\own\HTTPSinspectionParent.cer"
rem keytool -list -keystore "C:\own\java\javax86\jdk1.7.0_79\jre\lib\security\cacerts"
rem changeit

