package com.meadowsapps.jgameboy.gbc.core.apu;

import com.meadowsapps.jgameboy.core.apu.Apu;
import com.meadowsapps.jgameboy.gbc.core.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import javax.sound.sampled.*;

/**
 * Created by Dylan on 2/16/17.
 */
public class GbcApu extends AbstractGbcCoreElement implements Apu {

    private SourceDataLine line;

    private byte[][] soundBuffer;

    private byte[] soundBufferMix;

    private int soundBufferIndex;

    private int soundTimer;

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
        return 0;
    }

    @Override
    public void write(int value, int addr) {

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

    }

    private void initChannels() {
        initChannel1();
        initChannel2();
        initChannel3();
        initChannel4();
    }

    private void initChannel1() {

    }

    private void initChannel2() {

    }

    private void initChannel3() {

    }

    private void initChannel4() {

    }

}
