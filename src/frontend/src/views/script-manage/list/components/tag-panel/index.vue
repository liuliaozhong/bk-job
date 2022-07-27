<!--
 * Tencent is pleased to support the open source community by making BK-JOB蓝鲸智云作业平台 available.
 *
 * Copyright (C) 2021 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-JOB蓝鲸智云作业平台 is licensed under the MIT License.
 *
 * License for BK-JOB蓝鲸智云作业平台:
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
-->

<template>
    <div class="script-list-tag-panel" v-bkloading="{ isLoading }">
        <tab-item
            :name="$t('script.全部脚本')"
            :id="1"
            :value="classesId"
            icon="business-manage"
            :count="totalCount"
            :tooltips-disabled="true"
            @on-select="handleClassesSelect" />
        <tab-item
            :name="$t('script.未分类')"
            :id="2"
            :value="classesId"
            icon="unclassified"
            :count="unclassifiedCount"
            :tooltips-disabled="true"
            @on-select="handleClassesSelect" />
        <div class="line" />
        <template v-for="item in list">
            <tab-item
                v-if="item.relatedScriptNum > 0"
                :key="item.id"
                :id="item.id"
                :count="item.relatedScriptNum"
                :name="item.name"
                :value="tagId"
                :can-edit="true"
                :tag-list="list"
                :description="item.description"
                @on-select="handleSelect"
                @on-edit="handleEdit" />
        </template>
    </div>
</template>
<script>
    import I18n from '@/i18n';
    import ScriptManageService from '@service/script-manage';
    import PublicScriptManageService from '@service/public-script-manage';
    import TagManageService from '@service/tag-manage';
    import PubliceTagManageService from '@service/public-tag-manage';
    import { checkPublicScript } from '@utils/assist';
    import TabItem from './tab-item';

    export default {
        name: 'RenderTagTabItem',
        components: {
            TabItem,
        },
        data () {
            return {
                isLoading: false,
                classesId: 1,
                tagId: 0,
                list: [],
                countMap: {},
                isNumberLoading: false,
            };
        },
        computed: {
            totalCount () {
                return this.countMap.total || 0;
            },
            unclassifiedCount () {
                return this.countMap.unclassified || 0;
            },
        },
        created () {
            this.isPublicScript = checkPublicScript(this.$route);
            this.init();
        },
        mounted () {
            this.parseDefaultValueFromURL();
        },
        methods: {
            /**
             * @desc 获取tag列表
             */
            fetchTagList () {
                if (this.isPublicScript) {
                    return PubliceTagManageService.fetchTagList();
                }
                return TagManageService.fetchWholeList();
            },
            /**
             * @desc 获取tag的使用数量
             */
            fetchTagScriptNum () {
                if (this.isPublicScript) {
                    return PublicScriptManageService.fetchTagCount();
                }
                return ScriptManageService.fetchTagCount();
            },
            init () {
                this.isLoading = true;
                Promise.all([
                    this.fetchTagList(),
                    this.fetchTagScriptNum(),
                ]).then(([tagList, countMap]) => {
                    this.countMap = Object.freeze(countMap);
                    const list = [];
                    tagList.forEach((tag) => {
                        tag.relatedScriptNum = countMap.tagCount[tag.id] || 0;
                        list.push(tag);
                    });
                    this.list = Object.freeze(list);
                    this.$emit('on-init', list);
                })
                    .finally(() => {
                        this.isLoading = false;
                    });
            },
            /**
             * @desc 解析url中的默认tag
             */
            parseDefaultValueFromURL () {
                let classesId = 1;
                if (this.$route.query.panelType) {
                    classesId = ~~this.$route.query.panelType || 1;
                    this.handleClassesSelect(classesId);
                    return;
                }
                
                if (this.$route.query.panelTag) {
                    const currentTagId = parseInt(this.$route.query.panelTag, 10);
                    if (currentTagId > 0) {
                        this.classesId = 0;
                        this.handleSelect(currentTagId);
                    }
                }
            },
            /**
             * @desc 分类切换
             * @param {Number} id 分类id
             */
            handleClassesSelect (id) {
                if (this.classesId === id) {
                    return;
                }
                this.classesId = id;
                this.tagId = 0;
                this.$emit('on-change', {
                    panelType: this.classesId,
                    panelTag: '',
                });
            },
            /**
             * @desc tag切换
             * @param {Number} id 分类id
             */
            handleSelect (id) {
                if (id === this.tagId) return;
                this.tagId = id;
                this.classesId = 0;
                this.$emit('on-change', {
                    panelType: '',
                    panelTag: this.tagId,
                });
            },
            /**
             * @desc 编辑tag
             * @param {Object} payload 标签数据
             *
             * 编辑成功需要刷新标签数据
             */
            handleEdit (payload) {
                TagManageService.updateTag(payload)
                    .then(() => {
                        this.messageSuccess(I18n.t('script.标签名更新成功'));
                        this.fetchTagList();
                    });
            },
        },
    };
</script>
<style lang='postcss' scoped>
    .script-list-tag-panel {
        display: flex;
        flex-direction: column;
        min-height: 50%;
        padding: 24px 0;

        .line {
            height: 1px;
            margin: 10px 0;
            background: #f0f1f5;
        }
    }
</style>