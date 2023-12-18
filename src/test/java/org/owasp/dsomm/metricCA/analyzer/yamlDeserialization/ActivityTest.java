package org.owasp.dsomm.metricCA.analyzer.yamlDeserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ActivityTest {

    private static final String LEVEL = "level";
    private static final String ACTIVITY_NAME = "activity name";
    private static final String COMPONENT_NAME = "component name";

    @Mock
    private Component component;
    @InjectMocks
    private Activity activity;

    @Test
    public void setLevel(){
        activity.setLevel(LEVEL);

        assertThat(activity.getLevel()).isEqualTo(LEVEL);
    }

    @Test
    public void getLevel(){
        String level = activity.getLevel();
        assertEquals(level,activity.getLevel());
    }

    @Test
    public void getName(){
        String name = activity.getName();
        assertEquals(name,activity.getName());
    }
}
