package edu.stthomas.repo;

import edu.stthomas.model.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryRepoTest {

    @Test
    void getItem() {
        Item item = InventoryRepo.getItem("15");
        assertEquals(99,item.getThreshold());
    }
}
