package com.meadowsapps.jgameboy.gbc.core;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class GbcCartridge {

    private Header header;

    private byte[] contents;

    public GbcCartridge(byte[] contents) {
        this.contents = contents;
        this.header = new Header();
    }

    public static GbcCartridge load(File file) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] buffer = new byte[(int) raf.length()];
        raf.read(buffer);
        GbcCartridge cartridge = new GbcCartridge(buffer);
        return cartridge;
    }

    class Header {

        private byte[] logo;
        private String title;
        private String manufacturerCode;
        private byte cgbFlag;
        private String newLicenseeCode;
        private byte sgbFlag;
        private byte cartridgeType;
        private byte romSize;
        private byte ramSize;
        private byte destinationCode;
        private byte oldLicenseeCode;
        private byte maskRomVersionNumber;
        private byte headerChecksum;
        private short globalChecksum;

        public static final int HEADER_START = 0x0100;
        public static final int HEADER_STOP = 0x014F;
        public static final int HEADER_LENGTH = HEADER_STOP - HEADER_START;

        public static final int ENTRY_POINT = 0x0100;
        public static final int NINTENDO_LOGO = 0x0104;
        public static final int TITLE = 0x0134;
        public static final int MANUFACTURER_CODE = 0x013F;
        public static final int CGB_FLAG = 0x0143;
        public static final int NEW_LICENSEE_CODE = 0x0144;
        public static final int SGB_FLAG = 0x0146;
        public static final int CARTRIDGE_TYPE = 0x0147;
        public static final int ROM_SIZE = 0x0148;
        public static final int RAM_SIZE = 0x0149;
        public static final int DESTINATION_CODE = 0x014A;
        public static final int OLD_LICENSEE_CODE = 0x014B;
        public static final int MASK_ROM_VERSION_NUMBER = 0x014C;
        public static final int HEADER_CHECKSUM = 0x014D;
        public static final int GLOBAL_CHECKSUM = 0x014E;

        public Header() {
            this.logo = Arrays.copyOfRange(contents, NINTENDO_LOGO, TITLE);
            byte[] titleCopy = Arrays.copyOfRange(contents, TITLE, MANUFACTURER_CODE);
            this.title = new String(titleCopy);
            byte[] manufacturerCodeCopy = Arrays.copyOfRange(contents, MANUFACTURER_CODE, CGB_FLAG);
            this.manufacturerCode = new String(manufacturerCodeCopy);
            this.cgbFlag = contents[CGB_FLAG];
            byte[] newLicenseeCodeCopy = Arrays.copyOfRange(contents, NEW_LICENSEE_CODE, SGB_FLAG);
            this.newLicenseeCode = new String(newLicenseeCodeCopy);
            this.sgbFlag = contents[SGB_FLAG];
            this.cartridgeType = contents[CARTRIDGE_TYPE];
            this.romSize = contents[ROM_SIZE];
            this.ramSize = contents[RAM_SIZE];
            this.destinationCode = contents[DESTINATION_CODE];
            this.oldLicenseeCode = contents[OLD_LICENSEE_CODE];
            this.maskRomVersionNumber = contents[MASK_ROM_VERSION_NUMBER];
            this.headerChecksum = contents[HEADER_CHECKSUM];
            int globalChecksum = (contents[GLOBAL_CHECKSUM] << 8) + contents[GLOBAL_CHECKSUM + 1];
            this.globalChecksum = (short) globalChecksum;
            System.out.println();
        }

        public byte[] getLogo() {
            return logo;
        }

        public String getTitle() {
            return title;
        }

        public String getManufacturerCode() {
            return manufacturerCode;
        }

        public int getCgbFlag() {
            return cgbFlag & 0xFF;
        }

        public String getNewLicenseeCode() {
            return newLicenseeCode;
        }

        public int getCartridgeType() {
            return cartridgeType;
        }

        public int getRomSize() {
            return romSize & 0xFF;
        }

        public int getRamSize() {
            return ramSize & 0xFF;
        }

        public int getDestinationCode() {
            return destinationCode & 0xFF;
        }

        public int getOldLicenseeCode() {
            return oldLicenseeCode & 0xFF;
        }

        public byte getMaskRomVersionNumber() {
            return maskRomVersionNumber;
        }

        public byte getHeaderChecksum() {
            return headerChecksum;
        }

        public short getGlobalChecksum() {
            return globalChecksum;
        }
    }
}
