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

import com.tencent.bk.job.common.validation.NotBlankField;
import com.tencent.bk.job.common.validation.NotContainSpecialChar;
import com.tencent.bk.job.common.validation.ValidationConstants;
import com.tencent.bk.job.manage.model.web.vo.TagVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @since 25/12/2019 10:47
 */
@Data
@ApiModel("模版更新基础信息报文")
public class TemplateBasicInfoUpdateReq {
    /**
     * 模版 ID
     */
    @Deprecated
    @ApiModelProperty(value = "作业模板 ID", hidden = true)
    private Long id;

    /**
     * 模版名称
     */
    @ApiModelProperty(value = "模版名称", required = true)
    @NotBlankField(message = "{validation.constraints.InvalidTemplateName_empty.message}")
    @NotContainSpecialChar
    @Length(
        max = ValidationConstants.COMMON_MAX_60,
        message = "{validation.constraints.InvalidTemplateName_outOfLength.message}"
    )
    private String name;

    /**
     * 模版描述
     */
    @ApiModelProperty(value = "模版描述", required = true)
    private String description;

    /**
     * 模版标签
     */
    @ApiModelProperty(value = "模版标签, 新增、修改、删除需要传入", required = true)
    private List<TagVO> tags;
}
