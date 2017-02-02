package com.meadowsapps.jgameboy.gbc.core;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class GbcCartridge {

    private byte[] contents;

    private String title;

    public static final int ENTRY_POINT = 0x0100;
    public static final int NINTENDO_LOGO = 0x0104;
    public static final int TITLE = 0x0134;
    public static final int MANUFACTURER_CODE = 0x013F;
    public static final int CGB_FLAG = 0x0143;

    public static GbcCartridge load(File file) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        GbcCartridge cartridge = new GbcCartridge();
        cartridge.contents = new byte[(int) raf.length()];
        cartridge.title = getTitle(raf);
        System.out.println(cartridge.title);
        System.out.println(getManufacturerCode(raf));
        return cartridge;
    }

    private static String getTitle(RandomAccessFile raf) throws Exception {
        raf.seek(TITLE);
        byte[] buffer = new byte[MANUFACTURER_CODE - TITLE];
        raf.read(buffer);
        return new String(buffer);
    }

    private static String getManufacturerCode(RandomAccessFile raf) throws Exception {
        raf.seek(MANUFACTURER_CODE);
        byte[] buffer = new byte[CGB_FLAG - MANUFACTURER_CODE];
        raf.read(buffer);
        return new String(buffer);
    }
}
