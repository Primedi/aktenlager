package org.tdvogt.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tdvogt.web.rest.TestUtil;

class AkteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Akte.class);
        Akte akte1 = new Akte();
        akte1.setId(1L);
        Akte akte2 = new Akte();
        akte2.setId(akte1.getId());
        assertThat(akte1).isEqualTo(akte2);
        akte2.setId(2L);
        assertThat(akte1).isNotEqualTo(akte2);
        akte1.setId(null);
        assertThat(akte1).isNotEqualTo(akte2);
    }
}
