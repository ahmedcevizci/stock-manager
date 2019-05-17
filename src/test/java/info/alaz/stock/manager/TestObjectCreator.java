package info.alaz.stock.manager;

import java.util.UUID;

public final class TestObjectCreator {

    public static final UUID EXISTING_STOCK_ID1 = UUID.fromString("3f01795c-2b80-4e90-8065-0767a11588ed");
    public static final UUID EXISTING_STOCK_ID2 = UUID.fromString("920dddec-0900-4f8d-b4f1-a05ddeaa4159");
    public static final UUID EXISTING_STOCK_ID3 = UUID.fromString("c081fc48-332b-46d8-8b22-35eb8e02cc03");

    public static final String EXISTING_PRODUCT_ID1 = "vegetable-121";
    public static final String EXISTING_PRODUCT_ID2 = "vegetable-122";
    public static final String EXISTING_PRODUCT_ID3 = "vegetable-123";

    public static final String NOT_EXISTING_PRODUCT_ID = "ectoplasm-121";
    public static final UUID NOT_EXISTING_STOCK_ID = new UUID(0, 0);

}
