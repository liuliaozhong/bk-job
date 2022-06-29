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

package com.tencent.bk.job.manage.service.host.impl;

import com.tencent.bk.job.common.cc.service.CloudAreaService;
import com.tencent.bk.job.common.model.PageData;
import com.tencent.bk.job.common.model.dto.AppResourceScope;
import com.tencent.bk.job.common.model.dto.ApplicationDTO;
import com.tencent.bk.job.common.model.dto.ApplicationHostDTO;
import com.tencent.bk.job.common.util.StringUtil;
import com.tencent.bk.job.manage.model.web.request.ipchooser.AppTopologyTreeNode;
import com.tencent.bk.job.manage.service.ApplicationService;
import com.tencent.bk.job.manage.service.host.BizHostService;
import com.tencent.bk.job.manage.service.host.ScopeHostService;
import com.tencent.bk.job.manage.service.impl.topo.BizTopoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 资源范围下的主机服务
 */
@Slf4j
@Service
public class ScopeHostServiceImpl implements ScopeHostService {

    private final ApplicationService applicationService;
    private final BizHostService bizHostService;
    private final CloudAreaService cloudAreaService;
    private final BizTopoService bizTopoService;

    @Autowired
    public ScopeHostServiceImpl(ApplicationService applicationService,
                                BizHostService bizHostService,
                                CloudAreaService cloudAreaService,
                                BizTopoService bizTopoService) {
        this.applicationService = applicationService;
        this.bizHostService = bizHostService;
        this.cloudAreaService = cloudAreaService;
        this.bizTopoService = bizTopoService;
    }

    @Override
    public List<ApplicationHostDTO> getScopeHostsByIds(String username,
                                                       AppResourceScope appResourceScope,
                                                       Collection<Long> hostIds) {
        ApplicationDTO applicationDTO = applicationService.getAppByScope(appResourceScope);
        if (applicationDTO.isAllBizSet()) {
            // 全业务
            return bizHostService.getHostsByHostIds(hostIds);
        } else if (applicationDTO.isBizSet()) {
            // 业务集
            List<Long> bizIds = applicationDTO.getSubBizIds();
            return bizHostService.getHostsByBizAndHostIds(bizIds, hostIds);
        } else {
            // 普通业务
            Long bizId = Long.parseLong(applicationDTO.getScope().getId());
            return bizHostService.getHostsByBizAndHostIds(Collections.singletonList(bizId), hostIds);
        }
    }

    @Override
    public PageData<Long> listHostIdByBizTopologyNodes(String username,
                                                       AppResourceScope appResourceScope,
                                                       List<AppTopologyTreeNode> appTopoNodeList,
                                                       String searchContent,
                                                       Integer agentStatus,
                                                       Long start,
                                                       Long pageSize) {
        ApplicationDTO applicationDTO = applicationService.getAppByScope(appResourceScope);
        StopWatch watch = new StopWatch("listHostByBizTopologyNodes");
        watch.start("genConditions");
        List<Long> moduleIds = null;
        List<Long> bizIds = null;
        if (applicationDTO.isAllBizSet()) {
            // 全业务
            log.debug("listHostIdByBizTopologyNodes of allBizSet:{}", appResourceScope);
        } else if (applicationDTO.isBizSet()) {
            // 业务集
            bizIds = applicationDTO.getSubBizIds();
        } else {
            // 普通业务
            Long bizId = Long.parseLong(applicationDTO.getScope().getId());
            moduleIds = bizTopoService.findAllModuleIdsOfNodes(bizId, appTopoNodeList);
        }
        watch.stop();

        List<String> searchContents = null;
        if (searchContent != null) {
            searchContents = StringUtil.splitByNormalSeparator(searchContent);
        }

        //获取所有云区域，找出名称符合条件的所有CloudAreaId
        List<Long> cloudAreaIds = cloudAreaService.getAnyNameMatchedCloudAreaIds(searchContents);

        return bizHostService.pageListHostId(
            bizIds,
            moduleIds,
            cloudAreaIds,
            searchContents,
            agentStatus,
            start,
            pageSize
        );
    }
}