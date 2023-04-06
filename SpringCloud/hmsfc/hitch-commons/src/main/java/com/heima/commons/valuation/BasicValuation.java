package com.heima.commons.valuation;

public class BasicValuation implements Valuation {
    private Valuation valuation;

    private float basicPrice = 2.3F;

    public BasicValuation(Valuation valuation){
        this.valuation = valuation;
    }

    @Override
    public float calculation(float km) {
        float beforeCost = (valuation == null ? 0f : valuation.calculation(km));
        if (km <= 3) {
            return beforeCost;
        }
        return beforeCost + (km - 3) * basicPrice;
    }
}
