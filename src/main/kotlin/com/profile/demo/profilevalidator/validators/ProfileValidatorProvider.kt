package com.profile.demo.profilevalidator.validators

import com.profile.demo.profilevalidator.util.Constants.VALIDATOR_ID_QB_ACCOUNTING
import com.profile.demo.profilevalidator.util.Constants.VALIDATOR_ID_QB_PAYMENTS
import com.profile.demo.profilevalidator.util.Constants.VALIDATOR_ID_QB_PAYROLL
import com.profile.demo.profilevalidator.util.Constants.VALIDATOR_ID_QB_TSHEETS
import com.profile.demo.profilevalidator.exception.NotImplementedProfileValidatorException

object ProfileValidatorProvider {

    fun getValidator(productId: String): ProfileValidator {
        return when (productId) {
            VALIDATOR_ID_QB_ACCOUNTING -> QBAccounting
            VALIDATOR_ID_QB_PAYROLL -> QBPayroll
            VALIDATOR_ID_QB_PAYMENTS -> QBPayments
            VALIDATOR_ID_QB_TSHEETS -> TSheets
            else -> throw NotImplementedProfileValidatorException("Validator not implemented for the product")
        }
    }
}