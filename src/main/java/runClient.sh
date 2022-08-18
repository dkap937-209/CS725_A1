#!/bin/bash
javac -cp . util/Keyboard.java util/ReadChars.java
javac -cp . Conn_Info/Connection_Information.java
javac -cp . client/Client.java

java client/Client.java