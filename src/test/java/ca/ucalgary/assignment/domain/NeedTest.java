package ca.ucalgary.assignment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.ucalgary.assignment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NeedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Need.class);
        Need need1 = new Need();
        need1.setId(1L);
        Need need2 = new Need();
        need2.setId(need1.getId());
        assertThat(need1).isEqualTo(need2);
        need2.setId(2L);
        assertThat(need1).isNotEqualTo(need2);
        need1.setId(null);
        assertThat(need1).isNotEqualTo(need2);
    }
}
