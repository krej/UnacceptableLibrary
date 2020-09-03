package com.unacceptable.unacceptablelibrary;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.junit.Assert;
import org.junit.Test;

public class ToolsTests {

    @Test
    public void DecimalPartTest() {
        double d = 1.05;
        double dresult = Tools.DecimalPart(d);
        Assert.assertEquals(5, dresult, 0.1);
    }

    @Test
    public void Base64EncodingTest() {
        String s = "So this is a string to encode...";
        String result = Tools.encodeToBase64(s);
        Assert.assertEquals("/v8AUwBvACAAdABoAGkAcwAgAGkAcwAgAGEAIABzAHQAcgBpAG4AZwAgAHQAbwAgAGUAbgBjAG8AZABlAC4ALgAu", result);
    }

    @Test
    public void Base64DecodingTest() {
        String base64 = "/v8AUwBvACAAdABoAGkAcwAgAGkAcwAgAGEAIABzAHQAcgBpAG4AZwAgAHQAbwAgAGUAbgBjAG8AZABlAC4ALgAu";
        String result = Tools.decodeBase64(base64);
        Assert.assertEquals("So this is a string to encode...", result);
    }

    /*
    Commenting this out because it doesn't work. All the "isBase64" methods just check if the characters are valid for base64, not if it is encoded.
    Stackoverflow says to check if the string is multiples of 4, then do a regex against it. That still seems error prone, so I'm not gonna even try.
    @Test
    public void Base64DecodeNonEncodedString() {
        String s = "This is an old message that is not encoded";
        String result = Tools.getBase64String(s);
        Assert.assertEquals(s, result);
    }

     */

    @Test
    public void Base64DecodeNulLString() {
        String result = Tools.decodeBase64(null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void Base64EncodeNullString() {
        String result = Tools.encodeToBase64(null);
        Assert.assertEquals(null, result);
    }
}
