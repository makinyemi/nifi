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

package org.apache.nifi.processors.tests.system;

import org.apache.nifi.annotation.behavior.SupportsBatching;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.stream.io.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

@SupportsBatching
public class ReverseContents extends AbstractProcessor {
    public static final Relationship REL_SUCCESS = new Relationship.Builder()
        .name("success")
        .build();

    @Override
    public Set<Relationship> getRelationships() {
        return Collections.singleton(REL_SUCCESS);
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }

        final byte[] buffer = new byte[(int) flowFile.getSize()];
        try (final InputStream in = session.read(flowFile)) {
            StreamUtils.fillBuffer(in, buffer);
        } catch (final IOException e) {
            throw new ProcessException(e);
        }

        try (final OutputStream out = session.write(flowFile)) {
            for (int i = buffer.length - 1; i >= 0; i--) {
                out.write(buffer[i]);
            }
        } catch (final IOException e) {
            throw new ProcessException(e);
        }

        session.transfer(flowFile, REL_SUCCESS);
    }
}
