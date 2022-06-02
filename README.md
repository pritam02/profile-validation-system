# Profile Validation System
Profile Validation System is an application that lets users subscribe to different products of intuit. The application also handles validation of 
business profiles for the user

# Pre-requisites
* Java 1.8/1.11/1.15
* Maven
* Git

[git installation guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

[Java installation guide for Linux](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-18-04)

[Java installation guide MacOS](https://mkyong.com/java/how-to-install-java-on-mac-osx/)

[Java installation guide Windows](https://phoenixnap.com/kb/install-java-windows)

[Maven installation guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)



# Installation Steps
* browse to the directory where the project should be installed

* clone repository from [github](https://github.com/pritam02/profile-validation-system)

```sh
$ git clone https://github.com/pritam02/profile-validation-system
```
* go to the project directory

```sh
$ cd profile-validation-system
```


Scripts are provided inside the parent folder to execute the code.

Use `run.sh` if you are on Linux/Unix/macOS Operating systems and `run.bat` if you are on Windows.

* For Linux/Unix/macOS
```sh
$ ./run.sh
```

* For Windows
```sh
$ run.bat
```

Internally both the scripts run the following commands

* `mvn clean package` - This will create a jar file `profile-validation-system-1.0-SNAPSHOT.jar` in the `target` folder.
* `java -jar target/profile-validation-system-1.0-SNAPSHOT.jar` - This will execute the jar file and start the tomcat server in port 8080

* You need to change the path of the input file in `run.sh` or `run.bat` script if you want to use your own custom input files.


# Execute the unit tests

`mvn clean test` will execute the unit test cases

```sh
$ mvn clean test
``` 