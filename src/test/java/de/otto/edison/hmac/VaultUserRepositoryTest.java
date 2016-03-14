package de.otto.edison.hmac;

import com.google.common.collect.ImmutableSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.testng.annotations.Test;

import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Test
public class VaultUserRepositoryTest {

    private VaultUserRepository testee;

    @Test
    public void shouldParseAuthJson() throws Exception {
        // given
        String authJson = StreamUtils.copyToString(new ClassPathResource("auth.json").getInputStream(), Charset.defaultCharset());

        // when
        testee = new VaultUserRepository(authJson);

        // then
        assertThat(testee.getKey("user1"), is("password1"));
        assertThat(testee.getKey("user2"), is("password2"));
        assertThat(testee.getKey("user3"), is("password3"));

        assertThat(testee.getRolesForUser("user1"), is(ImmutableSet.of("role1")));
        assertThat(testee.getRolesForUser("user2"), is(ImmutableSet.of("role1", "role2")));
        assertThat(testee.getRolesForUser("user3"), is(ImmutableSet.of()));

        assertThat(testee.hasRole("user1", "role1"), is(true));
        assertThat(testee.hasRole("user1", "role2"), is(false));
        assertThat(testee.hasRole("user2", "role1"), is(true));
        assertThat(testee.hasRole("user2", "role2"), is(true));
        assertThat(testee.hasRole("user2", "role3"), is(false));
        assertThat(testee.hasRole("user3", "role1"), is(false));
    }
}
