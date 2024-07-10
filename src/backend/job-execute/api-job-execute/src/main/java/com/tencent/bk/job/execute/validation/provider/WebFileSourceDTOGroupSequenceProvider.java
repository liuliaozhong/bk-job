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

package com.tencent.bk.job.execute.validation.provider;

import com.tencent.bk.job.execute.model.web.vo.ExecuteFileSourceInfoVO;
import com.tencent.bk.job.manage.api.common.constants.task.TaskFileTypeEnum;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * web 文件源联合校验
 */
public class WebFileSourceDTOGroupSequenceProvider implements DefaultGroupSequenceProvider<ExecuteFileSourceInfoVO> {

    @Override
    public List<Class<?>> getValidationGroups(ExecuteFileSourceInfoVO fileSource) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(ExecuteFileSourceInfoVO.class);
        if (fileSource != null) {
            Integer fileType = fileSource.getFileType();
            if (fileType == null || TaskFileTypeEnum.SERVER.getType() == fileType) {
                defaultGroupSequence.add(ExecuteFileSourceInfoVO.ServerFileGroup.class);
            } else if (TaskFileTypeEnum.FILE_SOURCE.getType() == fileType) {
                defaultGroupSequence.add(ExecuteFileSourceInfoVO.FileSourceGroup.class);
            } else if (TaskFileTypeEnum.LOCAL.getType() == fileType) {
                defaultGroupSequence.add(ExecuteFileSourceInfoVO.LocalFileGroup.class);
            }
        }
        return defaultGroupSequence;
    }
}
