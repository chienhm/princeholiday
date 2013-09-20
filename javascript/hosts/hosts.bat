echo off
net user guest /active:yes
cls
if errorlevel 2 echo.& echo 请以管理员身份运行（右键点击本程序，选择以管理员身份运行）。& pause & exit
net user guest /active:no
REM https://smarthosts.googlecode.com/svn/trunk/hosts
REM 设置全局变量
set BAT_DIR=%~dp0
set TMP=%BAT_DIR%\tmp.txt
echo. > %TMP%
set OFFICE_FILES=baidu.txt base.txt adv.txt adobe.txt 
set HOME_FILES=google.txt base.txt adv.txt adobe.txt dropbox.txt

cls
echo ---------------------------------------------------------------------------
echo Select scenario:
echo ---------------------------------------------------------------------------
echo.
echo 1.OFFICE
echo 2.HOME
echo.
echo 3.退出

echo.
SET /P TT= 请输入相应序号(1-6)：
echo.
if /I "%TT%"=="1" goto office
if /I "%TT%"=="2" goto home
if /I "%TT%"=="3" exit
exit

:office
set TMP_FILES=%OFFICE_FILES%
call :genhosts

:home
set TMP_FILES=%HOME_FILES%
call :genhosts

:genhosts
for %%F in (%TMP_FILES%) do (
	type %BAT_DIR%\%%F >> %TMP%
	echo.>> %TMP%
)
move /y %TMP% %windir%\System32\drivers\etc\hosts
echo New hosts generated successfully: %windir%\System32\drivers\etc\hosts
ipconfig /flushdns

pause
exit
