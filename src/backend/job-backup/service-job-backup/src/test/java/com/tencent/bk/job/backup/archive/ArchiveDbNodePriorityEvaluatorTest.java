/*
 * Tencent is pleased to support the open source community by making BK-JOB蓝鲸智云作业平台 available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-JOB蓝鲸智云作业平台 is licensed under the MIT License.
 *
 * License for BK-JOB蓝鲸智云作业平台:
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.tencent.bk.job.backup.archive;

import com.tencent.bk.job.backup.archive.model.DbDataNode;
import com.tencent.bk.job.backup.archive.model.JobInstanceArchiveTaskInfo;
import com.tencent.bk.job.backup.constant.ArchiveTaskTypeEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArchiveDbNodePriorityEvaluatorTest {

    @Test
    void testStandaloneDb() {

        List<JobInstanceArchiveTaskInfo> runningTasks = new ArrayList<>();
        runningTasks.add(genTask(DbDataNode.standaloneDbDatNode(), 20240808, 2));
        runningTasks.add(genTask(DbDataNode.standaloneDbDatNode(), 20240808, 2));
        runningTasks.add(genTask(DbDataNode.standaloneDbDatNode(), 20240808, 3));

        Map<String, Integer> scheduleTaskCountGroupByDb = new HashMap<>();
        scheduleTaskCountGroupByDb.put(DbDataNode.STANDALONE_DS_NAME, 2);

        ArchiveDbNodePriorityEvaluator.DbNodeTasksInfo dbNodeTasksInfo =
            ArchiveDbNodePriorityEvaluator.evaluateHighestPriorityDbNode(runningTasks, scheduleTaskCountGroupByDb);
        assertThat(dbNodeTasksInfo).isNotNull();
        assertThat(dbNodeTasksInfo.getDbNodeId()).isEqualTo(DbDataNode.STANDALONE_DS_NAME);
        assertThat(dbNodeTasksInfo.getRunningTaskCount()).isEqualTo(3);
        assertThat(dbNodeTasksInfo.getScheduleTaskCount()).isEqualTo(2);
    }

    @Test
    void testShardingDb() {

        List<JobInstanceArchiveTaskInfo> runningTasks = new ArrayList<>();
        Map<String, Integer> scheduleTaskCountGroupByDb = new HashMap<>();

        runningTasks.add(genTask(DbDataNode.shardingDbDatNode("ds", 0, 1), 20240808, 1));
        runningTasks.add(genTask(DbDataNode.shardingDbDatNode("ds", 1, 0), 20240808, 1));
        runningTasks.add(genTask(DbDataNode.shardingDbDatNode("ds", 1, 1), 20240808, 1));
        scheduleTaskCountGroupByDb.put("ds:0", 2);
        scheduleTaskCountGroupByDb.put("ds:1", 1);

        ArchiveDbNodePriorityEvaluator.DbNodeTasksInfo dbNodeTasksInfo =
            ArchiveDbNodePriorityEvaluator.evaluateHighestPriorityDbNode(runningTasks, scheduleTaskCountGroupByDb);
        assertThat(dbNodeTasksInfo).isNotNull();
        assertThat(dbNodeTasksInfo.getDbNodeId()).isEqualTo("ds:0");

        runningTasks.clear();
        scheduleTaskCountGroupByDb.clear();

        runningTasks.add(genTask(DbDataNode.shardingDbDatNode("ds", 0, 1), 20240808, 1));
        runningTasks.add(genTask(DbDataNode.shardingDbDatNode("ds", 1, 1), 20240808, 1));
        scheduleTaskCountGroupByDb.put("ds:0", 2);
        scheduleTaskCountGroupByDb.put("ds:1", 1);

        dbNodeTasksInfo = ArchiveDbNodePriorityEvaluator.evaluateHighestPriorityDbNode(runningTasks, scheduleTaskCountGroupByDb);
        assertThat(dbNodeTasksInfo).isNotNull();
        assertThat(dbNodeTasksInfo.getDbNodeId()).isEqualTo("ds:0");

    }

    private JobInstanceArchiveTaskInfo genTask(DbDataNode dataNode, Integer day, Integer hour) {
        JobInstanceArchiveTaskInfo task = new JobInstanceArchiveTaskInfo();
        task.setTaskType(ArchiveTaskTypeEnum.JOB_INSTANCE);
        task.setDbDataNode(dataNode);
        task.setDay(day);
        task.setHour(hour);
        return task;
    }
}
