package com.meadowsapps.jgameboy.gbc.core.element;

import com.meadowsapps.jgameboy.core.element.InitializationException;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import static com.meadowsapps.jgameboy.gbc.core.element.GbcMemoryMap.DIV;
import static com.meadowsapps.jgameboy.gbc.core.element.GbcMemoryMap.TAC;

/**
 * Created by dmeadows on 3/1/2017.
 */
public class GbcTimer extends AbstractGbcCoreElement {

    private int main;

    private int sub;

    private int div;

    public GbcTimer(GbcCore core) {
        super(core);
    }

    @Override
    public void initialize() throws InitializationException {

    }

    @Override
    public void reset() {

    }

    public void inc() {
        sub += cpu().clock().m();
        if (sub >= 4) {
            main++;
            sub -= 4;

            div++;
            if (div == 16) {
                mmu().writeByte((div + 1) & 0xFF, DIV);
                div = 0;
            }
        }
    }

    private void check() {
        int tac = mmu().readByte(TAC);
        if ((tac & 0x04) == 1) {
            switch (tac & 0x03) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }
}
