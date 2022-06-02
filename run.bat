@echo off

mvn clean package
java -jar target\profile-validation-system-1.0-SNAPSHOT.jar