/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nifi.processors.evtx.parser.bxml.value;

import org.apache.nifi.processors.evtx.parser.bxml.BxmlNodeTestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiletimeTypeNodeTest extends BxmlNodeTestBase {
    @Test
    public void testFiletimeTypeNode() throws IOException {
        final Instant instant = Instant.now();
        assertEquals(FiletimeTypeNode.DATE_TIME_FORMATTER.format(instant.atOffset(ZoneOffset.UTC)),
                new FiletimeTypeNode(testBinaryReaderBuilder.putFileTime(Date.from(instant)).build(), chunkHeader, parent, -1).getValue());
    }
}
