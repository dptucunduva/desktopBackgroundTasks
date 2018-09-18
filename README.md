# Desktop Background Tasks

Simple app that will generate and install a windows desktop background with your notes. This app takes an base background image file and adds notes to it. Then, this generated image is set as a desktop background. Any image type supported by [JSE8 2D API](https://docs.oracle.com/javase/tutorial/2d/index.html) is supported by this app.

## Requirements

This application requires Windows to run. All you need is a Java runtime environment version 8 or higher installed. Most likely you already have it installed, but if not, you can download and install from Oracle website [here](https://www.java.com/download/).

## Installing

Just download the latest release version [here](https://github.com/dptucunduva/desktopBackgroundTasks/releases) and unpack the zip file anywhere you want. The following files are the ones that are relevant:

* _background.png_: Base background file. All notes will be added to this file. You can edit this file, replace it, create a different one - as long as it exists.
* _tasks.lst_: Tasks that will be transformed into notes in your desktop background image. Check [tasks file structure]() section to see how to manage tasks.
* _dbt.cmd_: This is the windows command file that starts the application in the background. Check [configuring]() section to see what you can change in this file to adapt the application to your usage.

If you double click in _dbt.cmd_ and everything is OK, you will see your desktop change to a black/gray background with two sample notes.  

## How it works

Common usage is to run this application in the background. The application will check every now and then if the tasks file was updated and update windows desktop background accordingly. The application also checks every now and then if there is a task that is overdue and update the note with an adequate symbol at the end of the title:
* **Thumbs UP** in black: Task due date is not today and it is not overdue
* **Hand Point** in yellow: Task need to be handled today
* **Warning** in red: Task is delayed 

## Creating tasks

Each line in the file is a task. A line has 4 sections, separated by a semicolon:

1. **Importance**: First part is the task importance. Possible values are:
   1. **CRITICAL**: Notes will be created in purple
   1. **MAJOR**: Notes will be created in yellow 
   1. **MINOR**: Notes will be created in green 
   1. **INFO**: Notes will be created in gray
1. **Due Date**: The date and time when this task starts to be considered delayed. This value must be filled using 'dd/MM/yyyy HH:mm' date/time format.
1. **Title**: Task title. This is a single line that is show at the header of the note. Just after the title, a symbol will tell you if this task is on time, needs to be finished today or is already overdue. 
1. **Description**: Task details. This is a text that in the default configuration can take up to 90 characters usually. If you want to split lines, use the char sequence ' | ' (space-pipe-space, no quotes).

Sample: 
`MAJOR;01/12/2018 18:00;JOB;Do that thing at my job`

Sample with more than one line in description:
`MINOR;01/12/2018 18:00;HOME;Cleanup garage | Organize tools`

If a line starts with #, it is considered a comment ad it will be ignored. Empty lines are also ignored.

Tasks are displayed in the same order of the file.
 
## Configuring the application

In _dbt.cmd_ file, the first section contains several parameters that can be configured:

* **SINGLE_RUN**: if set to 'true', the application will start, update the background and then exit. If set to false, application will start in daemon mode - i.e., from time to time it will check if the file was updated and update the background image accordingly.
* **SLEEP_TIME**: The interval that the application will check for updates in task file, in milliseconds. If not set, the default value is 10000.
* **LINES**: How many cards per line will be rendered. Default value is 4.
* **COLUMNS**: How many cards per columns will be rendered. Default value is 4.
* **CARD_WIDTH**: Card width in pixels. Default value is 250.
* **CARD_HEIGHT**: Card height in pixels. Default value is 125.
* **X_START**: Starting X point for card rendering, in pixels. Cards will be rendered after X pixels horizontally. Default value is 220.
* **Y_START**: Starting Y point for card rendering, in pixels. Cards will be rendered after Y pixels vertically. Default value is 174.

## Starting the application automatically

If you want to start the application every time windows starts, there are several ways. I personally prefer using windows scheduler - just create a task that runs _dbt.cmd_ everytime windows starts. Documentation on how to do it can be found at Microsoft website [here](https://docs.microsoft.com/en-us/windows/desktop/taskschd/using-the-task-scheduler).

Remember to change **SINGLE_RUN** to false in order to start the application in daemon mode.

