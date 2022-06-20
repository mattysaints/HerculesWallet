package com.example.herculeswallet.model

interface UsersDao {
    //@Query("SELECT * FROM user")
    fun getUsers(): List<User>
}