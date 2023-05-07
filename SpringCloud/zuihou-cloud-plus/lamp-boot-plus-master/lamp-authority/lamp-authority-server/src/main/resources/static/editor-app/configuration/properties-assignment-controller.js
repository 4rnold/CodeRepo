/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Assignment
 */
var KisBpmAssignmentCtrl = ['$scope', '$modal', function ($scope, $modal) {

    // Config for the modal window
    var opts = {
        template: 'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Assignment
 */
var KisBpmAssignmentCtrl = ['$scope', '$modal', function ($scope, $modal) {

    // Config for the modal window
    var opts = {
        template: 'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmAssignmentPopupCtrl = ['$scope', '$http', function ($scope, $http) {

    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.assignment !== undefined
        && $scope.property.value.assignment !== null) {
        $scope.assignment = $scope.property.value.assignment;
    } else {
        $scope.assignment = {};
    }

    $scope.popup = {
        assignment: {
            assignee: '',
            //已选择的候选人列表
            assigneeIds: [],
            // 候选人显示的值
            assigneeField: '',
            assigneeFieldReg: '',
            candidateUsers: [],
            candidateUserField: '',
            candidateGroups: [],
            candidateGroupField: ''
        }
    };

    if ($scope.assignment.assignee) {
        $scope.popup.assignment.assigneeIds = $scope.assignment.assignee.split(',');
    }

    if ($scope.assignment.candidateUsers && $scope.assignment.candidateUsers.length > 0) {
        for (var i = 0; i < $scope.assignment.candidateUsers.length; i++) {
            $scope.popup.assignment.candidateUsers.push($scope.assignment.candidateUsers[i]);
            $scope.popup.assignment.candidateUserField += (i == 0 ? '' : ',') + $scope.assignment.candidateUsers[i].value;
        }
    }

    if ($scope.assignment.candidateGroups && $scope.assignment.candidateGroups.length > 0) {
        for (var i = 0; i < $scope.assignment.candidateGroups.length; i++) {
            $scope.popup.assignment.candidateGroups.push($scope.assignment.candidateGroups[i]);
            $scope.popup.assignment.candidateGroupField += (i == 0 ? '' : ',') + $scope.assignment.candidateGroups[i].value;
        }
    }

    $scope.save = function () {
        debugger
        $scope.assignment.assignee = $scope.popup.assignment.assigneeIds.join(',') + ($scope.popup.assignment.assigneeFieldReg ? ',' + $scope.popup.assignment.assigneeFieldReg : '');
        $scope.assignment.candidateUsers = $scope.popup.assignment.candidateUsers;
        $scope.assignment.candidateGroups = $scope.popup.assignment.candidateGroups;
        $scope.property.value = {};
        $scope.property.value.assignment = $scope.assignment;

        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    // Close button handler
    $scope.close = function () {
        $scope.property.mode = 'read';
        $scope.$hide();
    };

    $scope.clearAssignee = function () {
        $scope.popup.assignment.assigneeIds = []
        $scope.popup.assignment.assigneeField = ''
    }
    $scope.clearCandidateUser = function () {
        $scope.popup.assignment.candidateUsers = []
        $scope.popup.assignment.candidateUserField = ''
    }
    $scope.clearGroup = function () {
        $scope.popup.assignment.candidateGroups = []
        $scope.popup.assignment.candidateGroupField = ''
    }

    //******************************自定义选择代理人，候选人，候选组******************************
    //参数初始化
    $scope.gridData = [];//表格数据
    $scope.gridDataTempUser = [];//用户数据集合
    $scope.gridDataTempRole = [];//角色数据集合
    $scope.gridDataName = 'gridData';
    $scope.selectTitle = '选择代理人';
    $scope.columnData = [];//表格列数据
    $scope.columnDataName = 'columnData';
    $scope.selectType = 0;//0-代理人，1-候选人，2-候选组
    $scope.totalServerItems = 0;//表格总条数
    //分页初始化
    $scope.pagingOptions = {
        pageSizes: [5, 10, 20],//page 行数可选值
        pageSize: 5, //每页行数
        currentPage: 1, //当前显示页数
    };
    //Grid 筛选
    $scope.projects = [];
    $scope.selectedProject = -1;

    //数据监视
    $scope.dataWatch = function () {
        //分页数据监视
        $scope.$watch('pagingOptions', function (newValue, oldValue) {
            $scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
        }, true);

        //当切换类型时，初始化当前页
        $scope.$watch('selectType', function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.pagingOptions.currentPage = 1;
                $scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
            }
        }, true);

        //切换平台
        $scope.change = function (x) {
            $scope.selectedProject = x;
            $scope.getPagedDataAsync($scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize, $scope.selectedProject);
        };
    };

    $scope.dataWatch();

    $scope.initData = function () {
        // 初始化用户列表
        var conf = ACTIVITI.CONFIG;
        $http({
            method: 'GET',
            url: ACTIVITI.CONFIG.authRoot + '/user/findAll',
            headers: {
                'token': conf.token,
                'tenant': conf.tenant,
                'Content-Type': 'application/json; charset=UTF-8'
            },
            data: {},
        }).then(function successCallback(response) {
            $scope.gridDataTempUser = response.data.data;
            $scope.setAssignment();
            $scope.setAssignment2('candidateUsers', 'candidateUserField');
        }, function errorCallback(response) {
            // 请求失败执行代码
        });

        // 初始化角色列表
        var conf = ACTIVITI.CONFIG;
        $http({
            method: 'POST',
            url: ACTIVITI.CONFIG.authRoot + '/role/query',
            headers: {
                'token': conf.token,
                'tenant': conf.tenant,
                'Content-Type': 'application/json; charset=UTF-8'
            },
            data: {},
        }).then(function successCallback(response) {
            $scope.gridDataTempRole = response.data.data;
            $scope.setAssignment2('candidateGroups', 'candidateGroupField');
        }, function errorCallback(response) {
            // 请求失败执行代码
        });
    }

    $scope.initData();

    //异步请求表格数据
    $scope.getPagedDataAsync = function (pageNum, pageSize, projectId) {
        var url = '/user/page'; // 获取用户数据接口
        $scope.columnData = $scope.userColumns;
        if ($scope.selectType == 2) {
            url = '/role/page'; // 获取组数据接口
            $scope.columnData = $scope.groupColumns;
        }
        var conf = ACTIVITI.CONFIG;
        $http({
            method: 'POST',
            url: ACTIVITI.CONFIG.authRoot + url,
            headers: {
                'token': conf.token,
                'tenant': conf.tenant,
                'Content-Type': 'application/json; charset=UTF-8'
            },
            data: {
                'model': {},
                'current': pageNum,
                'size': pageSize,
            },
        }).then(function successCallback(response) {
            var res = response.data;
            $scope.gridData = res.data.records;
            $scope.totalServerItems = res.data.total;
        }, function errorCallback(response) {
            // 请求失败执行代码
            $scope.gridData = [];
            $scope.totalServerItems = 0;
        });
    };

    // 判断id是否存在,field为键
    $scope.isEx = function (obj, field, id) {
        var list = obj.map(v => {
            return v[field];
        });
        if (list.indexOf(id) > 0) {
            return true;
        }
        return false;
    }

    // 判断是否符合${}表达式
    $scope.isRegDefult = function (value) {
        return /\$\{(\w+)\}/g.test(value);
    }

    // 代理人设置
    $scope.setAssignment = function () {
        debugger
        $scope.popup.assignment.assigneeIds.forEach((id, index) => {
            if ($scope.isRegDefult(id)) {
                $scope.popup.assignment.assigneeFieldReg + ','
                $scope.popup.assignment.assigneeFieldReg = $scope.popup.assignment.assigneeFieldReg + id;
                $scope.popup.assignment.assigneeIds.splice(index, 1);
            } else {
                $scope.gridDataTempUser.forEach(v => {
                    if (v.id == id) {
                        // $scope.popup.assignment.assigneeField = $scope.popup.assignment.assigneeField +
                        $scope.popup.assignment.assigneeField += ',' + v.name;
                    }
                });
            }
        });
        if ($scope.popup.assignment.assigneeField.charAt(0) === ',') {
            $scope.popup.assignment.assigneeField = $scope.popup.assignment.assigneeField.substr(1);
        }
        if ($scope.popup.assignment.assigneeFieldReg.charAt(0) === ',') {
            $scope.popup.assignment.assigneeFieldReg = $scope.popup.assignment.assigneeFieldReg.substr(1);
        }
    }

    // 复数设置
    $scope.setAssignment2 = function (idKey, labelKey) {
        $scope.popup.assignment[labelKey] = ''
        var list = [];
        if (idKey == 'candidateUsers') {
            list = $scope.gridDataTempUser
        } else {
            list = $scope.gridDataTempRole
        }
        list.forEach(v => {
            $scope.popup.assignment[idKey].forEach(o => {
                if (v.id == o.value) {
                    if ($scope.popup.assignment[labelKey].length > 0) {
                        $scope.popup.assignment[labelKey] = $scope.popup.assignment[labelKey] + ','
                    }
                    $scope.popup.assignment[labelKey] = $scope.popup.assignment[labelKey] + v.name;
                }
            });
        });
    }

    // 处理代理人人数据
    $scope.handleAssignment = function (data) {
        var notExist = true;
        for (var i = 0; i < $scope.popup.assignment.assigneeIds.length; i++) {
            if ($scope.popup.assignment.assigneeIds[i] == data) {
                $scope.popup.assignment.assigneeIds.splice(i, 1);
                notExist = false;
                break;
            }
        }
        if (notExist) {
            $scope.popup.assignment.assigneeIds.push(data);
        }
        $scope.popup.assignment.assigneeField = '';
        $scope.setAssignment()
    };


    // 处理候选人数据
    $scope.handleUsers = function (data) {
        var notExist = true;
        for (var i = 0; i < $scope.popup.assignment.candidateUsers.length; i++) {
            if ($scope.popup.assignment.candidateUsers[i].value == data) {
                $scope.popup.assignment.candidateUsers.splice(i, 1);
                notExist = false;
                break;
            }
        }
        if (notExist) {
            $scope.popup.assignment.candidateUsers.push({value: data});
        }
        $scope.popup.assignment.candidateUserField = '';
        $scope.setAssignment2('candidateUsers', 'candidateUserField')
    };

    // 处理候选组数据
    $scope.handleGroups = function (data) {
        var notExist = true;
        for (var i = 0; i < $scope.popup.assignment.candidateGroups.length; i++) {
            if ($scope.popup.assignment.candidateGroups[i].value == data) {
                $scope.popup.assignment.candidateGroups.splice(i, 1);
                notExist = false;
                break;
            }
        }
        if (notExist) {
            $scope.popup.assignment.candidateGroups.push({value: data});
        }
        $scope.popup.assignment.candidateGroupField = '';
        $scope.setAssignment2('candidateGroups', 'candidateGroupField')
    };

    //表格属性配置
    $scope.gridOptions = {
        data: $scope.gridDataName,
        multiSelect: false,//不可多选
        enablePaging: true,
        pagingOptions: $scope.pagingOptions,
        totalServerItems: 'totalServerItems',
        i18n: 'zh-cn',
        showFooter: true,
        showSelectionCheckbox: false,
        columnDefs: $scope.columnDataName,
        beforeSelectionChange: function (event) {
            var data = event.entity.id;
            var name = event.entity.name;

            if ($scope.selectType == 0) { // 选代理人
                $scope.handleAssignment(data, name);
            } else if ($scope.selectType == 1) { // 候选人
                $scope.handleUsers(data, name);
            } else if ($scope.selectType == 2) { // 候选组
                $scope.handleGroups(data, name);
            }
            return true;
        }
    };

    //选择用户时表头
    $scope.userColumns = [
        {
            field: 'id',
            type: 'number',
            displayName: '用户Id',
            minWidth: 100,
            width: '30%'
        },
        {
            field: 'name',
            displayName: '姓名',
            minWidth: 100,
            width: '30%'
        },
        {
            field: 'email',
            displayName: '邮箱',
            minWidth: 100,
            width: '40%'
        }
    ];

    //选择用户组时表头
    $scope.groupColumns = [
        {
            field: 'id',
            type: 'number',
            displayName: '角色Id',
            minWidth: 100,
            width: '30%'
        },
        {
            field: 'name',
            displayName: '角色名称',
            minWidth: 100,
            width: '70%'
        }
    ];

    // 代理人(审批人)
    $scope.selectAssignee = function () {
        $scope.selectType = 0;
        $scope.selectTitle = '选择代理人';
    };

    // 候选人
    $scope.selectCandidate = function () {
        $scope.selectType = 1;
        $scope.selectTitle = '选择候选人';
    };

    // 候选组
    $scope.selectGroup = function () {
        $scope.selectType = 2;
        $scope.selectTitle = '选择候选组';
    };

}];
