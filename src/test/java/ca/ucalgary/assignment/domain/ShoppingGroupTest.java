package ca.ucalgary.assignment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ucalgary.assignment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShoppingGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingGroup.class);
        ShoppingGroup shoppingGroup1 = new ShoppingGroup();
        shoppingGroup1.setId(1L);
        ShoppingGroup shoppingGroup2 = new ShoppingGroup();
        shoppingGroup2.setId(shoppingGroup1.getId());
        assertThat(shoppingGroup1).isEqualTo(shoppingGroup2);
        shoppingGroup2.setId(2L);
        assertThat(shoppingGroup1).isNotEqualTo(shoppingGroup2);
        shoppingGroup1.setId(null);
        assertThat(shoppingGroup1).isNotEqualTo(shoppingGroup2);
    }
}
