package com.nuvyz.app.data.source.local

object Constant {
    object Session {
        const val SELECTED_ADDRESS = "selectedAddress"
        const val SELECTED_PAYMENT_METHOD = "selectedPaymentMethod"
        const val VISIT_ID = "visitId"
        const val PHOTO_OUTLET = "photoOutlet"
        const val PHOTO_SELFIE = "photoSelfie"
    }

    object Result {
        const val ORDER_SUCCESS = 700
        const val ORDER_REFRESH = 701
        const val ADDRESS_SELECTED = 702
        const val COMM_SUCCESS = 703
        const val SCHEDULE_SUCCESS = 704
        const val LEAVE_SUCCESS = 705
        const val VISIT_REFRESH = 706
        const val VISIT_START = 707
        const val ORDER_BACK_VISIT = 708
        const val NOTIFICATION_REFRESH = 709
    }

    object Survey {
        const val SINGLE_CHOICE = "single-choice"
        const val MULTIPLE_CHOICE = "multiple-choice"
        const val TEXT = "text"
        const val PHOTO = "photo"
    }

    object Type {
        const val VISIT = "visit"
        const val CUSTOMER = "Customer"
        const val NEW_CUSTOMER = "New Customer"
        const val EXIST_CUSTOMER = "Exist Customer"
        const val INACTIVE = "Inactive"
        const val ACTIVE = "Active"

    }
}