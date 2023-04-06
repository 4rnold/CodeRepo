package com.heima.commons.valuation;

public class StartPriceValuation implements Valuation {
    private Valuation valuation;

    private float startingPrice = 13.0F;

    public StartPriceValuation(Valuation valuation) {
        this.valuation = valuation;
    }

    @Override
    public float calculation(float km) {
        float beforeCost = (valuation == null ? 0f : valuation.calculation(km));
        return beforeCost + startingPrice;
    }
}
