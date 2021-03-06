package jenkins.plugins.talk;

import hudson.model.Descriptor;
import hudson.util.FormValidation;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TalkNotifierTest extends TestCase {

    private TalkNotifierStub.DescriptorImplStub descriptor;
    private SlackServiceStub slackServiceStub;
    private boolean response;
    private FormValidation.Kind expectedResult;

    @Before
    @Override
    public void setUp() {
        descriptor = new TalkNotifierStub.DescriptorImplStub();
    }

    public TalkNotifierTest(SlackServiceStub slackServiceStub, boolean response, FormValidation.Kind expectedResult) {
        this.slackServiceStub = slackServiceStub;
        this.response = response;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection businessTypeKeys() {
        return Arrays.asList(new Object[][]{
                {new SlackServiceStub(), true, FormValidation.Kind.OK},
                {new SlackServiceStub(), false, FormValidation.Kind.ERROR},
                {null, false, FormValidation.Kind.ERROR}
        });
    }

    @Test
    public void testDoTestConnection() {
        if (slackServiceStub != null) {
            slackServiceStub.setResponse(response);
        }
        descriptor.setTalkService(slackServiceStub);
        try {
            FormValidation result = descriptor.doTestConnection("authToken", "buildServerUrl");
            assertEquals(result.kind, expectedResult);
        } catch (Descriptor.FormException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public static class SlackServiceStub implements TalkService {

        private boolean response;

        public boolean publish(String message) {
            return response;
        }

        public boolean publish(String message, String color) {
            return response;
        }

        public void setResponse(boolean response) {
            this.response = response;
        }
    }
}
