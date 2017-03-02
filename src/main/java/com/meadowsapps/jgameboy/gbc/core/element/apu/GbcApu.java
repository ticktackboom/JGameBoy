package com.meadowsapps.jgameboy.gbc.core.element.apu;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.core.element.apu.Apu;
import com.meadowsapps.jgameboy.core.element.apu.Channel;
import com.meadowsapps.jgameboy.core.element.apu.Envelope;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;
import com.meadowsapps.jgameboy.gbc.core.element.AbstractGbcCoreElement;
import com.meadowsapps.jgameboy.groovy.core.util.Register8Bit;

import javax.sound.sampled.*;

import static com.meadowsapps.jgameboy.gbc.core.element.GbcMemoryMap.*;

/**
 * Created by Dylan on 2/16/17.
 */
public class GbcApu extends AbstractGbcCoreElement implements Apu {

    private int soundTimer;

    private SourceDataLine line;

    private byte[][] soundBuffer;

    private byte[] soundBufferMix;

    private int soundBufferIndex;

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
    public void initialize() throws InitializationException {
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

                soundTimer = 0;
                line.start();

                channel1.setOn(false);
                channel2.setOn(false);
                channel3.setOn(false);
                channel4.setOn(false);
            }
        } catch (LineUnavailableException e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void reset() {
        soundBuffer = new byte[4][750];

        soundTimer = 0;
        line.start();

        channel1.setOn(false);
        channel2.setOn(false);
        channel3.setOn(false);
        channel4.setOn(false);
        soundBufferIndex = 0;
    }

    @Override
    public void step() {
        initChannels();

        int cycles = cpu().clock().m();
        soundTimer += cycles;
        if (soundTimer >= 93) {
            soundTimer -= 93;
            if (isAllSoundOn()) {
                updateChannel1();
                updateChannel2();
                updateChannel3();
                updateChannel4();
                mixSound();
            } else {
                soundBufferMix[(soundBufferIndex * 2)] = 0;
                soundBufferMix[(soundBufferIndex * 2) + 1] = 0;
            }
            soundBufferIndex++;

            if (soundBufferIndex >= 750) {
                int numSamples = (1500 >= (line.available() * 2)) ? line.available() * 2 : 1500;
                line.write(soundBufferMix, 0, numSamples);
                soundBufferIndex = 0;
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
        if (isSoundReset(1)) {
            removeSoundReset(1);
            setSoundOn(1);

            int nr10 = mmu().read(NR10);
            int nr11 = mmu().read(NR11);
            int nr12 = mmu().read(NR12);
            int nr13 = mmu().read(NR13);
            int nr14 = mmu().read(NR14);

            channel1.setOn(true);
            channel1.setWaveDuty((nr11 >> 6) & 0x3);
            channel1.setIndex(0);

            channel1.setGbFrequency((nr13 | ((nr14 & 0x7) << 8)) & 0x7FF);
            channel1.setFrequency(131072 / (2048 - channel1.getGbFrequency()));

            if ((nr14 & 0x40) == 0x40) {
                channel1.setCount(true);
                channel1.setLength(((64 - (nr11 & 0x3F)) * SAMPLE_RATE) / 256);
            } else {
                channel1.setCount(false);
            }

            Envelope volume = new Envelope();
            volume.setBase((nr12 >> 4) & 0x0F);
            volume.setDirection((nr12 & 0x8) == 0x8 ? 1 : 0);
            volume.setStepLength((nr12 & 0x7) * SAMPLE_RATE / 64);
            volume.setIndex(volume.getStepLength());
            channel1.setVolume(volume);

            channel1.setSweepLength(((nr10 >> 4) & 0x7) * SAMPLE_RATE / 128);
            channel1.setSweepIndex(channel1.getSweepLength());
            channel1.setSweepDirection((nr10 & 0x8) == 0x8 ? -1 : 1);
            channel1.setSweepShift(nr10 & 0x7);
        }
    }

    private void updateChannel1() {
        if (channel1.isOn()) {
            int index = channel1.getIndex();
            channel1.setIndex(index + 1);

            int i = (int) ((32 * channel1.getFrequency() * channel1.getIndex()) / SAMPLE_RATE) % 32;
            int value = channel1.getWave()[i];
            soundBuffer[0][soundBufferIndex] = (byte) (value * channel1.getVolume().getBase());

            if (channel1.isCount() && channel1.getLength() > 0) {
                int length = channel1.getLength();
                channel1.setLength(length - 1);
                if (channel1.getLength() == 0) {
                    channel1.setOn(false);
                    setSoundOff(1);
                }
            }

            channel1.getVolume().handleSweep();
            if (channel1.getSweepIndex() > 0 && channel1.getSweepLength() > 0) {
                int sweepIndex = channel1.getSweepIndex();
                channel1.setSweepIndex(sweepIndex - 1);

                if (channel1.getSweepIndex() == 0) {
                    channel1.setSweepIndex(channel1.getSweepLength());
                    channel1.setGbFrequency(channel1.getGbFrequency() + (channel1.getGbFrequency() >> channel1.getSweepShift()) * channel1.getSweepDirection());
                    if (channel1.getGbFrequency() > 2047) {
                        channel1.setOn(false);
                        setSoundOff(1);
                    } else {
                        int nr13 = channel1.getGbFrequency() & 0xFF;
                        mmu().write(nr13, NR13);
                        int nr14 = mmu().read(NR14);
                        nr14 = (nr14 & 0xF8) | ((channel1.getGbFrequency() >> 8) & 0x7);
                        mmu().write(nr14, NR14);
                        channel1.setFrequency(131072 / (2048 - channel1.getGbFrequency()));
                    }
                }
            }
        }
    }

    private void initChannel2() {
        if (isSoundReset(2)) {
            removeSoundReset(2);
            setSoundOn(2);

            int nr21 = mmu().read(NR21);
            int nr22 = mmu().read(NR22);
            int nr23 = mmu().read(NR23);
            int nr24 = mmu().read(NR24);

            channel2.setOn(true);
            channel2.setWaveDuty((nr21 >> 6) & 0x3);
            channel2.setIndex(0);

            int freqX = nr23 | ((nr24 & 0x7) << 8);
            channel2.setFrequency(131072 / (2048 - freqX));

            if ((nr24 & 0x40) == 0x40) {
                channel2.setCount(true);
                channel2.setLength(((64 - (nr21 & 0x3F)) * SAMPLE_RATE) / 256);
            } else {
                channel2.setCount(false);
            }

            Envelope volume = new Envelope();
            volume.setBase((nr22 >> 4) & 0x0F);
            volume.setDirection((nr22 & 0x8) == 0x8 ? 1 : 0);
            volume.setStepLength((nr22 & 0x7) * SAMPLE_RATE / 64);
            volume.setIndex(volume.getStepLength());
            channel2.setVolume(volume);
        }
    }

    private void updateChannel2() {
        if (channel2.isOn()) {
            int index = channel2.getIndex();
            channel2.setIndex(index + 1);

            int i = (int) ((32 * channel2.getFrequency() * channel2.getIndex()) / SAMPLE_RATE) % 32;
            int value = channel2.getWave()[i];
            soundBuffer[1][soundBufferIndex] = (byte) (value * channel2.getVolume().getBase());

            if (channel2.isCount() && channel2.getLength() > 0) {
                int length = channel2.getLength();
                channel2.setLength(length - 1);
                if (channel2.getLength() == 0) {
                    channel2.setOn(false);
                    setSoundOff(2);
                }
            }

            channel2.getVolume().handleSweep();
        }
    }

    private void initChannel3() {
        if (isSoundReset(3)) {
            removeSoundReset(3);
            setSoundOn(3);

            int nr30 = mmu().read(NR30);
            int nr31 = mmu().read(NR31);
            int nr33 = mmu().read(NR33);
            int nr34 = mmu().read(NR34);

            channel3.setOn((nr30 & 0x80) == 0x80);
            channel3.setIndex(0);

            int freqX3 = nr33 | ((nr34 & 0x7) << 8);
            channel3.setFrequency(65536 / (2048 - freqX3));

            int[] channel3wav = new int[32];
            for (int i = 0x30; i < 0x40; i++) {
                int addr = 0xFF00 + i;
                int value = mmu().read(addr);
                channel3wav[((i - 0x30) * 2)] = ((value >> 4) & 0xF);
                channel3wav[((i - 0x30) * 2) + 1] = (value & 0xF);
            }
            channel3.setWave(channel3wav);

            if ((nr34 & 0x40) == 0x40) {
                channel3.setCount(true);
                channel3.setLength((256 - nr31) * SAMPLE_RATE / 256);
            } else {
                channel3.setCount(false);
            }
        }
    }

    private void updateChannel3() {
        if (channel3.isOn()) {
            int nr30 = mmu().read(NR30);
            int nr32 = mmu().read(NR32);

            int index = channel3.getIndex();
            channel3.setIndex(index + 1);

            int i = (int) ((32 * channel3.getFrequency() * channel3.getIndex()) / 44100) % 32;
            int value = channel3.getWave()[i];
            if ((nr32 & 0x60) != 0x0) {
                value >>= (((nr32 >> 5) & 0x3) - 1);
            } else {
                value = 0;
            }
            value <<= 1;
            if ((nr30 & 0x80) == 0x80) {
                soundBuffer[2][soundBufferIndex] = (byte) (value - 0xF);
            } else {
                soundBuffer[2][soundBufferIndex] = 0;
            }

            if (channel3.isCount() && channel3.getLength() > 0) {
                int length = channel3.getLength();
                channel3.setLength(length - 1);
                if (channel3.getLength() == 0) {
                    channel3.setOn(false);
                    setSoundOff(3);
                }
            }
        }
    }

    private void initChannel4() {
        if (isSoundReset(4)) {
            removeSoundReset(4);
            setSoundOn(4);

            int nr41 = mmu().read(NR41);
            int nr42 = mmu().read(NR42);
            int nr43 = mmu().read(NR43);
            int nr44 = mmu().read(NR44);

            channel4.setOn(true);
            channel4.setIndex(0);

            if ((nr44 & 0x40) == 0x40) {
                channel4.setCount(true);
                channel4.setLength((64 - (nr41 & 0x3F)) * SAMPLE_RATE / 256);
            } else {
                channel4.setCount(false);
            }

            Envelope volume = new Envelope();
            volume.setBase((nr42 >> 4) & 0x0F);
            volume.setDirection((nr42 & 0x8) == 0x48 ? 1 : 0);
            volume.setStepLength((nr42 & 0x7) * SAMPLE_RATE / 64);
            volume.setIndex(volume.getStepLength());
            channel4.setVolume(volume);

            channel4.setShiftFrequency(((nr43 >> 4) & 0xF) + 1);
            channel4.setCounterStep((nr43 & 0x8) == 0x8 ? 1 : 0);
            channel4.setDivRatio(nr43 & 0x7);
            if (channel4.getDivRatio() == 0) {
                channel4.setDivRatio(0.5F);
            }
            channel4.setFrequency((int) (524288 / channel4.getDivRatio()) >> channel4.getShiftFrequency());
        }
    }

    private void updateChannel4() {
        if (channel4.isOn()) {
            int index = channel4.getIndex();
            channel4.setIndex(index + 1);

            byte value = 0;
            if (channel4.getCounterStep() == 1) {
                int i = (int) ((channel4.getFrequency() * channel4.getIndex()) / SAMPLE_RATE) % 0x7F;
                value = (byte) ((NoiseChannel.NOISE_7[i >> 3] >> (i & 0x7)) & 0x1);
            } else {
                int i = (int) ((channel4.getFrequency() * channel4.getIndex()) / SAMPLE_RATE) % 0x7FFF;
                value = (byte) ((NoiseChannel.NOISE_15[i >> 3] >> (i & 0x7)) & 0x1);
            }
            soundBuffer[3][soundBufferIndex] = (byte) ((value * 2 - 1) * channel4.getVolume().getBase());

            if (channel4.isCount() && channel4.getLength() > 0) {
                int length = channel4.getLength();
                channel4.setLength(length - 1);
                if (channel4.getLength() == 0) {
                    channel4.setOn(false);
                    setSoundOff(4);
                }
            }

            channel4.getVolume().handleSweep();
        }
    }

    private void mixSound() {
        int leftAmp = 0;
        int rightAmp = 0;
        Channel[] channels = {
                channel1, channel2,
                channel3, channel4
        };
        for (int i = 0; i < 4; i++) {
            Channel channel = channels[i];
            if (isSoundToTerminal(i + 1, 2) && channel.isOn()) {
                leftAmp += soundBuffer[i][soundBufferIndex];
            }
            if (isSoundToTerminal(i + 1, 1) && channel.isOn()) {
                rightAmp += soundBuffer[i][soundBufferIndex];
            }
        }

        leftAmp *= getSoundLevel(2);
        leftAmp /= 4;
        rightAmp *= getSoundLevel(1);
        rightAmp /= 4;

        if (leftAmp > 127) {
            leftAmp = 127;
        }
        if (rightAmp > 127) {
            rightAmp = 127;
        }
        if (leftAmp < -127) {
            leftAmp = -127;
        }
        if (rightAmp < -127) {
            rightAmp = -127;
        }

        soundBufferMix[(soundBufferIndex * 2)] = (byte) leftAmp;
        soundBufferMix[(soundBufferIndex * 2) + 1] = (byte) rightAmp;
    }

    private boolean isAllSoundOn() {
        int nr52 = mmu().read(NR52);
        return (nr52 & 0x80) == 0x80;
    }

    private boolean isSoundOn(int soundNum) {
        int mask = 1 << (soundNum - 1);
        int nr52 = mmu().read(NR52);
        return (nr52 & mask) == mask;
    }

    private void setSoundOn(int soundNum) {
        int mask = 1 << (soundNum - 1);
        int nr52 = mmu().read(NR52);
        mmu().write(nr52 | mask, NR52);
    }

    private void setSoundOff(int soundNum) {
        int mask = 1 << (soundNum - 1);
        int nr52 = mmu().read(NR52);
        mmu().write(nr52 & ~mask, NR52);
    }

    private boolean isSoundToTerminal(int soundNum, int soundOutput) {
        int nr51 = mmu().read(NR51);
        int mask = 1 << ((soundNum - 1) + (soundOutput - 1) * 4);
        return (nr51 & mask) == mask;
    }

    private int getSoundLevel(int soundOutput) {
        int nr50 = mmu().read(NR50);
        return (nr50 >> ((soundOutput - 1) * 4)) & 0x7;
    }

    private boolean isSoundReset(int soundNum) {
        boolean rv = false;
        switch (soundNum) {
            case 1:
                int nr14 = mmu().read(NR14);
                rv = (nr14 & 0x80) == 0x80;
                break;
            case 2:
                int nr24 = mmu().read(NR24);
                rv = (nr24 & 0x80) == 0x80;
                break;
            case 3:
                int nr34 = mmu().read(NR34);
                rv = (nr34 & 0x80) == 0x80;
                break;
            case 4:
                int nr44 = mmu().read(NR44);
                rv = (nr44 & 0x80) == 0x80;
                break;
        }
        Register8Bit r = new Register8Bit();
        return rv;
    }

    private void removeSoundReset(int soundNum) {
        switch (soundNum) {
            case 1:
                int nr14 = mmu().read(NR14);
                mmu().write(nr14 & 0x7F, NR14);
                break;
            case 2:
                int nr24 = mmu().read(NR24);
                mmu().write(nr24 & 0x7F, NR24);
                break;
            case 3:
                int nr34 = mmu().read(NR34);
                mmu().write(nr34 & 0x7F, NR34);
                break;
            case 4:
                int nr44 = mmu().read(NR44);
                mmu().write(nr44 & 0x7F, NR44);
                break;
        }
    }


}
