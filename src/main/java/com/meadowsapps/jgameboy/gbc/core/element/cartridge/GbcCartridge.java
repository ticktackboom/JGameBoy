package com.meadowsapps.jgameboy.gbc.core.element.cartridge;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.cartridge.Cartridge;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.mbc.GbcMbcFactory;
import com.meadowsapps.jgameboy.gbc.core.element.cartridge.mbc.MemoryBankController;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class GbcCartridge extends AbstractGbcCoreElement implements Cartridge {

    private MemoryBankController mbc;

    private GbcCartridgeHeader header;

    public GbcCartridge(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() throws InitializationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void load(File file) throws IOException {
        // read in the rom to the contents
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] contents = new byte[(int) raf.length()];
        raf.readFully(contents);

        // initialize the header with the contents
        this.header = new GbcCartridgeHeader();
        this.header.initialize(contents);

        // get the memory bank controller and initialize with the contents
        GbcMbcFactory factory = GbcMbcFactory.getFactory();
        int type = header.getCartridgeType();
        this.mbc = factory.getMbc(type, this);
        this.mbc.initialize(contents);
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

}
