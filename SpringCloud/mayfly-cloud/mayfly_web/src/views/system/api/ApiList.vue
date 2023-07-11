<template>
    <div>
        <div class="toolbar">
            <el-button v-auth="'account:add'" type="primary" icon="plus" @click="editApi(true)">添加</el-button>
            <el-button v-auth="'account:update'" :disabled="chooseId == null" @click="editApi(false)" type="primary" icon="Edit">编辑</el-button>
            <el-button v-auth="'account:del'" :disabled="chooseId == null" @click="deleteAccount()" type="danger" icon="Delete">删除</el-button>
            <div style="float: right">
                <el-select v-model="query.serviceId" default-first-option style="width: 250px" placeholder="请选择服务">
                    <el-option v-for="item in services" :key="item.id" :label="`${item.code} [${item.name}]`" :value="item.id"></el-option>
                </el-select>
                <!-- <el-input class="ml5" placeholder="请输入服务code" style="width: 150px" v-model="query.code" @clear="search()" clearable></el-input> -->
                <el-button @click="search()" type="success" icon="search" class="ml5"></el-button>
            </div>
        </div>
        <el-table :data="datas" border ref="table" @current-change="choose" show-overflow-tooltip>
            <el-table-column label="选择" width="50px">
                <template #default="scope">
                    <el-radio v-model="chooseId" :label="scope.row.id">
                        <i></i>
                    </el-radio>
                </template>
            </el-table-column>
            <el-table-column prop="serviceCode" label="服务标识" min-width="115"></el-table-column>
            <el-table-column prop="method" label="method" min-width="85"></el-table-column>
            <el-table-column prop="uri" label="请求地址" min-width="185" show-overflow-tooltip></el-table-column>
            <el-table-column prop="codeType" label="code类型" min-width="70">
                <template #default="scope">
                    <el-tag type="info" effect="plain">{{ enums.ApiCodeTypeEnum.getLabelByValue(scope.row.codeType) }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="code" label="code" min-width="115"></el-table-column>
            <el-table-column prop="name" label="描述" min-width="125" show-overflow-tooltip></el-table-column>

            <el-table-column align="center" prop="status" label="状态" min-width="65">
                <template #default="scope">
                    <el-tag v-if="scope.row.status == 1" type="success" size="small">正常</el-tag>
                    <el-tag v-if="scope.row.status == 0" type="danger" size="small">禁用</el-tag>
                </template>
            </el-table-column>

            <el-table-column min-width="115" prop="creator" label="创建账号"></el-table-column>
            <el-table-column min-width="160" prop="createTime" label="创建时间">
                <template #default="scope">
                    {{ $filters.dateFormat(scope.row.createTime) }}
                </template>
            </el-table-column>
            <el-table-column min-width="115" prop="modifier" label="更新账号"></el-table-column>
            <el-table-column min-width="160" prop="updateTime" label="修改时间">
                <template #default="scope">
                    {{ $filters.dateFormat(scope.row.updateTime) }}
                </template>
            </el-table-column>

            <el-table-column label="操作" min-width="200px"> </el-table-column>
        </el-table>
        <el-pagination
            @current-change="handlePageChange"
            style="text-align: center"
            background
            layout="prev, pager, next, total, jumper"
            :total="total"
            v-model:current-page="query.pageNum"
            :page-size="query.pageSize"
        />

        <api-edit
            :services="services"
            :roles="roles"
            :resources="resources"
            v-model:visible="apiEditDialog.visible"
            v-model:api="apiEditDialog.data"
            @val-change="valChange()"
        />
    </div>
</template>

<script lang='ts'>
import { toRefs, reactive, onMounted, defineComponent } from 'vue';
import ApiEdit from './ApiEdit.vue';
import enums from '../enums';
import { apiApi, serviceApi, roleApi, resourceApi } from '../api';
import { ElMessage } from 'element-plus';
export default defineComponent({
    name: 'ApiList',
    components: {
        ApiEdit,
    },
    setup() {
        const state = reactive({
            services: [],
            roles: null,
            resources: null,
            chooseId: null,
            /**
             * 选中的数据
             */
            chooseData: null,
            /**
             * 查询条件
             */
            query: {
                pageNum: 1,
                pageSize: 10,
            },
            datas: [],
            total: 0,
            apiEditDialog: {
                visible: false,
                data: null,
            },
        });

        onMounted(() => {
            search();
            getServices();
        });

        const choose = (item: any) => {
            if (!item) {
                return;
            }
            state.chooseId = item.id;
            state.chooseData = item;
        };

        const search = async () => {
            let res: any = await apiApi.list.request(state.query);
            state.datas = res.list;
            state.total = res.total;
        };

        const getServices = async () => {
            let res: any = await serviceApi.list.request({});
            state.services = res.list;
        };

        const handlePageChange = (curPage: number) => {
            state.query.pageNum = curPage;
            search();
        };

        const getRoles = async () => {
            let res = await roleApi.list.request({ pageNum: 1, pageSize: 100 });
            state.roles = res.list;
        };

        const getResources = async () => {
            let res = await resourceApi.list.request();
            state.resources = res;
        };

        const editApi = (isAdd = false) => {
            if (!state.roles) {
                getRoles();
            }
            if (!state.resources) {
                getResources();
            }
            if (isAdd) {
                state.apiEditDialog.data = null;
            } else {
                state.apiEditDialog.data = state.chooseData;
            }
            state.apiEditDialog.visible = true;
        };

        const valChange = () => {
            state.apiEditDialog.visible = false;
            state.chooseId = null;
            state.chooseData = null;
            search();
        };

        const deleteAccount = async () => {
            try {
                await apiApi.del.request({ id: state.chooseId });
                ElMessage.success('删除成功');
                search();
            } catch (error) {
                ElMessage.error('刪除失败');
            }
        };

        return {
            ...toRefs(state),
            enums,
            search,
            handlePageChange,
            choose,
            editApi,
            valChange,
            deleteAccount,
        };
    },
});
</script>
<style lang="scss">
</style>
