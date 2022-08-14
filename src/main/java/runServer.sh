#!/bin/bash
javac -cp "C:\Users\darpa\.m2\repository\com\googlecode\json-simple\json-simple\1.1.1\json-simple-1.1.1.jar" Conn_Info/Connection_Information.java util/ReadChars.java ./server/ClientThread.java ./server/Server.java
java server/Server
