# PhoneRemoteControlPlatform
Android Phone Remote Control minicap

A remote debugging Android mobile platform

Developed with Java language, with the help of minicap and adbkit, it realizes remote click on the mobile screen, ADB connection, obtaining logcat log, uploading and installing app

For application examples, please refer to http://ctscatp.com.cn/, and use the free trial account guest password guest to try.



Installation 

1. Configuring the JDK environment, version 1.8

2. Put phoneremotecontrolplatform.war in Tomcat

3. Install MySQL database software, execute installdb.sql under the dbinstall directory to initialize the database, and execute two SQL initialization tables, mobilephoneinfo and parameter

4. Put the resources directory into the directory consistent with the minipath configuration of the database parameter table

5. Install nodejs

6. Execute NPM I - G adbkit on the command line

7. The configuration database adbkitpath is consistent with the directory of the installed \ adbkit.cmd

8. Start Tomcat
