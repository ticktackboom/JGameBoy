package com.meadowsapps.jgameboy.gbc.core.element.apu;

import com.meadowsapps.jgameboy.core.element.apu.BaseChannel;
import com.meadowsapps.jgameboy.core.element.apu.Envelope;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class SquareWaveChannel extends BaseChannel {

    private Envelope volume;

    private int gbFrequency;

    private int sweepIndex;

    private int sweepLength;

    private int sweepDirection;

    private int sweepShift;

    public static final int[][] SOUNDWAVE_PATTERN = defineSoundWavePattern();

    public Envelope getVolume() {
        return volume;
    }

    public void setVolume(Envelope volume) {
        this.volume = volume;
    }

    public int getGbFrequency() {
        return gbFrequency;
    }

    public void setGbFrequency(int gbFrequency) {
        this.gbFrequency = gbFrequency;
    }

    public int getSweepIndex() {
        return sweepIndex;
    }

    public void setSweepIndex(int sweepIndex) {
        this.sweepIndex = sweepIndex;
    }

    public int getSweepLength() {
        return sweepLength;
    }

    public void setSweepLength(int sweepLength) {
        this.sweepLength = sweepLength;
    }

    public int getSweepDirection() {
        return sweepDirection;
    }

    public void setSweepDirection(int sweepDirection) {
        this.sweepDirection = sweepDirection;
    }

    public int getSweepShift() {
        return sweepShift;
    }

    public void setSweepShift(int sweepShift) {
        this.sweepShift = sweepShift;
    }

    public void setWaveDuty(int waveDuty) {
        setWave(SOUNDWAVE_PATTERN[waveDuty]);
    }

    private static int[][] defineSoundWavePattern() {
        int[] pattern = {4, 8, 16, 24};
        int[][] soundWavePattern = new int[4][32];
        for (int i = 0; i < 4; i++) {
            soundWavePattern[i] = new int[32];
            for (int j = 0; j < 32; j++) {
                soundWavePattern[i][j] = (j < pattern[i]) ? 1 : -1;
            }
        }
        return soundWavePattern;
    }

}
