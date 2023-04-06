package com.heima.commons.valuation;

public class FuelCostValuation implements Valuation {

    private Valuation valuation;
    private float fuelCosPrice = 1.0F;

    public FuelCostValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    @Override
    public float calculation(float km) {
        float beforeCost = (valuation == null ? 0f : valuation.calculation(km));
        return beforeCost + fuelCosPrice;
    }
}
