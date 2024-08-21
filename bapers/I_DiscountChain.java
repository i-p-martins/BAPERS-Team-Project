package bapers;

import bapers.bapacct.DISCOUNT_TYPE;

public interface I_DiscountChain {

    /**
     * @param nextDiscountChain
     */
    I_DiscountChain setNextChain(I_DiscountChain nextDiscountChain);

    /**
     * @param request
     */
    void calculateDiscount(DISCOUNT_TYPE request);

}