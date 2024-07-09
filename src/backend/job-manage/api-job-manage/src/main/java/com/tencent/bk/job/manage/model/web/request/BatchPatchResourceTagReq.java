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

package com.tencent.bk.job.manage.model.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tencent.bk.job.common.validation.ConditionType;
import com.tencent.bk.job.common.validation.ValidFieldsStrictValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("资源标签批量更新请求")
@ValidFieldsStrictValue(
    fieldNames = {"addTagIdList", "deleteTagIdList"},
    condition = ConditionType.AND,
    message = "{validation.constraints.InvalidTagId_empty.message}"
)
public class BatchPatchResourceTagReq {

    @ApiModelProperty(value = "资源类型, 可选值: 1-业务脚本,5-作业模板", required = true)
    @JsonProperty("resourceTypeList")
    @NotEmpty(message= "{validation.constraints.InvalidResourceType_empty.message}")
    private List<Integer> resourceTypeList;

    @ApiModelProperty(value = "新增的标签ID列表")
    @JsonProperty("addTagIdList")
    private List<Long> addTagIdList;

    @ApiModelProperty(value = "删除的标签ID列表")
    @JsonProperty("deleteTagIdList")
    private List<Long> deleteTagIdList;
}
