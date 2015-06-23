@echo off
rem skapar en projektstruktur som �r anpassad till gradle
rem anv�ndning, C:\own\ranch\01_FirstApp\GeoQuiz\2gradle.bat


rem skapa mappar f�r nya projektstrukturen
for %%* in (.) do set CurrDirName=%%~n*
set CurrDirName=own_%CurrDirName%
echo %CurrDirName%
mkdir ..\%CurrDirName%
mkdir ..\%CurrDirName%\src
mkdir ..\%CurrDirName%\libs
mkdir ..\%CurrDirName%\src\main
mkdir ..\%CurrDirName%\src\main\java
mkdir ..\%CurrDirName%\src\main\res

rem kopiera allt
copy AndroidManifest.xml ..\%CurrDirName%\src\main\AndroidManifest.xml
rem copy c:\own\build.gradle ..\%CurrDirName%\* ---lwa, anv�nder den uppdaterade nedan som inneh�ller support lib.
copy c:\own\buildSupport.gradle ..\%CurrDirName%\build.gradle
xcopy .\src\* ..\%CurrDirName%\src\main\java\* /E /R /Y
xcopy .\res\* ..\%CurrDirName%\src\main\res\* /E /R /Y
rem xcopy .\libs\* ..\%CurrDirName%\libs\* /E /R /Y ---lwa, anv�nder den nya ist�llet som ligger i own\libs\, verkar vara den enda lib som anv�nds, annars bortkommentera denna rad
xcopy c:\own\libs\* ..\%CurrDirName%\libs\* /E /R /Y



rem arkiv
rem echo %~dp0, echo %cd%, ECHO.%CD%
rem for %%* in (.) do set CurrDirName=%%~n*
rem echo %CurrDirName%