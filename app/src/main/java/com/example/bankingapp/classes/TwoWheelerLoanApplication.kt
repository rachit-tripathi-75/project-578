package com.example.bankingapp.classes


class TwoWheelerLoanApplication {
    // Getters and Setters
    var firstName: String? = null
    var lastName: String? = null
    var mobile: String? = null
    var email: String? = null
    var dateOfBirth: String? = null
    var panNumber: String? = null
    var loanAmount: Int = 0
    var tenure: Int = 0
    var interestRate: Double = 0.0
    var emi: Long = 0
    var timestamp: Long

    init {
        this.timestamp = System.currentTimeMillis()
    }

    override fun toString(): String {
        return "LoanApplication{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", loanAmount=" + loanAmount +
                ", tenure=" + tenure +
                ", emi=" + emi +
                '}'
    }
}