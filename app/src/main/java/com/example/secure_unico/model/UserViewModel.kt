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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


enum class SealedApiStatus {NOT_INITIALIZED, LOADING, ERROR, DONE }
enum class QrCodeStatus { LOADING, DONE }

const val bearer = "Bearer "

class UserViewModel : ViewModel() {
    //login data
    private val _loginStatus = MutableStateFlow(SealedApiStatus.NOT_INITIALIZED)
    private val _tokenResponse = MutableLiveData<TokenResponse>()
    private val _tickets = MutableLiveData<List<Ticket>>()
    private lateinit var loginRequest: LoginRequest
    private val _ticket = MutableLiveData<Ticket>()


    lateinit var bitmap: Bitmap
    private val _qrcodeStatus = MutableLiveData<QrCodeStatus>()



    //authdata


    val loginStatus: StateFlow<SealedApiStatus> = _loginStatus
    val tokenResponse: LiveData<TokenResponse> = _tokenResponse
    val tickets: LiveData<List<Ticket>> = _tickets
    val ticket: LiveData<Ticket> = _ticket


    fun getToken(username: String, password: String,) {


        viewModelScope.launch {
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


    fun onTicketClicked(ticket: Ticket) {
        _ticket.value = ticket

    }

    fun restoreStatus() {
        _loginStatus.value = SealedApiStatus.NOT_INITIALIZED
    }


}