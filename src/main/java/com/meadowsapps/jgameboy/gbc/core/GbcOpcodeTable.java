package com.meadowsapps.jgameboy.gbc.core;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dmeadows on 2/10/2017.
 */
public class GbcOpcodeTable {

    private Opcode[] opcode;

    private Opcode[] opcodeCB;

    public GbcOpcodeTable() {
        try {
            Gson gson = new Gson();
            String opcode = read("gbc/cpu/opcode.json");
            this.opcode = gson.fromJson(opcode, Opcode[].class);
            String opcodeCB = read("gbc/cpu/opcodeCB.json");
            this.opcodeCB = gson.fromJson(opcodeCB, Opcode[].class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String read(String resource) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("gbc/cpu/opcode.json");
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }

        return builder.toString();
    }

    public Opcode lookup(int id) {
        return opcode[id];
    }

    public Opcode lookupCB(int id) {
        return opcodeCB[id];
    }

}
