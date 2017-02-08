package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.Cartridge;
import com.meadowsapps.jgameboy.gbc.core.mbc.GbcMbcFactory;
import com.meadowsapps.jgameboy.gbc.core.mbc.MemoryBankController;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class GbcCartridge extends AbstractGbcCoreElement implements Cartridge {

    private byte[] contents;

    private GbcCartridgeHeader header;

    private MemoryBankController mbc;

    public GbcCartridge(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void load(File file) throws IOException {
        // read in the rom to the contents
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        this.contents = new byte[(int) raf.length()];
        raf.readFully(this.contents);

        // initialize the header with the contents
        this.header = new GbcCartridgeHeader();
        this.header.load(this.contents);

        // get the memory bank controller and initialize with the contents
        GbcMbcFactory factory = GbcMbcFactory.getFactory();
        int type = header.getCartridgeType();
        this.mbc = factory.getMbc(type, this);
        this.mbc.initialize(this.contents);

//        // initialize rom banks
//        int romBankCount = header.getRomBankCount();
//        romBankCount = (romBankCount == 0) ? 1 : romBankCount;
//        int romBankSize = (romBankCount == 1) ? 0x8000 : 0x4000;
//        rom = new int[romBankCount][romBankSize];
//
//        // initialize ram banks
//        int ramBankCount = header.getRamBankCount();
//        ramBankCount = (ramBankCount == 0) ? 1 : ramBankCount;
//        int ramBankSize = header.getRamSize();
//        ramBankSize = (ramBankCount == 1) ? ramBankSize : 0x2000;
//        ram = new int[ramBankCount][ramBankSize];
//
//        // read in the rom contents
//        int bank = 0;
//        raf.seek(0x0000);
//        for (int pos = 0; pos < file.length(); pos += romBankSize) {
//            // go to pos and read into buffer
//            raf.seek(pos);
//            byte[] buffer = new byte[romBankSize];
//            raf.read(buffer);
//
//            // store buffer as int[] to proper rom bank
//            IntBuffer intBuf = ByteBuffer.wrap(buffer).asIntBuffer();
//            rom[bank] = new int[intBuf.remaining()];
//            intBuf.get(rom[bank]);
//            bank++;
//        }
    }

    @Override
    public int read(int addr) {
        return mbc.read(addr);
    }

    @Override
    public void write(int value, int addr) {
        mbc.write(value, addr);
    }

    public GbcCartridgeHeader getHeader() {
        return header;
    }

    public byte[] getContents() {
        return contents;
    }
}
