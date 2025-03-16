package com.techhub.util.core

import java.util.regex.Pattern

object RegexHelper {

    const val digitsRegex = "[0-9]+"
    const val digitsLettersRegex = "[a-zA-Z0-9]+"
    const val usernameRegex = "[a-z0-9]+"
    const val nameRegex = "[a-zA-Z0-9-, ']+"
    const val postalCodeRegex = "^[0-9a-zA-Z\\s]+"
    const val addressRegex = "[a-zA-Z0-9-_.,/ ()]+"
    const val cityRegex = "[a-zA-Z0-9-_.()!/' ]+"
    const val coinRegex = "[0-9\\.]+"
    const val ignoreSpaceRegex = "[^ {}]+"
    const val FNAME_PATTERNS = "^[a-zA-Z]*\$"
    const val FULL_NAME_PATTERNS = "^[a-zA-Z\\s]*$"
    const val CVV_PATTERNS = "^\\d{3}(?:\\d{1})?$"
    const val CVV_REGEX_PATTERNS = "^[0-9]{0,4}$"
    const val SPENDING_LIMIT_REGEX_PATTERNS = "^[0-9]+$"
    const val AMOUNT_PATTERNS = "^[0-9]+(?:\\.[0-9]+)?\$"
    const val EDITBOX_PATTERNS = "^[A-Za-z0-9.,_ -]+\$"
    const val LNAME_PATTERNS = "^(?!.*['_-]{2})(?!.*[!\"#\$%&()*+,.\\/:;<=>?@\\\\[\\\\]\\^{|}~]).+"
    const val USERNAME_PATTERNS = "^[a-z0-9]{4,15}"
    const val MOBILE_PATTERNS = "^[0-9]+"
    const val EMPTY_STRING_PATTERNS = "^(\\w+\\S+)\$"
    const val ADDRESS_PATTERNS = "^(?!.*[!\"'#\$%&+:;<=>?@\\\\[\\\\]\\^{|}~]).+\$"
    const val POSTALCODE_PATTERNS = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
    const val EMAIL_PATTERNS = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z][a-zA-Z\\-]{0,25}" +
            ")+"

    // const val CUSTOM_SHARE_LINK_PATTERN = "^(?!.*[_-]{2})[a-z0-9_-]{4,20}\$"
    val PASSWORD_PATTERN = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$&()-`.+,/\"])[A-Za-zd!@#\$&()-`.+,/\"].{7,}"
    const val CUSTOM_SHARE_LINK_PATTERN = "^(?!.*[_-]{2})[a-z0-9_-]{0,20}\$"
    val Digits = "(\\p{Digit}+)"
    val HexDigits = "(\\p{XDigit}+)"
    val Exp = "[eE][+-]?$Digits"
    val pinRegex = "^(?!.*(\\d)\\1{2})[0-9]{6}$"
    val fpRegex = "[\\x00-\\x20]*" +  // Optional leading "whitespace"
            "[+-]?(" +  // Optional sign character
            "NaN|" +  // "NaN" string
            "Infinity|" +  // "Infinity" string
            // A decimal floating-point string representing a finite positive
            // number without a leading sign has at most five basic pieces:
            // Digits . Digits ExponentPart FloatTypeSuffix
            //
            // Since this method allows integer-only strings as input
            // in addition to strings of floating-point literals, the
            // two sub-patterns below are simplifications of the grammar
            // productions from the Java Language Specification, 2nd
            // edition, section 3.10.2.
            // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
            "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +  // . Digits ExponentPart_opt FloatTypeSuffix_opt
            "(\\.(" + Digits + ")(" + Exp + ")?)|" +  // Hexadecimal strings
            "((" +  // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "(\\.)?)|" +  // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
            "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
            ")[pP][+-]?" + Digits + "))" +
            "[fFdD]?))" +
            "[\\x00-\\x20]*" // Optional trailing "whitespace"

    fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex()) // Regular expression for numeric strings
    }
    fun isPasswordIsStrong(password: String): Boolean {
        val PASSWORD_PATTERN =
            "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$&()-`.+,/\"])[A-Za-zd!@#\$&()-`.+,/\"].{7,}"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
    fun extractYTId(youTubeUrl: String?): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }
    fun isEmail(email: String): Boolean {
        return matchPattern(regex = EMAIL_PATTERNS, text =  email)
    }

    fun matchPattern(regex: String, text: String): Boolean {
        return Pattern.compile(regex.toRegex().toString()).matcher(text).matches()
    }

}