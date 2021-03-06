/*
 * Copyright 2013-2014 eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kylinolap.cube;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.kylinolap.common.util.JsonUtil;
import com.kylinolap.cube.model.CubeDesc;
import com.kylinolap.cube.model.validation.IValidatorRule;
import com.kylinolap.cube.model.validation.ValidateContext;
import com.kylinolap.cube.model.validation.rule.AggregationGroupSizeRule;

/**
 * @author jianliu
 * 
 */
public class AggregationGroupSizeRuleTest {

    private CubeDesc cube;
    private ValidateContext vContext = new ValidateContext();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        CubeDesc desc2 = JsonUtil.readValue(getClass().getClassLoader().getResourceAsStream("data/TEST2_desc.json"), CubeDesc.class);
        this.cube = desc2;

    }

    @Test
    public void testOneMandatoryColumn() {
        IValidatorRule<CubeDesc> rule = new AggregationGroupSizeRule() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.kylinolap.metadata.validation.rule.AggregationGroupSizeRule
             * #getMaxAgrGroupSize()
             */
            @Override
            protected int getMaxAgrGroupSize() {
                return 3;
            }
        };
        rule.validate(cube, vContext);
        vContext.print(System.out);
        assertEquals("Failed to validate aggragation group error", vContext.getResults().length, 2);
        assertTrue("Failed to validate aggragation group error", vContext.getResults()[0].getMessage().startsWith("Length of the number"));
        assertTrue("Failed to validate aggragation group error", vContext.getResults()[1].getMessage().startsWith("Length of the number"));
        // assertTrue("Failed to validate aggragation group error",
        // vContext.getResults()[2].getMessage()
        // .startsWith("Hierachy column"));
    }

    @Test
    public void testAggColumnSize() {
        AggregationGroupSizeRule rule = new AggregationGroupSizeRule() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * com.kylinolap.metadata.validation.rule.AggregationGroupSizeRule
             * #getMaxAgrGroupSize()
             */
            @Override
            protected int getMaxAgrGroupSize() {
                return 20;
            }
        };
        rule.validate(cube, vContext);
        vContext.print(System.out);
        assertEquals("Failed to validate aggragation group error", vContext.getResults().length, 0);
        // assertTrue("Failed to validate aggragation group error",
        // vContext.getResults()[0].getMessage()
        // .startsWith("Aggregation group"));
        // assertTrue("Failed to validate aggragation group error",
        // vContext.getResults()[0].getMessage()
        // .startsWith("Hierachy column"));
    }
}
