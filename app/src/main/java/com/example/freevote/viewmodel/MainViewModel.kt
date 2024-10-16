package com.example.freevote.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var lName by mutableStateOf("")
    var names by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")
    var gender by mutableStateOf("")
    var address by mutableStateOf("")
    var idNumber by mutableStateOf("")
    var pinCode by mutableStateOf("")
    var pinChange by mutableStateOf("")
    var confirm by mutableStateOf("")

    // Methods to update data
    fun updateLastName(lastName: String) {
        lName = lastName
    }

    fun updateNames(names: String) {
        this.names = names
    }

    fun updatePhoneNumber(phone: String) {
        phoneNumber = phone
    }

    fun updateEmail(emailAddress: String) {
        email = emailAddress
    }

    fun updateGender(gender: String) {
        this.gender = gender
    }

    fun updateAddress(address: String) {
        this.address = address
    }

    fun updateIdNumber(newId: String) {
        idNumber = newId
    }

    fun updatePinCode(pin: String) {
        pinCode = pin
    }

    fun updatePinChange(pin: String) {
        pinChange = pin
    }

    fun updateConfirmPin(pin: String) {
        confirm = pin
    }

    fun resetAllVariables() {
        lName = ""
        names = ""
        phoneNumber = ""
        email = ""
        gender = ""
        address = ""
        idNumber = "" // Set to null since it's of type String?
        pinCode = ""
        pinChange = ""
        confirm = ""
    }
    fun resetOtherVariables() {
        lName = ""
        names = ""
        phoneNumber = ""
        email = ""
        gender = ""
        address = ""// Set to null since it's of type String?
        pinCode = ""
        pinChange = ""
        confirm = ""
    }
}
