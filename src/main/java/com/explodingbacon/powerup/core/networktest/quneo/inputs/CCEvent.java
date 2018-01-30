package com.explodingbacon.powerup.core.networktest.quneo.inputs;

@FunctionalInterface
public interface CCEvent {

    void onControlChange(int cc, int data);
}
