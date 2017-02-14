package com.meadowsapps.jgameboy.gbc.core.apu;

import com.meadowsapps.jgameboy.core.apu.Envelope;

/**
 * Created by dmeadows on 1/24/2017.
 */
public class SquareWaveGenerator extends GbcSoundGenerator {

    /**
     * Current position in the waveform (in samples)
     */
    int cyclePos;

    /**
     * Length of the waveform (in samples)
     */
    int cycleLength;

    /**
     * Amplitude of the waveform
     */
    int amplitude;

    /**
     * Amount of time the sample stays high in a single waveform (in eighths)
     */
    int dutyCycle;

    private Envelope envelope;

    /**
     * Current position in the envelope
     */
    int counter;

    /**
     * Frequency of the sound in internal GB format
     */
    int frequency;

    /**
     * Amount of time between sweep steps.
     */
    int timeSweep;

    /**
     * Number of sweep steps
     */
    int numSweep;

    /**
     * If true, sweep will decrease the sound frequency, otherwise, it will increase
     */
    boolean decreaseSweep;

    /**
     * Current position in the sweep
     */
    int counterSweep;

    public SquareWaveGenerator(int sampleRate) {
        super(sampleRate);
        setChannel(CHANNEL_LEFT | CHANNEL_RIGHT);
        envelope = new Envelope();

    }

    @Override
    public void play(byte[] b, int length, int offset) {
        if (getLength() != 0) {
            int newLength = getLength() - 1;
            setLength(newLength);

            if (timeSweep != 0) {
                counterSweep++;
                if (counterSweep > timeSweep) {
                    if (decreaseSweep) {
                        setFrequency(frequency - (frequency >> numSweep));
                    } else {
                        setFrequency(frequency + (frequency >> numSweep));
                    }
                    counterSweep = 0;
                }
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

            for (int r = offset; r < offset + length; r++) {
                int v = 0;
                if (cycleLength != 0) {
                    v = (((8 * cyclePos) / cycleLength) >= dutyCycle) ? amplitude : -amplitude;
                }

                if ((getChannel() & CHANNEL_LEFT) != 0) b[r * 2] += v;
                if ((getChannel() & CHANNEL_RIGHT) != 0) b[r * 2 + 1] += v;
                if ((getChannel() & CHANNEL_MONO) != 0) b[r] += v;
                cyclePos = (cyclePos + 256) % cycleLength;
            }
        }
    }

    public int getDutyCycle() {
        return dutyCycle;
    }

    public void setDutyCycle(int dutyCycle) {
        switch (dutyCycle) {
            case 0:
                dutyCycle = 1;
                break;
            case 1:
                dutyCycle = 2;
                break;
            case 2:
                dutyCycle = 4;
                break;
            case 3:
                dutyCycle = 6;
                break;
        }
        this.dutyCycle = dutyCycle;
    }

    public void setFrequency(int gbFrequency) {
        try {
            float frequency = 131072 / 2048;

            if (gbFrequency != 2048) {
                frequency = ((float) 131072 / (float) (2048 - gbFrequency));
            }
            this.frequency = gbFrequency;
            if (frequency != 0) {
                cycleLength = (256 * getSampleRate()) / (int) frequency;
            } else {
                cycleLength = 65535;
            }
            if (cycleLength == 0) cycleLength = 1;
//  System.out.println("Cycle length : " + cycleLength + " samples");
        } catch (ArithmeticException e) {
            // Skip ip
        }
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public void setEnvelope(int initialValue, int numberOfSteps, boolean increase) {
        envelope.setInitialValue(initialValue);
        envelope.setNumberOfSteps(numberOfSteps);
        envelope.setIncrease(increase);
        amplitude = initialValue * 2;
    }

    public void setSweep(int time, int num, boolean decrease) {
        timeSweep = (time + 1) / 2;
        numSweep = num;
        decreaseSweep = decrease;
        counterSweep = 0;
//  System.out.println("Sweep: " + time + ", " + num + ", " + decrease);
    }

}
