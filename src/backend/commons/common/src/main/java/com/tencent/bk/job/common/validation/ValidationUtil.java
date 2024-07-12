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

package com.tencent.bk.job.common.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class ValidationUtil {

    /**
     * 搜索时间范围是否合理
     */
    public static boolean validateSearchTimeRange(Long startTime, Long endTime) {
        if (startTime == null || startTime < 1) {
            log.warn("startTime is empty or illegal, startTime={}", startTime);
            return false;
        }
        if (endTime == null || endTime < 1) {
            log.warn("endTime is empty or illegal, endTime={}", endTime);
            return false;
        }
        long period = endTime - startTime;
        if (period <= 0) {
            log.warn("startTime is greater or equal to endTime, startTime={}, endTime={}", startTime, endTime);
            return false;
        } else if (period > ValidationConstants.MAX_SEARCH_TASK_HISTORY_RANGE_MILLS) {
            log.warn("Search time range greater than {} days!", ValidationConstants.MAX_SEARCH_TASK_HISTORY_RANGE_MILLS);
            return false;
        }
        return true;
    }

    /**
     * 校验value是否合法
     * String: notEmpty, Long: notNull && >0, Integer: notNull && >0, List: notEmpty
     */
    public static boolean isValuePositiveOrNonEmpty (Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String) {
            return StringUtils.isNotBlank((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue() > 0;
        }
        if (value instanceof List) {
            return !((List<?>) value).isEmpty();
        }

        return true;
    }
}