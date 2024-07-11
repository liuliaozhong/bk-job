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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class NotBlankFieldValidator implements ConstraintValidator<NotBlankField, CharSequence> {

    private String fieldName;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void initialize(NotBlankField constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
        log.info("---------fieldName={}", fieldName);
        if (charSequence == null) {
            if (StringUtils.isNotEmpty(fieldName)) {
                context.disableDefaultConstraintViolation();
                String localizedFieldName = messageSource.getMessage(fieldName, null, LocaleContextHolder.getLocale());
                String errorMessage = messageSource.getMessage(context.getDefaultConstraintMessageTemplate(), null,
                    LocaleContextHolder.getLocale());
                String finalMessage = localizedFieldName + errorMessage;
                log.info("---------localizedFieldName={}, errorMessage={}", localizedFieldName, errorMessage);
                context.buildConstraintViolationWithTemplate(finalMessage).addConstraintViolation();
            }
            return false;
        }

        return charSequence.toString().trim().length() > 0;
    }
}
