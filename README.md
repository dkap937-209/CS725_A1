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
Name: temp Path: user1/temp Size: 17928
Name: user1.jpg Path: user1/user1.jpg Size: 0
Name: user1.txt Path: user1/user1.txt Size: 1628
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
