package com.example.herculeswallet

import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.repository.CryptoRepository
import com.example.herculeswallet.utils.Encryption
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {

    private var encryption = Encryption()
    private val user : User = User(
        email = "prova@email.com",
        wallet = emptyMap<String,Crypto>(),
        preferences = mutableListOf<String>()
    )

    @Test
    fun calculateMD5(){
        assertEquals(encryption.md5(user.email + "/Bitcoin"),"6c415b24e64a9fa77e2edf420d847d2e")
    }


}