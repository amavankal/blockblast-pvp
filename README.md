# blockblast-pvp

To run this project, clone the repo to your local machine and run the following commands in the root directory:

```
mkdir bin
javac -d bin $(find src/blockblast -name "*.java")
cp -r src/resources/images bin/
java -cp bin blockblast.gui.BigGUI
```

In order to compile and run this project, you must have the JRE and JDK installed so that you can use `java` and `javac`. You can check if you have these installed with `java --version` and `javac --version`. If you don't have them installed, navigate to [this link](https://www.oracle.com/java/technologies/downloads/) and download the appropriate version for your OS.
