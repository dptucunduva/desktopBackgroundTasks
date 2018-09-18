REM Change directory
cd %~dp0

REM Configuration
SET SINGLE_RUN=true
SET SLEEP_TIME=3000
SET LINES=4
SET COLUMNS=4
SET CARD_WIDTH=250
SET CARD_HEIGHT=125
SET X_START=220
SET Y_START=174

REM Running it.
IF %SINGLE_RUN%==true (
    java -DxStart=%X_START% -DyStart=%Y_START% -DcardWidth=%CARD_WIDTH% -DcardHeight=%CARD_HEIGHT% -Dlines=%LINES% -Dcolumns=%COLUMNS% -DsingleRun=true -cp ".\libs\jna-4.2.1.jar;.\libs\jna-platform-4.2.1.jar" -jar "dbt.jar" 
) ELSE (
    start javaw -DxStart=%X_START% -DyStart=%Y_START% -DcardWidth=%CARD_WIDTH% -DcardHeight=%CARD_HEIGHT% -Dlines=%LINES% -Dcolumns=%COLUMNS% -DsleepTime=%SLEEP_TIME% -cp ".\libs\jna-4.2.1.jar;.\libs\jna-platform-4.2.1.jar" -jar "dbt.jar" 
)
