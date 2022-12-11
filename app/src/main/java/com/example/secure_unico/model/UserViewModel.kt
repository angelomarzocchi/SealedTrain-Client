package com.example.secure_unico.model

import android.graphics.Bitmap
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_unico.network.LoginRequest
import com.example.secure_unico.network.SealedApi
import com.example.secure_unico.network.Ticket
import com.example.secure_unico.network.TokenResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


enum class SealedApiStatus { LOADING, ERROR, DONE }
enum class QrCodeStatus { LOADING, DONE }

const val bearer = "Bearer "

class UserViewModel : ViewModel() {
    //login data
    private val _loginStatus = MutableLiveData<SealedApiStatus>()
    private val _tokenResponse = MutableLiveData<TokenResponse>()
    private val _tickets = MutableLiveData<List<Ticket>>()
    private lateinit var loginRequest: LoginRequest
    private val _ticket = MutableLiveData<Ticket>()


    lateinit var bitmap: Bitmap
    private val _qrcodeStatus = MutableLiveData<QrCodeStatus>()
    private val qrCodeStatus: LiveData<QrCodeStatus> = _qrcodeStatus


    //authdata


    val loginStatus: LiveData<SealedApiStatus> = _loginStatus
    val tokenResponse: LiveData<TokenResponse> = _tokenResponse
    val tickets: LiveData<List<Ticket>> = _tickets
    val ticket: LiveData<Ticket> = _ticket


    fun getToken(username: String, password: String) {


        runBlocking {
            loginRequest = LoginRequest(username, password)
            _loginStatus.value = SealedApiStatus.LOADING
            try {
                _tokenResponse.value = SealedApi.retrofitService.getToken(loginRequest)
                _loginStatus.value = SealedApiStatus.DONE


            } catch (e: Exception) {
                Log.d("loginError", e.message.toString())
                _loginStatus.value = SealedApiStatus.ERROR
            }
        }

    }

    fun authenticate() {
        viewModelScope.launch {
            try {
                SealedApi.retrofitService.authenticate(bearer + tokenResponse.value!!.token)
            } catch (e: Exception) {
                Log.d("authError", e.message.toString())
            }
        }
    }

    fun getTicketsFromApi() {
        viewModelScope.launch {
            try {
                _tickets.value =
                    SealedApi.retrofitService.getTickets(bearer + tokenResponse.value!!.token)
            } catch (e: Exception) {
                _tickets.value = listOf()
                Log.d("getTicketsError", e.message.toString())
            }
        }
    }

    fun generateQrCode(color: Int) {
        viewModelScope.launch {
            _qrcodeStatus.value = QrCodeStatus.LOADING
            val qrgEncoder = QRGEncoder(ticket.value?.qrcode, null, QRGContents.Type.TEXT, 1024)
            qrgEncoder.colorBlack = color
            bitmap = qrgEncoder.getBitmap(0)
            _qrcodeStatus.value = QrCodeStatus.DONE
        }
    }


    fun getRoute(): String {
        return "${ticket.value?.startingPoint} - ${ticket.value?.endingPoint}"
    }

    fun getType(): String {
        return when (ticket.value?.type) {
            "FULL_YEAR" -> "Full Year"
            "SEVEN_DAYS" -> "Seven Days"
            "ONE_DAY" -> "One Day"
            else -> "Error"
        }
    }

    fun getValidation(): String {
        return "${
            ticket.value?.startValidation?.subSequence(
                0,
                10
            )
        }\n${ticket.value?.startValidation?.subSequence(11, 19)}"
    }

    fun onTicketClicked(ticket: Ticket) {
        _ticket.value = ticket

    }


}