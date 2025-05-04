package hr.ht.bitt.fix.orderTrackingService.constants

/**
 * This is a class containing all the errors with their codes and messages
 */
enum class ExceptionMessages(val code: String, val message: String) {
    PARENT_ORDER_ID_MISSING_FOR_PROVISIONING_ORDER_TYPE(
        "OTS-1000",
        "Parent order id is mandatory if order type is set to PROVISIONING_ORDER",
    ),
    NO_QUERY_PARAMETERS_PROVIDED(
        "OTS-1001",
        "No query parameters were provided, provide at least one parameter. If parameter orderType is provided, orderId must be provided as well and vice versa"
    ),
    ORDER_ID_NOT_PROVIDED("OTS-1002", "Order type was provided without orderId. Both query parameters must be present"),
    ORDER_TYPE_NOT_PROVIDED(
        "OTS-1003",
        "Order id was provided without orderType. Both query parameters must be present"
    ),
    DATE_FROM_TO_MISSING(
        "OTS-1004",
        "Either dateFrom or dateTo is missing. Both query parameters must be present"
    ),
    DATE_RANGE_EXCEEDED_ONE_YEAR(
        "OTS-1005",
        "Span between date from and date to should not be more than 1 year"
    ),
    DATE_RANGE_EXCEEDED_10_DAYS(
        "OTS-1006",
        "Span between date from and date to should not be more than 10 days"
    ),
    ACTION_OFFER_ID_MISSING(
        "OTS-1007",
        "Either action or  offer id is missing. Both query parameters must be present"
    ),
    ACTION_OFFER_ID_WITH_OTHER_MISSING(
        "OTS-1008",
        "When providing both action and offer ID as query parameters, include at least one additional parameter."
    ),
    ORDER_ID_TYPE_MISSING(
        "OTS-1009",
        "Either OrderId or Order Type is missing. Both query parameters must be present"
    ),
    SUBSCRIPTIONS_NOT_FOUND_FOR_PORTABILITY("OTS-1010", "Subscriptions cannot be empty or null for portability order"),
    DATE_FROM_IS_AFTER_DATE_TO("OTS-1011", "Date from should not be after Date to"),
    MISSING_USER_DATA_PARAMETER(
        "OTS-1012",
        "When providing Username, Shop Code and Sales Channel Code, either all of them or none of them must be provided"
    )
}
