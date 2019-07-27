package com.unacceptable.unacceptablelibrary;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;
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
}
