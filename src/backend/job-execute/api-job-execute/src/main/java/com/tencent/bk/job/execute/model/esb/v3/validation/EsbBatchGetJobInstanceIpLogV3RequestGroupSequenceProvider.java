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

package com.tencent.bk.job.execute.model.esb.v3.validation;

import com.tencent.bk.job.common.validation.ValidationGroups;
import com.tencent.bk.job.execute.model.esb.v3.request.EsbBatchGetJobInstanceIpLogV3Request;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * EsbBatchGetJobInstanceIpLogV3Request 根据主机列表批量查询作业执行日志请求参数分组校验
 */
@Slf4j
public class EsbBatchGetJobInstanceIpLogV3RequestGroupSequenceProvider
    implements DefaultGroupSequenceProvider<EsbBatchGetJobInstanceIpLogV3Request> {
    @Override
    public List<Class<?>> getValidationGroups(EsbBatchGetJobInstanceIpLogV3Request request) {
        List<Class<?>> validationGroups = new ArrayList<>();
        validationGroups.add(EsbBatchGetJobInstanceIpLogV3Request.class);
        if (request != null) {
            if (request.getHostIdList() != null) {
                validationGroups.add(ValidationGroups.EsbServerV3.HostId.class);
            } else if (request.getIpList() != null){
                validationGroups.add(ValidationGroups.EsbServerV3.IP.class);
            } else {
                validationGroups.add(ValidationGroups.EsbServerV3.HostId.class);
            }
        }
        return validationGroups;
    }
}