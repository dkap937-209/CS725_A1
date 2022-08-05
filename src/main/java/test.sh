#!/bin/bash
#javac Client.java

javac -cp . util/Keyboard.java util/ReadChars.java
javac -cp . Conn_Info/Connection_Information.java
javac -cp . client/Client.java

echo "Test 1" > results.txt
{ echo "user user1"; echo "done"; } | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 2" >> results.txt
{ echo "user user2"; echo "acct acct1"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 3" >> results.txt
{ echo "user user3"; echo "pass pass3"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 4" >> results.txt
{ echo "user user4"; echo "acct acct1"; echo "pass pass4"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 5" >> results.txt
{ echo "user user5"; echo "pass pass5"; echo "acct acct1";  echo "acct acct2"; echo "acct acct3";echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 6" >> results.txt
{ echo "user user7"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 7" >> results.txt
{ echo "user user4"; echo "acct acct2"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 8" >> results.txt
{ echo "user user4"; echo "pass wrong"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 9" >> results.txt
{ echo "user user7 user7";  echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 10" >> results.txt
{ echo "user user4";  echo "acct acct2 acct2"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 11" >> results.txt
{ echo "user user4";  echo "pass wrong wrong"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 12" >> results.txt
{ echo "user user1";  echo "type a"; echo "type b"; echo "type c"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 13" >> results.txt
{ echo "user user1";  echo "type a a"; echo "type d"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 14" >> results.txt
{ echo "user user1";  echo "list f"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 15" >> results.txt
{ echo "user user1";  echo "list f temp"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 16" >> results.txt
{ echo "user user1";  echo "list v"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 17" >> results.txt
{ echo "user user1";  echo "list f fake"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 18" >> results.txt
{ echo "user user1";  echo "list f user1.txt"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 19" >> results.txt
{ echo "user user1";  echo "list f / /"; echo "list g"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 20" >> results.txt
{ echo "user user1";  echo "cdir temp"; echo "cdir folder2"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 21" >> results.txt
{ echo "user user1";  echo "cdir /"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 22" >> results.txt
{ echo "user user1";  echo "cdir /temp/folder2"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 23" >> results.txt
{ echo "user user1";  echo "cdir temp/folder2/folder3"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 24" >> results.txt
{ echo "user user1";  echo "cdir temp/folder2/evenMoreUser1.txt"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 25" >> results.txt
{ echo "user user2";  echo "cdir temp"; echo "acct acct1"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 26" >> results.txt
{ echo "user user3";  echo "cdir temp"; echo "pass pass3"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 27" >> results.txt
{ echo "user user4";  echo "cdir temp"; echo "acct acct1"; echo "pass pass4"; echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 28" >> results.txt
{ echo "user user1";  echo "cdir folder1 folder2";  echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 29" >> results.txt
{ echo "user user1";  echo "kill delete.txt";  echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 30" >> results.txt
{ echo "user user1";  echo "kill fake.txt";  echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

echo "Test 31" >> results.txt
{ echo "user user1";  echo "kill fake.txt fake.txt";  echo "done";} | java client/Client.java >> results.txt
echo "">> results.txt

#echo "Test 32" >> results.txt
#{ echo "user user1";  echo "name rename.txt"; echo "tobe new.txt"; echo "kill new.txt" echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 33" >> results.txt
#{ echo "user user1";  echo "name fake.txt"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 34" >> results.txt
#{ echo "user user1";  echo "tobe file2.txt"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 35" >> results.txt
#{ echo "user user1";  echo "name fake.txt fake.txt" echo "name file.txt" echo "tobe new.txt new.txt" ; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 36" >> results.txt
#{ echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 37" >> results.txt
#{ echo "done done"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 38" >> results.txt
#{ echo "user user1"; echo "retr temp/data.csv"; echo "send" echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 39" >> results.txt
#{ echo "user user1"; echo "retr temp/data.csv"; echo "stop" echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 39" >> results.txt
#{ echo "user user1"; echo "retr temp/data.csv"; echo "stop" echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 40" >> results.txt
#{ echo "user user1"; echo "retr fake.txt"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 41" >> results.txt
#{ echo "user user1"; echo "retr temp"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 42" >> results.txt
#{ echo "user user1"; echo "retr file.txt file.txt"; echo "retr file.txt"; echo "send send"; echo "stop stop"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 43" >> results.txt
#{ echo "user user1"; echo "stor new file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 44" >> results.txt
#{ echo "user user1"; echo "stor new file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 45" >> results.txt
#{ echo "user user1"; echo "stor old file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 46" >> results.txt
#{ echo "user user1"; echo "stor old file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 47" >> results.txt
#{ echo "user user1"; echo "stor app file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 48" >> results.txt
#{ echo "user user1"; echo "stor app file.txt"; echo "size 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 49" >> results.txt
#{ echo "user user1"; echo "stor new fake.txt"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 50" >> results.txt
#{ echo "user user1"; echo "stor new client"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 51" >> results.txt
#{ echo "user user1"; echo "stor app file.txt file.txt"; echo "stor a"; echo "stor app file.txt"; echo "size 8 8"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 52" >> results.txt
#{ echo "type a"; echo "list f"; echo "stor a"; echo "name rename.txt"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt
#
#echo "Test 53" >> results.txt
#{ echo "unknown"; echo "done";} | java client/Client.java >> results.txt
#echo "">> results.txt

rm oneline.txt
rm lines.txt
