package com.hey.idbyimage.idbyimage.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataCollectorTest {
    DataCollector dc = DataCollector.getDataCollectorInstance();
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCheckConnectivity(){
        try {
            assertTrue(dc.getServerStatus());
        }catch(Exception e){
            //System.out.println(e.getMessage());
            fail(e.getMessage());
        }
    }




}