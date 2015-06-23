@echo off
rem skapar en projektstruktur som är anpassad till gradle
rem användning, C:\own\ranch\01_FirstApp\GeoQuiz\2gradle.bat


rem skapa mappar för nya projektstrukturen
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
rem copy c:\own\build.gradle ..\%CurrDirName%\* ---lwa, använder den uppdaterade nedan som innehåller support lib.
copy c:\own\buildSupport.gradle ..\%CurrDirName%\build.gradle
xcopy .\src\* ..\%CurrDirName%\src\main\java\* /E /R /Y
xcopy .\res\* ..\%CurrDirName%\src\main\res\* /E /R /Y
rem xcopy .\libs\* ..\%CurrDirName%\libs\* /E /R /Y ---lwa, använder den nya istället som ligger i own\libs\, verkar vara den enda lib som används, annars bortkommentera denna rad
xcopy c:\own\libs\* ..\%CurrDirName%\libs\* /E /R /Y



rem arkiv
rem echo %~dp0, echo %cd%, ECHO.%CD%
rem for %%* in (.) do set CurrDirName=%%~n*
rem echo %CurrDirName%