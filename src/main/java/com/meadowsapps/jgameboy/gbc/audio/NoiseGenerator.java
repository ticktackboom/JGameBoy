package com.meadowsapps.jgameboy.gbc.audio;

import com.meadowsapps.jgameboy.core.Envelope;

import java.util.Random;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class NoiseGenerator extends GbcSoundGenerator {

    private boolean[] random;

    private int cyclePos;

    private int cycleOffset;

    private int cycleLength;

    private Envelope envelope;

    private int amplitude;

    private int frequency;

    private int counter;

    public NoiseGenerator(int sampleRate) {
        super(sampleRate);
        setChannel(CHANNEL_LEFT | CHANNEL_RIGHT);
        envelope = new Envelope();
        cycleLength = 2;
        amplitude = 32;

        Random rand = new Random();
        random = new boolean[0x7FFF];
        for (int i = 0; i < 0x7FFF; i++) {
            random[i] = rand.nextBoolean();
        }
    }

    @Override
    public void play(byte[] b, int length, int offset) {
        if (getLength() != 0) {
            int newLength = getLength() - 1;
            setLength(newLength);
        }

        counter++;
        if (envelope.getNumberOfSteps() != 0) {
            if (((counter % envelope.getNumberOfSteps()) == 0) && (amplitude > 0)) {
                if (!envelope.isIncrease()) {
                    if (amplitude > 0) amplitude -= 2;
                } else {
                    if (amplitude < 16) amplitude += 2;
                }
            }
        }

        int step = ((frequency) / (getSampleRate() >> 8));
        for (int i = offset; i < offset + length; i++) {
            boolean value = random[((cycleOffset) + (cyclePos >> 8)) & 0x7FFF];
            int v = value ? (amplitude / 2) : (-amplitude / 2);

            if ((getChannel() & CHANNEL_LEFT) != 0) b[i * 2] += v;
            if ((getChannel() & CHANNEL_RIGHT) != 0) b[i * 2 + 1] += v;
            if ((getChannel() & CHANNEL_MONO) != 0) b[i] += v;

            cyclePos = (cyclePos + step) % cycleLength;
        }
    }

    public void configure(float ratio, int frequencyShift, boolean usePolynomialSteps) {
        if (!usePolynomialSteps) {
            cycleLength = 32767 << 8;
            cycleOffset = 0;
        } else {
            Random rand = new Random();
            cycleLength = 63 << 8;
            cycleOffset = (int) (rand.nextFloat() * 1000);
        }
        if (ratio == 0) ratio = 0.5f;
        frequency = ((int) (4194304 / 8 / ratio)) >> (frequencyShift + 1);
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
        int initialValue = envelope.getInitialValue();
        amplitude = initialValue * 2;
    }

    public void setEnvelope(int initialValue, int numberOfSteps, boolean increase) {
        envelope.setInitialValue(initialValue);
        envelope.setNumberOfSteps(numberOfSteps);
        envelope.setIncrease(increase);
        amplitude = initialValue * 2;
    }
}
