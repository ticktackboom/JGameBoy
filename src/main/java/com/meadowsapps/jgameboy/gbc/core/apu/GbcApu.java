package com.meadowsapps.jgameboy.gbc.core.apu;

import com.meadowsapps.jgameboy.core.apu.Apu;
import com.meadowsapps.jgameboy.gbc.core.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import javax.sound.sampled.*;

/**
 * Created by Dylan on 2/16/17.
 */
public class GbcApu extends AbstractGbcCoreElement implements Apu {

    private int soundTimer;

    private SourceDataLine line;

    private byte[][] soundBuffer;

    private byte[] soundBufferMix;

    private int soundBufferIndex;

    private int[] registers;

    private SquareWaveChannel channel1;

    private SquareWaveChannel channel2;

    private WaveChannel channel3;

    private NoiseChannel channel4;

    public static final int SAMPLE_SIZE = 2;

    public static final int SAMPLE_RATE = 44100;

    public GbcApu(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() {
        try {
            registers = new int[0x15];
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, 8, 2, 2, SAMPLE_RATE, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (AudioSystem.isLineSupported(info)) {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);

                soundBuffer = new byte[4][750];
                soundBufferMix = new byte[line.getBufferSize()];

                channel1 = new SquareWaveChannel();
                channel2 = new SquareWaveChannel();
                channel3 = new WaveChannel();
                channel4 = new NoiseChannel();
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public int read(int addr) {
        addr -= 0xFF10;
        return registers[addr];
    }

    @Override
    public void write(int value, int addr) {
        addr -= 0xFF10;
        registers[addr] = value;
    }

    public void startAudio() {
        soundTimer = 0;
        line.start();

        channel1.setOn(false);
        channel2.setOn(false);
        channel3.setOn(false);
        channel4.setOn(false);
        soundBufferIndex = 0;
    }

    public void updateSound(int cyclesRun) {
        initChannels();

        soundTimer += cyclesRun;
        if (soundTimer >= 93) {
            soundTimer -= 93;
            if (isAllSoundOn()) {
                updateChannel1();
                updateChannel2();
                updateChannel3();
                updateChannel4();

            }
        }
    }

    private void initChannels() {
        initChannel1();
        initChannel2();
        initChannel3();
        initChannel4();
    }

    private void initChannel1() {

    }

    private void updateChannel1() {

    }

    private void initChannel2() {

    }

    private void updateChannel2() {

    }

    private void initChannel3() {

    }

    private void updateChannel3() {

    }

    private void initChannel4() {

    }

    private void updateChannel4() {

    }

    private void mixSound() {
        int leftAmp = 0;
        if (isSoundToTerminal(1, 2) && channel1.isOn()) {
            leftAmp += soundBuffer[0][soundBufferIndex];
        }
        if (isSoundToTerminal(2, 2) && channel2.isOn()) {
            leftAmp += soundBuffer[1][soundBufferIndex];
        }
        if (isSoundToTerminal(3, 2) && channel3.isOn()) {
            leftAmp += soundBuffer[2][soundBufferIndex];
        }
        if (isSoundToTerminal(4, 2) && channel4.isOn()) {
            leftAmp += soundBuffer[3][soundBufferIndex];
        }

        leftAmp *= getSoundLevel(2);
        leftAmp /= 4;

        int rightAmp = 0;

    }

    private boolean isAllSoundOn() {
        int nr52 = mmu().readByte(NR52);
        return (nr52 & 0x80) == 0x80;
    }

    private boolean isSoundOn(int soundNum) {
        int mask = 1 << (soundNum - 1);
        int nr52 = mmu().readByte(NR52);
        return (nr52 & mask) == mask;
    }

    private boolean isSoundToTerminal(int soundNum, int soundOutput) {
        int nr51 = mmu().readByte(NR51);
        int mask = 1 << ((soundNum - 1) + (soundOutput - 1) * 4);
        return (nr51 & mask) == mask;
    }

    private int getSoundLevel(int soundOutput) {
        int nr50 = mmu().readByte(NR50);
        return (nr50 >> ((soundOutput - 1) * 4)) & 0x7;
    }

}
