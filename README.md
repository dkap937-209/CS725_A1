# RFC 913

## Setup
Although this implementation is designed to be cross-platform, and will work on any machine, the associated scripts are designed to be run on a windows system.


## Test cases

### Test 1
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>done
+Closing connection
```


### Test 2
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user2
+User-id valid, send account and password
>acct acct1
!Account valid, logged-in
>done
+Closing connection
```

### Test 3
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user3
+User-id valid, send account and password
>pass pass3
! Logged in
>done
+Closing connection
```


### Test 4
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>acct acct1
+Account valid, send password
>pass pass4
! Logged in
>done
+Closing connection
```


### Test 5
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user5
+User-id valid, send account and password
>pass pass5
+Send account
>acct acct1
! Account valid, logged-in
>acct acct2
! Account valid, logged-in
>acct acct3
! Account valid, logged-in
>done
+Closing connection
```

### Test 6
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user7
-Invalid user-id, try again
>done
+Closing connection
```

### Test 7
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>acct acct2
-Invalid account, try again
>done
+Closing connection
```


### Test 8
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>pass wrong
-Wrong password, try again
>done
+Closing connection
```

### Test 9
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user7 user7
ERROR: Invalid Arguments
Usage: USER user-id
>done
+Closing connection
```

### Test 10
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>acct acct2 acct2
ERROR: Invalid Arguments
Usage: ACCT account
>done
+Closing connection
```


### Test 11
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>pass wrong wrong
ERROR: Invalid Arguments
Usage: PASS password
>done
+Closing connection
```

### Test 12
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>type a
+Using Ascii mode
>type b
+Using Binary mode
>type c
+Using Continuous mode
>done
+Closing connection
```

### Test 13
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>type a a
ERROR: Invalid Arguments
Usage: TYPE { A | B | C }
>type d
-Type not valid
>done
+Closing connection
```

### Test 14
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list f
+user1/
delete.txt
file.txt
file2.txt
rename.txt
temp
user1.jpg
user1.txt
>done
+Closing connection
```

### Test 15
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list f temp
+user1/temp/
folder2
moreUser1.txt
>done
+Closing connection
```

### Test 16
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list v
+user1/
Name: delete.txt Path: user1/delete.txt Size: 16 Bytes
Name: file.txt Path: user1/file.txt Size: 14 Bytes
Name: file2.txt Path: user1/file2.txt Size: 14 Bytes
Name: rename.txt Path: user1/rename.txt Size: 16 Bytes
Name: temp Path: user1/temp Size: 17928 Bytes
Name: user1.jpg Path: user1/user1.jpg Size: 0 Bytes
Name: user1.txt Path: user1/user1.txt Size: 1628 Bytes
>done
+Closing connection
```


### Test 17

```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list f fake
-Cant list directory because: user1 does not exist
>done
+Closing connection
```


### Test 18

```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list f user1.txt
-Cant list directory because: user1 is not a directory
>done
+Closing connection
```


### Test 19
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>list f / /
ERROR: Invalid Arguments
Usage: LIST { F | V } directory-path
>list g
+user1/
-Argument error
>done
+Closing connection
```

### Test 20
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir temp
!Changed working dur to user1/temp
>cdir folder2
!Changed working dur to user1/temp/folder2
>done
+Closing connection
```

### Test 21
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir /
!Changed working dir to user1/
>done
+Closing connection
```

### Test 22 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir /temp/folder2
!Changed working dir to user1/temp/folder2
>done
+Closing connection
```

### Test 23
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir temp/folder2/folder3
-Cant connect to directory because: user1/temp/folder2/folder3 does not exist
>done
+Closing connection
```
### Test 24
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir temp/folder2/evenMoreUser1.txt
-Cant list directory because: user1/temp/folder2/evenMoreUser1.txt is not a directory
>done
+Closing connection
```

### Test 25
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user2
+User-id valid, send account and password
>cdir temp
+Directory exists, send account/password
>acct acct1
!Account valid, logged-in
Changed working dir to user2/temp
>done
+Closing connection
```

### Test 26
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user3
+User-id valid, send account and password
>cdir temp
+Directory exists, send account/password
>pass pass3
! Logged in
Changed working dir to user3/temp
>done
+Closing connection
```

### Test 27
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user4
+User-id valid, send account and password
>cdir temp
+Directory exists, send account/password
>acct acct1
+Account valid, send password
>pass pass4
! Logged in
Changed working dir to user4/temp
>done
+Closing connection
```

### Test 28
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>cdir folder1 folder2
ERROR: Invalid Arguments
Usage: CDIR new-directory
>done
+Closing connection
```

### Test 29
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>kill delete.txt
+user1/delete.txt deleted
>done
+user1/delete.txt deleted
```

### Test 30
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>kill fake.txt
-Not deleted because user1/fake.txt does not exist
>done
+Closing connection
```

### Test 31
```terminal
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>kill fake.txt fake.txt
ERROR: Invalid Arguments
Usage: KILL file-spec
>done
+Closing connection
```

### Test 32
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>name rename.txt
+File exists
>tobe new.txt
user1/rename.txt renamed to user1/new.txt
>kill new.txt
+user1/new.txt deleted
>done
+Closing connection
```

### Test 33
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>name fake.txt
Can't find user1/fake.txt
>done
+Closing connection
```
### Test 34
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>tobe file2.txt
File wasn't renamed because user1/file2.txt already exists
>done
+Closing connection
```

### Test 35 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>name fake.txt fake.txt
ERROR: Invalid Arguments
Usage: NAME old-file-spec
>name file.txt
+File exists
>tobe rename.txt rename.txt
ERROR: Invalid Arguments
Usage: TOBE new-file-spec
>done
+Closing connection
```

### Test 36 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>done
+Closing connection
```

### Test 37 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>done done
ERROR: Invalid Arguments
Usage: DONE
>done
+Closing connection
```

### Test 38
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>retr file.txt
+14 bytes will be sent
>send
+File sent
>done
+Closing connection
```

### Test 39 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>retr file.txt
+14 bytes will be sent
>stop
+File will not be sent
>done
+Closing connection
```

### Test 40 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>retr fake.txt
-File doesn't exist
>done
+Closing connection
```

### Test 41 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>retr temp
-Specifier is not a file
>done
+Closing connection
```
### Test 42 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>retr file.txt file.txt
ERROR: Invalid Arguments
Usage: RETR file-spec
>retr file.txt
+14 bytes will be sent
>send send
ERROR: Invalid Arguments
Usage: SEND
>stop stop
ERROR: Invalid Arguments
Usage: STOP
>done
+Closing connection
```

### Test 43
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor new fromClient.txt
+File does not exist, will create new file
>size 28
+Saved user1/fromClient.txt
>done
+Closing connection
```

### Test 44 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor new file2.txt
+File exists, will create new generation of file
>size 31
+Saved user1/file3.txt
>done
+Closing connection
```

### Test 45
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor old fromClient.txt
+Will create new file
>size 28
+Saved user1/fromClient.txt
>done
+Closing connection
```

### Test 46
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor old file2.txt
+Will write over old file
>size 31
+Saved user1\file2.txt
>done
+Closing connection
```

### Test 47 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor app fromClient.txt
+Will create new file
>size 28
+Saved user1/fromClient.txt
>done
+Closing connection
```

### Test 48 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor app file2.txt
+Will append to file
>size 31
+Saved user1\file2.txt
>done
+Closing connection
```

### Test 49 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor new fake.txt
ERROR: File doesn't exist
>done
+Closing connection
```

### Test 50
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>user user1
!user1 logged in
>stor new client
ERROR: Specifier is not a file
>done
+Closing connection
```

### Test 52
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>type a
-Please log in first
>list f
Please log in first
>name rename.txt
-Please log in first
>done
+Closing connection
```

### Test 53 
```terminal 
Successfully connected to localhost on port 6789
+RFC 913 SFTP Server
>unknown
ERROR: Invalid Command
Available Commands: "USER", "ACCT", "PASS", "TYPE", "LIST", "CDIR", "KILL", "NAME", "TOBE", "DONE", "RETR", "SEND", "STOP", "STOR", "SIZE"
>done
+Closing connection
```