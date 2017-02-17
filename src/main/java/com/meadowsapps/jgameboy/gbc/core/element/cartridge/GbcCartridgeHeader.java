package com.meadowsapps.jgameboy.gbc.core.element.cartridge;

import com.meadowsapps.jgameboy.core.util.Constants;

import java.util.Arrays;

/**
 * Created by Dylan on 2/6/17.
 */
public class GbcCartridgeHeader implements Constants {

    private byte[] logo;
    private String title;
    private String manufacturerCode;
    private byte cgbFlag;
    private String newLicenseeCode;
    private byte sgbFlag;
    private byte cartridgeType;
    private byte romSizeType;
    private byte ramSizeType;
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

    public GbcCartridgeHeader() {
    }

    public void initialize(byte[] contents) {
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
        this.romSizeType = contents[ROM_SIZE];
        this.ramSizeType = contents[RAM_SIZE];
        this.destinationCode = contents[DESTINATION_CODE];
        this.oldLicenseeCode = contents[OLD_LICENSEE_CODE];
        this.maskRomVersionNumber = contents[MASK_ROM_VERSION_NUMBER];
        this.headerChecksum = contents[HEADER_CHECKSUM];
        int globalChecksum = (contents[GLOBAL_CHECKSUM] << 8) + contents[GLOBAL_CHECKSUM + 1];
        this.globalChecksum = (short) globalChecksum;
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

    public int getSgbFlag() {
        return sgbFlag & 0xFF;
    }

    public int getCartridgeType() {
        return cartridgeType & 0xFF;
    }

    public int getRomSizeType() {
        return romSizeType & 0xFF;
    }

    public int getRomSize() {
        int rv = -1;

        switch (getRomSizeType()) {

            // 32KByte (no banks)
            case 0x00:
                rv = 32 * KILOBYTE;
                break;

            // 64KByte (4 banks)
            case 0x01:
                rv = 64 * KILOBYTE;
                break;

            // 128KByte (8 banks)
            case 0x02:
                rv = 128 * KILOBYTE;
                break;

            // 256KByte (16 banks)
            case 0x03:
                rv = 256 * KILOBYTE;
                break;

            // 512KByte (32 banks)
            case 0x04:
                rv = 512 * KILOBYTE;
                break;

            // 1MByte (64 banks)
            case 0x05:
                rv = 1 * MEGABYTE;
                break;

            // 2MByte (128 banks)
            case 0x06:
                rv = 2 * MEGABYTE;
                break;

            // 4MByte (256 banks)
            case 0x07:
                rv = 4 * MEGABYTE;
                break;

            // 1.1MByte (72 banks)
            case 0x52:
                rv = MEGABYTE + (128 * KILOBYTE);
                break;

            // 1.2MByte (80 banks)
            case 0x53:
                rv = MEGABYTE + (256 * KILOBYTE);
                break;

            // 1.5MByte (96 banks)
            case 0x54:
                rv = MEGABYTE + (512 * KILOBYTE);
                break;
        }

        return rv;
    }

    public int getRomBankCount() {
        int rv = 0;
        if (getRomSizeType() != 0) {
            rv = (getRomSize() / (16 * KILOBYTE));
        }
        return rv;
    }

    public int getRamSizeType() {
        return ramSizeType & 0xFF;
    }

    public int getRamSize() {
        int rv = -1;

        switch (getRamSizeType()) {

            // None
            case 0x00:
                rv = 0;
                break;

            // 2KBytes
            case 0x01:
                rv = 2 * KILOBYTE;
                break;

            // 8KBytes
            case 0x02:
                rv = 8 * KILOBYTE;
                break;

            // 32KBytes
            case 0x03:
                rv = 32 * KILOBYTE;
                break;

            // 1MByte
            case 0x04:
                rv = MEGABYTE;
                break;
        }

        return rv;
    }

    public int getRamBankCount() {
        int rv = 0;
        int ramSize = getRamSize();
        if (ramSize > 0x2000) {
            rv = ramSize / 0x2000;
        }
        return rv;
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
