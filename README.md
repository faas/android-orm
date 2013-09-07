Android-Orm
===========

This is an ORM desinged and build espessialy for Android.

You will need to write a SQLiteDatabase wrapper wich implements Session.java

The primary intension is to enable simple database use through an ORM (which is a bit based on hibernate priciples) and the second intetion is to give you the freedom of what type of Database you use in android (eq. standard SQLite, SQLCipher, etc).

Proguard
--------

This is still in testing phase (like the whole project actually ;)), but it is very easely possible to use proguard. 
Add the following rules to you proguard config:

    -keep interface nl.spikey.orm.annotations.Column { *; }
    -keepclassmembers class * {
        @nl.spikey.orm.annotations.Column *;
    }
