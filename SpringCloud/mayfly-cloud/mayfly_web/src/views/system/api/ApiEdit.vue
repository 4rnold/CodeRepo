<template>
    <div>
        <el-dialog
            :title="form.id ? '编辑api' : '新增api'"
            v-model="dialogVisible"
            :show-close="false"
            :before-close="cancel"
            width="35%"
            :destroy-on-close="true"
        >
            <el-form :model="form" ref="apiForm" :rules="rules" label-width="95px">
                <el-form-item prop="serviceId" label="服务:" required>
                    <el-select v-model="form.serviceId" default-first-option style="width: 100%" placeholder="请选择服务">
                        <el-option v-for="item in services" :key="item.id" :label="`${item.code} [${item.name}]`" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="method" label="请求方法:" required>
                    <el-select v-model="form.method" default-first-option style="width: 100%" placeholder="请选择请求方法">
                        <el-option key="GET" label="GET" value="GET"></el-option>
                        <el-option key="POST" label="POST" value="POST"></el-option>
                        <el-option key="PUT" label="PUT" value="PUT"></el-option>
                        <el-option key="DELETE" label="DELETE" value="DELETE"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="uri" label="uri:" required>
                    <el-input v-model.trim="form.uri" placeholder="请输入请求地址" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="描述:" required>
                    <el-input v-model.trim="form.name" placeholder="请输入api描述" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="status" label="状态:" required>
                    <el-select v-model="form.status" default-first-option style="width: 100%" placeholder="请选择状态">
                        <el-option key="1" label="启用" :value="1"></el-option>
                        <el-option key="0" label="禁用" :value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="codeType" label="code类型:" required>
                    <el-select
                        v-model="form.codeType"
                        default-first-option
                        style="width: 100%"
                        placeholder="请选择关联的编码类型"
                        @change="form.code = ''"
                    >
                        <el-option v-for="item in enums.ApiCodeTypeEnum" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item v-if="form.codeType == enums.ApiCodeTypeEnum['ROLE'].value" prop="code" label="code:" required>
                    <el-select v-model="form.code" default-first-option filterable style="width: 100%" placeholder="请选择关联的角色code">
                        <el-option v-for="item in roles" :key="item.id" :label="`${item.code} [${item.name}]`" :value="item.code"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-if="form.codeType == enums.ApiCodeTypeEnum['RESOURCE'].value" prop="code" label="code:" required>
                    <el-input v-model.trim="form.code" placeholder="请输入关联的资源code" auto-complete="off"></el-input>
                    <!-- <el-tree-select v-model="form.code" placeholder="请选择关联的资源code" style="width: 100%" auto-complete="off"></el-tree-select> -->
                </el-form-item>
            </el-form>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="cancel()">取 消</el-button>
                    <el-button type="primary" :loading="btnLoading" @click="btnOk">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts">
import { toRefs, reactive, watch, defineComponent, ref } from 'vue';
import { apiApi } from '../api';
import enums from '../enums';
import { ElMessage } from 'element-plus';

export default defineComponent({
    name: 'ApiEdit',
    props: {
        visible: {
            type: Boolean,
        },
        api: {
            type: [Boolean, Object],
        },
        roles: {
            type: [Array],
        },
        resources: {
            type: [Array],
        },
        services: {
            type: [Array],
        },
    },
    setup(props: any, { emit }) {
        const apiForm: any = ref(null);
        const state = reactive({
            dialogVisible: false,
            edit: false,
            services: [],
            roles: [],
            resources: [],
            form: {
                id: null,
                serviceId: null,
                method: null,
                uri: null,
                name: null,
                code: null,
                status: null,
            },
            btnLoading: false,
            rules: {
                serviceId: [
                    {
                        required: true,
                        message: '请选择服务',
                        trigger: ['change', 'blur'],
                    },
                ],
                method: [
                    {
                        required: true,
                        message: '请选择请求方法',
                        trigger: ['change', 'blur'],
                    },
                ],
                uri: [
                    {
                        required: true,
                        message: '请填写请求地址',
                        trigger: ['change', 'blur'],
                    },
                ],
                name: [
                    {
                        required: true,
                        message: '请输入api描述',
                        trigger: ['change', 'blur'],
                    },
                ],
                code: [
                    {
                        required: true,
                        message: '请输入关联的资源code',
                        trigger: ['change', 'blur'],
                    },
                ],
            },
        });

        watch(props, (newValue) => {
            if (newValue.api) {
                state.form = { ...newValue.api };
                state.edit = true;
            } else {
                state.form = {} as any;
            }
            state.services = newValue.services;
            state.roles = newValue.roles;
            state.resources = newValue.resources;
            state.dialogVisible = newValue.visible;
        });

        const btnOk = async () => {
            let p = state.form.id ? apiApi.update : apiApi.save;

            apiForm.value.validate((valid: boolean) => {
                if (valid) {
                    p.request(state.form).then(() => {
                        ElMessage.success('操作成功');
                        emit('val-change', state.form);
                        state.btnLoading = true;
                        setTimeout(() => {
                            state.btnLoading = false;
                        }, 1000);
                        //重置表单域
                        apiForm.value.resetFields();
                        state.form = {} as any;
                    });
                } else {
                    ElMessage.error('表单填写有误');
                    return false;
                }
            });
        };

        const cancel = () => {
            emit('update:visible', false);
            emit('cancel');
        };

        return {
            ...toRefs(state),
            enums,
            apiForm,
            btnOk,
            cancel,
        };
    },
});
</script>
<style lang="scss">
</style>
