# Santorini (Software Engineering Project 2020)
Java implementation of the table game Santorini by Cranio Creations as part of the Bachelor thesis. The highest possible grade has been assigned to this project. 

## Team AM01 - Prof. Margara
-    10604455     Luca Danelutti ([@LucaDanelutti](https://github.com/LucaDanelutti)) <br> luca.danelutti@mail.polimi.it
-    10574949    Riccardo De Santi ([@riccardodesanti](https://github.com/riccardodesanti)) <br>  riccardo.desanti@mail.polimi.it
-    10580652    Ian Di Dio Lavore ([@ian-ofgod](https://github.com/ian-ofgod)) <br>  ian.didio@mail.polimi.it

## Development state 
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | ✔️ |
| Complete rules | ✔️ |
| Socket | ✔️ |
| GUI | ✔️ |
| CLI | ✔️ |
| Multiple games | ❌  |
| Persistence | ❌ |
| Advanced Gods | ✔️ |
| Undo | ✔️ |

> ❌ = not implemented <br>
🚧 = work in progress <br>
✔️ = completed

## Generate project JAR
We suppose that you have properly configured in your system:
- JDK (version >= 13.0) - please refer to [Oracle JDK - Download and Installation](https://www.oracle.com/java/technologies/javase-downloads.html)
- Maven - please refer to [Maven Installation guide](https://maven.apache.org/install.html)

In the main folder of the project (where you find the POM.xml file), run from the command line: 
```bash
mvn package -DskipTests
```
A new folder will appear in the directory of the project called "shade", inside you will find the generated AM01.jar


## Usage
The .jar can be executed into three different mode by using the proper parameters
 
### Run a Client 
- To start a GUI client you should be able to double-click on it, in other cases just type: 
```bash
java -jar AM01.jar
```
- To start a CLI client, type: 
```bash
java -jar AM01.jar -cli [-hostname address] [-port number]

example: 
java -jar AM01.jar -hostname 192.168.0.4 -port 12345
```
If optional parameters are omitted it will prompt to insert them
### Run a Server
- To start a server, type: 
```bash
java -jar AM01.jar -server [-port number]

example:
java -jar AM01.jar -server -port 12345 
```
If port parameter is ommited the server will start listening on default port 12345
