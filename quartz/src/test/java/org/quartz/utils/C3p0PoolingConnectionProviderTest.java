/*
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 * Copyright IBM Corp. 2024, 2025
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package org.quartz.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;
import org.quartz.integrations.tests.JdbcQuartzDerbyUtilities;
import org.quartz.integrations.tests.QuartzDerbyTestSupport;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A integration test to ensure PoolConnectionProvider is working properly.
 */
public class C3p0PoolingConnectionProviderTest extends QuartzDerbyTestSupport {
    boolean testConnectionProviderClass = false;

    @Test
    void testC3p0PoolProviderWithExtraProps() throws Exception {
        validateC3p0PoolProviderClassWithExtraProps();

        // Turn flag on for next test.
        testConnectionProviderClass = true;
    }

    @Test
    void testC3p0PoolProviderClassWithExtraProps() throws Exception {
        validateC3p0PoolProviderClassWithExtraProps();

        // Turn flag off for next test.
        testConnectionProviderClass = false;
    }

    private void validateC3p0PoolProviderClassWithExtraProps() {
        DBConnectionManager dbManager = DBConnectionManager.getInstance();
        ConnectionProvider provider = dbManager.getConnectionProvider("myDS");

        ComboPooledDataSource ds = ((C3p0PoolingConnectionProvider)provider).getDataSource();

        assertThat(ds.getDriverClass(), Matchers.is("org.apache.derby.jdbc.ClientDriver"));
        assertThat(ds.getJdbcUrl(), Matchers.is(JdbcQuartzDerbyUtilities.DATABASE_CONNECTION_PREFIX));
        assertThat(ds.getUser(), Matchers.is("quartz"));
        assertThat(ds.getPassword(), Matchers.is("quartz"));
        assertThat(ds.getMaxPoolSize(), Matchers.is(5));

        assertThat(ds.getMinPoolSize(), Matchers.is(5));
        assertThat(ds.getAcquireIncrement(), Matchers.is(5));
        assertThat(ds.getAcquireRetryAttempts(), Matchers.is(3));
        assertThat(ds.getAcquireRetryDelay(), Matchers.is(3000));
    }



    @Override
    protected Properties createSchedulerProperties() {
        Properties properties = new Properties();
        properties.put("org.quartz.scheduler.instanceName","TestScheduler");
        properties.put("org.quartz.scheduler.instanceId","AUTO");
        properties.put("org.quartz.scheduler.skipUpdateCheck","true");
        properties.put("org.quartz.threadPool.class","org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount","12");
        properties.put("org.quartz.threadPool.threadPriority","5");
        properties.put("org.quartz.jobStore.misfireThreshold","10000");
        properties.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.put("org.quartz.jobStore.driverDelegateClass","org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.put("org.quartz.jobStore.useProperties","true");
        properties.put("org.quartz.jobStore.dataSource","myDS");
        properties.put("org.quartz.jobStore.tablePrefix","QRTZ_");
        properties.put("org.quartz.jobStore.isClustered", "false");

        if (testConnectionProviderClass)
            properties.put("org.quartz.dataSource.myDS.connectionProvider.class", "org.quartz.utils.PoolingConnectionProvider");

        properties.put("org.quartz.dataSource.myDS.provider", "c3p0");

        properties.put("org.quartz.dataSource.myDS.driver", "org.apache.derby.jdbc.ClientDriver");
        properties.put("org.quartz.dataSource.myDS.URL",JdbcQuartzDerbyUtilities.DATABASE_CONNECTION_PREFIX);
        properties.put("org.quartz.dataSource.myDS.user","quartz");
        properties.put("org.quartz.dataSource.myDS.password","quartz");
        properties.put("org.quartz.dataSource.myDS.maxConnections","5");

        // Set extra properties
        properties.put("org.quartz.dataSource.myDS.minPoolSize","5");
        properties.put("org.quartz.dataSource.myDS.acquireIncrement","5");
        properties.put("org.quartz.dataSource.myDS.acquireRetryAttempts","3");
        properties.put("org.quartz.dataSource.myDS.acquireRetryDelay","3000");
        properties.put("org.quartz.dataSource.myDS.maxIdleTime","60");

        return properties;
    }
}
