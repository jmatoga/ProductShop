package jm.product_service.application.product.query;

import java.util.UUID;

import static jm.common.util.UtilHelper.PRODUCT_NAME;
import static jm.common.util.UtilHelper.checkNullOrEmpty;

public record GetProductQuery(UUID productId) {
    public GetProductQuery {
        checkNullOrEmpty(productId, PRODUCT_NAME);
    }
}
