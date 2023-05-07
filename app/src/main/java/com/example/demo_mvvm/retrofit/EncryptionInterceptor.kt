package com.example.demo_mvvm.retrofit

import android.util.Base64
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionInterceptor : Interceptor {
    /**
     * Encryption mode enumeration
     */
    private enum class EncryptMode {
        ENCRYPT, DECRYPT
    }

    // cipher to be used for encryption and decryption
    private val _cx: Cipher

    // encryption key and initialization vector
    private val _key: ByteArray
    private val _iv: ByteArray

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        /* Log.e("===============ENCRYPTING REQUEST===============", "data")*/
        var request: Request = chain.request()
        val rawBody = request.body
        var encryptedBody = ""
        val mediaType = "text/plain; charset=utf-8".toMediaTypeOrNull()
        /*  if (mEncryptionStrategy != null) {*/try {
            val rawBodyString: String = requestBodyToString(rawBody)
            encryptedBody = encrypt(rawBodyString)
            Log.e("Raw body=> %s", rawBodyString)
            Log.e("Encrypted BODY=> %s", encryptedBody)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /* }else {
            throw new IllegalArgumentException("No encryption strategy!");
        }*/
        val body = RequestBody.create(mediaType, encryptedBody)

        //build new request
        request = request.newBuilder()
            .header("Content-Type", body.contentType().toString())
            .header("Content-Length", body.contentLength().toString())
            .method(request.method, body).build()
        return chain.proceed(request)
    }


    @Throws(IOException::class)
    fun requestBodyToString(requestBody: RequestBody?): String {
        val buffer = Buffer()
        if (requestBody != null) {
            requestBody.writeTo(buffer)
        }
        return buffer.readUtf8()
    }


    @Throws(Exception::class)
    fun encrypt(data: String): String {
        val bytes = encryptDecrypt(
            generateRandomIV16() + data,
            SHA256(key, 32),
            EncryptMode.ENCRYPT,
            generateRandomIV16()
        )
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    /**
     * @param inputText     Text to be encrypted or decrypted
     * @param encryptionKey Encryption key to used for encryption / decryption
     * @param mode          specify the mode encryption / decryption
     * @param initVector    Initialization vector
     * @return encrypted or decrypted bytes based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    @Throws(
        UnsupportedEncodingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun encryptDecrypt(
        inputText: String, encryptionKey: String,
        mode: EncryptMode, initVector: String
    ): ByteArray {
        var len = encryptionKey.toByteArray(charset("UTF-8")).size // length of the key	provided
        if (encryptionKey.toByteArray(charset("UTF-8")).size > _key.size) len = _key.size
        var ivlength = initVector.toByteArray(charset("UTF-8")).size
        if (initVector.toByteArray(charset("UTF-8")).size > _iv.size) ivlength = _iv.size
        System.arraycopy(encryptionKey.toByteArray(charset("UTF-8")), 0, _key, 0, len)
        System.arraycopy(initVector.toByteArray(charset("UTF-8")), 0, _iv, 0, ivlength)
        val keySpec = SecretKeySpec(
            _key,
            "AES"
        ) // Create a new SecretKeySpec for the specified key data and algorithm name.
        val ivSpec =
            IvParameterSpec(_iv) // Create a new IvParameterSpec instance with the bytes from the specified buffer iv used as initialization vector.

        // encryption
        return if (mode == EncryptMode.ENCRYPT) {
            // Potentially insecure random numbers on Android 4.3 and older. Read for more info.
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec) // Initialize this cipher instance
            _cx.doFinal(inputText.toByteArray(charset("UTF-8"))) // Finish multi-part transformation (encryption)
        } else {
            _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec) // Initialize this cipher instance
            val decodedValue = Base64.decode(inputText.toByteArray(), Base64.DEFAULT)
            _cx.doFinal(decodedValue) // Finish multi-part transformation (decryption)
        }
    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @param length length of the text to be returned
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun SHA256(text: String, length: Int): String {
        val resultString: String
        val md = MessageDigest.getInstance("SHA-256")
        md.update(text.toByteArray(charset("UTF-8")))
        val digest = md.digest()
        val result = StringBuilder()
        for (b in digest) {
            result.append(String.format("%02x", b)) //convert to hex
        }
        resultString = if (length > result.toString().length) {
            result.toString()
        } else {
            result.toString().substring(0, length)
        }
        return resultString
    }

    companion object {
        private val TAG = EncryptionInterceptor::class.java.simpleName

        private const val key = "GDEssQKasSHFAUNZbqvwR+yPq,a"
        // private const val key = "BlVssQKxzAHFAUNZbqvwS+yKw/m"

        /**
         * Generate IV with 16 bytes
         *
         * @return
         */
        fun generateRandomIV16(): String {
            val ranGen = SecureRandom()
            val aesKey = ByteArray(16)
            ranGen.nextBytes(aesKey)
            val result = StringBuilder()
            for (b in aesKey) {
                result.append(String.format("%02x", b)) //convert to hex
            }
            return if (16 > result.toString().length) {
                result.toString()
            } else {
                result.toString().substring(0, 16)
            }
        }
    }

    init {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        _cx = Cipher.getInstance("AES/CBC/PKCS5Padding")
        _key = ByteArray(32) //256 bit key space
        _iv = ByteArray(16) //128 bit IV
    }
}