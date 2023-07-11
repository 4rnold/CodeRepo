<template>
    <div>
        <el-dialog
            :title="form.id ? '编辑服务' : '新增服务'"
            v-model="dialogVisible"
            :show-close="false"
            :before-close="cancel"
            width="35%"
            :destroy-on-close="true"
        >
            <el-form :model="form" ref="serviceForm" :rules="rules" label-width="85px">
                <el-form-item prop="code" label="code:" required>
                    <el-input :disabled="form.id" v-model.trim="form.code" placeholder="请输入服务code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="name" label="服务名:" required>
                    <el-input v-model.trim="form.name" placeholder="请输入服务名" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="status" label="状态:" required>
                    <el-select v-model="form.status" default-first-option style="width: 100%" placeholder="请选择状态">
                        <el-option key="1" label="启用" :value="1"></el-option>
                        <el-option key="0" label="禁用" :value="0"></el-option>
                    </el-select>
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
import { serviceApi } from '../api';
import { ElMessage } from 'element-plus';

export default defineComponent({
    name: 'ServiceEdit',
    props: {
        visible: {
            type: Boolean,
        },
        service: {
            type: [Boolean, Object],
        },
    },
    setup(props: any, { emit }) {
        const serviceForm: any = ref(null);
        const state = reactive({
            dialogVisible: false,
            edit: false,
            form: {
                id: null,
                name: null,
                code: null,
                status: null,
            },
            btnLoading: false,
            rules: {
                name: [
                    {
                        required: true,
                        message: '请输入服务名',
                        trigger: ['change', 'blur'],
                    },
                ],
                code: [
                    {
                        required: true,
                        message: '请输入code',
                        trigger: ['change', 'blur'],
                    },
                ],
            },
        });

        watch(props, (newValue) => {
            if (newValue.service) {
                state.form = { ...newValue.service };
            } else {
                state.form = {} as any;
            }
            state.dialogVisible = newValue.visible;
        });

        const btnOk = async () => {
            let p = state.form.id ? serviceApi.update : serviceApi.save;
            serviceForm.value.validate((valid: boolean) => {
                if (valid) {
                    p.request(state.form).then(() => {
                        ElMessage.success('操作成功');
                        emit('val-change', state.form);
                        state.btnLoading = true;
                        setTimeout(() => {
                            state.btnLoading = false;
                        }, 1000);
                        //重置表单域
                        serviceForm.resetFields();
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
            serviceForm,
            btnOk,
            cancel,
        };
    },
});
</script>
<style lang="scss">
</style>
